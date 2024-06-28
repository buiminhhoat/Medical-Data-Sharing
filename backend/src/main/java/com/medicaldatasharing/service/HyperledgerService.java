package com.medicaldatasharing.service;

import com.medicaldatasharing.chaincode.Config;
import com.medicaldatasharing.chaincode.client.RegisterUserHyperledger;
import com.medicaldatasharing.chaincode.dto.*;
import com.medicaldatasharing.chaincode.util.ConnectionParamsUtil;
import com.medicaldatasharing.chaincode.util.WalletUtil;
import com.medicaldatasharing.dto.MedicalRecordDto;
import com.medicaldatasharing.dto.MedicalRecordPreviewDto;
import com.medicaldatasharing.dto.MedicationPreviewDto;
import com.medicaldatasharing.dto.SendViewPrescriptionRequestDto;
import com.medicaldatasharing.form.*;
import com.medicaldatasharing.model.Doctor;
import com.medicaldatasharing.model.DrugStore;
import com.medicaldatasharing.model.MedicalInstitution;
import com.medicaldatasharing.model.User;
import com.medicaldatasharing.repository.MedicalInstitutionRepository;
import com.medicaldatasharing.util.Constants;
import com.medicaldatasharing.util.StringUtil;
import com.owlike.genson.GenericType;
import com.owlike.genson.Genson;
import lombok.SneakyThrows;
import org.bouncycastle.util.encoders.Hex;
import org.hyperledger.fabric.gateway.Contract;
import org.hyperledger.fabric.gateway.Gateway;
import org.hyperledger.fabric.gateway.Identity;
import org.hyperledger.fabric.gateway.Network;
import org.hyperledger.fabric.sdk.BlockEvent;
import org.hyperledger.fabric.sdk.Channel;
import org.hyperledger.fabric.sdk.exception.InvalidArgumentException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.*;
import java.util.function.Consumer;
import java.util.logging.Logger;

@Service
public class HyperledgerService {
    private final static Logger LOG = Logger.getLogger(HyperledgerService.class.getName());

    static {
        System.setProperty("org.hyperledger.fabric.sdk.service_discovery.as_localhost", "true");
    }

    @Autowired
    private MedicalInstitutionRepository medicalInstitutionRepository;

    public static void registerListener(Network network, Channel channel, Contract contract) throws InvalidArgumentException {
        Consumer<BlockEvent> e = new Consumer<BlockEvent>() {
            @SneakyThrows
            @Override
            public void accept(BlockEvent blockEvent) {
                long bN = blockEvent.getBlockNumber();
                System.out.println("network blockListener" + bN);
                for (BlockEvent.TransactionEvent transactionEvent : blockEvent.getTransactionEvents()) {
                    String mspId = transactionEvent.getCreator().getMspid();
                    String peer = transactionEvent.getPeer().getName();
                    System.out.println(String.format("[NetworkBlockEventListener] transactionEventId: %s, creatorMspId: %s, peer: %s", transactionEvent.getTransactionID(), mspId, peer));
                }
            }

            @Override
            public Consumer<BlockEvent> andThen(Consumer<? super BlockEvent> after) {
                System.out.println("done accept event op");
                return null;
            }
        };
        network.addBlockListener(e);
    }

    private String hashValue(String originalString) {
        MessageDigest digest = null;
        try {
            digest = MessageDigest.getInstance("SHA-256");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        byte[] hash = digest.digest(
                originalString.getBytes(StandardCharsets.UTF_8));
        return new String(Hex.encode(hash));
    }

    private String determineOrg(User user) {
        if (user.getRole().equals(Constants.ROLE_PATIENT) || user.getRole().equals(Constants.ROLE_SUPER_ADMIN)) {
            return Config.PATIENT_ORG;
        }
        if (user.getRole().equals(Constants.ROLE_DOCTOR)) {
            return Config.DOCTOR_ORG;
        }
        if (user.getRole().equals(Constants.ROLE_MANUFACTURER)) {
            return Config.MANUFACTURER_ORG;
        }
        if (user.getRole().equals(Constants.ROLE_DRUG_STORE)) {
            return Config.DRUG_STORE_ORG;
        }
        return null;
    }

    private MedicalInstitution getMedicalInstitution(User user) {
        Doctor doctor = (Doctor) user;
        return doctor.getMedicalInstitution();
    }

    private Contract getContract(User user) throws Exception {
        String userWalletIdentity = user.getEmail();
        String userIdentity = user.getId();

        String org = determineOrg(user);
        Map<String, String> connectionConfigParams = ConnectionParamsUtil.setOrgConfigParams(org);
        String connectionProfilePath = connectionConfigParams.get("networkConfigPath");

        Gateway gateway = connect(userWalletIdentity, connectionProfilePath, userIdentity, org);
        Network network = gateway.getNetwork(Config.CHANNEL_NAME);
        Contract contract = network.getContract(Config.CHAINCODE_NAME);
        registerListener(network, network.getChannel(), contract);
        return contract;
    }

    private Gateway connect(String userWalletIdentity, String connectionProfilePath, String userIdentity, String org) throws Exception {
        Identity identity = RegisterUserHyperledger.enrollOrgAppUsers(userWalletIdentity, org, userIdentity);
        if (identity == null) {
            throw new Exception(String.format("Cannot find %s's idenitty", userWalletIdentity));
        }

        Gateway.Builder builder = Gateway.createBuilder();
        Path networkConfigPath = Paths.get(connectionProfilePath);
        WalletUtil walletUtil = new WalletUtil();
        builder.identity(walletUtil.getWallet(), userWalletIdentity).networkConfig(networkConfigPath).discovery(true);

        return builder.connect();
    }

    public MedicalRecord addMedicalRecord(User user, MedicalRecordDto medicalRecordDto) throws Exception {
        MedicalRecord medicalRecord = null;
        try {
            Contract contract = getContract(user);
            LOG.info("Submit Transaction: AddMedicalRecord");

            JSONObject jsonObject = new JSONObject();
            jsonObject.put("requestId", medicalRecordDto.getRequestId());
            jsonObject.put("patientId", medicalRecordDto.getPatientId());
            jsonObject.put("doctorId", medicalRecordDto.getDoctorId());
            jsonObject.put("medicalInstitutionId", medicalRecordDto.getMedicalInstitutionId());
            jsonObject.put("dateModified", medicalRecordDto.getDateModified());
            jsonObject.put("testName", medicalRecordDto.getTestName());
            jsonObject.put("details", medicalRecordDto.getDetails());
            jsonObject.put("hashFile", medicalRecordDto.getHashFile());
            jsonObject.put("addPrescription", medicalRecordDto.getAddPrescription());

            byte[] result = contract.submitTransaction(
                    "addMedicalRecord",
                    jsonObject.toString()
            );
            String medicalRecordStr = new String(result);
            medicalRecord = new Genson().deserialize(medicalRecordStr, MedicalRecord.class);
            LOG.info("result: " + medicalRecord);
        } catch (Exception e) {
            formatExceptionMessage(e);
        }
        return medicalRecord;
    }

    public MedicalRecord getMedicalRecord(User user, String medicalRecordId) throws Exception {
        MedicalRecord medicalRecord = null;
        try {
            Contract contract = getContract(user);
            LOG.info("Evaluate Transaction: GetMedicalRecord");
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("medicalRecordId", medicalRecordId);
            byte[] result = contract.evaluateTransaction(
                    "getMedicalRecord",
                    jsonObject.toString()
            );
            String medicalRecordStr = new String(result);
            medicalRecord = new Genson().deserialize(medicalRecordStr, MedicalRecord.class);
            LOG.info("result: " + medicalRecord);
        } catch (Exception e) {
            formatExceptionMessage(e);
        }
        return medicalRecord;
    }

    public EditRequest getEditRequest(User user, String requestId) throws Exception {
        EditRequest editRequest = null;
        try {
            Contract contract = getContract(user);
            LOG.info("Evaluate Transaction: getEditRequest");
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("requestId", requestId);
            byte[] result = contract.evaluateTransaction(
                    "getEditRequest",
                    jsonObject.toString()
            );
            String editRequestStr = new String(result);
            editRequest = new Genson().deserialize(editRequestStr, EditRequest.class);
            LOG.info("result: " + editRequest);
        } catch (Exception e) {
            formatExceptionMessage(e);
        }
        return editRequest;
    }

    public MedicalRecord defineEditRequest(User user, DefineRequestForm defineRequestForm) throws Exception {
        MedicalRecord medicalRecord = null;
        try {
            Contract contract = getContract(user);
            LOG.info("Submit Transaction: defineEditRequest");
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("requestId", defineRequestForm.getRequestId());
            jsonObject.put("requestStatus", defineRequestForm.getRequestStatus());
            System.out.println(jsonObject.toString());
            byte[] result = contract.submitTransaction(
                    "defineEditRequest",
                    jsonObject.toString()
            );
            String medicalRecordStr = new String(result);
            medicalRecord = new Genson().deserialize(medicalRecordStr, MedicalRecord.class);
            LOG.info("result: " + medicalRecord);
        } catch (Exception e) {
            formatExceptionMessage(e);
        }
        return medicalRecord;
    }

    public MedicalRecord editMedicalRecord(User user, String requestId) throws Exception {
        MedicalRecord medicalRecord = null;
        try {
            Contract contract = getContract(user);
            LOG.info("Submit Transaction: editMedicalRecord");
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("requestId", requestId);
            byte[] result = contract.submitTransaction(
                    "editMedicalRecord",
                    jsonObject.toString()
            );
            String medicalRecordStr = new String(result);
            medicalRecord = new Genson().deserialize(medicalRecordStr, MedicalRecord.class);
            LOG.info("result: " + medicalRecord);
        } catch (Exception e) {
            formatExceptionMessage(e);
        }
        return medicalRecord;
    }

    public AppointmentRequest sendAppointmentRequest(
            User user,
            SendAppointmentRequestForm sendAppointmentRequestForm
    ) throws Exception {
        AppointmentRequest appointmentRequest = null;
        try {
            Contract contract = getContract(user);
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("senderId", sendAppointmentRequestForm.getSenderId());
            jsonObject.put("recipientId", sendAppointmentRequestForm.getRecipientId());
            jsonObject.put("dateModified", sendAppointmentRequestForm.getDateModified());

            byte[] result = contract.submitTransaction(
                    "sendAppointmentRequest",
                    jsonObject.toString()
            );
            String appointmentRequestStr = new String(result);
            appointmentRequest = new Genson().deserialize(appointmentRequestStr, AppointmentRequest.class);
            LOG.info("result: " + appointmentRequest);
        } catch (Exception e) {
            formatExceptionMessage(e);
        }
        return appointmentRequest;
    }

    public EditRequest sendEditRequest(
            User user,
            SendEditRequestForm sendEditRequestForm
    ) throws Exception {
        EditRequest editRequest = null;
        try {
            Contract contract = getContract(user);
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("senderId", sendEditRequestForm.getSenderId());
            jsonObject.put("recipientId", sendEditRequestForm.getRecipientId());
            jsonObject.put("dateModified", sendEditRequestForm.getDateModified());
            jsonObject.put("medicalRecordJson", sendEditRequestForm.getMedicalRecordJson());
            byte[] result = contract.submitTransaction(
                    "sendEditRequest",
                    jsonObject.toString()
            );

            String editRequestStr = new String(result);
            editRequest = new Genson().deserialize(editRequestStr, EditRequest.class);
            LOG.info("result: " + editRequest);
        } catch (Exception e) {
            formatExceptionMessage(e);
        }
        return editRequest;
    }

    public ViewRequest sendViewRequest(
            User user,
            SendViewRequestForm sendViewRequestForm
    ) throws Exception {
        ViewRequest viewRequest = null;
        try {
            Contract contract = getContract(user);
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("senderId", sendViewRequestForm.getSenderId());
            jsonObject.put("recipientId", sendViewRequestForm.getRecipientId());
            jsonObject.put("dateModified", sendViewRequestForm.getDateModified());
            byte[] result = contract.submitTransaction(
                    "sendViewRequest",
                    jsonObject.toString()
            );

            String viewRequestStr = new String(result);
            viewRequest = new Genson().deserialize(viewRequestStr, ViewRequest.class);
            LOG.info("result: " + viewRequest);
        } catch (Exception e) {
            formatExceptionMessage(e);
        }
        return viewRequest;
    }

    public MedicalRecord defineMedicalRecord(
            User user,
            DefineMedicalRecordForm defineMedicalRecordForm
    ) throws Exception {
        MedicalRecord medicalRecord = null;
        try {
            Contract contract = getContract(user);
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("medicalRecordId", defineMedicalRecordForm.getMedicalRecordId());
            jsonObject.put("medicalRecordStatus", defineMedicalRecordForm.getMedicalRecordStatus());

            byte[] result = contract.submitTransaction(
                    "defineMedicalRecord",
                    jsonObject.toString()
            );
            String medicalRecordStr = new String(result);
            medicalRecord = new Genson().deserialize(medicalRecordStr, MedicalRecord.class);
            LOG.info("result: " + medicalRecord);
        } catch (Exception e) {
            formatExceptionMessage(e);
        }
        return medicalRecord;
    }

    public List<MedicalRecordPreviewDto> getListMedicalRecordByPatientQuery(
            User user,
            SearchMedicalRecordForm searchMedicalRecordForm
    ) throws Exception {
        List<MedicalRecordPreviewDto> medicalRecordPreviewDtoList = new ArrayList<>();
        try {
            Contract contract = getContract(user);

            Map<String, String> searchParams = prepareSearchMedicalRecordParams(searchMedicalRecordForm);

            JSONObject jsonObject = new JSONObject();

            for (Map.Entry<String, String> entry : searchParams.entrySet()) {
                jsonObject.put(entry.getKey(), entry.getValue());
            }

            byte[] result = contract.evaluateTransaction(
                    "getListMedicalRecordByPatientQuery",
                    jsonObject.toString()
            );

            String medicalRecordListStr = new String(result);
            List<MedicalRecord> medicalRecordList = new Genson().deserialize(
                    medicalRecordListStr,
                    new GenericType<List<MedicalRecord>>() {}
            );

            LOG.info("result: " + medicalRecordList);
            for (MedicalRecord medicalRecord : medicalRecordList) {
                MedicalRecordPreviewDto medicalRecordPreviewDto = new MedicalRecordPreviewDto();

                medicalRecordPreviewDto.setMedicalRecordId(medicalRecord.getMedicalRecordId());
                medicalRecordPreviewDto.setPatientId(medicalRecord.getPatientId());
                medicalRecordPreviewDto.setDoctorId(medicalRecord.getDoctorId());
                medicalRecordPreviewDto.setMedicalInstitutionId(medicalRecord.getMedicalInstitutionId());
                medicalRecordPreviewDto.setDateModified(medicalRecord.getDateModified());
                medicalRecordPreviewDto.setTestName(medicalRecord.getTestName());
                medicalRecordPreviewDto.setDetails(medicalRecord.getDetails());
                medicalRecordPreviewDto.setMedicalRecordStatus(medicalRecord.getMedicalRecordStatus());
                medicalRecordPreviewDtoList.add(medicalRecordPreviewDto);
            }

        } catch (Exception e) {
            formatExceptionMessage(e);
        }
        return medicalRecordPreviewDtoList;
    }

    public List<MedicationPreviewDto> getListMedication(
            User user,
            SearchMedicationForm searchMedicationForm
    ) throws Exception {
        List<MedicationPreviewDto> medicationPreviewDtoList = new ArrayList<>();
        try {
            Contract contract = getContract(user);

            Map<String, String> searchParams = prepareSearchMedicationParams(searchMedicationForm);

            JSONObject jsonObject = new JSONObject();

            for (Map.Entry<String, String> entry : searchParams.entrySet()) {
                jsonObject.put(entry.getKey(), entry.getValue());
            }

            byte[] result = contract.evaluateTransaction(
                    "getListMedication",
                    jsonObject.toString()
            );

            String medicationListStr = new String(result);
            List<Medication> medicationList = new Genson().deserialize(
                    medicationListStr,
                    new GenericType<List<Medication>>() {
                    }
            );

            LOG.info("result: " + medicationList);
            for (Medication medication : medicationList) {
                MedicationPreviewDto medicationPreviewDto = new MedicationPreviewDto();
                medicationPreviewDto.setMedicationId(medication.getMedicationId());
                medicationPreviewDto.setManufacturerId(medication.getManufacturerId());
                medicationPreviewDto.setMedicationName(medication.getMedicationName());
                medicationPreviewDto.setDescription(medication.getDescription());
                medicationPreviewDto.setDateModified(medication.getDateModified());
                medicationPreviewDtoList.add(medicationPreviewDto);
            }

        } catch (Exception e) {
            formatExceptionMessage(e);
        }
        return medicationPreviewDtoList;
    }

    private Map<String, String> prepareSearchMedicalRecordParams(SearchMedicalRecordForm searchMedicalRecordForm) {
        String medicalRecordId = searchMedicalRecordForm.getMedicalRecordId() == null ? "" : searchMedicalRecordForm.getMedicalRecordId();
        String patientId = searchMedicalRecordForm.getPatientId() == null ? "" : searchMedicalRecordForm.getPatientId();
        String doctorId = searchMedicalRecordForm.getDoctorId() == null ? "" : searchMedicalRecordForm.getDoctorId();
        String testName = searchMedicalRecordForm.getTestName() == null ? "" : searchMedicalRecordForm.getTestName();
        String medicalInstitutionId = searchMedicalRecordForm.getMedicalInstitutionId() == null ? "" : searchMedicalRecordForm.getMedicalInstitutionId();
        String medicalRecordStatus = searchMedicalRecordForm.getMedicalRecordStatus() == null ? "" : searchMedicalRecordForm.getMedicalRecordStatus();
        String prescriptionId = searchMedicalRecordForm.getPrescriptionId() == null ? "" : searchMedicalRecordForm.getPrescriptionId();
        String hashFile = searchMedicalRecordForm.getHashFile() == null ? "" : searchMedicalRecordForm.getHashFile();
        String from;
        if (searchMedicalRecordForm.getFrom() == null) {
            Calendar calendar = Calendar.getInstance();
            calendar.add(Calendar.MONTH, -6);
            from = StringUtil.parseDate(calendar.getTime());
        } else {
            from = StringUtil.parseDate(searchMedicalRecordForm.getFrom());
        }
        String until;
        if (searchMedicalRecordForm.getFrom() == null) {
            until = StringUtil.parseDate(new Date());
        } else {
            until = StringUtil.parseDate(searchMedicalRecordForm.getUntil());
        }
        String sortingOrder = searchMedicalRecordForm.getSortingOrder() == null ? "desc" : searchMedicalRecordForm.getSortingOrder();
        String details = searchMedicalRecordForm.getDetails() == null ?
                "" : searchMedicalRecordForm.getDetails();

        return new HashMap<String, String>() {{
            put("medicalRecordId", medicalRecordId);
            put("patientId", patientId);
            put("doctorId", doctorId);
            put("medicalInstitutionId", medicalInstitutionId);
            put("from", from);
            put("until", until);
            put("testName", testName);
            put("medicalRecordStatus", medicalRecordStatus);
            put("details", details);
            put("sortingOrder", sortingOrder);
            put("prescriptionId", prescriptionId);
            put("hashFile", hashFile);
        }};
    }

    private Map<String, String> prepareSearchMedicationParams(SearchMedicationForm searchMedicationForm) {
        String medicationId = searchMedicationForm.getMedicationId() == null ? "" : searchMedicationForm.getMedicationId();
        String manufacturerId = searchMedicationForm.getManufacturerId() == null ? "" : searchMedicationForm.getManufacturerId();
        String medicationName = searchMedicationForm.getMedicationName() == null ? "" : searchMedicationForm.getMedicationName();
        String description = searchMedicationForm.getDescription() == null ? "" : searchMedicationForm.getDescription();

        String from;
        if (searchMedicationForm.getFrom() == null) {
            Calendar calendar = Calendar.getInstance();
            calendar.add(Calendar.MONTH, -6);
            from = StringUtil.parseDate(calendar.getTime());
        } else {
            from = StringUtil.parseDate(searchMedicationForm.getFrom());
        }
        String until;
        if (searchMedicationForm.getFrom() == null) {
            until = StringUtil.parseDate(new Date());
        } else {
            until = StringUtil.parseDate(searchMedicationForm.getUntil());
        }
        String sortingOrder = searchMedicationForm.getSortingOrder() == null ? "desc" : searchMedicationForm.getSortingOrder();

        return new HashMap<String, String>() {{
            put("medicationId", medicationId);
            put("manufacturerId", manufacturerId);
            put("medicationName", medicationName);
            put("description", description);
            put("from", from);
            put("until", until);
            put("sortingOrder", sortingOrder);
        }};
    }

    private void formatExceptionMessage(Exception e) throws Exception {
        String msg = e.getMessage();
        String errorMsg = msg.substring(msg.lastIndexOf(":") + 1);
        throw new Exception(errorMsg);
    }

    public List<ViewRequest> getListViewRequestBySenderQuery(User user,
                                                             SearchViewRequestForm searchViewRequestForm) throws Exception {
        List<ViewRequest> viewRequestList = null;
        try {
            Contract contract = getContract(user);

            String from;
            if (searchViewRequestForm.getFrom() == null) {
                Calendar calendar = Calendar.getInstance();
                calendar.add(Calendar.MONTH, -6);
                from = StringUtil.parseDate(calendar.getTime());
            } else {
                from = StringUtil.parseDate(searchViewRequestForm.getFrom());
            }
            String until;
            if (searchViewRequestForm.getFrom() == null) {
                until = StringUtil.parseDate(new Date());
            } else {
                until = StringUtil.parseDate(searchViewRequestForm.getUntil());
            }

            JSONObject jsonObject = new JSONObject();
            jsonObject.put("requestId", searchViewRequestForm.getRequestId());
            jsonObject.put("senderId", searchViewRequestForm.getSenderId());
            jsonObject.put("recipientId", searchViewRequestForm.getRecipientId());
            jsonObject.put("requestType", searchViewRequestForm.getRequestType());
            jsonObject.put("requestStatus", searchViewRequestForm.getRequestStatus());
            jsonObject.put("from", from);
            jsonObject.put("until", until);
            jsonObject.put("sortingOrder", searchViewRequestForm.getSortingOrder());

            byte[] result = contract.evaluateTransaction(
                    "getListViewRequestBySenderQuery",
                    jsonObject.toString()
            );

            String viewRequestListStr = new String(result);
            viewRequestList = new Genson().deserialize(viewRequestListStr,
                    new GenericType<List<ViewRequest>>() {});
        } catch (Exception e) {
            formatExceptionMessage(e);
        }
        return viewRequestList;
    }

    public List<MedicalRecord> getMedicalRecordChangeHistory(User user, String medicalRecordId) throws Exception {
        List<MedicalRecord> changeHistory = new ArrayList<>();
        try {
            Contract contract = getContract(user);

            JSONObject jsonDto = new JSONObject();
            jsonDto.put("medicalRecordId", medicalRecordId);
            byte[] result = contract.submitTransaction(
                    "getMedicalRecordChangeHistory",
                    jsonDto.toString()
            );
            changeHistory = new Genson().deserialize(new String(result), List.class);
            LOG.info("result: " + changeHistory);
        } catch (Exception e) {
            formatExceptionMessage(e);
        }
        return changeHistory;
    }

    public Medication addMedication(User user, AddMedicationForm addMedicationForm) throws Exception {
        Medication medication = null;
        try {
            Contract contract = getContract(user);
            JSONObject jsonDto = addMedicationForm.toJSONObject();
            byte[] result = contract.submitTransaction(
                    "addMedication",
                    jsonDto.toString()
            );
            medication = new Genson().deserialize(new String(result), Medication.class);
            LOG.info("result: " + medication);
        } catch (Exception e) {
            formatExceptionMessage(e);
        }
        return medication;
    }

    public Prescription addPrescription(User user, AddPrescriptionForm addPrescriptionForm) throws Exception {
        Prescription prescription = null;
        try {
            Contract contract = getContract(user);
            JSONObject jsonDto = addPrescriptionForm.toJSONObject();
            byte[] result = contract.submitTransaction(
                    "addPrescription",
                    jsonDto.toString()
            );
            prescription = new Genson().deserialize(new String(result), Prescription.class);
            LOG.info("result: " + prescription);
        } catch (Exception e) {
            formatExceptionMessage(e);
        }
        return prescription;
    }

    public ViewPrescriptionRequest sendViewPrescriptionRequest(User user, SendViewPrescriptionRequestDto sendViewPrescriptionRequestDto) throws Exception {
        ViewPrescriptionRequest viewPrescriptionRequest = null;
        try {
            Contract contract = getContract(user);
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("senderId", sendViewPrescriptionRequestDto.getSenderId());
            jsonObject.put("recipientId", sendViewPrescriptionRequestDto.getRecipientId());
            jsonObject.put("dateModified", sendViewPrescriptionRequestDto.getDateModified());
            jsonObject.put("prescriptionId", sendViewPrescriptionRequestDto.getPrescriptionId());
            byte[] result = contract.submitTransaction(
                    "sendViewPrescriptionRequest",
                    jsonObject.toString()
            );

            String viewPrescriptionRequestStr = new String(result);
            viewPrescriptionRequest = new Genson().deserialize(viewPrescriptionRequestStr, ViewPrescriptionRequest.class);
            LOG.info("result: " + viewPrescriptionRequest);
        } catch (Exception e) {
            formatExceptionMessage(e);
        }
        return viewPrescriptionRequest;
    }
}

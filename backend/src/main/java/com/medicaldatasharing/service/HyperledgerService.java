package com.medicaldatasharing.service;

import com.medicaldatasharing.chaincode.Config;
import com.medicaldatasharing.chaincode.client.RegisterUserHyperledger;
import com.medicaldatasharing.chaincode.dto.*;
import com.medicaldatasharing.chaincode.util.ConnectionParamsUtil;
import com.medicaldatasharing.chaincode.util.WalletUtil;
import com.medicaldatasharing.dto.*;
import com.medicaldatasharing.form.*;
import com.medicaldatasharing.model.*;
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
        if (user.getRole().equals(Constants.ROLE_PATIENT) || user.getRole().equals(Constants.ROLE_ADMIN)) {
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
        if (user.getRole().equals(Constants.ROLE_SCIENTIST)) {
            return Config.SCIENTIST_ORG;
        }
        if (user.getRole().equals(Constants.ROLE_INSURANCE_COMPANY)) {
            return Config.INSURANCE_COMPANY_ORG;
        }
        return null;
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

    public MedicalRecord addMedicalRecord(User user, JSONObject jsonDto) throws Exception {
        MedicalRecord medicalRecord = null;
        try {
            Contract contract = getContract(user);
            LOG.info("Submit Transaction: AddMedicalRecord");

            JSONObject jsonObject = jsonDto;

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

    public MedicalRecord getMedicalRecordByPatient(User user, String medicalRecordId) throws Exception {
        MedicalRecord medicalRecord = null;
        try {
            Contract contract = getContract(user);
            LOG.info("Evaluate Transaction: getMedicalRecordByPatient");
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("medicalRecordId", medicalRecordId);
            byte[] result = contract.evaluateTransaction(
                    "getMedicalRecordByPatient",
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

    public MedicalRecord getMedicalRecordByDoctor(User user, String medicalRecordId) throws Exception {
        MedicalRecord medicalRecord = null;
        try {
            Contract contract = getContract(user);
            LOG.info("Evaluate Transaction: getMedicalRecordByDoctor");
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("medicalRecordId", medicalRecordId);
            byte[] result = contract.evaluateTransaction(
                    "getMedicalRecordByDoctor",
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

    public List<MedicalRecord> getListAuthorizedMedicalRecordByDoctorQuery(User user,
                                                                              GetListAuthorizedMedicalRecordByDoctorQueryDto getListAuthorizedMedicalRecordByDoctorQueryDto) throws Exception {
        List<MedicalRecord> getListAuthorizedMedicalRecordByDoctorQueryList = new ArrayList<>();
        try {
            Contract contract = getContract(user);

            JSONObject jsonObject = getListAuthorizedMedicalRecordByDoctorQueryDto.toJSONObject();

            byte[] result = contract.evaluateTransaction(
                    "getListAuthorizedMedicalRecordByDoctorQuery",
                    jsonObject.toString()
            );

            String getListAuthorizedMedicalRecordByDoctorQueryListStr = new String(result);
            getListAuthorizedMedicalRecordByDoctorQueryList = new Genson().deserialize(
                    getListAuthorizedMedicalRecordByDoctorQueryListStr,
                    new GenericType<List<MedicalRecord>>() {}
            );
        } catch (Exception e) {
            formatExceptionMessage(e);
        }
        return getListAuthorizedMedicalRecordByDoctorQueryList;
    }

    public List<DrugReactionDto> getListDrugReactionByManufacturer(User user,
                                                                           GetDrugReactionForm getDrugReactionForm) throws Exception {
        List<DrugReactionDto> getListDrugReactionByManufacturerList = new ArrayList<>();
        try {
            Contract contract = getContract(user);

            JSONObject jsonObject = getDrugReactionForm.toJSONObject();

            byte[] result = contract.evaluateTransaction(
                    "getListDrugReactionByManufacturer",
                    jsonObject.toString()
            );

            String getListDrugReactionByManufacturerStr = new String(result);
            getListDrugReactionByManufacturerList = new Genson().deserialize(
                    getListDrugReactionByManufacturerStr,
                    new GenericType<List<DrugReactionDto>>() {}
            );
        } catch (Exception e) {
            formatExceptionMessage(e);
        }
        return getListDrugReactionByManufacturerList;
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
            jsonObject.put("medicalInstitutionId", sendAppointmentRequestForm.getMedicalInstitutionId());
            jsonObject.put("dateCreated", sendAppointmentRequestForm.getDateCreated());
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

    public AppointmentRequest defineAppointmentRequest(User user,
                                         DefineAppointmentRequestForm defineAppointmentRequestForm) throws Exception {
        AppointmentRequest appointmentRequest = null;
        try {
            Contract contract = getContract(user);
            LOG.info("Submit Transaction: defineAppointmentRequest");
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("requestId", defineAppointmentRequestForm.getRequestId());
            jsonObject.put("requestStatus", defineAppointmentRequestForm.getRequestStatus());
            System.out.println(jsonObject.toString());

            byte[] result = contract.submitTransaction(
                    "defineAppointmentRequest",
                    jsonObject.toString()
            );

            String appointmentRequestStr = new String(result);
            appointmentRequest = new Genson().deserialize(
                    appointmentRequestStr,
                    AppointmentRequest.class
            );
            LOG.info("result: " + appointmentRequest);
        } catch (Exception e) {
            formatExceptionMessage(e);
        }
        return appointmentRequest;
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
            jsonObject.put("dateCreated", sendViewRequestForm.getDateCreated());
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

    public ViewRequest defineViewRequest(User user,
                                         DefineViewRequestForm defineViewRequestForm) throws Exception {
        ViewRequest viewRequest = null;
        try {
            Contract contract = getContract(user);
            LOG.info("Submit Transaction: defineViewRequest");
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("requestId", defineViewRequestForm.getRequestId());
            jsonObject.put("requestStatus", defineViewRequestForm.getRequestStatus());
            System.out.println(jsonObject.toString());

            byte[] result = contract.submitTransaction(
                    "defineViewRequest",
                    jsonObject.toString()
            );

            String viewRequestStr = new String(result);
            viewRequest = new Genson().deserialize(
                    viewRequestStr,
                    ViewRequest.class
            );
            LOG.info("result: " + viewRequestStr);
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

    public List<MedicalRecord> getListMedicalRecordByPatientQuery(
            User user,
            SearchMedicalRecordForm searchMedicalRecordForm
    ) throws Exception {
        List<MedicalRecord> medicalRecordList = new ArrayList<>();
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
            medicalRecordList = new Genson().deserialize(
                    medicalRecordListStr,
                    new GenericType<List<MedicalRecord>>() {}
            );

            LOG.info("result: " + medicalRecordList);

        } catch (Exception e) {
            formatExceptionMessage(e);
        }
        return medicalRecordList;
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
//            Calendar calendar = Calendar.getInstance();
//            calendar.add(Calendar.MONTH, -6);
//            from = StringUtil.parseDate(calendar.getTime());
            from = "";
        } else {
            from = StringUtil.parseDate(searchMedicalRecordForm.getFrom());
        }
        String until;
        if (searchMedicalRecordForm.getFrom() == null) {
//            until = StringUtil.parseDate(new Date());
            until = "";
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
            from = "";
        } else {
            from = StringUtil.parseDate(searchMedicationForm.getFrom());
        }
        String until;
        if (searchMedicationForm.getFrom() == null) {
            until = "";
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

    private Map<String, String> prepareSearchDrugParams(SearchDrugForm searchDrugForm) {
        String drugId = searchDrugForm.getDrugId() == null ? "" : searchDrugForm.getDrugId();
        String medicationId = searchDrugForm.getMedicationId() == null ? "" : searchDrugForm.getMedicationId();
        String manufactureDate = searchDrugForm.getManufactureDate() == null ? "" : searchDrugForm.getManufactureDate();
        String expirationDate = searchDrugForm.getExpirationDate() == null ? "" : searchDrugForm.getExpirationDate();
        String ownerId = searchDrugForm.getOwnerId() == null ? "" : searchDrugForm.getOwnerId();

        return new HashMap<String, String>() {{
            put("drugId", drugId);
            put("medicationId", medicationId);
            put("manufactureDate", manufactureDate);
            put("expirationDate", expirationDate);
            put("ownerId", ownerId);
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
                from = "";
            } else {
                from = StringUtil.parseDate(searchViewRequestForm.getFrom());
            }
            String until;
            if (searchViewRequestForm.getFrom() == null) {
                until = "";
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

    public List<ViewRequest> getListViewRequestByRecipientQuery(User user,
                                                             SearchViewRequestForm searchViewRequestForm) throws Exception {
        List<ViewRequest> viewRequestList = null;
        try {
            Contract contract = getContract(user);

            String from;
            if (searchViewRequestForm.getFrom() == null) {
                from = "";
            } else {
                from = StringUtil.parseDate(searchViewRequestForm.getFrom());
            }
            String until;
            if (searchViewRequestForm.getFrom() == null) {
                until = "";
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

    public Medication editMedication(User user, EditMedicationForm editMedicationForm) throws Exception {
        Medication medication = null;
        try {
            Contract contract = getContract(user);
            JSONObject jsonDto = editMedicationForm.toJSONObject();
            byte[] result = contract.submitTransaction(
                    "editMedication",
                    jsonDto.toString()
            );
            medication = new Genson().deserialize(new String(result), Medication.class);
            LOG.info("result: " + medication);
        } catch (Exception e) {
            formatExceptionMessage(e);
        }
        return medication;
    }

    public List<Medication> getAllMedication(
            User user
    ) throws Exception {
        List<Medication> medicationList = new ArrayList<>();
        try {
            Contract contract = getContract(user);


            byte[] result = contract.evaluateTransaction(
                    "getAllMedication"
            );

            String medicationListStr = new String(result);
            medicationList = new Genson().deserialize(
                    medicationListStr,
                    new GenericType<List<Medication>>() {
                    }
            );

            LOG.info("result: " + medicationList);
        } catch (Exception e) {
            formatExceptionMessage(e);
        }
        return medicationList;
    }

    public List<Medication> getListMedication(
            User user,
            SearchMedicationForm searchMedicationForm
    ) throws Exception {
        List<Medication> medicationList = new ArrayList<>();
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
            medicationList = new Genson().deserialize(
                    medicationListStr,
                    new GenericType<List<Medication>>() {
                    }
            );

            LOG.info("result: " + medicationList);
        } catch (Exception e) {
            formatExceptionMessage(e);
        }
        return medicationList;
    }

    public List<Drug> getListDrugByOwnerId(
            User user,
            SearchDrugForm searchDrugForm
    ) throws Exception {
        List<Drug> drugList = new ArrayList<>();
        try {
            Contract contract = getContract(user);

            Map<String, String> searchParams = prepareSearchDrugParams(searchDrugForm);

            JSONObject jsonObject = new JSONObject();

            for (Map.Entry<String, String> entry : searchParams.entrySet()) {
                jsonObject.put(entry.getKey(), entry.getValue());
            }

            byte[] result = contract.evaluateTransaction(
                    "getListDrugByOwnerId",
                    jsonObject.toString()
            );

            String drugListStr = new String(result);
            drugList = new Genson().deserialize(
                    drugListStr,
                    new GenericType<List<Drug>>() {
                    }
            );

            LOG.info("result: " + drugListStr);
        } catch (Exception e) {
            formatExceptionMessage(e);
        }
        return drugList;
    }

    public List<Purchase> getListPurchaseByPatientId(
            User user,
            SearchPurchaseForm searchPurchaseForm
    ) throws Exception {
        List<Purchase> purchaseList = new ArrayList<>();
        try {
            Contract contract = getContract(user);

            Map<String, String> searchParams = prepareSearchPurchaseParams(searchPurchaseForm);

            JSONObject jsonObject = new JSONObject();

            for (Map.Entry<String, String> entry : searchParams.entrySet()) {
                jsonObject.put(entry.getKey(), entry.getValue());
            }

            byte[] result = contract.evaluateTransaction(
                    "getListPurchaseByPatientId",
                    jsonObject.toString()
            );

            String purchaseListStr = new String(result);
            purchaseList = new Genson().deserialize(
                    purchaseListStr,
                    new GenericType<List<Purchase>>() {
                    }
            );

            LOG.info("result: " + purchaseList);
        } catch (Exception e) {
            formatExceptionMessage(e);
        }
        return purchaseList;
    }

    public List<Purchase> getListPurchaseByDrugStoreId(
            User user,
            SearchPurchaseForm searchPurchaseForm
    ) throws Exception {
        List<Purchase> purchaseList = new ArrayList<>();
        try {
            Contract contract = getContract(user);

            Map<String, String> searchParams = prepareSearchPurchaseParams(searchPurchaseForm);

            JSONObject jsonObject = new JSONObject();

            for (Map.Entry<String, String> entry : searchParams.entrySet()) {
                jsonObject.put(entry.getKey(), entry.getValue());
            }

            byte[] result = contract.evaluateTransaction(
                    "getListPurchaseByDrugStoreId",
                    jsonObject.toString()
            );

            String purchaseListStr = new String(result);
            purchaseList = new Genson().deserialize(
                    purchaseListStr,
                    new GenericType<List<Purchase>>() {
                    }
            );

            LOG.info("result: " + purchaseList);
        } catch (Exception e) {
            formatExceptionMessage(e);
        }
        return purchaseList;
    }

    public PurchaseDto getPurchaseByPurchaseId(
            User user,
            String purchaseId
    ) throws Exception {
        PurchaseDto purchaseDto = null;
        try {
            Contract contract = getContract(user);

            byte[] result = contract.evaluateTransaction(
                    "getPurchaseByPurchaseId",
                    purchaseId
            );

            String purchaseDtoStr = new String(result);
            purchaseDto = new Genson().deserialize(
                    purchaseDtoStr,
                    PurchaseDto.class
            );

            LOG.info("result: " + purchaseDto);
        } catch (Exception e) {
            formatExceptionMessage(e);
        }
        return purchaseDto;
    }

    public List<Purchase> getListPurchaseByDrugId(
            User user,
            SearchPurchaseForm searchPurchaseForm
    ) throws Exception {
        List<Purchase> purchaseList = new ArrayList<>();
        try {
            Contract contract = getContract(user);

            Map<String, String> searchParams = prepareSearchPurchaseParams(searchPurchaseForm);

            JSONObject jsonObject = new JSONObject();

            for (Map.Entry<String, String> entry : searchParams.entrySet()) {
                jsonObject.put(entry.getKey(), entry.getValue());
            }

            byte[] result = contract.evaluateTransaction(
                    "getListPurchaseByDrugId",
                    jsonObject.toString()
            );

            String purchaseListStr = new String(result);
            purchaseList = new Genson().deserialize(
                    purchaseListStr,
                    new GenericType<List<Purchase>>() {
                    }
            );

            LOG.info("result: " + purchaseList);
        } catch (Exception e) {
            formatExceptionMessage(e);
        }
        return purchaseList;
    }

    private Map<String, String> prepareSearchPurchaseParams(SearchPurchaseForm searchPurchaseForm) {
        String purchaseId = searchPurchaseForm.getPurchaseId() == null ? "" : searchPurchaseForm.getPurchaseId();
        String prescriptionId = searchPurchaseForm.getPrescriptionId() == null ? "" : searchPurchaseForm.getPrescriptionId();
        String patientId = searchPurchaseForm.getPatientId() == null ? "" : searchPurchaseForm.getPatientId();
        String drugStoreId = searchPurchaseForm.getDrugStoreId() == null ? "" : searchPurchaseForm.getDrugStoreId();

        return new HashMap<String, String>() {{
            put("purchaseId", purchaseId);
            put("prescriptionId", prescriptionId);
            put("patientId", patientId);
            put("drugStoreId", drugStoreId);
        }};
    }


    public PrescriptionDto getPrescriptionByPatient(User user, GetPrescriptionForm getPrescriptionForm) throws Exception {
        PrescriptionDto prescriptionDto = null;
        try {
            Contract contract = getContract(user);
            JSONObject jsonDto = getPrescriptionForm.toJSONObject();
            byte[] result = contract.submitTransaction(
                    "getPrescriptionByPatient",
                    jsonDto.toString()
            );
            prescriptionDto = new Genson().deserialize(new String(result), PrescriptionDto.class);
            LOG.info("result: " + prescriptionDto);
        } catch (Exception e) {
            formatExceptionMessage(e);
        }
        return prescriptionDto;
    }

    public PrescriptionDto getPrescriptionByDrugStore(User user, GetPrescriptionForm getPrescriptionForm) throws Exception {
        PrescriptionDto prescriptionDto = null;
        try {
            Contract contract = getContract(user);
            JSONObject jsonDto = getPrescriptionForm.toJSONObject();
            byte[] result = contract.submitTransaction(
                    "getPrescriptionByDrugStore",
                    jsonDto.toString()
            );
            prescriptionDto = new Genson().deserialize(new String(result), PrescriptionDto.class);
            LOG.info("result: " + prescriptionDto);
        } catch (Exception e) {
            formatExceptionMessage(e);
        }
        return prescriptionDto;
    }

    public ViewPrescriptionRequest sendViewPrescriptionRequest(User user, SendViewPrescriptionRequestForm sendViewPrescriptionRequestForm) throws Exception {
        ViewPrescriptionRequest viewPrescriptionRequest = null;
        try {
            Contract contract = getContract(user);
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("senderId", sendViewPrescriptionRequestForm.getSenderId());
            jsonObject.put("recipientId", sendViewPrescriptionRequestForm.getRecipientId());
            jsonObject.put("dateCreated", sendViewPrescriptionRequestForm.getDateCreated());
            jsonObject.put("dateModified", sendViewPrescriptionRequestForm.getDateModified());
            jsonObject.put("prescriptionId", sendViewPrescriptionRequestForm.getPrescriptionId());
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

    public ViewPrescriptionRequest sharePrescriptionByPatient(User user,
                                                              SendViewPrescriptionRequestForm sendViewPrescriptionRequestForm) throws Exception {
        ViewPrescriptionRequest viewPrescriptionRequest = null;
        try {
            Contract contract = getContract(user);
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("senderId", sendViewPrescriptionRequestForm.getSenderId());
            jsonObject.put("recipientId", sendViewPrescriptionRequestForm.getRecipientId());
            jsonObject.put("dateCreated", sendViewPrescriptionRequestForm.getDateCreated());
            jsonObject.put("dateModified", sendViewPrescriptionRequestForm.getDateModified());
            jsonObject.put("prescriptionId", sendViewPrescriptionRequestForm.getPrescriptionId());
            byte[] result = contract.submitTransaction(
                    "sharePrescriptionByPatient",
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

    public ViewPrescriptionRequest defineViewPrescriptionRequest(User user, DefineViewPrescriptionRequestForm defineViewPrescriptionRequestForm) throws Exception {
        ViewPrescriptionRequest viewPrescriptionRequest = null;
        try {
            Contract contract = getContract(user);
            LOG.info("Submit Transaction: definePrescriptionRequest");
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("requestId", defineViewPrescriptionRequestForm.getRequestId());
            jsonObject.put("requestStatus", defineViewPrescriptionRequestForm.getRequestStatus());
            System.out.println(jsonObject.toString());
            byte[] result = contract.submitTransaction(
                    "defineViewPrescriptionRequest",
                    jsonObject.toString()
            );
            String viewPrescriptionRequestStr = new String(result);
            viewPrescriptionRequest = new Genson().deserialize(
                    viewPrescriptionRequestStr,
                    ViewPrescriptionRequest.class
            );
            LOG.info("result: " + viewPrescriptionRequestStr);
        } catch (Exception e) {
            formatExceptionMessage(e);
        }
        return viewPrescriptionRequest;
    }

    public Drug transferDrug(User user, TransferDrugDto transferDrugDto) throws Exception {
        Drug drug = null;
        try {
            Contract contract = getContract(user);
            LOG.info("Submit Transaction: transferDrug");
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("drugId", transferDrugDto.getDrugId());
            jsonObject.put("newOwnerId", transferDrugDto.getNewOwnerId());
            System.out.println(jsonObject.toString());
            byte[] result = contract.submitTransaction(
                    "transferDrug",
                    jsonObject.toString()
            );
            String drugStr = new String(result);
            drug = new Genson().deserialize(
                    drugStr,
                    Drug.class
            );
            LOG.info("result: " + drug);
        } catch (Exception e) {
            formatExceptionMessage(e);
        }
        return drug;
    }

    public List<Drug> addDrug(User user, AddDrugForm addDrugForm) throws Exception {
        List<Drug> drugList = null;
        try {
            Contract contract = getContract(user);
            JSONObject jsonDto = addDrugForm.toJSONObject();
            byte[] result = contract.submitTransaction(
                    "addDrug",
                    jsonDto.toString()
            );
            drugList = new Genson().deserialize(new String(result), new GenericType<List<Drug>>() {});
            LOG.info("result: " + drugList);
        } catch (Exception e) {
            formatExceptionMessage(e);
        }
        return drugList;
    }

    public Purchase addPurchase(User user, PurchaseDto addPurchaseDto) throws Exception {
        Purchase purchase = null;
        try {
            Contract contract = getContract(user);
            JSONObject jsonDto = addPurchaseDto.toJSONObject();
            byte[] result = contract.submitTransaction(
                    "addPurchase",
                    jsonDto.toString()
            );
            purchase = new Genson().deserialize(new String(result), Purchase.class);
            LOG.info("result: " + purchase);
        } catch (Exception e) {
            formatExceptionMessage(e);
        }
        return purchase;
    }

    public List<String> getListAllAuthorizedPatientForScientist(User user,
                                                                GetListAllAuthorizedPatientForScientistDto getListAllAuthorizedPatientForScientistDto) throws Exception {
        List<String> getListAllAuthorizedPatientForScientistList = new ArrayList<>();
        try {
            Contract contract = getContract(user);

            JSONObject jsonObject = getListAllAuthorizedPatientForScientistDto.toJSONObject();

            byte[] result = contract.evaluateTransaction(
                    "getListAllAuthorizedPatientForScientist",
                    jsonObject.toString()
            );

            String getListAllAuthorizedPatientForScientistListStr = new String(result);
            getListAllAuthorizedPatientForScientistList = new Genson().deserialize(
                    getListAllAuthorizedPatientForScientistListStr,
                    new GenericType<List<String>>() {}
            );
        } catch (Exception e) {
            formatExceptionMessage(e);
        }
        return getListAllAuthorizedPatientForScientistList;
    }

    public List<MedicalRecord> getListAuthorizedMedicalRecordByScientistQuery(User user,
                                                                              GetListAuthorizedMedicalRecordByScientistQueryDto getListAuthorizedMedicalRecordByScientistQueryDto) throws Exception {
        List<MedicalRecord> getListAuthorizedMedicalRecordByScientistQueryList = new ArrayList<>();
        try {
            Contract contract = getContract(user);

            JSONObject jsonObject = getListAuthorizedMedicalRecordByScientistQueryDto.toJSONObject();

            byte[] result = contract.evaluateTransaction(
                    "getListAuthorizedMedicalRecordByScientistQuery",
                    jsonObject.toString()
            );

            String getListAuthorizedMedicalRecordByScientistQueryListStr = new String(result);
            getListAuthorizedMedicalRecordByScientistQueryList = new Genson().deserialize(
                    getListAuthorizedMedicalRecordByScientistQueryListStr,
                    new GenericType<List<MedicalRecord>>() {}
            );
        } catch (Exception e) {
            formatExceptionMessage(e);
        }
        return getListAuthorizedMedicalRecordByScientistQueryList;
    }

    public Prescription updateDrugReactionFromPatient(User user, UpdateDrugReactionForm updateDrugReactionForm) throws Exception {
        Prescription prescription = null;
        try {
            JSONObject jsonObject = updateDrugReactionForm.toJSONObject();
            Contract contract = getContract(user);
            byte[] result = contract.submitTransaction(
                    "updateDrugReactionFromPatient",
                    jsonObject.toString()
            );
            prescription = new Genson().deserialize(new String(result), Prescription.class);
            LOG.info("result: " + prescription);
        } catch (Exception e) {
            formatExceptionMessage(e);
        }
        return prescription;
    }


    public InsuranceProduct addInsuranceProduct(User user, AddInsuranceProductForm addInsuranceProductForm) throws Exception {
        InsuranceProduct insuranceProduct = null;
        try {
            Contract contract = getContract(user);
            JSONObject jsonDto = addInsuranceProductForm.toJSONObject();
            byte[] result = contract.submitTransaction(
                    "addInsuranceProduct",
                    jsonDto.toString()
            );
            insuranceProduct = new Genson().deserialize(new String(result), InsuranceProduct.class);
            LOG.info("result: " + insuranceProduct);
        } catch (Exception e) {
            formatExceptionMessage(e);
        }
        return insuranceProduct;
    }

    public InsuranceProduct editInsuranceProduct(User user, EditInsuranceProductForm editInsuranceProductForm) throws Exception {
        InsuranceProduct insuranceProduct = null;
        try {
            Contract contract = getContract(user);
            JSONObject jsonDto = editInsuranceProductForm.toJSONObject();
            byte[] result = contract.submitTransaction(
                    "editInsuranceProduct",
                    jsonDto.toString()
            );
            insuranceProduct = new Genson().deserialize(new String(result), InsuranceProduct.class);
            LOG.info("result: " + insuranceProduct);
        } catch (Exception e) {
            formatExceptionMessage(e);
        }
        return insuranceProduct;
    }

    public List<InsuranceProduct> getListInsuranceProduct(
            User user,
            SearchInsuranceProductForm searchInsuranceProductForm
    ) throws Exception {
        List<InsuranceProduct> insuranceProductList = new ArrayList<>();
        try {
            Contract contract = getContract(user);

            Map<String, String> searchParams = prepareSearchInsuranceProductParams(searchInsuranceProductForm);

            JSONObject jsonObject = new JSONObject();

            for (Map.Entry<String, String> entry : searchParams.entrySet()) {
                jsonObject.put(entry.getKey(), entry.getValue());
            }

            byte[] result = contract.evaluateTransaction(
                    "getListInsuranceProduct",
                    jsonObject.toString()
            );

            String insuranceProductListStr = new String(result);
            insuranceProductList = new Genson().deserialize(
                    insuranceProductListStr,
                    new GenericType<List<InsuranceProduct>>() {
                    }
            );
            System.out.println(insuranceProductList);
        } catch (Exception e) {
            formatExceptionMessage(e);
        }
        return insuranceProductList;
    }

    private Map<String, String> prepareSearchInsuranceProductParams(SearchInsuranceProductForm searchInsuranceProductForm) {
        String insuranceProductId = searchInsuranceProductForm.getInsuranceProductId() == null ? "" : searchInsuranceProductForm.getInsuranceProductId();
        String insuranceProductName = searchInsuranceProductForm.getInsuranceProductName() == null ? "" : searchInsuranceProductForm.getInsuranceProductName();
        String insuranceCompanyId = searchInsuranceProductForm.getInsuranceCompanyId() == null ? "" : searchInsuranceProductForm.getInsuranceCompanyId();
        String dateCreated = searchInsuranceProductForm.getDateCreated() == null ? "" : searchInsuranceProductForm.getDateCreated();
        String dateModified = searchInsuranceProductForm.getDateModified() == null ? "" : searchInsuranceProductForm.getDateModified();
        String description = searchInsuranceProductForm.getDescription() == null ? "" : searchInsuranceProductForm.getDescription();
        String hashFile = searchInsuranceProductForm.getHashFile() == null ? "" : searchInsuranceProductForm.getHashFile();

        String from;
        if (searchInsuranceProductForm.getFrom() == null) {
            Calendar calendar = Calendar.getInstance();
            calendar.add(Calendar.MONTH, -6);
            from = StringUtil.parseDate(calendar.getTime());
        } else {
            from = StringUtil.parseDate(searchInsuranceProductForm.getFrom());
        }
        String until;
        if (searchInsuranceProductForm.getFrom() == null) {
            Calendar calendar = Calendar.getInstance();
            calendar.add(Calendar.MONTH, +6);
            until = StringUtil.parseDate(calendar.getTime());
        } else {
            until = StringUtil.parseDate(searchInsuranceProductForm.getUntil());
        }
        String sortingOrder = searchInsuranceProductForm.getSortingOrder() == null ? "desc" : searchInsuranceProductForm.getSortingOrder();

        return new HashMap<String, String>() {{
            put("insuranceProductId", insuranceProductId);
            put("insuranceProductName", insuranceProductName);
            put("insuranceCompanyId", insuranceCompanyId);
            put("dateCreated", dateCreated);
            put("dateModified", dateModified);
            put("description", description);
            put("hashFile", hashFile);
            put("from", from);
            put("until", until);
            put("sortingOrder", sortingOrder);
        }};
    }

    public PurchaseRequest sendPurchaseRequest(User user, SendPurchaseRequestForm sendPurchaseRequestForm)
            throws Exception {
        PurchaseRequest purchaseRequest = null;
        try {
            Contract contract = getContract(user);
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("senderId", sendPurchaseRequestForm.getSenderId());
            jsonObject.put("recipientId", sendPurchaseRequestForm.getRecipientId());
            jsonObject.put("dateCreated", sendPurchaseRequestForm.getDateCreated());
            jsonObject.put("dateModified", sendPurchaseRequestForm.getDateModified());
            jsonObject.put("insuranceProductId", sendPurchaseRequestForm.getInsuranceProductId());
            jsonObject.put("startDate", sendPurchaseRequestForm.getStartDate());

            byte[] result = contract.submitTransaction(
                    "sendPurchaseRequest",
                    jsonObject.toString()
            );
            String purchaseRequestStr = new String(result);
            purchaseRequest = new Genson().deserialize(purchaseRequestStr, PurchaseRequest.class);
            LOG.info("result: " + purchaseRequest);
        } catch (Exception e) {
            formatExceptionMessage(e);
        }
        return purchaseRequest;
    }

    public PurchaseRequest definePurchaseRequest(User user, DefinePurchaseRequestForm definePurchaseRequestForm) throws Exception {
        PurchaseRequest purchaseRequest = null;
        try {
            Contract contract = getContract(user);
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("requestId", definePurchaseRequestForm.getRequestId());
            jsonObject.put("requestStatus", definePurchaseRequestForm.getRequestStatus());
            jsonObject.put("hashFile", definePurchaseRequestForm.getHashFile());
            jsonObject.put("dateCreated", definePurchaseRequestForm.getDateCreated());
            jsonObject.put("dateModified", definePurchaseRequestForm.getDateModified());

            byte[] result = contract.submitTransaction(
                    "definePurchaseRequest",
                    jsonObject.toString()
            );
            String purchaseRequestStr = new String(result);
            purchaseRequest = new Genson().deserialize(purchaseRequestStr, PurchaseRequest.class);
            LOG.info("result: " + purchaseRequest);
        } catch (Exception e) {
            formatExceptionMessage(e);
        }
        return purchaseRequest;
    }

    public List<InsuranceContract> getListInsuranceContractByPatientQuery(User user,
                                                                          SearchInsuranceContractForm searchInsuranceContractForm) throws Exception {
        List<InsuranceContract> insuranceContractList = new ArrayList<>();
        try {
            Contract contract = getContract(user);

            Map<String, String> searchParams = prepareSearchInsuranceContractParams(searchInsuranceContractForm);

            JSONObject jsonObject = new JSONObject();

            for (Map.Entry<String, String> entry : searchParams.entrySet()) {
                jsonObject.put(entry.getKey(), entry.getValue());
            }

            byte[] result = contract.evaluateTransaction(
                    "getListInsuranceContractByPatientQuery",
                    jsonObject.toString()
            );

            String insuranceContractListStr = new String(result);
            insuranceContractList = new Genson().deserialize(
                    insuranceContractListStr,
                    new GenericType<List<InsuranceContract>>() {}
            );

            LOG.info("result: " + insuranceContractList);

        } catch (Exception e) {
            formatExceptionMessage(e);
        }
        return insuranceContractList;
    }

    private Map<String, String> prepareSearchInsuranceContractParams(SearchInsuranceContractForm searchInsuranceContractForm) {
        String insuranceContractId = searchInsuranceContractForm.getInsuranceContractId() == null ? "" : searchInsuranceContractForm.getInsuranceContractId();
        String insuranceProductId = searchInsuranceContractForm.getInsuranceProductId() == null ? "" : searchInsuranceContractForm.getInsuranceProductId();
        String patientId = searchInsuranceContractForm.getPatientId() == null ? "" : searchInsuranceContractForm.getPatientId();
        String insuranceCompanyId = searchInsuranceContractForm.getInsuranceCompanyId() == null ? "" : searchInsuranceContractForm.getInsuranceCompanyId();
        String startDate = searchInsuranceContractForm.getStartDate() == null ? "" : searchInsuranceContractForm.getStartDate();
        String endDate = searchInsuranceContractForm.getEndDate() == null ? "" : searchInsuranceContractForm.getEndDate();
        String hashFile = searchInsuranceContractForm.getHashFile() == null ? "" : searchInsuranceContractForm.getHashFile();
        String from;
        if (searchInsuranceContractForm.getFrom() == null) {
            Calendar calendar = Calendar.getInstance();
            calendar.add(Calendar.MONTH, -6);
            from = StringUtil.parseDate(calendar.getTime());
        } else {
            from = StringUtil.parseDate(searchInsuranceContractForm.getFrom());
        }
        String until;
        if (searchInsuranceContractForm.getFrom() == null) {
            Calendar calendar = Calendar.getInstance();
            calendar.add(Calendar.MONTH, +6);
            until = StringUtil.parseDate(calendar.getTime());
        } else {
            until = StringUtil.parseDate(searchInsuranceContractForm.getUntil());
        }
        String sortingOrder = searchInsuranceContractForm.getSortingOrder() == null ? "desc" : searchInsuranceContractForm.getSortingOrder();

        return new HashMap<String, String>() {{
            put("insuranceContractId", insuranceContractId);
            put("insuranceProductId", insuranceProductId);
            put("patientId", patientId);
            put("insuranceCompanyId", insuranceCompanyId);
            put("startDate", startDate);
            put("endDate", endDate);
            put("hashFile", hashFile);
            put("from", from);
            put("until", until);
            put("sortingOrder", sortingOrder);
        }};
    }

    public PaymentRequest sendPaymentRequest(User user, SendPaymentRequestForm sendPaymentRequestForm) throws Exception {
        PaymentRequest paymentRequest = null;
        try {
            Contract contract = getContract(user);
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("senderId", sendPaymentRequestForm.getSenderId());
            jsonObject.put("recipientId", sendPaymentRequestForm.getRecipientId());
            jsonObject.put("dateCreated", sendPaymentRequestForm.getDateCreated());
            jsonObject.put("dateModified", sendPaymentRequestForm.getDateModified());
            jsonObject.put("insuranceContractId", sendPaymentRequestForm.getInsuranceContractId());
            jsonObject.put("medicalRecordId", sendPaymentRequestForm.getMedicalRecordId());

            byte[] result = contract.submitTransaction(
                    "sendPaymentRequest",
                    jsonObject.toString()
            );

            String paymentRequestStr = new String(result);
            paymentRequest = new Genson().deserialize(paymentRequestStr, PaymentRequest.class);
            LOG.info("result: " + paymentRequest);
        } catch (Exception e) {
            formatExceptionMessage(e);
        }
        return paymentRequest;
    }

    public PaymentRequest definePaymentRequest(User user, DefinePaymentRequestForm definePaymentRequestForm) throws Exception {
        PaymentRequest paymentRequest = null;
        try {
            Contract contract = getContract(user);
            LOG.info("Submit Transaction: definePaymentRequest");
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("requestId", definePaymentRequestForm.getRequestId());
            jsonObject.put("requestStatus", definePaymentRequestForm.getRequestStatus());
            jsonObject.put("dateCreated", definePaymentRequestForm.getDateCreated());
            jsonObject.put("dateModified", definePaymentRequestForm.getDateModified());
            System.out.println(jsonObject.toString());

            byte[] result = contract.submitTransaction(
                    "definePaymentRequest",
                    jsonObject.toString()
            );
            String paymentRequestStr = new String(result);
            paymentRequest = new Genson().deserialize(paymentRequestStr, PaymentRequest.class);
            LOG.info("result: " + paymentRequest);
        } catch (Exception e) {
            formatExceptionMessage(e);
        }
        return paymentRequest;
    }

    public List<ConfirmPaymentRequest> getListConfirmPaymentRequestBySenderQuery(
            User user,
            SearchConfirmPaymentRequestForm searchConfirmPaymentRequestForm
    ) throws Exception {
        List<ConfirmPaymentRequest> confirmPaymentRequestList = new ArrayList<>();
        try {
            Contract contract = getContract(user);

            Map<String, String> searchParams = prepareSearchConfirmPaymentRequestParams(searchConfirmPaymentRequestForm);

            JSONObject jsonObject = new JSONObject();

            for (Map.Entry<String, String> entry : searchParams.entrySet()) {
                jsonObject.put(entry.getKey(), entry.getValue());
            }

            byte[] result = contract.evaluateTransaction(
                    "getListConfirmPaymentRequestBySenderQuery",
                    jsonObject.toString()
            );

            String confirmPaymentRequestListStr = new String(result);
            confirmPaymentRequestList = new Genson().deserialize(
                    confirmPaymentRequestListStr,
                    new GenericType<List<ConfirmPaymentRequest>>() {}
            );

            LOG.info("result: " + confirmPaymentRequestList);

        } catch (Exception e) {
            formatExceptionMessage(e);
        }
        return confirmPaymentRequestList;
    }

    public List<ConfirmPaymentRequest> getListConfirmPaymentRequestByRecipientQuery(
            User user,
            SearchConfirmPaymentRequestForm searchConfirmPaymentRequestForm
    ) throws Exception {
        List<ConfirmPaymentRequest> confirmPaymentRequestList = new ArrayList<>();
        try {
            Contract contract = getContract(user);

            Map<String, String> searchParams = prepareSearchConfirmPaymentRequestParams(searchConfirmPaymentRequestForm);

            JSONObject jsonObject = new JSONObject();

            for (Map.Entry<String, String> entry : searchParams.entrySet()) {
                jsonObject.put(entry.getKey(), entry.getValue());
            }

            byte[] result = contract.evaluateTransaction(
                    "getListConfirmPaymentRequestByRecipientQuery",
                    jsonObject.toString()
            );

            String confirmPaymentRequestListStr = new String(result);
            confirmPaymentRequestList = new Genson().deserialize(
                    confirmPaymentRequestListStr,
                    new GenericType<List<ConfirmPaymentRequest>>() {}
            );

            LOG.info("result: " + confirmPaymentRequestList);

        } catch (Exception e) {
            formatExceptionMessage(e);
        }
        return confirmPaymentRequestList;
    }

    public AppointmentRequest getAppointmentRequest(
            User user,
            String requestId
    ) throws Exception {
        AppointmentRequest request = new AppointmentRequest();
        try {
            Contract contract = getContract(user);

            JSONObject jsonObject = new JSONObject();

            jsonObject.put("requestId", requestId);

            byte[] result = contract.evaluateTransaction(
                    "getAppointmentRequest",
                    jsonObject.toString()
            );

            String requestStr = new String(result);
            request = new Genson().deserialize(requestStr, AppointmentRequest.class);

            LOG.info("result: " + request);

        } catch (Exception e) {
            formatExceptionMessage(e);
        }
        return request;
    }

    public ViewRequest getViewRequest(
            User user,
            String requestId
    ) throws Exception {
        ViewRequest request = new ViewRequest();
        try {
            Contract contract = getContract(user);

            JSONObject jsonObject = new JSONObject();

            jsonObject.put("requestId", requestId);

            byte[] result = contract.evaluateTransaction(
                    "getViewRequest",
                    jsonObject.toString()
            );

            String requestStr = new String(result);
            request = new Genson().deserialize(requestStr, ViewRequest.class);

            LOG.info("result: " + request);

        } catch (Exception e) {
            formatExceptionMessage(e);
        }
        return request;
    }

    public ViewPrescriptionRequest getViewPrescriptionRequest(
            User user,
            String requestId
    ) throws Exception {
        ViewPrescriptionRequest request = new ViewPrescriptionRequest();
        try {
            Contract contract = getContract(user);

            JSONObject jsonObject = new JSONObject();

            jsonObject.put("requestId", requestId);

            byte[] result = contract.evaluateTransaction(
                    "getViewPrescriptionRequest",
                    jsonObject.toString()
            );

            String requestStr = new String(result);
            request = new Genson().deserialize(requestStr, ViewPrescriptionRequest.class);

            LOG.info("result: " + request);

        } catch (Exception e) {
            formatExceptionMessage(e);
        }
        return request;
    }

    public PurchaseRequest getPurchaseRequest(
            User user,
            String requestId
    ) throws Exception {
        PurchaseRequest request = new PurchaseRequest();
        try {
            Contract contract = getContract(user);

            JSONObject jsonObject = new JSONObject();

            jsonObject.put("requestId", requestId);

            byte[] result = contract.evaluateTransaction(
                    "getPurchaseRequest",
                    jsonObject.toString()
            );

            String requestStr = new String(result);
            request = new Genson().deserialize(requestStr, PurchaseRequest.class);

            LOG.info("result: " + request);

        } catch (Exception e) {
            formatExceptionMessage(e);
        }
        return request;
    }

    public PaymentRequest getPaymentRequest(
            User user,
            String requestId
    ) throws Exception {
        PaymentRequest request = new PaymentRequest();
        try {
            Contract contract = getContract(user);

            JSONObject jsonObject = new JSONObject();

            jsonObject.put("requestId", requestId);

            byte[] result = contract.evaluateTransaction(
                    "getPaymentRequest",
                    jsonObject.toString()
            );

            String requestStr = new String(result);
            request = new Genson().deserialize(requestStr, PaymentRequest.class);

            LOG.info("result: " + request);

        } catch (Exception e) {
            formatExceptionMessage(e);
        }
        return request;
    }

    public ConfirmPaymentRequest getConfirmPaymentRequest(
            User user,
            String requestId
    ) throws Exception {
        ConfirmPaymentRequest request = new ConfirmPaymentRequest();
        try {
            Contract contract = getContract(user);

            JSONObject jsonObject = new JSONObject();

            jsonObject.put("requestId", requestId);

            byte[] result = contract.evaluateTransaction(
                    "getConfirmPaymentRequest",
                    jsonObject.toString()
            );

            String requestStr = new String(result);
            request = new Genson().deserialize(requestStr, ConfirmPaymentRequest.class);

            LOG.info("result: " + request);

        } catch (Exception e) {
            formatExceptionMessage(e);
        }
        return request;
    }

    public List<Request> getAllRequest(
            User user,
            String userId
    ) throws Exception {
        List<Request> requestList = new ArrayList<>();
        try {
            Contract contract = getContract(user);

            JSONObject jsonObject = new JSONObject();

            jsonObject.put("userId", userId);

            byte[] result = contract.evaluateTransaction(
                    "getListAllRequestByUserQuery",
                    jsonObject.toString()
            );

            String requestListStr = new String(result);
            requestList = new Genson().deserialize(
                    requestListStr,
                    new GenericType<List<Request>>() {}
            );

            LOG.info("result: " + requestList);

        } catch (Exception e) {
            formatExceptionMessage(e);
        }
        return requestList;
    }

    private Map<String, String> prepareSearchConfirmPaymentRequestParams(SearchConfirmPaymentRequestForm searchConfirmPaymentRequestForm) {
        String requestId = searchConfirmPaymentRequestForm.getRequestId() == null ? "" : searchConfirmPaymentRequestForm.getRequestId();
        String senderId = searchConfirmPaymentRequestForm.getSenderId() == null ? "" : searchConfirmPaymentRequestForm.getSenderId();
        String recipientId = searchConfirmPaymentRequestForm.getRecipientId() == null ? "" : searchConfirmPaymentRequestForm.getRecipientId();
        String dateCreated = searchConfirmPaymentRequestForm.getDateCreated() == null ? "" : searchConfirmPaymentRequestForm.getDateCreated();
        String dateModified = searchConfirmPaymentRequestForm.getDateModified() == null ? "" : searchConfirmPaymentRequestForm.getDateModified();
        String requestType = searchConfirmPaymentRequestForm.getRequestType() == null ? "" : searchConfirmPaymentRequestForm.getRequestType();
        String requestStatus = searchConfirmPaymentRequestForm.getRequestStatus() == null ? "" : searchConfirmPaymentRequestForm.getRequestStatus();
        String paymentRequestId = searchConfirmPaymentRequestForm.getPaymentRequestId() == null ? "" : searchConfirmPaymentRequestForm.getPaymentRequestId();
        String from;
        if (searchConfirmPaymentRequestForm.getFrom() == null) {
            Calendar calendar = Calendar.getInstance();
            calendar.add(Calendar.MONTH, -6);
            from = StringUtil.parseDate(calendar.getTime());
        } else {
            from = StringUtil.parseDate(searchConfirmPaymentRequestForm.getFrom());
        }
        String until;
        if (searchConfirmPaymentRequestForm.getFrom() == null) {
            Calendar calendar = Calendar.getInstance();
            calendar.add(Calendar.MONTH, +6);
            until = StringUtil.parseDate(calendar.getTime());
        } else {
            until = StringUtil.parseDate(searchConfirmPaymentRequestForm.getUntil());
        }
        String sortingOrder = searchConfirmPaymentRequestForm.getSortingOrder() == null ? "desc" : searchConfirmPaymentRequestForm.getSortingOrder();

        return new HashMap<String, String>() {{
            put("requestId", requestId);
            put("senderId", senderId);
            put("recipientId", recipientId);
            put("requestType", requestType);
            put("from", from);
            put("until", until);
            put("dateCreated", dateCreated);
            put("dateModified", dateModified);
            put("requestStatus", requestStatus);
            put("sortingOrder", sortingOrder);
            put("paymentRequestId", paymentRequestId);
        }};
    }

    public ConfirmPaymentRequest defineConfirmPaymentRequest(User user,
                                                             DefineConfirmPaymentRequestForm defineConfirmPaymentRequestForm) throws Exception {
        ConfirmPaymentRequest confirmPaymentRequest = null;
        try {
            Contract contract = getContract(user);
            LOG.info("Submit Transaction: defineConfirmPaymentRequest");
            JSONObject jsonObject = defineConfirmPaymentRequestForm.toJSONObject();
            System.out.println(jsonObject.toString());

            byte[] result = contract.submitTransaction(
                    "defineConfirmPaymentRequest",
                    jsonObject.toString()
            );
            String confirmPaymentRequestStr = new String(result);
            confirmPaymentRequest = new Genson().deserialize(confirmPaymentRequestStr, ConfirmPaymentRequest.class);
            LOG.info("result: " + confirmPaymentRequest);
        } catch (Exception e) {
            formatExceptionMessage(e);
        }
        return confirmPaymentRequest;
    }
}

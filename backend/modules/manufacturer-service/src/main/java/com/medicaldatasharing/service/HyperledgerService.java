package com.medicaldatasharing.service;

import com.medicaldatasharing.chaincode.Config;
import com.medicaldatasharing.chaincode.client.RegisterUserHyperledger;
import com.medicaldatasharing.chaincode.dto.*;
import com.medicaldatasharing.chaincode.util.ConnectionParamsUtil;
import com.medicaldatasharing.chaincode.util.WalletUtil;
import com.medicaldatasharing.dto.*;
import com.medicaldatasharing.form.*;
import com.medicaldatasharing.model.User;
import com.medicaldatasharing.util.Constants;
import com.medicaldatasharing.util.StringUtil;
import com.owlike.genson.GenericType;
import com.owlike.genson.Genson;
import lombok.SneakyThrows;
import org.hyperledger.fabric.gateway.Contract;
import org.hyperledger.fabric.gateway.Gateway;
import org.hyperledger.fabric.gateway.Identity;
import org.hyperledger.fabric.gateway.Network;
import org.hyperledger.fabric.sdk.BlockEvent;
import org.hyperledger.fabric.sdk.Channel;
import org.hyperledger.fabric.sdk.exception.InvalidArgumentException;
import org.json.JSONObject;
import org.springframework.stereotype.Service;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import java.util.logging.Logger;

@Service
public class HyperledgerService {
    private final static Logger LOG = Logger.getLogger(HyperledgerService.class.getName());

    static {
        System.setProperty("org.hyperledger.fabric.sdk.service_discovery.as_localhost", "true");
    }

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

    public MedicalRecord addMedicalRecord(User user, AddMedicalRecordForm addMedicalRecordForm) throws Exception {
        MedicalRecord medicalRecord = null;
        try {
            Contract contract = getContract(user);
            LOG.info("Submit Transaction: AddMedicalRecord");

            addMedicalRecordForm.encrypt();

            JSONObject jsonObject = addMedicalRecordForm.toJSONObject();

            byte[] result = contract.submitTransaction(
                    "addMedicalRecord",
                    jsonObject.toString()
            );
            String medicalRecordStr = new String(result);
            medicalRecord = new Genson().deserialize(medicalRecordStr, MedicalRecord.class);

            medicalRecord.decrypt();
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
            medicalRecord.decrypt();
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
            medicalRecord.decrypt();
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

            for (MedicalRecord medicalRecord: getListAuthorizedMedicalRecordByDoctorQueryList) {
                medicalRecord.decrypt();
            }
        } catch (Exception e) {
            formatExceptionMessage(e);
        }
        return getListAuthorizedMedicalRecordByDoctorQueryList;
    }

    public List<MedicalRecord> getListAuthorizedMedicalRecordByManufacturerQuery(User user,
                                                                           GetListAuthorizedMedicalRecordByManufacturerQueryDto getListAuthorizedMedicalRecordByManufacturerQueryDto) throws Exception {
        List<MedicalRecord> getListAuthorizedMedicalRecordByManufacturerQueryList = new ArrayList<>();
        try {
            Contract contract = getContract(user);

            JSONObject jsonObject = getListAuthorizedMedicalRecordByManufacturerQueryDto.toJSONObject();

            byte[] result = contract.evaluateTransaction(
                    "getListAuthorizedMedicalRecordByManufacturerQuery",
                    jsonObject.toString()
            );

            String getListAuthorizedMedicalRecordByManufacturerQueryListStr = new String(result);
            getListAuthorizedMedicalRecordByManufacturerQueryList = new Genson().deserialize(
                    getListAuthorizedMedicalRecordByManufacturerQueryListStr,
                    new GenericType<List<MedicalRecord>>() {}
            );

            for (MedicalRecord medicalRecord: getListAuthorizedMedicalRecordByManufacturerQueryList) {
                medicalRecord.decrypt();
            }
        } catch (Exception e) {
            formatExceptionMessage(e);
        }
        return getListAuthorizedMedicalRecordByManufacturerQueryList;
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

            for (DrugReactionDto drugReactionDto: getListDrugReactionByManufacturerList) {
                drugReactionDto.decrypt();
            }
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
            medicalRecord.decrypt();
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

            searchMedicalRecordForm.encrypt();

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

            for (MedicalRecord medicalRecord: medicalRecordList) {
                medicalRecord.decrypt();
            }
            LOG.info("result: " + medicalRecordList);

        } catch (Exception e) {
            formatExceptionMessage(e);
        }
        return medicalRecordList;
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
            changeHistory = new Genson().deserialize(new String(result), new GenericType<List<MedicalRecord>>() {});
            for (MedicalRecord medicalRecord: changeHistory) {
                medicalRecord.decrypt();
            }
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
            addMedicationForm.encrypt();
            JSONObject jsonDto = addMedicationForm.toJSONObject();
            byte[] result = contract.submitTransaction(
                    "addMedication",
                    jsonDto.toString()
            );
            medication = new Genson().deserialize(new String(result), Medication.class);
            medication.decrypt();
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
            editMedicationForm.encrypt();
            JSONObject jsonDto = editMedicationForm.toJSONObject();
            byte[] result = contract.submitTransaction(
                    "editMedication",
                    jsonDto.toString()
            );
            medication = new Genson().deserialize(new String(result), Medication.class);
            medication.decrypt();
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

            for (Medication medication: medicationList) {
                medication.decrypt();
            }

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

            searchMedicationForm.encrypt();
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

            for (Medication medication: medicationList) {
                medication.decrypt();
            }
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
            prescriptionDto.decrypt();
        } catch (Exception e) {
            formatExceptionMessage(e);
        }
        return prescriptionDto;
    }

    public PrescriptionDto getPrescriptionByDrugStore(User user,
                                                      GetPrescriptionForm getPrescriptionForm) throws Exception {
        PrescriptionDto prescriptionDto = null;
        try {
            Contract contract = getContract(user);
            JSONObject jsonDto = getPrescriptionForm.toJSONObject();
            byte[] result = contract.submitTransaction(
                    "getPrescriptionByDrugStore",
                    jsonDto.toString()
            );
            prescriptionDto = new Genson().deserialize(new String(result), PrescriptionDto.class);
            prescriptionDto.decrypt();
            LOG.info("result: " + prescriptionDto);
        } catch (Exception e) {
            formatExceptionMessage(e);
        }
        return prescriptionDto;
    }

    public PrescriptionDto getPrescriptionByManufacturer(User user,
                                                         GetPrescriptionForm getPrescriptionForm) throws Exception {
        PrescriptionDto prescriptionDto = null;
        try {
            Contract contract = getContract(user);
            JSONObject jsonDto = getPrescriptionForm.toJSONObject();
            byte[] result = contract.submitTransaction(
                    "getPrescriptionByManufacturer",
                    jsonDto.toString()
            );
            prescriptionDto = new Genson().deserialize(new String(result), PrescriptionDto.class);
            prescriptionDto.decrypt();
            LOG.info("result: " + prescriptionDto);
        } catch (Exception e) {
            formatExceptionMessage(e);
        }
        return prescriptionDto;
    }

    public PrescriptionDto getPrescriptionByDoctor(User user, GetPrescriptionForm getPrescriptionForm) throws Exception {
        PrescriptionDto prescriptionDto = null;
        try {
            Contract contract = getContract(user);
            JSONObject jsonDto = getPrescriptionForm.toJSONObject();
            byte[] result = contract.submitTransaction(
                    "getPrescriptionByDoctor",
                    jsonDto.toString()
            );
            prescriptionDto = new Genson().deserialize(new String(result), PrescriptionDto.class);
            prescriptionDto.decrypt();
            LOG.info("result: " + prescriptionDto);
        } catch (Exception e) {
            formatExceptionMessage(e);
        }
        return prescriptionDto;
    }

    public PrescriptionDto getPrescriptionByScientist(User user,
                                                      GetPrescriptionForm getPrescriptionForm) throws Exception {
        PrescriptionDto prescriptionDto = null;
        try {
            Contract contract = getContract(user);
            JSONObject jsonDto = getPrescriptionForm.toJSONObject();
            byte[] result = contract.submitTransaction(
                    "getPrescriptionByScientist",
                    jsonDto.toString()
            );
            prescriptionDto = new Genson().deserialize(new String(result), PrescriptionDto.class);
            prescriptionDto.decrypt();
            LOG.info("result: " + prescriptionDto);
        } catch (Exception e) {
            formatExceptionMessage(e);
        }
        return prescriptionDto;
    }

    public ViewPrescriptionRequest sendViewPrescriptionRequest(User user,
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

    public ViewPrescriptionRequest defineViewPrescriptionRequest(User user,
                                                                 DefineViewPrescriptionRequestForm defineViewPrescriptionRequestForm) throws Exception {
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

    public List<String> getListAllAuthorizedPatientForDoctor(User user,
                                                             GetListAllAuthorizedPatientForDoctorDto getListAllAuthorizedPatientForDoctorDto) throws Exception {
        List<String> getListAllAuthorizedPatientForDoctorList = new ArrayList<>();
        try {
            Contract contract = getContract(user);

            JSONObject jsonObject = getListAllAuthorizedPatientForDoctorDto.toJSONObject();

            byte[] result = contract.evaluateTransaction(
                    "getListAllAuthorizedPatientForDoctor",
                    jsonObject.toString()
            );

            String getListAllAuthorizedPatientForDoctorListStr = new String(result);
            getListAllAuthorizedPatientForDoctorList = new Genson().deserialize(
                    getListAllAuthorizedPatientForDoctorListStr,
                    new GenericType<List<String>>() {}
            );
        } catch (Exception e) {
            formatExceptionMessage(e);
        }
        return getListAllAuthorizedPatientForDoctorList;
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

    public List<String> getListAllAuthorizedPatientForManufacturer(User user,
                                                                   GetListAllAuthorizedPatientForManufacturerDto getListAllAuthorizedPatientForManufacturerDto) throws Exception {
        List<String> getListAllAuthorizedPatientForManufacturerList = new ArrayList<>();
        try {
            Contract contract = getContract(user);

            JSONObject jsonObject = getListAllAuthorizedPatientForManufacturerDto.toJSONObject();

            byte[] result = contract.evaluateTransaction(
                    "getListAllAuthorizedPatientForManufacturer",
                    jsonObject.toString()
            );

            String getListAllAuthorizedPatientForManufacturerListStr = new String(result);
            getListAllAuthorizedPatientForManufacturerList = new Genson().deserialize(
                    getListAllAuthorizedPatientForManufacturerListStr,
                    new GenericType<List<String>>() {}
            );
        } catch (Exception e) {
            formatExceptionMessage(e);
        }
        return getListAllAuthorizedPatientForManufacturerList;
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

            for (MedicalRecord medicalRecord: getListAuthorizedMedicalRecordByScientistQueryList) {
                medicalRecord.decrypt();
            }
        } catch (Exception e) {
            formatExceptionMessage(e);
        }
        return getListAuthorizedMedicalRecordByScientistQueryList;
    }

    public Prescription updateDrugReactionFromPatient(User user, UpdateDrugReactionForm updateDrugReactionForm) throws Exception {
        Prescription prescription = null;
        try {
            updateDrugReactionForm.encrypt();
            JSONObject jsonObject = updateDrugReactionForm.toJSONObject();
            Contract contract = getContract(user);
            byte[] result = contract.submitTransaction(
                    "updateDrugReactionFromPatient",
                    jsonObject.toString()
            );
            prescription = new Genson().deserialize(new String(result), Prescription.class);
            prescription.decrypt();
            LOG.info("result: " + prescription);
        } catch (Exception e) {
            formatExceptionMessage(e);
        }
        return prescription;
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

    public List<Drug> transferDrugs(User user, TransferDrugsForm transferDrugsForm) throws Exception {
        List<Drug> drugList = null;
        try {
            Contract contract = getContract(user);
            JSONObject jsonDto = transferDrugsForm.toJSONObject();
            byte[] result = contract.submitTransaction(
                    "transferDrugs",
                    jsonDto.toString()
            );
            drugList = new Genson().deserialize(new String(result), new GenericType<List<Drug>>() {});
            LOG.info("result: " + drugList);
        } catch (Exception e) {
            formatExceptionMessage(e);
        }
        return drugList;
    }

    public Medication getMedication(User user, String medicationId) throws Exception {
        Medication medication = null;
        try {
            Contract contract = getContract(user);

            JSONObject jsonObject = new JSONObject();
            jsonObject.put("medicationId", medicationId);

            byte[] result = contract.evaluateTransaction(
                    "getMedication",
                    jsonObject.toString()
            );

            String medicationStr = new String(result);
            medication = new Genson().deserialize(
                    medicationStr,
                    new GenericType<Medication>() {
                    }
            );

            medication.decrypt();
            LOG.info("result: " + medication);
        } catch (Exception e) {
            formatExceptionMessage(e);
        }
        return medication;
    }

}

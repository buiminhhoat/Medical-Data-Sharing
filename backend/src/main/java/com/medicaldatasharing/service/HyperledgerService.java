package com.medicaldatasharing.service;

import com.medicaldatasharing.chaincode.Config;
import com.medicaldatasharing.chaincode.client.RegisterUserHyperledger;
import com.medicaldatasharing.chaincode.dto.*;
import com.medicaldatasharing.chaincode.util.ConnectionParamsUtil;
import com.medicaldatasharing.chaincode.util.WalletUtil;
import com.medicaldatasharing.dto.MedicalRecordDto;
import com.medicaldatasharing.dto.MedicalRecordPreviewDto;
import com.medicaldatasharing.form.*;
import com.medicaldatasharing.model.Doctor;
import com.medicaldatasharing.model.MedicalInstitution;
import com.medicaldatasharing.model.User;
import com.medicaldatasharing.repository.MedicalInstitutionRepository;
import com.medicaldatasharing.util.Constants;
import com.medicaldatasharing.util.StringUtil;
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
            byte[] result = contract.submitTransaction(
                    "addMedicalRecord",
                    medicalRecordDto.getRequestId(),
                    medicalRecordDto.getPatientId(),
                    medicalRecordDto.getDoctorId(),
                    medicalRecordDto.getMedicalInstitutionId(),
                    medicalRecordDto.getDateCreated(),
                    medicalRecordDto.getTestName(),
                    medicalRecordDto.getDetails()
            );
            medicalRecord = MedicalRecord.deserialize(result);
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
            byte[] result = contract.evaluateTransaction(
                    "getMedicalRecord",
                    medicalRecordId
            );
            medicalRecord = MedicalRecord.deserialize(result);
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
            LOG.info("Evaluate Transaction: GetEditRequest");
            byte[] result = contract.evaluateTransaction(
                    "getEditRequest",
                    requestId
            );
            editRequest = EditRequest.deserialize(result);
            LOG.info("result: " + editRequest);
        } catch (Exception e) {
            formatExceptionMessage(e);
        }
        return editRequest;
    }

    public AppointmentRequest sendAppointmentRequest(
            User user,
            SendAppointmentRequestForm sendAppointmentRequestForm
    ) throws Exception {
        AppointmentRequest appointmentRequest = null;
        try {
            Contract contract = getContract(user);
            byte[] result = contract.submitTransaction(
                    "sendAppointmentRequest",
                    sendAppointmentRequestForm.getSenderId(),
                    sendAppointmentRequestForm.getRecipientId(),
                    sendAppointmentRequestForm.getDateCreated()
            );
            appointmentRequest = AppointmentRequest.deserialize(result);
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
            byte[] result = contract.submitTransaction(
                    "sendEditRequest",
                    sendEditRequestForm.getSenderId(),
                    sendEditRequestForm.getRecipientId(),
                    sendEditRequestForm.getDateCreated(),
                    sendEditRequestForm.getMedicalRecordJson()
            );

            editRequest = EditRequest.deserialize(result);
            LOG.info("result: " + editRequest);
        } catch (Exception e) {
            formatExceptionMessage(e);
        }
        return editRequest;
    }

    public AppointmentRequest defineRequest(
            User user,
            DefineRequestForm defineRequestForm
    ) throws Exception {
        AppointmentRequest appointmentRequest = null;
        try {
            Contract contract = getContract(user);
            byte[] result = contract.submitTransaction(
                    "defineRequest",
                    defineRequestForm.getRequestId(),
                    defineRequestForm.getRequestStatus(),
                    defineRequestForm.getAccessAvailableFrom(),
                    defineRequestForm.getAccessAvailableUntil()
            );
            appointmentRequest = AppointmentRequest.deserialize(result);
            LOG.info("result: " + appointmentRequest);
        } catch (Exception e) {
            formatExceptionMessage(e);
        }
        return appointmentRequest;
    }

    public MedicalRecord defineMedicalRecord(
            User user,
            DefineMedicalRecordForm defineMedicalRecordForm
    ) throws Exception {
        MedicalRecord medicalRecord = null;
        try {
            Contract contract = getContract(user);
            byte[] result = contract.submitTransaction(
                    "defineMedicalRecord",
                    defineMedicalRecordForm.getMedicalRecordId(),
                    defineMedicalRecordForm.getMedicalRecordStatus()
            );
            medicalRecord = MedicalRecord.deserialize(result);
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

            Map<String, String> searchParams = prepareSearchParams(searchMedicalRecordForm);

            byte[] result = contract.evaluateTransaction(
                    "getListMedicalRecordByPatientQuery",
                    searchParams.get("patientId"),
                    searchParams.get("doctorId"),
                    searchParams.get("medicalInstitutionId"),
                    searchParams.get("from"),
                    searchParams.get("until"),
                    searchParams.get("testName"),
                    searchParams.get("medicalRecordStatus"),
                    searchParams.get("details"),
                    searchParams.get("sortingOrder")
            );

            LOG.info(String.format(
                    "Evaluate Transaction: getMedicalRecordsByPatientId(%s, %s, %s, %s, %s, %s, %s, %s, %s), result: %s",
                    searchParams.get("patientId"),
                    searchParams.get("doctorId"),
                    searchParams.get("medicalInstitutionId"),
                    searchParams.get("from"),
                    searchParams.get("until"),
                    searchParams.get("testName"),
                    searchParams.get("medicalRecordStatus"),
                    searchParams.get("details"),
                    searchParams.get("sortingOrder"), new String(result)));

            MedicalRecordPreviewResponse medicalRecordPreviewResponse = MedicalRecordPreviewResponse.deserialize(result);
            LOG.info("result: " + medicalRecordPreviewResponse);
            for (MedicalRecordDto medicalRecordDto : medicalRecordPreviewResponse.getMedicalRecordDtoList()) {
                MedicalRecordPreviewDto medicalRecordPreviewDto = new MedicalRecordPreviewDto();

                medicalRecordPreviewDto.setMedicalRecordId(medicalRecordDto.getMedicalRecordId());
                medicalRecordPreviewDto.setPatientId(medicalRecordDto.getPatientId());
                medicalRecordPreviewDto.setDoctorId(medicalRecordDto.getDoctorId());
                medicalRecordPreviewDto.setMedicalInstitutionId(medicalRecordDto.getMedicalInstitutionId());
                medicalRecordPreviewDto.setDateCreated(medicalRecordDto.getDateCreated());
                medicalRecordPreviewDto.setTestName(medicalRecordDto.getTestName());
                medicalRecordPreviewDto.setDetails(medicalRecordDto.getDetails());
                medicalRecordPreviewDto.setMedicalRecordStatus(medicalRecordDto.getMedicalRecordStatus());
                medicalRecordPreviewDto.setChangeHistory(medicalRecordDto.getChangeHistory());
                medicalRecordPreviewDtoList.add(medicalRecordPreviewDto);
            }

        } catch (Exception e) {
            formatExceptionMessage(e);
        }
        return medicalRecordPreviewDtoList;
    }

    private Map<String, String> prepareSearchParams(SearchMedicalRecordForm searchMedicalRecordForm) {
        String patientId = searchMedicalRecordForm.getPatientId() == null ? "" : searchMedicalRecordForm.getPatientId();
        String doctorId = searchMedicalRecordForm.getDoctorId() == null ? "" : searchMedicalRecordForm.getDoctorId();
        String testName = searchMedicalRecordForm.getTestName() == null ? "" : searchMedicalRecordForm.getTestName();
        String medicalInstitutionId = searchMedicalRecordForm.getMedicalInstitutionId() == null ? "" : searchMedicalRecordForm.getMedicalInstitutionId();
        String medicalRecordStatus = searchMedicalRecordForm.getMedicalRecordStatus() == null ? "" : searchMedicalRecordForm.getMedicalRecordStatus();
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
            put("patientId", patientId);
            put("doctorId", doctorId);
            put("medicalInstitutionId", medicalInstitutionId);
            put("from", from);
            put("until", until);
            put("testName", testName);
            put("medicalRecordStatus", medicalRecordStatus);
            put("details", details);
            put("sortingOrder", sortingOrder);
        }};
    }

    private void formatExceptionMessage(Exception e) throws Exception {
        String msg = e.getMessage();
        String errorMsg = msg.substring(msg.lastIndexOf(":") + 1);
        throw new Exception(errorMsg);
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
        return null;
    }

    private MedicalInstitution getMedicalInstitution(User user) {
        Doctor doctor = (Doctor) user;
        return doctor.getMedicalInstitution();
    }


}

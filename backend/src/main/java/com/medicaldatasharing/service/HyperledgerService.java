package com.medicaldatasharing.service;

import com.medicaldatasharing.chaincode.Config;
import com.medicaldatasharing.chaincode.client.RegisterUserHyperledger;
import com.medicaldatasharing.chaincode.dto.MedicalRecord;
import com.medicaldatasharing.chaincode.dto.MedicalRecordAccessRequest;
import com.medicaldatasharing.chaincode.util.ConnectionParamsUtil;
import com.medicaldatasharing.chaincode.util.WalletUtil;
import com.medicaldatasharing.dto.MedicalRecordAccessSendRequestDto;
import com.medicaldatasharing.dto.MedicalRecordDto;
import com.medicaldatasharing.dto.form.MedicalRecordAccessSendRequestForm;
import com.medicaldatasharing.model.Doctor;
import com.medicaldatasharing.model.MedicalInstitution;
import com.medicaldatasharing.model.User;
import com.medicaldatasharing.util.Constants;
import lombok.SneakyThrows;
import org.bouncycastle.util.encoders.Hex;
import org.hyperledger.fabric.gateway.Contract;
import org.hyperledger.fabric.gateway.Gateway;
import org.hyperledger.fabric.gateway.Identity;
import org.hyperledger.fabric.gateway.Network;
import org.hyperledger.fabric.sdk.BlockEvent;
import org.hyperledger.fabric.sdk.Channel;
import org.hyperledger.fabric.sdk.exception.InvalidArgumentException;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
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
                    medicalRecordDto.getPatientId(),
                    medicalRecordDto.getDoctorId(),
                    medicalRecordDto.getMedicalInstitutionId(),
                    medicalRecordDto.getDateCreated(),
                    medicalRecordDto.getTestName(),
                    medicalRecordDto.getRelevantParameters()
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

    public MedicalRecordAccessRequest sendMedicalRecordAccessRequest(
            User user,
            MedicalRecordAccessSendRequestForm medicalRecordAccessSendRequestForm
    ) throws Exception {
        MedicalRecordAccessRequest medicalRecordAccessRequest = null;
        try {
            Contract contract = getContract(user);
            byte[] result = contract.submitTransaction(
                    "sendMedicalRecordAccessRequest",
                    medicalRecordAccessSendRequestForm.getPatientId(),
                    medicalRecordAccessSendRequestForm.getRequesterId(),
                    medicalRecordAccessSendRequestForm.getMedicalRecordId(),
                    medicalRecordAccessSendRequestForm.getDateCreated()
            );
            medicalRecordAccessRequest = MedicalRecordAccessRequest.deserialize(result);
            LOG.info("result: " + medicalRecordAccessRequest);
        } catch (Exception e) {
            formatExceptionMessage(e);
        }
        return medicalRecordAccessRequest;
    }

    public MedicalRecordAccessRequest defineMedicalRecordAccessRequest(
            User user,
            String medicalRecordAccessRequestId,
            String decision,
            String accessAvailableFrom,
            String accessAvailableUntil
    ) throws Exception {
        MedicalRecordAccessRequest medicalRecordAccessRequest = null;
        try {
            Contract contract = getContract(user);
            byte[] result = contract.submitTransaction(
                    "defineMedicalRecordAccessRequest",
                    medicalRecordAccessRequestId,
                    decision,
                    accessAvailableFrom,
                    accessAvailableUntil
            );
            medicalRecordAccessRequest = MedicalRecordAccessRequest.deserialize(result);
            LOG.info("result: " + medicalRecordAccessRequest);
        } catch (Exception e) {
            formatExceptionMessage(e);
        }
        return medicalRecordAccessRequest;
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

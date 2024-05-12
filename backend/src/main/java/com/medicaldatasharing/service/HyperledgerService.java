package com.medicaldatasharing.service;

import com.medicaldatasharing.chaincode.Config;
import com.medicaldatasharing.chaincode.client.RegisterUserHyperledger;
import com.medicaldatasharing.chaincode.dto.ChaincodeMedicalRecord;
import com.medicaldatasharing.chaincode.util.ConnectionParamsUtil;
import com.medicaldatasharing.chaincode.util.WalletUtil;
import com.medicaldatasharing.dto.MedicalRecordDto;
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

    public ChaincodeMedicalRecord addMedicalRecord(User user, MedicalRecordDto medicalRecordDto) throws Exception {
        String userWalletIdentity = user.getEmail();
        String userIdentity = user.getId();

        String org = determineOrg(user);
        Map<String, String> connectionConfigParams = ConnectionParamsUtil.setOrgConfigParams(org);
        String connectionProfilePath = connectionConfigParams.get("networkConfigPath");
        try (Gateway gateway = connect(userWalletIdentity, connectionProfilePath, userIdentity, org)) {

            // get the network and contract
            Network network = gateway.getNetwork(Config.CHANNEL_NAME);
            Contract contract = network.getContract(Config.CHAINCODE_NAME);

            byte[] result = contract.submitTransaction(
                    "addMedicalRecord",
                    "patient-1",
                    "doctor-2",
                    "medical-institution-3",
                    "2024-05-08",
                    "Blood Test",
                    ":)"
            );
            ChaincodeMedicalRecord chaincodeMedicalRecord = null;
            chaincodeMedicalRecord = ChaincodeMedicalRecord.deserialize(result);
            LOG.info("result: " + chaincodeMedicalRecord);
            return chaincodeMedicalRecord;
        } catch (Exception e) {
            throw e;
        }
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

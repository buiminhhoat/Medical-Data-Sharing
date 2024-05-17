package healthInformationSharing.contract;

import healthInformationSharing.component.MedicalRecordContext;
import healthInformationSharing.dao.RequestDAO;
import healthInformationSharing.entity.MedicalRecord;
import healthInformationSharing.entity.Request;
import org.hyperledger.fabric.contract.Context;
import org.hyperledger.fabric.contract.ContractInterface;
import org.hyperledger.fabric.contract.annotation.*;
import org.hyperledger.fabric.shim.ChaincodeException;
import org.hyperledger.fabric.shim.ChaincodeStub;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.logging.Logger;

@Contract(name = "chaincode",
        info = @Info(
                title = "Medical Record contract",
                description = "",
                version = "0.0.1-SNAPSHOT",
                license = @License(name = "SPDX-License-Identifier: Apache-2.0", url = "http://www.apache.org/licenses/LICENSE-2.0.html"),
                contact = @Contact(email = "official.buiminhhoat@gmail.com",
                        name = "Bui Minh Hoat", url = "https://github.com/buiminhhoat")
        )
)
@Default
public class MedicalRecordContract implements ContractInterface {
    private final static Logger LOG = Logger.getLogger(MedicalRecordContract.class.getName());

    @Override
    public Context createContext(ChaincodeStub stub) {
        LOG.info("createContext()");
        return new MedicalRecordContext(stub);
    }

    @Override
    public void beforeTransaction(Context ctx) {
        List<String> paramList = ctx.getStub().getParameters();
        String params = String.join(",", paramList);
        String function = ctx.getStub().getFunction();

        System.out.println();
        System.out.println("----------------------------------- BEGIN -----------------------------------");
        LOG.info(String.format("Function name: %s, params: [%s]", function, params));
        clientIdentityInfo(ctx);
    }

    private void clientIdentityInfo(final Context ctx) {
        try {
            String userIdentityId = ctx.getClientIdentity().getAttributeValue("userIdentityId");
            String clientIdentityId = ctx.getClientIdentity().getId();
            String clientIdentityMspId = ctx.getClientIdentity().getMSPID();
            LOG.info(String.format("clientIdentityId: %s, clientIdentityMspId: %s, userIdentityId: %s", clientIdentityId, clientIdentityMspId, userIdentityId));
        } catch (Exception e) {
            String errorMessage = "Error during method ctx.getClientIdentity.getAttributeValue()";
            System.out.println(errorMessage);
            throw new ChaincodeException(errorMessage, MedicalRecordContractErrors.UNAUTHORIZED_EDIT_ACCESS.toString());
        }
    }

    @Override
    public void afterTransaction(Context ctx, Object result) {
        String function = ctx.getStub().getFunction();
        LOG.info(String.format("Function name: %s", function));
        if (result == null) {
            LOG.info("result is null");
        } else {
            LOG.info(String.format("object type: %s, else: ", result.getClass().getTypeName(), result.getClass().toString()));
            if (result.getClass().getTypeName().equals("java.util.ArrayList")) {
                ArrayList<String> list = (ArrayList<String>) result;
                LOG.info(String.format("arraylist size: %d", list.size()));
            }
        }
        LOG.info("----------------------------------- END -----------------------------------");
        System.out.println("-----------------------------------");
    }

    private void authorizeRequest(MedicalRecordContext ctx, String userIdentityInDb, String methodName) {
        String userIdentityId = "";
        try {
            userIdentityId = ctx.getClientIdentity().getAttributeValue("userIdentityId");
        } catch (Exception e) {
            String errorMessage = "Error during method ctx.getClientIdentity.getAttributeValue(...)";
            System.out.println(errorMessage);
            throw new ChaincodeException(errorMessage, MedicalRecordContractErrors.UNAUTHORIZED_EDIT_ACCESS.toString());
        }
        if (!userIdentityId.equals(userIdentityInDb)) {
            String errorMessage = String.format("Error during method: %s , identified user does not have write rights", methodName);
            System.out.println(errorMessage);
            throw new ChaincodeException(errorMessage, MedicalRecordContractErrors.UNAUTHORIZED_EDIT_ACCESS.toString());
        }
    }

    @Transaction(intent = Transaction.TYPE.SUBMIT)
    public MedicalRecord addMedicalRecord(
            MedicalRecordContext ctx,
            String patientId,
            String doctorId,
            String medicalInstitutionId,
            String dateCreated,
            String testName,
            String relevantParameters
    ) {
        String medicalRecordId = ctx.getStub().getTxId();
        MedicalRecord medicalRecord = ctx.getMedicalRecordDAO().addMedicalRecord(
                medicalRecordId,
                patientId,
                doctorId,
                medicalInstitutionId,
                dateCreated,
                testName,
                relevantParameters
        );

        return medicalRecord;
    }

    @Transaction(intent = Transaction.TYPE.EVALUATE)
    public MedicalRecord getMedicalRecord(MedicalRecordContext ctx, String medicalRecordId) {
        if (ctx.getMedicalRecordDAO().medicalRecordExist(medicalRecordId)) {
            return ctx.getMedicalRecordDAO().getMedicalRecord(medicalRecordId);
        } else {
            String errorMessage = String.format("Medical Record %s does not exist", medicalRecordId);
            System.out.println(errorMessage);
            throw new ChaincodeException(errorMessage, MedicalRecordContractErrors.MEDICAL_RECORD_NOT_FOUND.toString());
        }
    }

    @Transaction(intent = Transaction.TYPE.SUBMIT)
    public Request sendRequest(
            MedicalRecordContext ctx,
            String senderId,
            String recipientId,
            String medicalRecordId,
            String dateCreated,
            String requestType
    ) {
        authorizeRequest(ctx, senderId, "sendRequest(validate senderId)");
        MedicalRecord medicalRecord = getMedicalRecord(ctx, medicalRecordId);
        if (!Objects.equals(recipientId, medicalRecord.getPatientId())) {
            throw new ChaincodeException("recipientId does not match medicalRecord.getPatientId()", MedicalRecordContractErrors.UNAUTHORIZED_EDIT_ACCESS.toString());
        }
        if (Objects.equals(senderId, medicalRecord.getPatientId())) {
            throw new ChaincodeException("Requester is owner of this medical record", MedicalRecordContractErrors.UNAUTHORIZED_EDIT_ACCESS.toString());
        }
        return ctx.getRequestDAO().sendRequest(
                senderId,
                recipientId,
                medicalRecordId,
                medicalRecord.getTestName(),
                dateCreated,
                requestType
        );
    }

    @Transaction(intent = Transaction.TYPE.SUBMIT)
    public Request defineRequest(
            MedicalRecordContext ctx,
            String requestId,
            String requestStatus,
            String accessAvailableFrom,
            String accessAvailableUntil
    ) {
        RequestDAO requestDAO = ctx.getRequestDAO();
        if (!requestDAO.requestExist(requestId)) {
            throw new ChaincodeException("Request " + requestId + " does not exist",
                    MedicalRecordContractErrors.MEDICAL_RECORD_ACCESS_REQUEST_NOT_FOUND.toString());
        }

        Request request = requestDAO.getRequest(requestId);
        authorizeRequest(ctx, request.getRecipientId(), "defineRequest(validate recipientId");

        return ctx.getRequestDAO().defineRequest(
                requestId,
                requestStatus,
                accessAvailableFrom,
                accessAvailableUntil
        );
    }

    private enum MedicalRecordContractErrors {
        MEDICAL_RECORD_NOT_FOUND,
        MEDICAL_RECORD_ACCESS_REQUEST_NOT_FOUND,
        UNAUTHORIZED_EDIT_ACCESS,
        VALIDATE_MEDICAL_RECORD_ACCESS_ERROR
    }
}

package healthInformationSharing.contract;

import healthInformationSharing.component.MedicalRecordContext;
import healthInformationSharing.dao.RequestDAO;
import healthInformationSharing.dto.MedicalRecordDto;
import healthInformationSharing.dto.MedicalRecordsPreviewResponse;
import healthInformationSharing.entity.MedicalRecord;
import healthInformationSharing.entity.Request;
import healthInformationSharing.enumeration.RequestStatus;
import healthInformationSharing.enumeration.RequestType;
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
            String requestId,
            String patientId,
            String doctorId,
            String medicalInstitutionId,
            String dateCreated,
            String testName,
            String details
    ) {
        if (!ctx.getRequestDAO().requestExist(requestId)) {
            throw new ChaincodeException("Request " + requestId + " does not exist",
                    MedicalRecordContractErrors.REQUEST_NOT_FOUND.toString());
        }
        Request request = ctx.getRequestDAO().getRequest(requestId);
        if (!Objects.equals(request.getSenderId(), patientId)) {
            throw new ChaincodeException("request.getSenderId() does not match patientId",
                    MedicalRecordContractErrors.UNAUTHORIZED_EDIT_ACCESS.toString());
        }
        if (!Objects.equals(request.getRecipientId(), doctorId)) {
            throw new ChaincodeException("request.getRecipientId() does not match doctorId",
                    MedicalRecordContractErrors.UNAUTHORIZED_EDIT_ACCESS.toString());
        }
        if (!Objects.equals(request.getRequestType(), RequestType.APPOINTMENT)) {
            throw new ChaincodeException("request.getRequestType() does not match RequestType.APPOINTMENT",
                    MedicalRecordContractErrors.UNAUTHORIZED_EDIT_ACCESS.toString());
        }
        if (!Objects.equals(request.getRequestStatus(), RequestStatus.PENDING)) {
            throw new ChaincodeException("request.getRequestStatus() does not match RequestStatus.PENDING",
                    MedicalRecordContractErrors.UNAUTHORIZED_EDIT_ACCESS.toString());
        }
        authorizeRequest(ctx, doctorId, "addMedicalRecord(validate doctorId)");
        String medicalRecordId = ctx.getStub().getTxId();
        MedicalRecord medicalRecord = ctx.getMedicalRecordDAO().addMedicalRecord(
                medicalRecordId,
                patientId,
                doctorId,
                medicalInstitutionId,
                dateCreated,
                testName,
                details
        );

        request = ctx.getRequestDAO().defineRequest(requestId, RequestStatus.ACCEPTED);
        LOG.info(String.valueOf(request));
        return medicalRecord;
    }

    @Transaction
    public MedicalRecord defineMedicalRecord(
            MedicalRecordContext ctx,
            String medicalRecordId,
            String medicalRecordStatus
    ) {
        if (!ctx.getMedicalRecordDAO().medicalRecordExist(medicalRecordId)) {
            String errorMessage = String.format("Medical Record %s does not exist", medicalRecordId);
            System.out.println(errorMessage);
            throw new ChaincodeException(errorMessage, MedicalRecordContractErrors.MEDICAL_RECORD_NOT_FOUND.toString());
        }

        MedicalRecord medicalRecord = ctx.getMedicalRecordDAO().defineMedicalRecord(
                medicalRecordId,
                medicalRecordStatus
        );

        authorizeRequest(ctx, medicalRecord.getPatientId(), "defineMedicalRecord(validate patientId)");

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
    public Request sendAppointmentRequest(
            MedicalRecordContext ctx,
            String senderId,
            String recipientId,
            String dateCreated
    ) {
        authorizeRequest(ctx, senderId, "sendAppointmentRequest(validate senderId)");
        return ctx.getAppointmentRequestDAO().sendAppointmentRequest(
                senderId,
                recipientId,
                dateCreated,
                RequestType.APPOINTMENT
        );
    }

//    @Transaction(intent = Transaction.TYPE.SUBMIT)
//    public Request sendRequest(
//            MedicalRecordContext ctx,
//            String senderId,
//            String recipientId,
//            String medicalRecordId,
//            String dateCreated,
//            String requestType
//    ) {
//        authorizeRequest(ctx, senderId, "sendRequest(validate senderId)");
//        MedicalRecord medicalRecord = getMedicalRecord(ctx, medicalRecordId);
//        if (!Objects.equals(recipientId, medicalRecord.getPatientId())) {
//            throw new ChaincodeException("recipientId does not match medicalRecord.getPatientId()", MedicalRecordContractErrors.UNAUTHORIZED_EDIT_ACCESS.toString());
//        }
//        if (Objects.equals(senderId, medicalRecord.getPatientId())) {
//            throw new ChaincodeException("Requester is owner of this medical record", MedicalRecordContractErrors.UNAUTHORIZED_EDIT_ACCESS.toString());
//        }
//        return ctx.getRequestDAO().sendRequest(
//                senderId,
//                recipientId,
//                medicalRecordId,
//                medicalRecord.getTestName(),
//                dateCreated,
//                requestType
//        );
//    }

//    @Transaction(intent = Transaction.TYPE.SUBMIT)
//    public Request defineRequest(
//            MedicalRecordContext ctx,
//            String requestId,
//            String requestStatus,
//            String accessAvailableFrom,
//            String accessAvailableUntil
//    ) {
//        RequestDAO requestDAO = ctx.getRequestDAO();
//        if (!requestDAO.requestExist(requestId)) {
//            throw new ChaincodeException("Request " + requestId + " does not exist",
//                    MedicalRecordContractErrors.REQUEST_NOT_FOUND.toString());
//        }
//
//        Request request = requestDAO.getRequest(requestId);
//        authorizeRequest(ctx, request.getRecipientId(), "defineRequest(validate recipientId");
//
//        return ctx.getRequestDAO().defineRequest(
//                requestId,
//                requestStatus,
//                accessAvailableFrom,
//                accessAvailableUntil
//        );
//    }

    @Transaction(intent = Transaction.TYPE.EVALUATE)
    public MedicalRecordsPreviewResponse getMedicalRecordsByPatientId(
            MedicalRecordContext ctx,
            String patientId,
            String doctorId,
            String medicalInstitutionId,
            String from,
            String until,
            String testName,
            String medicalRecordStatus,
            String details,
            String sortingOrder
    ) {
        authorizeRequest(ctx, patientId, "sendRequest(validate patientId)");
        List<MedicalRecordDto> medicalRecordDtoList = ctx.getMedicalRecordDAO().getMedicalRecordsPreview(
                patientId,
                doctorId,
                medicalInstitutionId,
                from,
                until,
                testName,
                medicalRecordStatus,
                details,
                sortingOrder
        );
        return new MedicalRecordsPreviewResponse(medicalRecordDtoList.size(), medicalRecordDtoList);
    }

    @Transaction(intent = Transaction.TYPE.EVALUATE)
    public MedicalRecordsPreviewResponse getMedicalRecordsByDoctorId(
            MedicalRecordContext ctx,
            String patientId,
            String doctorId,
            String medicalInstitutionId,
            String from,
            String until,
            String testName,
            String medicalRecordStatus,
            String details,
            String sortingOrder
    ) {
        authorizeRequest(ctx, doctorId, "sendRequest(validate doctorId)");
        List<MedicalRecordDto> medicalRecordDtoList = ctx.getMedicalRecordDAO().getMedicalRecordsPreview(
                patientId,
                doctorId,
                medicalInstitutionId,
                from,
                until,
                testName,
                medicalRecordStatus,
                details,
                sortingOrder
        );
        return new MedicalRecordsPreviewResponse(medicalRecordDtoList.size(), medicalRecordDtoList);
    }

    private enum MedicalRecordContractErrors {
        MEDICAL_RECORD_NOT_FOUND,
        REQUEST_NOT_FOUND,
        UNAUTHORIZED_EDIT_ACCESS,
        VALIDATE_MEDICAL_RECORD_ACCESS_ERROR
    }
}

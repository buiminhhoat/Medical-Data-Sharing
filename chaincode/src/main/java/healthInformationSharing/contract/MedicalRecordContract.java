package healthInformationSharing.contract;

import com.owlike.genson.Genson;
import healthInformationSharing.component.MedicalRecordContext;
import healthInformationSharing.dao.AppointmentRequestDAO;
import healthInformationSharing.dao.EditRequestDAO;
import healthInformationSharing.dto.MedicalRecordDto;
import healthInformationSharing.dto.MedicalRecordsPreviewResponse;
import healthInformationSharing.entity.*;
import healthInformationSharing.enumeration.RequestStatus;
import healthInformationSharing.enumeration.RequestType;
import org.hyperledger.fabric.contract.Context;
import org.hyperledger.fabric.contract.ContractInterface;
import org.hyperledger.fabric.contract.annotation.*;
import org.hyperledger.fabric.shim.ChaincodeException;
import org.hyperledger.fabric.shim.ChaincodeStub;
import org.json.JSONObject;

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
        if (!ctx.getAppointmentRequestDAO().requestExist(requestId)) {
            throw new ChaincodeException("Request " + requestId + " does not exist",
                    MedicalRecordContractErrors.REQUEST_NOT_FOUND.toString());
        }
        AppointmentRequest appointmentRequest = ctx.getAppointmentRequestDAO().getAppointmentRequest(requestId);
        System.out.println(appointmentRequest.toString());
        System.out.println("getSenderId: " + appointmentRequest.getSenderId());
        System.out.println(patientId);
        if (!Objects.equals(appointmentRequest.getSenderId(), patientId)) {
            throw new ChaincodeException("request.getSenderId() does not match patientId",
                    MedicalRecordContractErrors.UNAUTHORIZED_EDIT_ACCESS.toString());
        }
        if (!Objects.equals(appointmentRequest.getRecipientId(), doctorId)) {
            throw new ChaincodeException("request.getRecipientId() does not match doctorId",
                    MedicalRecordContractErrors.UNAUTHORIZED_EDIT_ACCESS.toString());
        }
        if (!Objects.equals(appointmentRequest.getRequestType(), RequestType.APPOINTMENT)) {
            throw new ChaincodeException("request.getRequestType() does not match RequestType.APPOINTMENT",
                    MedicalRecordContractErrors.UNAUTHORIZED_EDIT_ACCESS.toString());
        }
        if (!Objects.equals(appointmentRequest.getRequestStatus(), RequestStatus.PENDING)) {
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

        appointmentRequest = ctx.getAppointmentRequestDAO().defineRequest(requestId, RequestStatus.ACCEPTED);
        LOG.info(String.valueOf(appointmentRequest));
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

    @Transaction(intent = Transaction.TYPE.EVALUATE)
    public EditRequest getEditRequest(MedicalRecordContext ctx, String requestId) {
        if (ctx.getEditRequestDAO().requestExist(requestId)) {
            EditRequest editRequest = ctx.getEditRequestDAO().getEditRequest(requestId);
            System.out.println("getEditRequest: " + editRequest);
            return editRequest;
        } else {
            String errorMessage = String.format("Edit Request %s does not exist", requestId);
            System.out.println(errorMessage);
            throw new ChaincodeException(errorMessage, MedicalRecordContractErrors.EDIT_REQUEST_NOT_FOUND.toString());
        }
    }

    @Transaction(intent = Transaction.TYPE.SUBMIT)
    public AppointmentRequest sendAppointmentRequest(
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

    @Transaction(intent = Transaction.TYPE.SUBMIT)
    public AppointmentRequest defineAppointmentRequest(
            MedicalRecordContext ctx,
            String requestId,
            String requestStatus,
            String accessAvailableFrom,
            String accessAvailableUntil
    ) {
        AppointmentRequestDAO appointmentRequestDAO = ctx.getAppointmentRequestDAO();
        if (!appointmentRequestDAO.requestExist(requestId)) {
            throw new ChaincodeException("AppointmentRequest " + requestId + " does not exist",
                    MedicalRecordContractErrors.REQUEST_NOT_FOUND.toString());
        }

        AppointmentRequest appointmentRequest = appointmentRequestDAO.getAppointmentRequest(requestId);
        authorizeRequest(ctx, appointmentRequest.getRecipientId(), "defineAppointmentRequest(validate recipientId");

        return ctx.getAppointmentRequestDAO().defineRequest(
                requestId,
                requestStatus,
                accessAvailableFrom,
                accessAvailableUntil
        );
    }

    @Transaction(intent = Transaction.TYPE.SUBMIT)
    public EditRequest sendEditRequest(
            MedicalRecordContext ctx,
            String senderId,
            String recipientId,
            String dateCreated,
            String medicalRecordJson
    ) {
        Genson genson = new Genson();
        MedicalRecord medicalRecord = genson.deserialize(medicalRecordJson, MedicalRecord.class);
        authorizeRequest(ctx, medicalRecord.getDoctorId(), "sendEditRequest(validate doctorId)");
        if (!Objects.equals(medicalRecord.getPatientId(), recipientId)) {
            throw new ChaincodeException("medicalRecord.getPatientId() does not match recipientId",
                    MedicalRecordContractErrors.UNAUTHORIZED_EDIT_ACCESS.toString());
        }
        EditRequest editRequest = ctx.getEditRequestDAO().sendEditRequest(
                senderId,
                recipientId,
                dateCreated,
                RequestType.EDIT_RECORD,
                medicalRecordJson
        );
        System.out.println("sendEditRequest - editRequest: " + editRequest);
        return editRequest;
    }

    @Transaction(intent = Transaction.TYPE.SUBMIT)
    public MedicalRecord defineEditRequest(
        MedicalRecordContext ctx,
        String requestId,
        String requestStatus
    ) {
        EditRequestDAO editRequestDAO = ctx.getEditRequestDAO();
        if (!editRequestDAO.requestExist(requestId)) {
            throw new ChaincodeException("EditRequestDAO " + requestId + " does not exist",
                    MedicalRecordContractErrors.REQUEST_NOT_FOUND.toString());
        }

        EditRequest editRequest = editRequestDAO.getEditRequest(requestId);
        authorizeRequest(ctx, editRequest.getRecipientId(), "defineEditRequest(validate recipientId");

        Genson genson = new Genson();
        MedicalRecord medicalRecord = genson.deserialize(editRequest.getMedicalRecord(), MedicalRecord.class);

        MedicalRecord curMedicalRecord = getMedicalRecord(ctx, medicalRecord.getMedicalRecordId());

        if (!Objects.equals(medicalRecord.getPatientId(), curMedicalRecord.getPatientId())) {
            throw new ChaincodeException("editMedicalRecord.medicalRecordJson.getPatientId() does not match curMedicalRecord.getPatientId()",
                    MedicalRecordContractErrors.UNAUTHORIZED_EDIT_ACCESS.toString());
        }

        if (!Objects.equals(medicalRecord.getDoctorId(), curMedicalRecord.getDoctorId())) {
            throw new ChaincodeException("editMedicalRecord.medicalRecordJson.getDoctorId() does not match curMedicalRecord.getDoctorId()",
                    MedicalRecordContractErrors.UNAUTHORIZED_EDIT_ACCESS.toString());
        }

        editRequest = ctx.getEditRequestDAO().defineEditRequest(
            requestId,
            requestStatus
        );

        medicalRecord = null;
        if (Objects.equals(editRequest.getRequestStatus(), RequestStatus.ACCEPTED)) {
            medicalRecord = ctx.getMedicalRecordDAO().editMedicalRecord(editRequest.getMedicalRecord());
        }
        else {
            throw new ChaincodeException("editRequest.getRequestStatus() does not match RequestStatus.ACCEPTED",
                    MedicalRecordContractErrors.UNAUTHORIZED_EDIT_ACCESS.toString());
        }
        return medicalRecord;
    }


    @Transaction(intent = Transaction.TYPE.SUBMIT)
    public ViewRequest sendViewRequest(
            MedicalRecordContext ctx,
            String senderId,
            String recipientId,
            String dateCreated,
            String medicalRecordId
    ) {
        authorizeRequest(ctx, senderId, "sendViewRequest(validate senderId)");
        return ctx.getViewRequestDAO().sendViewRequest(
                senderId,
                recipientId,
                dateCreated,
                RequestType.VIEW_RECORD,
                medicalRecordId
        );
    }


    @Transaction(intent = Transaction.TYPE.EVALUATE)
    public MedicalRecordsPreviewResponse getListMedicalRecordByPatientQuery(
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
        authorizeRequest(ctx, patientId, "getListMedicalRecordByPatientQuery(validate patientId)");
        List<MedicalRecordDto> medicalRecordByQueryDtoList = ctx.getMedicalRecordDAO().getListMedicalRecordByQuery(
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
        return new MedicalRecordsPreviewResponse(medicalRecordByQueryDtoList.size(), medicalRecordByQueryDtoList);
    }

    @Transaction(intent = Transaction.TYPE.EVALUATE)
    public MedicalRecordsPreviewResponse getListMedicalRecordByDoctorQuery(
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
        authorizeRequest(ctx, doctorId, "getListMedicalRecordByDoctorQuery(validate doctorId)");
        List<MedicalRecordDto> medicalRecordByQueryDtoList = ctx.getMedicalRecordDAO().getListMedicalRecordByQuery(
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
        return new MedicalRecordsPreviewResponse(medicalRecordByQueryDtoList.size(), medicalRecordByQueryDtoList);
    }

    private enum MedicalRecordContractErrors {
        MEDICAL_RECORD_NOT_FOUND,
        REQUEST_NOT_FOUND,
        UNAUTHORIZED_EDIT_ACCESS,
        VALIDATE_MEDICAL_RECORD_ACCESS_ERROR,
        EDIT_REQUEST_NOT_FOUND
    }
}

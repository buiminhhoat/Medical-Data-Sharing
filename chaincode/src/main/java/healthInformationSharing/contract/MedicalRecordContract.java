package healthInformationSharing.contract;

import com.owlike.genson.GenericType;
import com.owlike.genson.Genson;
import healthInformationSharing.component.MedicalRecordContext;
import healthInformationSharing.dao.*;
import healthInformationSharing.dto.MedicationPurchaseDto;
import healthInformationSharing.dto.PrescriptionDetailsDto;
import healthInformationSharing.dto.PrescriptionDto;
import healthInformationSharing.dto.PurchaseDto;
import healthInformationSharing.entity.*;
import healthInformationSharing.enumeration.MedicalRecordStatus;
import healthInformationSharing.enumeration.RequestStatus;
import healthInformationSharing.enumeration.RequestType;
import org.hyperledger.fabric.contract.Context;
import org.hyperledger.fabric.contract.ContractInterface;
import org.hyperledger.fabric.contract.annotation.*;
import org.hyperledger.fabric.shim.ChaincodeException;
import org.hyperledger.fabric.shim.ChaincodeStub;
import org.hyperledger.fabric.shim.ledger.CompositeKey;
import org.hyperledger.fabric.shim.ledger.KeyModification;
import org.hyperledger.fabric.shim.ledger.QueryResultsIterator;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
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
            throw new ChaincodeException(errorMessage, ContractErrors.UNAUTHORIZED_EDIT_ACCESS.toString());
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
            throw new ChaincodeException(errorMessage, ContractErrors.UNAUTHORIZED_EDIT_ACCESS.toString());
        }
        if (!userIdentityId.equals(userIdentityInDb)) {
            String errorMessage = String.format("Error during method: %s , identified user does not have write rights", methodName);
            System.out.println(errorMessage);
            throw new ChaincodeException(errorMessage, ContractErrors.UNAUTHORIZED_EDIT_ACCESS.toString());
        }
    }

    @Transaction(intent = Transaction.TYPE.SUBMIT)
    public String addMedicalRecord(
            MedicalRecordContext ctx,
            String jsonString
    ) {
        JSONObject jsonObject = new JSONObject(jsonString);
        String requestId = jsonObject.getString("requestId");
        String patientId = jsonObject.getString("patientId");
        String doctorId = jsonObject.getString("doctorId");
        String medicalInstitutionId = jsonObject.getString("medicalInstitutionId");
        String dateCreated = jsonObject.getString("dateCreated");
        String dateModified = jsonObject.getString("dateModified");
        String testName = jsonObject.getString("testName");
        String details = jsonObject.getString("details");
        String addPrescription = jsonObject.getString("addPrescription");
        String hashFile = jsonObject.getString("hashFile");

        if (!ctx.getAppointmentRequestDAO().requestExist(requestId)) {
            throw new ChaincodeException("Request " + requestId + " does not exist",
                    ContractErrors.REQUEST_NOT_FOUND.toString());
        }
        AppointmentRequest appointmentRequest = ctx.getAppointmentRequestDAO().getAppointmentRequest(requestId);
        System.out.println(appointmentRequest.toString());
        System.out.println("getSenderId: " + appointmentRequest.getSenderId());
        System.out.println(patientId);
        if (!Objects.equals(appointmentRequest.getSenderId(), patientId)) {
            throw new ChaincodeException("request.getSenderId() does not match patientId",
                    ContractErrors.UNAUTHORIZED_EDIT_ACCESS.toString());
        }
        if (!Objects.equals(appointmentRequest.getRecipientId(), doctorId)) {
            throw new ChaincodeException("request.getRecipientId() does not match doctorId",
                    ContractErrors.UNAUTHORIZED_EDIT_ACCESS.toString());
        }
        if (!Objects.equals(appointmentRequest.getRequestType(), RequestType.APPOINTMENT)) {
            throw new ChaincodeException("request.getRequestType() does not match RequestType.APPOINTMENT",
                    ContractErrors.UNAUTHORIZED_EDIT_ACCESS.toString());
        }
        if (!Objects.equals(appointmentRequest.getRequestStatus(), RequestStatus.PENDING)) {
            throw new ChaincodeException("request.getRequestStatus() does not match RequestStatus.PENDING",
                    ContractErrors.UNAUTHORIZED_EDIT_ACCESS.toString());
        }
        authorizeRequest(ctx, doctorId, "addMedicalRecord(validate doctorId)");
        String medicalRecordId = ctx.getStub().getTxId();

        Prescription prescription = addPrescription(ctx, addPrescription);

        JSONObject jsonDto = new JSONObject();

        jsonDto.put("medicalRecordId", medicalRecordId);
        jsonDto.put("patientId", patientId);
        jsonDto.put("doctorId", doctorId);
        jsonDto.put("medicalInstitutionId", medicalInstitutionId);
        jsonDto.put("dateCreated", dateCreated);
        jsonDto.put("dateModified", dateModified);
        jsonDto.put("testName", testName);
        jsonDto.put("details", details);
        jsonDto.put("prescriptionId", prescription.getPrescriptionId());
        jsonDto.put("hashFile", hashFile);

        MedicalRecord medicalRecord = ctx.getMedicalRecordDAO().addMedicalRecord(jsonDto);

        appointmentRequest = ctx.getAppointmentRequestDAO().defineRequest(requestId, RequestStatus.ACCEPTED);
        LOG.info(String.valueOf(appointmentRequest));

        return new Genson().serialize(medicalRecord);
    }

    @Transaction
    public String defineMedicalRecord(
            MedicalRecordContext ctx,
            String jsonString
    ) {
        JSONObject jsonObject = new JSONObject(jsonString);
        String medicalRecordId = jsonObject.getString("medicalRecordId");
        String medicalRecordStatus = jsonObject.getString("medicalRecordStatus");

        JSONObject jsonDto = new JSONObject();

        if (!ctx.getMedicalRecordDAO().medicalRecordExist(medicalRecordId)) {
            String errorMessage = String.format("Medical Record %s does not exist", medicalRecordId);
            System.out.println(errorMessage);
            throw new ChaincodeException(errorMessage, ContractErrors.MEDICAL_RECORD_NOT_FOUND.toString());
        }

        jsonDto = new JSONObject();

        jsonDto.put("medicalRecordId", medicalRecordId);
        jsonDto.put("medicalRecordStatus", medicalRecordStatus);

        MedicalRecord medicalRecord = ctx.getMedicalRecordDAO().getMedicalRecord(medicalRecordId);

        authorizeRequest(ctx, medicalRecord.getPatientId(), "defineMedicalRecord(validate patientId)");

        medicalRecord = ctx.getMedicalRecordDAO().defineMedicalRecord(jsonDto);

        return new Genson().serialize(medicalRecord);
    }

    @Transaction
    public String revokeMedicalRecord(
            MedicalRecordContext ctx,
            String jsonString
    ) {
        JSONObject jsonObject = new JSONObject(jsonString);
        String medicalRecordId = jsonObject.getString("medicalRecordId");
        String medicalRecordStatus = MedicalRecordStatus.REVOKED;

        JSONObject jsonDto = new JSONObject();

        if (!ctx.getMedicalRecordDAO().medicalRecordExist(medicalRecordId)) {
            String errorMessage = String.format("Medical Record %s does not exist", medicalRecordId);
            System.out.println(errorMessage);
            throw new ChaincodeException(errorMessage, ContractErrors.MEDICAL_RECORD_NOT_FOUND.toString());
        }

        jsonDto = new JSONObject();

        jsonDto.put("medicalRecordId", medicalRecordId);
        jsonDto.put("medicalRecordStatus", medicalRecordStatus);

        MedicalRecord medicalRecord = ctx.getMedicalRecordDAO().getMedicalRecord(medicalRecordId);
        authorizeRequest(ctx, medicalRecord.getDoctorId(), "revokeMedicalRecord(validate doctorId)");


        medicalRecord = ctx.getMedicalRecordDAO().defineMedicalRecord(jsonDto);


        return new Genson().serialize(medicalRecord);
    }

    @Transaction(intent = Transaction.TYPE.EVALUATE)
    public String getMedicalRecordByPatient(MedicalRecordContext ctx,
                                          String jsonString) {
        JSONObject jsonObject = new JSONObject(jsonString);
        String medicalRecordId = jsonObject.getString("medicalRecordId");

        if (ctx.getMedicalRecordDAO().medicalRecordExist(medicalRecordId)) {
            MedicalRecord medicalRecord = ctx.getMedicalRecordDAO().getMedicalRecord(medicalRecordId);
            authorizeRequest(ctx, medicalRecord.getPatientId(), "getMedicalRecordByPatient(validate medicalRecord.getPatientId())");
            return new Genson().serialize(medicalRecord);
        } else {
            String errorMessage = String.format("Medical Record %s does not exist", medicalRecordId);
            System.out.println(errorMessage);
            throw new ChaincodeException(errorMessage, ContractErrors.MEDICAL_RECORD_NOT_FOUND.toString());
        }
    }

    @Transaction(intent = Transaction.TYPE.EVALUATE)
    public String getMedicalRecordByDoctor(MedicalRecordContext ctx,
                                            String jsonString) {
        JSONObject jsonObject = new JSONObject(jsonString);
        String medicalRecordId = jsonObject.getString("medicalRecordId");

        if (ctx.getMedicalRecordDAO().medicalRecordExist(medicalRecordId)) {
            MedicalRecord medicalRecord = ctx.getMedicalRecordDAO().getMedicalRecord(medicalRecordId);
            authorizeRequest(ctx, medicalRecord.getDoctorId(), "getMedicalRecordByDoctor(validate medicalRecord.getDoctorId())");
            return new Genson().serialize(medicalRecord);
        } else {
            String errorMessage = String.format("Medical Record %s does not exist", medicalRecordId);
            System.out.println(errorMessage);
            throw new ChaincodeException(errorMessage, ContractErrors.MEDICAL_RECORD_NOT_FOUND.toString());
        }
    }
    
    @Transaction(intent = Transaction.TYPE.EVALUATE)
    public String getMedicalRecordChangeHistory(MedicalRecordContext ctx, String jsonString) throws Exception {
        JSONObject jsonObject = new JSONObject(jsonString);
        String medicalRecordId = jsonObject.getString("medicalRecordId");
        try {
            CompositeKey compositeKey = ctx.getStub().createCompositeKey("MedicalRecord", medicalRecordId);
            String dbKey = compositeKey.toString();
            ChaincodeStub stub = ctx.getStub();
            QueryResultsIterator<KeyModification> results = stub.getHistoryForKey(dbKey);
            List<MedicalRecord> changeHistory = new ArrayList<>();
            for (KeyModification keyModification : results) {
                if (keyModification == null) continue;
                System.out.println(keyModification.getStringValue());
                MedicalRecord medicalRecord = new Genson().deserialize(keyModification.getStringValue(), MedicalRecord.class);
                changeHistory.add(medicalRecord);
            }
            System.out.println("changeHistory.size(): " + changeHistory.size());
            String jsonChangeHistoryString = new Genson().serialize(changeHistory);
            System.out.println("jsonString: " + jsonChangeHistoryString);
            return jsonChangeHistoryString;
        }
        catch (Exception exception) {
            System.out.println(exception.getMessage());
            throw new Exception(exception.getMessage());
        }
    }

    @Transaction(intent = Transaction.TYPE.EVALUATE)
    public String getAppointmentRequest(MedicalRecordContext ctx, String jsonString) {
        JSONObject jsonObject = new JSONObject(jsonString);
        String requestId = jsonObject.getString("requestId");
        if (ctx.getAppointmentRequestDAO().requestExist(requestId)) {
            AppointmentRequest appointmentRequest = ctx.getAppointmentRequestDAO().getAppointmentRequest(requestId);
            try {
                authorizeRequest(ctx, appointmentRequest.getSenderId(), "getAppointmentRequest(validate senderId)");
            }
            catch (ChaincodeException chaincodeException) {
                try {
                    authorizeRequest(ctx, appointmentRequest.getRecipientId(), "getAppointmentRequest(validate recipientId)");
                }
                catch (ChaincodeException ce) {
                    throw ce;
                }
            }
            System.out.println("getAppointmentRequest: " + appointmentRequest);
            return new Genson().serialize(appointmentRequest);
        } else {
            String errorMessage = String.format("Appointment Request %s does not exist", requestId);
            System.out.println(errorMessage);
            throw new ChaincodeException(errorMessage, ContractErrors.REQUEST_NOT_FOUND.toString());
        }
    }

    @Transaction(intent = Transaction.TYPE.EVALUATE)
    public String getViewRequest(MedicalRecordContext ctx, String jsonString) {
        JSONObject jsonObject = new JSONObject(jsonString);
        String requestId = jsonObject.getString("requestId");
        if (ctx.getViewRequestDAO().requestExist(requestId)) {
            ViewRequest viewRequest = ctx.getViewRequestDAO().getViewRequest(requestId);
            try {
                authorizeRequest(ctx, viewRequest.getSenderId(), "getViewRequest(validate senderId)");
            }
            catch (ChaincodeException chaincodeException) {
                try {
                    authorizeRequest(ctx, viewRequest.getRecipientId(), "getViewRequest(validate recipientId)");
                }
                catch (ChaincodeException ce) {
                    throw ce;
                }
            }
            System.out.println("getViewRequest: " + viewRequest);
            return new Genson().serialize(viewRequest);
        } else {
            String errorMessage = String.format("View Request %s does not exist", requestId);
            System.out.println(errorMessage);
            throw new ChaincodeException(errorMessage, ContractErrors.REQUEST_NOT_FOUND.toString());
        }
    }

    @Transaction(intent = Transaction.TYPE.EVALUATE)
    public String getViewPrescriptionRequest(MedicalRecordContext ctx, String jsonString) {
        JSONObject jsonObject = new JSONObject(jsonString);
        String requestId = jsonObject.getString("requestId");
        if (ctx.getViewPrescriptionRequestDAO().requestExist(requestId)) {
            ViewPrescriptionRequest viewPrescriptionRequest = ctx.getViewPrescriptionRequestDAO().getViewPrescriptionRequest(requestId);
            try {
                authorizeRequest(ctx, viewPrescriptionRequest.getSenderId(), "getViewPrescriptionRequest(validate senderId)");
            }
            catch (ChaincodeException chaincodeException) {
                try {
                    authorizeRequest(ctx, viewPrescriptionRequest.getRecipientId(), "getViewPrescriptionRequest(validate recipientId)");
                }
                catch (ChaincodeException ce) {
                    throw ce;
                }
            }
            System.out.println("getViewPrescriptionRequest: " + viewPrescriptionRequest);
            return new Genson().serialize(viewPrescriptionRequest);
        } else {
            String errorMessage = String.format("View Prescription Request %s does not exist", requestId);
            System.out.println(errorMessage);
            throw new ChaincodeException(errorMessage, ContractErrors.REQUEST_NOT_FOUND.toString());
        }
    }

    @Transaction(intent = Transaction.TYPE.EVALUATE)
    public String getPurchaseRequest(MedicalRecordContext ctx, String jsonString) {
        JSONObject jsonObject = new JSONObject(jsonString);
        String requestId = jsonObject.getString("requestId");
        if (ctx.getPurchaseRequestDAO().requestExist(requestId)) {
            PurchaseRequest purchaseRequest = ctx.getPurchaseRequestDAO().getPurchaseRequest(requestId);
            try {
                authorizeRequest(ctx, purchaseRequest.getSenderId(), "getPurchaseRequest(validate senderId)");
            }
            catch (ChaincodeException chaincodeException) {
                try {
                    authorizeRequest(ctx, purchaseRequest.getRecipientId(), "getPurchaseRequest(validate recipientId)");
                }
                catch (ChaincodeException ce) {
                    throw ce;
                }
            }
            System.out.println("getPurchaseRequest: " + purchaseRequest);
            return new Genson().serialize(purchaseRequest);
        } else {
            String errorMessage = String.format("Purchase Request %s does not exist", requestId);
            System.out.println(errorMessage);
            throw new ChaincodeException(errorMessage, ContractErrors.REQUEST_NOT_FOUND.toString());
        }
    }

    @Transaction(intent = Transaction.TYPE.EVALUATE)
    public String getPaymentRequest(MedicalRecordContext ctx, String jsonString) {
        JSONObject jsonObject = new JSONObject(jsonString);
        String requestId = jsonObject.getString("requestId");
        if (ctx.getPaymentRequestDAO().requestExist(requestId)) {
            PaymentRequest paymentRequest = ctx.getPaymentRequestDAO().getPaymentRequest(requestId);
            try {
                authorizeRequest(ctx, paymentRequest.getSenderId(), "getPaymentRequest(validate senderId)");
            }
            catch (ChaincodeException chaincodeException) {
                try {
                    authorizeRequest(ctx, paymentRequest.getRecipientId(), "getPaymentRequest(validate recipientId)");
                }
                catch (ChaincodeException ce) {
                    throw ce;
                }
            }
            System.out.println("getPaymentRequest: " + paymentRequest);
            return new Genson().serialize(paymentRequest);
        } else {
            String errorMessage = String.format("Payment Request %s does not exist", requestId);
            System.out.println(errorMessage);
            throw new ChaincodeException(errorMessage, ContractErrors.REQUEST_NOT_FOUND.toString());
        }
    }

    @Transaction(intent = Transaction.TYPE.EVALUATE)
    public String getConfirmPaymentRequest(MedicalRecordContext ctx, String jsonString) {
        JSONObject jsonObject = new JSONObject(jsonString);
        String requestId = jsonObject.getString("requestId");
        if (ctx.getConfirmPaymentRequestDAO().requestExist(requestId)) {
            ConfirmPaymentRequest confirmPaymentRequest = ctx.getConfirmPaymentRequestDAO().getConfirmPaymentRequest(requestId);
            try {
                authorizeRequest(ctx, confirmPaymentRequest.getSenderId(), "getConfirmPaymentRequest(validate senderId)");
            }
            catch (ChaincodeException chaincodeException) {
                try {
                    authorizeRequest(ctx, confirmPaymentRequest.getRecipientId(), "getConfirmPaymentRequest(validate recipientId)");
                }
                catch (ChaincodeException ce) {
                    throw ce;
                }
            }
            System.out.println("getConfirmPaymentRequest: " + confirmPaymentRequest);
            return new Genson().serialize(confirmPaymentRequest);
        } else {
            String errorMessage = String.format("Confirm Payment Request %s does not exist", requestId);
            System.out.println(errorMessage);
            throw new ChaincodeException(errorMessage, ContractErrors.REQUEST_NOT_FOUND.toString());
        }
    }

    @Transaction(intent = Transaction.TYPE.SUBMIT)
    public String sendAppointmentRequest(
            MedicalRecordContext ctx,
            String jsonString
    ) {
        JSONObject jsonObject = new JSONObject(jsonString);
        String senderId = jsonObject.getString("senderId");
        String recipientId = jsonObject.getString("recipientId");
        String medicalInstitutionId = jsonObject.getString("medicalInstitutionId");
        String dateCreated = jsonObject.getString("dateCreated");
        String dateModified = jsonObject.getString("dateModified");
        authorizeRequest(ctx, senderId, "sendAppointmentRequest(validate senderId)");

        JSONObject jsonDto = jsonObject;
        jsonDto.put("requestType", RequestType.APPOINTMENT);
        AppointmentRequest appointmentRequest = ctx.getAppointmentRequestDAO().sendAppointmentRequest(jsonDto);
        ctx.getViewRequestDAO().sendViewRequestAccepted(new JSONObject().put("senderId", recipientId)
                .put("recipientId", senderId)
                .put("dateCreated", dateCreated)
                .put("dateModified", dateModified));
        return new Genson().serialize(appointmentRequest);
    }

    @Transaction(intent = Transaction.TYPE.SUBMIT)
    public String defineAppointmentRequest(
            MedicalRecordContext ctx,
            String jsonString
    ) {
        JSONObject jsonObject = new JSONObject(jsonString);
        String requestId = jsonObject.getString("requestId");
        String requestStatus = jsonObject.getString("requestStatus");
        AppointmentRequestDAO appointmentRequestDAO = ctx.getAppointmentRequestDAO();
        
        if (!appointmentRequestDAO.requestExist(requestId)) {
            throw new ChaincodeException("AppointmentRequest " + requestId + " does not exist",
                    ContractErrors.REQUEST_NOT_FOUND.toString());
        }

        AppointmentRequest appointmentRequest = appointmentRequestDAO.getAppointmentRequest(requestId);
        authorizeRequest(ctx, appointmentRequest.getRecipientId(), "defineAppointmentRequest(validate recipientId");

        JSONObject jsonDto = new JSONObject();
        jsonDto.put("requestId", requestId);
        jsonDto.put("requestStatus", requestStatus);
        appointmentRequest = ctx.getAppointmentRequestDAO().defineRequest(
                jsonDto
        );
        return new Genson().serialize(appointmentRequest);
    }

    @Transaction(intent = Transaction.TYPE.SUBMIT)
    public String sendViewRequest(
            MedicalRecordContext ctx,
            String jsonString
    ) {
        JSONObject jsonObject = new JSONObject(jsonString);

        String senderId = jsonObject.getString("senderId");

        authorizeRequest(ctx, senderId, "sendViewRequest(validate senderId)");

        JSONObject jsonDto = jsonObject;

        ViewRequest viewRequest = ctx.getViewRequestDAO().sendViewRequest(
                jsonDto
        );
        return new Genson().serialize(viewRequest);
    }

    @Transaction(intent = Transaction.TYPE.SUBMIT)
    public String defineViewRequest(
            MedicalRecordContext ctx,
            String jsonString
    ) {
        JSONObject jsonObject = new JSONObject(jsonString);
        String requestId = jsonObject.getString("requestId");
        String requestStatus = jsonObject.getString("requestStatus");
        ViewRequestDAO viewRequestDAO = ctx.getViewRequestDAO();

        if (!viewRequestDAO.requestExist(requestId)) {
            throw new ChaincodeException("ViewRequest " + requestId + " does not exist",
                    ContractErrors.REQUEST_NOT_FOUND.toString());
        }

        ViewRequest viewRequest = viewRequestDAO.getViewRequest(requestId);
        authorizeRequest(ctx, viewRequest.getRecipientId(), "defineViewRequest(validate recipientId");

        JSONObject jsonDto = new JSONObject();
        jsonDto.put("requestId", requestId);
        jsonDto.put("requestStatus", requestStatus);
        viewRequest = ctx.getViewRequestDAO().defineViewRequest(
                jsonDto
        );
        return new Genson().serialize(viewRequest);
    }

    @Transaction(intent = Transaction.TYPE.EVALUATE)
    public String getListMedicalRecordByPatientQuery(
            MedicalRecordContext ctx,
            String jsonString
    ) {
        JSONObject jsonObject = new JSONObject(jsonString);
        String patientId = jsonObject.getString("patientId");

        authorizeRequest(ctx, patientId, "getListMedicalRecordByPatientQuery(validate patientId)");

        JSONObject jsonDto = jsonObject;

        List<MedicalRecord> medicalRecordList = ctx.getMedicalRecordDAO().getListMedicalRecordByQuery(
                jsonDto
        );
        return new Genson().serialize(medicalRecordList);
    }

    @Transaction(intent = Transaction.TYPE.EVALUATE)
    public String getListAuthorizedMedicalRecordByDoctorQuery(
            MedicalRecordContext ctx,
            String jsonString
    ) {
        JSONObject jsonObject = new JSONObject(jsonString);
        String doctorId = jsonObject.getString("doctorId");
        String patientId = jsonObject.has("patientId") ? jsonObject.getString("patientId") : "";
        authorizeRequest(ctx, doctorId, "getListAuthorizedMedicalRecordByDoctorQuery(validate doctorId)");

        JSONObject jsonViewRequestDto = new JSONObject();

        jsonViewRequestDto.put("senderId", doctorId);
        if (!patientId.isEmpty()) {
            jsonViewRequestDto.put("recipientId", patientId);
        }
        jsonViewRequestDto.put("requestType", RequestType.VIEW_RECORD);
        jsonViewRequestDto.put("requestStatus", RequestStatus.ACCEPTED);

        System.out.println(jsonViewRequestDto.toString());

        List<ViewRequest> viewRequestList = ctx.getViewRequestDAO().getListViewRequestBySenderQuery(
                jsonViewRequestDto
        );

        System.out.println("viewRequestList.size(): " + viewRequestList.size());
        System.out.println("viewRequestList: " + viewRequestList.toString());

        List<MedicalRecord> medicalRecordList = new ArrayList<>();
        for (ViewRequest viewRequest: viewRequestList) {
            JSONObject jsonDto = new JSONObject();
            System.out.println("viewRequest.getRecipientId(): " + viewRequest.getRecipientId());
            jsonDto.put("patientId", viewRequest.getRecipientId());
            List<MedicalRecord> medicalRecords = ctx.getMedicalRecordDAO()
                    .getListAuthorizedMedicalRecordByDoctorQuery(jsonDto);
            for (MedicalRecord medicalRecord: medicalRecords) {
                medicalRecordList.add(medicalRecord);
            }
        }
        return new Genson().serialize(medicalRecordList);
    }

    @Transaction(intent = Transaction.TYPE.EVALUATE)
    public String getListAllAuthorizedPatientForScientist(
            MedicalRecordContext ctx,
            String jsonString
    ) {
        JSONObject jsonObject = new JSONObject(jsonString);
        String scientistId = jsonObject.getString("scientistId");
        authorizeRequest(ctx, scientistId, "getListMedicalRecordByScientistQuery(validate scientistId)");
        JSONObject jsonDto = jsonObject;
        jsonDto.put("senderId", scientistId);
        jsonDto.put("requestType", RequestType.VIEW_RECORD);
        jsonDto.put("requestStatus", RequestStatus.ACCEPTED);
        List<String> authorizedPatientList = ctx.getViewRequestDAO().getListAllAuthorizedPatientForScientist(
                jsonDto
        );
        return new Genson().serialize(authorizedPatientList);
    }

    @Transaction(intent = Transaction.TYPE.EVALUATE)
    public String getListAuthorizedMedicalRecordByScientistQuery(
            MedicalRecordContext ctx,
            String jsonString
    ) {
        JSONObject jsonObject = new JSONObject(jsonString);
        String scientistId = jsonObject.getString("scientistId");
        String patientId = jsonObject.has("patientId") ? jsonObject.getString("patientId") : "";
        authorizeRequest(ctx, scientistId, "getListMedicalRecordByScientistQuery(validate scientistId)");

        JSONObject jsonViewRequestDto = new JSONObject();

        jsonViewRequestDto.put("senderId", scientistId);
        if (!patientId.isEmpty()) {
            jsonViewRequestDto.put("recipientId", patientId);
        }
        jsonViewRequestDto.put("requestType", RequestType.VIEW_RECORD);
        jsonViewRequestDto.put("requestStatus", RequestStatus.ACCEPTED);

        System.out.println(jsonViewRequestDto.toString());

        List<ViewRequest> viewRequestList = ctx.getViewRequestDAO().getListViewRequestBySenderQuery(
                jsonViewRequestDto
        );

        System.out.println("viewRequestList.size(): " + viewRequestList.size());
        System.out.println("viewRequestList: " + viewRequestList.toString());

        List<MedicalRecord> medicalRecordList = new ArrayList<>();
        for (ViewRequest viewRequest: viewRequestList) {
            JSONObject jsonDto = new JSONObject();
            System.out.println("viewRequest.getRecipientId(): " + viewRequest.getRecipientId());
            jsonDto.put("patientId", viewRequest.getRecipientId());
            List<MedicalRecord> medicalRecords = ctx.getMedicalRecordDAO()
                    .getListAuthorizedMedicalRecordByScientistQuery(jsonDto);
            for (MedicalRecord medicalRecord: medicalRecords) {
                medicalRecordList.add(medicalRecord);
            }
        }
        return new Genson().serialize(medicalRecordList);
    }

    @Transaction(intent = Transaction.TYPE.EVALUATE)
    public String getListViewRequestBySenderQuery(
            MedicalRecordContext ctx,
            String jsonString
    ) {
        JSONObject jsonObject = new JSONObject(jsonString);
        String senderId = jsonObject.getString("senderId");
        authorizeRequest(ctx, senderId, "getListViewRequestBySenderQuery(validate senderId)");

        JSONObject jsonDto = jsonObject;

        List<ViewRequest> viewRequestList = ctx.getViewRequestDAO().getListViewRequestBySenderQuery(
                jsonDto
        );

        System.out.println("viewRequestList.size(): " + viewRequestList.size());
        for (ViewRequest viewRequest: viewRequestList) {
            System.out.println("viewRequest: " + viewRequest);
        }

        return new Genson().serialize(viewRequestList);
    }

    @Transaction(intent = Transaction.TYPE.EVALUATE)
    public String getListViewRequestByRecipientQuery(
            MedicalRecordContext ctx,
            String jsonString
    ) {
        JSONObject jsonObject = new JSONObject(jsonString);
        String recipientId = jsonObject.getString("recipientId");
        authorizeRequest(ctx, recipientId, "getListViewRequestByRecipientQuery(validate recipientId)");

        JSONObject jsonDto = jsonObject;

        List<ViewRequest> viewRequestList = ctx.getViewRequestDAO().getListViewRequestByRecipientQuery(
                jsonDto
        );

        System.out.println("viewRequestList.size(): " + viewRequestList.size());
        for (ViewRequest viewRequest: viewRequestList) {
            System.out.println("viewRequest: " + viewRequest);
        }

        return new Genson().serialize(viewRequestList);
    }

    @Transaction(intent = Transaction.TYPE.SUBMIT)
    public String addMedication(
            MedicalRecordContext ctx,
            String jsonString
    ) {
        JSONObject jsonObject = new JSONObject(jsonString);
        String manufacturerId = jsonObject.getString("manufacturerId");

        authorizeRequest(ctx, manufacturerId, "addMedication(validate manufacturerId)");
        JSONObject jsonDto = jsonObject;

        Medication medication = ctx.getMedicationDAO().addMedication(jsonDto);

        return new Genson().serialize(medication);
    }

    @Transaction(intent = Transaction.TYPE.SUBMIT)
    public String editMedication(
            MedicalRecordContext ctx,
            String jsonString
    ) {
        JSONObject jsonObject = new JSONObject(jsonString);
        if (!jsonObject.has("medicationId")) {
            throw new ChaincodeException("MedicationId is empty",
                    ContractErrors.EMPTY_MEDICATION_ID_ERROR.toString());
        }
        String medicationId = jsonObject.getString("medicationId");
        String manufacturerId = jsonObject.getString("manufacturerId");

        if (!ctx.getMedicationDAO().medicationExist(medicationId)) {
            throw new ChaincodeException("Medication " + medicationId + " does not exist",
                    ContractErrors.MEDICATION_NOT_FOUND.toString());
        }

        authorizeRequest(ctx, manufacturerId, "editMedication(validate manufacturerId)");
        JSONObject jsonDto = jsonObject;

        Medication medication = ctx.getMedicationDAO().editMedication(jsonDto);

        return new Genson().serialize(medication);
    }

    @Transaction(intent = Transaction.TYPE.EVALUATE)
    public String getAllMedication(
            MedicalRecordContext ctx
    ) {
        JSONObject jsonDto = new JSONObject();

        List<Medication> medicationList = ctx.getMedicationDAO().getListMedication(
                jsonDto
        );
        return new Genson().serialize(medicationList);
    }

    @Transaction(intent = Transaction.TYPE.EVALUATE)
    public String getListMedication(
            MedicalRecordContext ctx,
            String jsonString
    ) {
        JSONObject jsonObject = new JSONObject(jsonString);

        JSONObject jsonDto = jsonObject;

        List<Medication> medicationList = ctx.getMedicationDAO().getListMedication(
                jsonDto
        );
        return new Genson().serialize(medicationList);
    }

    @Transaction(intent = Transaction.TYPE.SUBMIT)
    public String addDrug(
            MedicalRecordContext ctx,
            String jsonString
    ) {
        try {
            JSONObject jsonObject = new JSONObject(jsonString);
            String medicationId = jsonObject.getString("medicationId");
            String manufactureDate = jsonObject.getString("manufactureDate");
            String expirationDate = jsonObject.getString("expirationDate");
            String quantity = jsonObject.getString("quantity");

            if (!ctx.getMedicationDAO().medicationExist(medicationId)) {
                throw new ChaincodeException("Medication " + medicationId + " does not exist",
                        ContractErrors.MEDICATION_NOT_FOUND.toString());
            }

            String manufacturerId = ctx.getMedicationDAO().getManufacturerId(medicationId);

            authorizeRequest(ctx, manufacturerId, "addDrug(validate jsonString)");

            JSONObject jsonDto = jsonObject;
            jsonDto.put("ownerId", manufacturerId);

            List<Drug> drugList = ctx.getDrugDAO().addDrug(jsonDto);
            return new Genson().serialize(drugList);
        }
        catch (Exception exception) {
            throw exception;
        }
    }

    @Transaction(intent = Transaction.TYPE.SUBMIT)
    public String transferDrug(
            MedicalRecordContext ctx,
            String jsonString
    ) {
        JSONObject jsonObject = new JSONObject(jsonString);
        String drugId = jsonObject.getString("drugId");
        String newOwnerId = jsonObject.getString("newOwnerId");

        Drug drug = ctx.getDrugDAO().getDrug(drugId);

        authorizeRequest(ctx, drug.getOwnerId(), "transferDrug(validate jsonString)");

        JSONObject jsonDto = jsonObject;
        drug = ctx.getDrugDAO().transferDrug(jsonDto);
        return new Genson().serialize(drug);
    }

    @Transaction(intent = Transaction.TYPE.EVALUATE)
    public String getListDrugByOwnerId(
            MedicalRecordContext ctx,
            String jsonString
    ) {
        JSONObject jsonObject = new JSONObject(jsonString);
        if (!jsonObject.has("ownerId")) {
            throw new ChaincodeException("ownerId not found",
                    ContractErrors.UNAUTHORIZED_VIEW_ACCESS.toString());
        }

        JSONObject jsonDto = jsonObject;

        List<Drug> drugList = ctx.getDrugDAO().getListDrugByOwnerId(
                jsonDto
        );
        return new Genson().serialize(drugList);
    }

    public Prescription addPrescription(
            MedicalRecordContext ctx,
            String jsonString
    ) {
        JSONObject jsonObject = new JSONObject(jsonString);
        String prescriptionDetailsListStr = jsonObject.getString("prescriptionDetailsList");

        JSONObject jsonDto = jsonObject;
        Prescription prescription = ctx.getPrescriptionDAO().addPrescription(jsonDto);

        System.out.println("prescriptionDetailsListStr = " + prescriptionDetailsListStr);
        List<PrescriptionDetails> prescriptionDetailsList = new Genson().deserialize(prescriptionDetailsListStr,
                new GenericType<List<PrescriptionDetails>>() {});
        System.out.println("prescriptionDetailsListStr.size(): " + prescriptionDetailsList.size());

        int id = 0;
        for (PrescriptionDetails prescriptionDetails: prescriptionDetailsList) {
            prescriptionDetails.setPrescriptionId(prescription.getPrescriptionId());
            prescriptionDetails.setPrescriptionDetailId(prescription.getPrescriptionId() + "-" + id++);
            prescriptionDetails.setEntityName(PrescriptionDetails.class.getSimpleName());
            prescriptionDetails.setPurchasedQuantity("0");
            PrescriptionDetails pd = ctx.getPrescriptionDetailsDAO()
                    .addPrescriptionDetails(prescriptionDetails);
        }
        return prescription;
    }

    @Transaction(intent = Transaction.TYPE.EVALUATE)
    public String updateDrugReactionFromPatient(
            MedicalRecordContext ctx,
            String jsonString
    ) {
        JSONObject jsonObject = new JSONObject(jsonString);
        String medicalRecordId = jsonObject.getString("medicalRecordId");
        String prescriptionId = jsonObject.getString("prescriptionId");
        String drugReaction = jsonObject.getString("drugReaction");

        MedicalRecord medicalRecord = ctx.getMedicalRecordDAO().getMedicalRecord(medicalRecordId);

        if (!Objects.equals(prescriptionId, medicalRecord.getPrescriptionId())) {
            throw new ChaincodeException("prescriptionId " + prescriptionId + " does not match medicalRecord.getPrescription()",
                    ContractErrors.UNAUTHORIZED_EDIT_ACCESS.toString());
        }

        List<Purchase> purchaseList = ctx.getPurchaseDAO().getListPurchaseByQuery(
                new JSONObject().put("prescriptionId", prescriptionId)
        );

        if (purchaseList.isEmpty()) {
            throw new ChaincodeException("Not found purchase",
                    ContractErrors.PURCHASE_NOT_FOUND.toString());
        }

        authorizeRequest(ctx, medicalRecord.getPatientId(), "sendDrugReactionFromPatient(validate medicalRecord.getPatientId())");

        JSONObject jsonDto = jsonObject;
        Prescription prescription = ctx.getPrescriptionDAO().updateDrugReactionFromPatient(jsonDto);
        return new Genson().serialize(prescription);
    }

    @Transaction(intent = Transaction.TYPE.EVALUATE)
    public String getPrescriptionByPatient(
            MedicalRecordContext ctx,
            String jsonString
    ) {
        JSONObject jsonObject = new JSONObject(jsonString);
        String prescriptionId = jsonObject.getString("prescriptionId");
        String patientId = jsonObject.getString("patientId");
        authorizeRequest(ctx, patientId, "getPrescriptionByPatient(validate patientId)");

        String medicalRecordId = prescriptionId;
        if (!ctx.getMedicalRecordDAO().medicalRecordExist(medicalRecordId)) {
            throw new ChaincodeException("UNAUTHORIZED_VIEW_PRESCRIPTION_ACCESS",
                    ContractErrors.UNAUTHORIZED_VIEW_PRESCRIPTION_ACCESS.toString());
        }

        MedicalRecord medicalRecord = ctx.getMedicalRecordDAO().getMedicalRecord(prescriptionId);

        if (!Objects.equals(medicalRecord.getPrescriptionId(), prescriptionId)) {
            throw new ChaincodeException("UNAUTHORIZED_VIEW_PRESCRIPTION_ACCESS",
                    ContractErrors.UNAUTHORIZED_VIEW_PRESCRIPTION_ACCESS.toString());
        }

        if (!Objects.equals(medicalRecord.getPatientId(), patientId)) {
            throw new ChaincodeException("UNAUTHORIZED_VIEW_PRESCRIPTION_ACCESS",
                    ContractErrors.UNAUTHORIZED_VIEW_PRESCRIPTION_ACCESS.toString());
        }

        Prescription prescription = ctx.getPrescriptionDAO().getPrescription(prescriptionId);

        PrescriptionDto prescriptionDto = new PrescriptionDto();
        prescriptionDto.setPrescriptionId(prescription.getPrescriptionId());
        prescriptionDto.setDrugReaction(prescription.getDrugReaction());
        prescriptionDto.setEntityName(prescription.getEntityName());

        List<PrescriptionDetails> prescriptionDetailsList = ctx.getPrescriptionDetailsDAO().getListPrescriptionDetails(prescriptionId);
        List<PrescriptionDetailsDto> prescriptionDetailsDtoList = new ArrayList<>();
        for (PrescriptionDetails prescriptionDetails: prescriptionDetailsList) {
            PrescriptionDetailsDto prescriptionDetailsDto = new PrescriptionDetailsDto(prescriptionDetails);
            Medication medication = ctx.getMedicationDAO().getMedication(prescriptionDetailsDto.getMedicationId());
            prescriptionDetailsDto.setMedicationName(medication.getMedicationName());
            prescriptionDetailsDtoList.add(prescriptionDetailsDto);
        }
        prescriptionDto.setPrescriptionDetailsListDto(prescriptionDetailsDtoList);
        return new Genson().serialize(prescriptionDto);
    }

    @Transaction(intent = Transaction.TYPE.EVALUATE)
    public String getPrescriptionByDrugStore(
            MedicalRecordContext ctx,
            String jsonString
    ) {
        JSONObject jsonObject = new JSONObject(jsonString);
        String prescriptionId = jsonObject.getString("prescriptionId");
        String drugStoreId = jsonObject.getString("drugStoreId");
        authorizeRequest(ctx, drugStoreId, "getPrescriptionByDrugStore(validate drugStoreId)");

        JSONObject jsonDto = new JSONObject();
        jsonDto.put("prescriptionId", prescriptionId);
        jsonDto.put("senderId", drugStoreId);
        jsonDto.put("requestType", RequestType.VIEW_PRESCRIPTION);
        jsonDto.put("requestStatus", RequestStatus.ACCEPTED);

        List<ViewPrescriptionRequest> viewPrescriptionRequestList = ctx.getViewPrescriptionRequestDAO()
                .getListViewPrescriptionRequestByDrugStoreQuery(jsonDto);

        if (viewPrescriptionRequestList.isEmpty()) {
            throw new ChaincodeException("UNAUTHORIZED_VIEW_PRESCRIPTION_ACCESS",
                    ContractErrors.UNAUTHORIZED_VIEW_PRESCRIPTION_ACCESS.toString());
        }

        Prescription prescription = ctx.getPrescriptionDAO().getPrescription(prescriptionId);

        PrescriptionDto prescriptionDto = new PrescriptionDto();
        prescriptionDto.setPrescriptionId(prescription.getPrescriptionId());
        prescriptionDto.setDrugReaction(prescription.getDrugReaction());
        prescriptionDto.setEntityName(prescription.getEntityName());
        List<PrescriptionDetails> prescriptionDetailsList = ctx.getPrescriptionDetailsDAO().getListPrescriptionDetails(prescriptionId);
        List<PrescriptionDetailsDto> prescriptionDetailsDtoList = new ArrayList<>();
        for (PrescriptionDetails prescriptionDetails: prescriptionDetailsList) {
            PrescriptionDetailsDto prescriptionDetailsDto = new PrescriptionDetailsDto(prescriptionDetails);
            Medication medication = ctx.getMedicationDAO().getMedication(prescriptionDetailsDto.getMedicationId());
            prescriptionDetailsDto.setMedicationName(medication.getMedicationName());
            prescriptionDetailsDtoList.add(prescriptionDetailsDto);
        }
        prescriptionDto.setPrescriptionDetailsListDto(prescriptionDetailsDtoList);
        return new Genson().serialize(prescriptionDto);
    }

    @Transaction(intent = Transaction.TYPE.SUBMIT)
    public String sendViewPrescriptionRequest(
            MedicalRecordContext ctx,
            String jsonString
    ) {
        JSONObject jsonObject = new JSONObject(jsonString);

        String senderId = jsonObject.getString("senderId");
        String recipientId = jsonObject.getString("recipientId");
        String dateCreated = jsonObject.getString("dateCreated");
        String dateModified = jsonObject.getString("dateModified");
        String prescriptionId = jsonObject.getString("prescriptionId");

        authorizeRequest(ctx, senderId, "sendViewPrescriptionRequest(validate senderId)");

        JSONObject jsonDto = new JSONObject();
        jsonDto.put("senderId", senderId);
        jsonDto.put("recipientId", recipientId);
        jsonDto.put("dateCreated", dateCreated);
        jsonDto.put("dateModified", dateModified);
        jsonDto.put("requestType", RequestType.VIEW_PRESCRIPTION);
        jsonDto.put("prescriptionId", prescriptionId);
        ViewPrescriptionRequest viewPrescriptionRequest = ctx.getViewPrescriptionRequestDAO().sendViewPrescriptionRequest(
                jsonDto
        );
        return new Genson().serialize(viewPrescriptionRequest);
    }

    @Transaction(intent = Transaction.TYPE.SUBMIT)
    public String defineViewPrescriptionRequest(
            MedicalRecordContext ctx,
            String jsonString
    ) {
        JSONObject jsonObject = new JSONObject(jsonString);
        String requestId = jsonObject.getString("requestId");
        String requestStatus = jsonObject.getString("requestStatus");
        ViewPrescriptionRequestDAO viewPrescriptionRequestDAO = ctx.getViewPrescriptionRequestDAO();

        if (!viewPrescriptionRequestDAO.requestExist(requestId)) {
            throw new ChaincodeException("ViewPrescriptionRequest " + requestId + " does not exist",
                    ContractErrors.REQUEST_NOT_FOUND.toString());
        }

        ViewPrescriptionRequest viewPrescriptionRequest = viewPrescriptionRequestDAO.getViewPrescriptionRequest(requestId);

        authorizeRequest(ctx, viewPrescriptionRequest.getRecipientId(), "defineViewPrescriptionRequest(validate recipientId");

        JSONObject jsonDto = new JSONObject();
        jsonDto.put("requestId", requestId);
        jsonDto.put("requestStatus", requestStatus);
        viewPrescriptionRequest = ctx.getViewPrescriptionRequestDAO().defineViewPrescriptionRequest(
                jsonDto
        );
        return new Genson().serialize(viewPrescriptionRequest);
    }

    public boolean checkDrugConditions(Drug drug, String dateCreatedStr, String drugStoreId) throws ChaincodeException, ParseException {
        if (!Objects.equals(drug.getOwnerId(), drugStoreId)) {
            throw new ChaincodeException("Drug " + drug.getDrugId() + " does not belong to the drug store " + drugStoreId,
                    ContractErrors.DRUG_OWNERSHIP_ERROR.toString());
        }

        Date expirationDate = new SimpleDateFormat("yyyy-MM-dd").parse(drug.getExpirationDate());
        Date dateCreated = new SimpleDateFormat("yyyy-MM-dd").parse(dateCreatedStr);

        if (dateCreated.after(expirationDate)) {
            throw new ChaincodeException("Drug " + drug.getDrugId() + " is expired",
                    ContractErrors.DRUG_EXPIRED_ERROR.toString());
        }
        return true;
    }

    @Transaction(intent = Transaction.TYPE.SUBMIT)
    public String addPurchase(
            MedicalRecordContext ctx,
            String jsonString
    ) throws ParseException {
        JSONObject jsonObject = new JSONObject(jsonString);
        String prescriptionId = jsonObject.getString("prescriptionId");
        String patientId = jsonObject.getString("patientId");
        String dateCreated = jsonObject.getString("dateCreated");
        String dateModified = jsonObject.getString("dateModified");
        String drugStoreId = jsonObject.getString("drugStoreId");
        String medicationPurchaseListStr = jsonObject.getString("medicationPurchaseList");

        if (!ctx.getPrescriptionDAO().prescriptionExist(prescriptionId)) {
            throw new ChaincodeException("Prescription " + prescriptionId + " does not exist",
                    ContractErrors.PRESCRIPTION_NOT_FOUND.toString());
        }

        authorizeRequest(ctx, drugStoreId, "addPurchase(validate drugStoreId)");

        List<MedicationPurchaseDto> medicationPurchaseList = new Genson().deserialize(
                medicationPurchaseListStr,
                new GenericType<List<MedicationPurchaseDto>>() {}
        );

        List<PrescriptionDetails> updatePrescriptionDetailsList = new ArrayList<>();
        List<PurchaseDetails> createPurchaseDetailsList = new ArrayList<>();
        List<Drug> transferDrug = new ArrayList<>();

        for (MedicationPurchaseDto medicationPurchaseDto: medicationPurchaseList) {
            String medicationId = medicationPurchaseDto.getMedicationId();
            String prescriptionDetailId = medicationPurchaseDto.getPrescriptionDetailId();
            PrescriptionDetails prescriptionDetails = ctx.getPrescriptionDetailsDAO().getPrescriptionDetails(prescriptionDetailId);
            List<String> drugIdList = medicationPurchaseDto.getDrugIdList();
            int count = 0;
            for (String drugId: drugIdList) {
                Drug drug = ctx.getDrugDAO().getDrug(drugId);
                if (checkDrugConditions(drug, dateCreated, drugStoreId)) {
                    ++count;
                }
                else {
                    throw new ChaincodeException("The drug is not qualified for sale",
                            ContractErrors.NOT_QUALIFIED_FOR_SALE.toString());
                }

                transferDrug.add(drug);

                PurchaseDetails purchaseDetails = new PurchaseDetails();
                purchaseDetails.setPrescriptionDetailId(prescriptionDetailId);
                purchaseDetails.setMedicationId(medicationId);
                purchaseDetails.setDrugId(drugId);
                purchaseDetails.setEntityName(PurchaseDetails.class.getSimpleName());

                createPurchaseDetailsList.add(purchaseDetails);
            }
            Long newPurchasedQuantity = Long.parseLong(prescriptionDetails.getPurchasedQuantity()) + count;
            if (newPurchasedQuantity <= Long.parseLong(prescriptionDetails.getQuantity())) {
                prescriptionDetails.setPurchasedQuantity(newPurchasedQuantity.toString());
                updatePrescriptionDetailsList.add(prescriptionDetails);
            }
            else {
                throw new ChaincodeException("The quantity purchased is more than the quantity prescribed in the Prescription Details",
                        ContractErrors.EXCEEDED_THE_QUANTITY_PURCHASED_IN_THE_PRESCRIPTION_DETAILS.toString());
            }
        }

        PurchaseDto purchaseDto = new PurchaseDto();
        purchaseDto.setPrescriptionId(prescriptionId);
        purchaseDto.setPatientId(patientId);
        purchaseDto.setDrugStoreId(drugStoreId);
        purchaseDto.setDateCreated(dateCreated);
        purchaseDto.setDateModified(dateModified);

        Purchase purchase = ctx.getPurchaseDAO().addPurchase(
                purchaseDto.toJSONObject()
        );

        for (PrescriptionDetails prescriptionDetails: updatePrescriptionDetailsList) {
            prescriptionDetails = ctx.getPrescriptionDetailsDAO().updatePrescriptionDetails(prescriptionDetails);
        }

        for (PurchaseDetails purchaseDetails: createPurchaseDetailsList) {
            purchaseDetails = ctx.getPurchaseDetailsDAO().addPurchaseDetails(purchaseDetails.toJSONObject());
        }

        for (Drug drug: transferDrug) {
            JSONObject jsonDto = new JSONObject();
            jsonDto.put("drugId", drug.getDrugId());
            jsonDto.put("newOwnerId", patientId);
            drug = ctx.getDrugDAO().transferDrug(jsonDto);
        }

        return new Genson().serialize(purchase);
    }

    @Transaction(intent = Transaction.TYPE.SUBMIT)
    public String addInsuranceProduct(
            MedicalRecordContext ctx,
            String jsonString
    ) {
        JSONObject jsonObject = new JSONObject(jsonString);
        String insuranceCompanyId = jsonObject.getString("insuranceCompanyId");

        authorizeRequest(ctx, insuranceCompanyId, "addInsuranceProduct(validate insuranceCompanyId)");
        JSONObject jsonDto = jsonObject;

        InsuranceProduct insuranceProduct = ctx.getInsuranceProductDAO().addInsuranceProduct(jsonDto);

        return new Genson().serialize(insuranceProduct);
    }

    @Transaction(intent = Transaction.TYPE.SUBMIT)
    public String editInsuranceProduct(
            MedicalRecordContext ctx,
            String jsonString
    ) {
        JSONObject jsonObject = new JSONObject(jsonString);
        if (!jsonObject.has("insuranceProductId")) {
            throw new ChaincodeException("InsuranceProductId is empty",
                    ContractErrors.EMPTY_INSURANCE_PRODUCT_ID_ERROR.toString());
        }
        String insuranceProductId = jsonObject.getString("insuranceProductId");

        if (!ctx.getInsuranceProductDAO().insuranceProductExist(insuranceProductId)) {
            throw new ChaincodeException("InsuranceProduct " + insuranceProductId + " does not exist",
                    ContractErrors.INSURANCE_PRODUCT_NOT_FOUND.toString());
        }

        String insuranceCompanyId = jsonObject.getString("insuranceCompanyId");
        authorizeRequest(ctx, insuranceCompanyId, "editInsuranceProduct(validate insuranceCompanyId)");
        JSONObject jsonDto = jsonObject;

        InsuranceProduct insuranceProduct = ctx.getInsuranceProductDAO().editInsuranceProduct(jsonDto);

        return new Genson().serialize(insuranceProduct);
    }

    @Transaction(intent = Transaction.TYPE.EVALUATE)
    public String getListInsuranceProduct(
            MedicalRecordContext ctx,
            String jsonString
    ) {
        JSONObject jsonDto = new JSONObject(jsonString);

        List<InsuranceProduct> insuranceProductList = ctx.getInsuranceProductDAO().getListInsuranceProduct(
                jsonDto
        );
        return new Genson().serialize(insuranceProductList);
    }

    @Transaction(intent = Transaction.TYPE.SUBMIT)
    public String sendPurchaseRequest(
            MedicalRecordContext ctx,
            String jsonString
    ) {
        JSONObject jsonObject = new JSONObject(jsonString);
        String senderId = jsonObject.getString("senderId");
        String recipientId = jsonObject.getString("recipientId");
        String dateCreated = jsonObject.getString("dateCreated");
        String dateModified = jsonObject.getString("dateModified");
        String insuranceProductId = jsonObject.getString("insuranceProductId");

        authorizeRequest(ctx, senderId, "sendPurchaseRequest(validate senderId)");

        JSONObject jsonDto = jsonObject;
        jsonDto.put("requestType", RequestType.PURCHASE);

        InsuranceProduct insuranceProduct = ctx.getInsuranceProductDAO().getInsuranceProductCRUD()
                .getInsuranceProduct(insuranceProductId);

        jsonDto.put("numberOfDaysInsured", insuranceProduct.getNumberOfDaysInsured());
        System.out.println("numberOfDaysInsured: " + insuranceProduct.getNumberOfDaysInsured());

        PurchaseRequest purchaseRequest = ctx.getPurchaseRequestDAO().sendPurchaseRequest(jsonDto);

        ctx.getViewRequestDAO().sendViewRequestAccepted(
                new JSONObject().put("senderId", insuranceProduct.getInsuranceCompanyId())
                        .put("recipientId", senderId)
                        .put("dateCreated", dateCreated)
                        .put("dateModified", dateModified)
        );
        return new Genson().serialize(purchaseRequest);
    }

    @Transaction(intent = Transaction.TYPE.SUBMIT)
    public String definePurchaseRequest(
            MedicalRecordContext ctx,
            String jsonString
    ) {
        JSONObject jsonObject = new JSONObject(jsonString);
        String requestId = jsonObject.getString("requestId");
        String requestStatus = jsonObject.getString("requestStatus");
        String hashFile = jsonObject.has("hashFile") ? jsonObject.getString("hashFile") : "";
        String dateCreated = jsonObject.has("dateCreated") ? jsonObject.getString("dateCreated") : "";
        String dateModified = jsonObject.has("dateModified") ? jsonObject.getString("dateModified") : "";
        PurchaseRequestDAO purchaseRequestDAO = ctx.getPurchaseRequestDAO();

        if (!purchaseRequestDAO.requestExist(requestId)) {
            throw new ChaincodeException("PurchaseRequest " + requestId + " does not exist",
                    ContractErrors.REQUEST_NOT_FOUND.toString());
        }

        PurchaseRequest purchaseRequest = purchaseRequestDAO.getPurchaseRequest(requestId);
        authorizeRequest(ctx, purchaseRequest.getRecipientId(), "definePurchaseRequest(validate recipientId");

        if (Objects.equals(requestStatus, RequestStatus.APPROVED) && !jsonObject.has("hashFile")) {
            throw new ChaincodeException("Error: hashFile is empty",
                    ContractErrors.HASH_FILE_IS_EMPTY.toString());
        }

        if (Objects.equals(requestStatus, RequestStatus.ACCEPTED)) {
            if (!Objects.equals(hashFile, purchaseRequest.getHashFile())) {
                throw new ChaincodeException("Error: Cannot edit hashFile",
                        ContractErrors.CAN_NOT_EDIT_HASH_FILE.toString());
            }
            else {
                JSONObject jsonInsuranceContractDto = new JSONObject();
                jsonInsuranceContractDto.put("insuranceContractId", ctx.getStub().getTxId());
                jsonInsuranceContractDto.put("insuranceProductId", purchaseRequest.getInsuranceProductId());
                jsonInsuranceContractDto.put("patientId", purchaseRequest.getSenderId());
                jsonInsuranceContractDto.put("insuranceCompanyId", purchaseRequest.getRecipientId());
                jsonInsuranceContractDto.put("startDate", purchaseRequest.getStartDate());
                jsonInsuranceContractDto.put("endDate", purchaseRequest.getEndDate());
                jsonInsuranceContractDto.put("dateCreated", dateCreated);
                jsonInsuranceContractDto.put("dateModified", dateModified);
                jsonInsuranceContractDto.put("hashFile", hashFile);
                ctx.getInsuranceContractDAO().addInsuranceContract(
                        jsonInsuranceContractDto
                );
            }
        }

        JSONObject jsonDto = jsonObject;
        purchaseRequest = ctx.getPurchaseRequestDAO().defineRequest(
                jsonDto
        );

        return new Genson().serialize(purchaseRequest);
    }

    @Transaction(intent = Transaction.TYPE.SUBMIT)
    public String sendPaymentRequest(
            MedicalRecordContext ctx,
            String jsonString
    ) {
        JSONObject jsonObject = new JSONObject(jsonString);
        String senderId = jsonObject.getString("senderId");
        String recipientId = jsonObject.getString("recipientId");
        String dateCreated = jsonObject.getString("dateCreated");
        String dateModified = jsonObject.getString("dateModified");
        String insuranceContractId = jsonObject.getString("insuranceContractId");
        String medicalRecordId = jsonObject.getString("medicalRecordId");
        Genson genson = new Genson();

        InsuranceContract insuranceContract = ctx.getInsuranceContractDAO().getInsuranceContract(insuranceContractId);
        authorizeRequest(ctx, insuranceContract.getPatientId(), "sendPaymentRequest(validate senderId)");

        JSONObject jsonDto = jsonObject;
        jsonDto.put("requestType", RequestType.PAYMENT);
        PaymentRequest paymentRequest = ctx.getPaymentRequestDAO().sendPaymentRequest(
                jsonDto
        );
        System.out.println("sendPaymentRequest - paymentRequest: " + paymentRequest);
        return new Genson().serialize(paymentRequest);
    }

    @Transaction(intent = Transaction.TYPE.SUBMIT)
    public String definePaymentRequest(
            MedicalRecordContext ctx,
            String jsonString
    ) {
        System.out.println("jsonString: " + jsonString);
        JSONObject jsonObject = new JSONObject(jsonString);

        System.out.println("jsonObject: " + jsonObject);

        String requestId = jsonObject.getString("requestId");
        String requestStatus = jsonObject.getString("requestStatus");
        String dateCreated = jsonObject.getString("dateCreated");
        String dateModified = jsonObject.getString("dateModified");

        System.out.println("requestId: " + requestId);
        System.out.println("requestStatus: " + requestStatus);

        PaymentRequestDAO paymentRequestDAO = ctx.getPaymentRequestDAO();
        if (!paymentRequestDAO.requestExist(requestId)) {
            throw new ChaincodeException("PaymentRequest " + requestId + " does not exist",
                    ContractErrors.REQUEST_NOT_FOUND.toString());
        }

        PaymentRequest paymentRequest = paymentRequestDAO.getPaymentRequest(requestId);
        authorizeRequest(ctx, paymentRequest.getRecipientId(), "definePaymentRequest(validate recipientId");

        JSONObject jsonDto = new JSONObject();

        jsonDto = new JSONObject();
        jsonDto.put("requestId", requestId);
        jsonDto.put("requestStatus", requestStatus);

        System.out.println("jsonDto: " + jsonDto.toString());
        paymentRequest = ctx.getPaymentRequestDAO().definePaymentRequest(
                jsonDto
        );

        if (Objects.equals(requestStatus, RequestStatus.ACCEPTED)) {
            ctx.getConfirmPaymentRequestDAO().sendConfirmPaymentRequest(
                    new JSONObject()
                            .put("senderId", paymentRequest.getRecipientId())
                            .put("recipientId", paymentRequest.getSenderId())
                            .put("dateCreated", dateCreated)
                            .put("dateModified", dateModified)
                            .put("paymentRequestId", paymentRequest.getRequestId())
            );
        }
        return new Genson().serialize(paymentRequest);
    }

    @Transaction(intent = Transaction.TYPE.SUBMIT)
    public String sendConfirmPaymentRequest(
            MedicalRecordContext ctx,
            String jsonString
    ) {
        JSONObject jsonObject = new JSONObject(jsonString);
        String senderId = jsonObject.getString("senderId");
        String recipientId = jsonObject.getString("recipientId");
        String dateCreated = jsonObject.getString("dateCreated");
        String dateModified = jsonObject.getString("dateModified");
        String paymentRequestId = jsonObject.getString("paymentRequestId");
        Genson genson = new Genson();

        PaymentRequest paymentRequest = ctx.getPaymentRequestDAO().getPaymentRequest(paymentRequestId);
        authorizeRequest(ctx, paymentRequest.getRecipientId(), "sendConfirmPaymentRequest(validate paymentRequest.getRecipientId())");

        JSONObject jsonDto = jsonObject;
        jsonDto.put("requestType", RequestType.CONFIRM_PAYMENT);
        ConfirmPaymentRequest confirmPaymentRequest = ctx.getConfirmPaymentRequestDAO().sendConfirmPaymentRequest(
                jsonDto
        );
        System.out.println("confirmPaymentRequest: " + confirmPaymentRequest);
        return new Genson().serialize(confirmPaymentRequest);
    }

    @Transaction(intent = Transaction.TYPE.SUBMIT)
    public String defineConfirmPaymentRequest(
            MedicalRecordContext ctx,
            String jsonString
    ) {
        System.out.println("jsonString: " + jsonString);
        JSONObject jsonObject = new JSONObject(jsonString);

        System.out.println("jsonObject: " + jsonObject);

        String requestId = jsonObject.getString("requestId");
        String requestStatus = jsonObject.getString("requestStatus");
        String dateCreated = jsonObject.getString("dateCreated");
        String dateModified = jsonObject.getString("dateModified");

        System.out.println("requestId: " + requestId);
        System.out.println("requestStatus: " + requestStatus);

        ConfirmPaymentRequestDAO confirmPaymentRequestDAO = ctx.getConfirmPaymentRequestDAO();
        if (!confirmPaymentRequestDAO.requestExist(requestId)) {
            throw new ChaincodeException("ConfirmPaymentRequest " + requestId + " does not exist",
                    ContractErrors.REQUEST_NOT_FOUND.toString());
        }

        ConfirmPaymentRequest confirmPaymentRequest = confirmPaymentRequestDAO.getConfirmPaymentRequest(requestId);

        authorizeRequest(ctx, confirmPaymentRequest.getRecipientId(), "defineConfirmPaymentRequest(validate recipientId");

        JSONObject jsonDto = jsonObject;

        System.out.println("jsonDto: " + jsonDto.toString());
        confirmPaymentRequest = ctx.getConfirmPaymentRequestDAO().defineConfirmPaymentRequest(
                jsonDto
        );

        return new Genson().serialize(confirmPaymentRequest);
    }

    @Transaction(intent = Transaction.TYPE.EVALUATE)
    public String getListInsuranceContractByPatientQuery(
            MedicalRecordContext ctx,
            String jsonString
    ) {
        JSONObject jsonObject = new JSONObject(jsonString);
        if (!jsonObject.has("patientId")
                || (jsonObject.has("patientId") & Objects.equals(jsonObject.getString("patientId"), ""))) {
            throw new ChaincodeException("Error: patientId is empty",
                    ContractErrors.UNAUTHORIZED_EDIT_ACCESS.toString());
        }

        String patientId = jsonObject.getString("patientId");

        authorizeRequest(ctx, patientId, "getListMedicalRecordByPatientQuery(validate patientId)");

        JSONObject jsonDto = jsonObject;

        List<InsuranceContract> insuranceContractList = ctx.getInsuranceContractDAO().getListInsuranceContractByQuery(
                jsonDto
        );
        return new Genson().serialize(insuranceContractList);
    }

    @Transaction(intent = Transaction.TYPE.EVALUATE)
    public String getListConfirmPaymentRequestBySenderQuery(
            MedicalRecordContext ctx,
            String jsonString
    ) {
        JSONObject jsonObject = new JSONObject(jsonString);

        if (!jsonObject.has("senderId")
                || (jsonObject.has("senderId") & Objects.equals(jsonObject.getString("senderId"), ""))) {
            throw new ChaincodeException("Error: senderId is empty",
                    ContractErrors.UNAUTHORIZED_VIEW_ACCESS.toString());
        }

        String senderId = jsonObject.getString("senderId");

        authorizeRequest(ctx, senderId, "getListConfirmPaymentRequestBySenderQuery(validate senderId)");

        JSONObject jsonDto = jsonObject;

        List<ConfirmPaymentRequest> confirmPaymentRequestList = ctx.getConfirmPaymentRequestDAO().getListConfirmPaymentRequestByQuery(
                jsonDto
        );

        return new Genson().serialize(confirmPaymentRequestList);
    }

    @Transaction(intent = Transaction.TYPE.EVALUATE)
    public String getListConfirmPaymentRequestByRecipientQuery(
            MedicalRecordContext ctx,
            String jsonString
    ) {
        JSONObject jsonObject = new JSONObject(jsonString);

        if (!jsonObject.has("recipientId")
                || (jsonObject.has("recipientId") & Objects.equals(jsonObject.getString("recipientId"), ""))) {
            throw new ChaincodeException("Error: recipientId is empty",
                    ContractErrors.UNAUTHORIZED_VIEW_ACCESS.toString());
        }

        String recipientId = jsonObject.getString("recipientId");

        authorizeRequest(ctx, recipientId, "getListConfirmPaymentRequestByRecipientQuery(validate recipientId)");

        JSONObject jsonDto = jsonObject;

        List<ConfirmPaymentRequest> confirmPaymentRequestList = ctx.getConfirmPaymentRequestDAO().getListConfirmPaymentRequestByQuery(
                jsonDto
        );

        return new Genson().serialize(confirmPaymentRequestList);
    }


    @Transaction(intent = Transaction.TYPE.EVALUATE)
    public String getListAllRequestByUserQuery(
            MedicalRecordContext ctx,
            String jsonString
    ) {
        JSONObject jsonObject = new JSONObject(jsonString);

        if (!jsonObject.has("userId")
                || (jsonObject.has("userId") & Objects.equals(jsonObject.getString("userId"), ""))) {
            throw new ChaincodeException("Error: userId is empty",
                    ContractErrors.UNAUTHORIZED_VIEW_ACCESS.toString());
        }

        String userId = jsonObject.getString("userId");

        authorizeRequest(ctx, userId, "getListAllRequestByUserQuery(validate userId)");

        JSONObject jsonDto = new JSONObject();
        jsonDto.put("senderId", userId);

        List<Request> requestListBySender = ctx.getRequestDAO().getListRequest(
                jsonDto
        );

        jsonDto = new JSONObject();
        jsonDto.put("recipientId", userId);

        List<Request> requestListByRecipient = ctx.getRequestDAO().getListRequest(
                jsonDto
        );

        List<Request> requestList = new ArrayList<>();
        for (Request request: requestListBySender) requestList.add(request);
        for (Request request: requestListByRecipient) requestList.add(request);
        return new Genson().serialize(requestList);
    }

    public enum ContractErrors {
        MEDICAL_RECORD_NOT_FOUND,
        REQUEST_NOT_FOUND,
        UNAUTHORIZED_VIEW_ACCESS,
        UNAUTHORIZED_VIEW_PRESCRIPTION_ACCESS,
        UNAUTHORIZED_EDIT_ACCESS,
        VALIDATE_MEDICAL_RECORD_ACCESS_ERROR,
        VALIDATE_VIEW_PRESCRIPTION_ACCESS_ERROR,
        EDIT_REQUEST_NOT_FOUND,
        MEDICATION_NOT_FOUND,
        EMPTY_MEDICATION_ID_ERROR,
        DRUG_NOT_FOUND,
        PRESCRIPTION_NOT_FOUND,
        PURCHASE_NOT_FOUND,
        PRESCRIPTION_DETAIL_NOT_FOUND,
        NOT_QUALIFIED_FOR_SALE,
        EXCEEDED_THE_QUANTITY_PURCHASED_IN_THE_PRESCRIPTION_DETAILS,
        DRUG_OWNERSHIP_ERROR,
        DRUG_EXPIRED_ERROR,
        EMPTY_INSURANCE_PRODUCT_ID_ERROR,
        INSURANCE_PRODUCT_NOT_FOUND,
        HASH_FILE_IS_EMPTY,
        CAN_NOT_EDIT_HASH_FILE;
    }
}

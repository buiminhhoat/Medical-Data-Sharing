package healthInformationSharing.contract;

import com.owlike.genson.GenericType;
import com.owlike.genson.Genson;
import healthInformationSharing.component.MedicalRecordContext;
import healthInformationSharing.dao.AppointmentRequestDAO;
import healthInformationSharing.dao.EditRequestDAO;
import healthInformationSharing.dao.ViewPrescriptionRequestDAO;
import healthInformationSharing.dao.ViewRequestDAO;
import healthInformationSharing.dto.PrescriptionDto;
import healthInformationSharing.entity.*;
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
    public String addMedicalRecord(
            MedicalRecordContext ctx,
            String jsonString
    ) {
        JSONObject jsonObject = new JSONObject(jsonString);
        String requestId = jsonObject.getString("requestId");
        String patientId = jsonObject.getString("patientId");
        String doctorId = jsonObject.getString("doctorId");
        String medicalInstitutionId = jsonObject.getString("medicalInstitutionId");
        String dateModified = jsonObject.getString("dateModified");
        String testName = jsonObject.getString("testName");
        String details = jsonObject.getString("details");
        String addPrescription = jsonObject.getString("addPrescription");
        String hashFile = jsonObject.getString("hashFile");

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

        Prescription prescription = addPrescription(ctx, addPrescription);

        JSONObject jsonDto = new JSONObject();

        jsonDto.put("medicalRecordId", medicalRecordId);
        jsonDto.put("patientId", patientId);
        jsonDto.put("doctorId", doctorId);
        jsonDto.put("medicalInstitutionId", medicalInstitutionId);
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
            throw new ChaincodeException(errorMessage, MedicalRecordContractErrors.MEDICAL_RECORD_NOT_FOUND.toString());
        }

        jsonDto = new JSONObject();

        jsonDto.put("medicalRecordId", medicalRecordId);
        jsonDto.put("medicalRecordStatus", medicalRecordStatus);

        MedicalRecord medicalRecord = ctx.getMedicalRecordDAO().defineMedicalRecord(jsonDto);

        authorizeRequest(ctx, medicalRecord.getPatientId(), "defineMedicalRecord(validate patientId)");

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
            throw new ChaincodeException(errorMessage, MedicalRecordContractErrors.MEDICAL_RECORD_NOT_FOUND.toString());
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
    public String getEditRequest(MedicalRecordContext ctx, String jsonString) {
        JSONObject jsonObject = new JSONObject(jsonString);
        String requestId = jsonObject.getString("requestId");
        if (ctx.getEditRequestDAO().requestExist(requestId)) {
            EditRequest editRequest = ctx.getEditRequestDAO().getEditRequest(requestId);
            System.out.println("getEditRequest: " + editRequest);
            return new Genson().serialize(editRequest);
        } else {
            String errorMessage = String.format("Edit Request %s does not exist", requestId);
            System.out.println(errorMessage);
            throw new ChaincodeException(errorMessage, MedicalRecordContractErrors.EDIT_REQUEST_NOT_FOUND.toString());
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
        String dateModified = jsonObject.getString("dateModified");
        authorizeRequest(ctx, senderId, "sendAppointmentRequest(validate senderId)");

        JSONObject jsonDto = new JSONObject();

        jsonDto.put("senderId", senderId);
        jsonDto.put("recipientId", recipientId);
        jsonDto.put("dateModified", dateModified);
        jsonDto.put("requestType", RequestType.APPOINTMENT);
        AppointmentRequest appointmentRequest = ctx.getAppointmentRequestDAO().sendAppointmentRequest(jsonDto);
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
                    MedicalRecordContractErrors.REQUEST_NOT_FOUND.toString());
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
    public String sendEditRequest(
            MedicalRecordContext ctx,
            String jsonString
    ) {
        JSONObject jsonObject = new JSONObject(jsonString);
        String senderId = jsonObject.getString("senderId");
        String recipientId = jsonObject.getString("recipientId");
        String dateModified = jsonObject.getString("dateModified");
        String medicalRecordJson = jsonObject.getString("medicalRecordJson");
        Genson genson = new Genson();
        MedicalRecord medicalRecord = genson.deserialize(medicalRecordJson, MedicalRecord.class);
        authorizeRequest(ctx, medicalRecord.getDoctorId(), "sendEditRequest(validate doctorId)");
        if (!Objects.equals(medicalRecord.getPatientId(), recipientId)) {
            throw new ChaincodeException("medicalRecord.getPatientId() does not match recipientId",
                    MedicalRecordContractErrors.UNAUTHORIZED_EDIT_ACCESS.toString());
        }
        
        JSONObject jsonDto = new JSONObject();
        jsonDto.put("senderId", senderId);
        jsonDto.put("recipientId", recipientId);
        jsonDto.put("dateModified", dateModified);
        jsonDto.put("requestType", RequestType.EDIT_RECORD);
        jsonDto.put("medicalRecordJson", medicalRecordJson);
        EditRequest editRequest = ctx.getEditRequestDAO().sendEditRequest(
                jsonDto
        );
        System.out.println("sendEditRequest - editRequest: " + editRequest);
        return new Genson().serialize(editRequest);
    }

    @Transaction(intent = Transaction.TYPE.SUBMIT)
    public String defineEditRequest(
        MedicalRecordContext ctx,
        String jsonString
    ) {
        System.out.println("jsonString: " + jsonString);
        JSONObject jsonObject = new JSONObject(jsonString);

        System.out.println("jsonObject: " + jsonObject);

        String requestId = jsonObject.getString("requestId");
        String requestStatus = jsonObject.getString("requestStatus");

        System.out.println("requestId: " + requestId);
        System.out.println("requestStatus: " + requestStatus);

        EditRequestDAO editRequestDAO = ctx.getEditRequestDAO();
        if (!editRequestDAO.requestExist(requestId)) {
            throw new ChaincodeException("EditRequestDAO " + requestId + " does not exist",
                    MedicalRecordContractErrors.REQUEST_NOT_FOUND.toString());
        }

        EditRequest editRequest = editRequestDAO.getEditRequest(requestId);
        authorizeRequest(ctx, editRequest.getRecipientId(), "defineEditRequest(validate recipientId");

        MedicalRecord medicalRecord = new Genson().deserialize(editRequest.getMedicalRecord(), MedicalRecord.class);

        System.out.println("medicalRecord: " + medicalRecord.toString());

        JSONObject jsonDto = new JSONObject();
        jsonDto.put("medicalRecordId", medicalRecord.getMedicalRecordId());
        MedicalRecord curMedicalRecord = ctx.getMedicalRecordDAO().getMedicalRecord(medicalRecord.getMedicalRecordId());

        if (!Objects.equals(medicalRecord.getPatientId(), curMedicalRecord.getPatientId())) {
            throw new ChaincodeException("editMedicalRecord.medicalRecordJson.getPatientId() does not match curMedicalRecord.getPatientId()",
                    MedicalRecordContractErrors.UNAUTHORIZED_EDIT_ACCESS.toString());
        }

        if (!Objects.equals(medicalRecord.getDoctorId(), curMedicalRecord.getDoctorId())) {
            throw new ChaincodeException("editMedicalRecord.medicalRecordJson.getDoctorId() does not match curMedicalRecord.getDoctorId()",
                    MedicalRecordContractErrors.UNAUTHORIZED_EDIT_ACCESS.toString());
        }

        jsonDto = new JSONObject();
        jsonDto.put("requestId", requestId);
        jsonDto.put("requestStatus", requestStatus);

        System.out.println("jsonDto: " + jsonDto.toString());
        editRequest = ctx.getEditRequestDAO().defineEditRequest(
            jsonDto
        );

        medicalRecord = null;
        if (Objects.equals(editRequest.getRequestStatus(), RequestStatus.ACCEPTED)) {
            jsonDto = new JSONObject();
            jsonDto.put("medicalRecordJson", editRequest.getMedicalRecord());
            medicalRecord = ctx.getMedicalRecordDAO().editMedicalRecord(jsonDto);
        }
        else {
            throw new ChaincodeException("editRequest.getRequestStatus() does not match RequestStatus.ACCEPTED",
                    MedicalRecordContractErrors.UNAUTHORIZED_EDIT_ACCESS.toString());
        }
        return new Genson().serialize(medicalRecord);
    }


    @Transaction(intent = Transaction.TYPE.SUBMIT)
    public String sendViewRequest(
            MedicalRecordContext ctx,
            String jsonString
    ) {
        JSONObject jsonObject = new JSONObject(jsonString);

        String senderId = jsonObject.getString("senderId");
        String recipientId = jsonObject.getString("recipientId");
        String dateModified = jsonObject.getString("dateModified");

        authorizeRequest(ctx, senderId, "sendViewRequest(validate senderId)");

        JSONObject jsonDto = new JSONObject();
        jsonDto.put("senderId", senderId);
        jsonDto.put("recipientId", recipientId);
        jsonDto.put("dateModified", dateModified);
        jsonDto.put("requestType", RequestType.VIEW_RECORD);
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
                    MedicalRecordContractErrors.REQUEST_NOT_FOUND.toString());
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
    public String getListMedicalRecordByDoctorQuery(
            MedicalRecordContext ctx,
            String jsonString
    ) {
        JSONObject jsonObject = new JSONObject(jsonString);
        String medicalRecordId = jsonObject.getString("medicalRecordId");
        String patientId = jsonObject.getString("patientId");
        String doctorId = jsonObject.getString("doctorId");
        String medicalInstitutionId = jsonObject.getString("medicalInstitutionId");
        String from = jsonObject.getString("from");
        String until = jsonObject.getString("until");
        String testName = jsonObject.getString("testName");
        String details = jsonObject.getString("details");
        String medicalRecordStatus = jsonObject.getString("medicalRecordStatus");
        String sortingOrder = jsonObject.getString("sortingOrder");

        authorizeRequest(ctx, doctorId, "getListMedicalRecordByDoctorQuery(validate doctorId)");

        JSONObject jsonDto = new JSONObject();
        jsonDto.put("medicalRecordId", medicalRecordId);
        jsonDto.put("patientId", patientId);
        jsonDto.put("doctorId", doctorId);
        jsonDto.put("medicalInstitutionId", medicalInstitutionId);
        jsonDto.put("from", from);
        jsonDto.put("until", until);
        jsonDto.put("testName", testName);
        jsonDto.put("details", details);
        jsonDto.put("medicalRecordStatus", medicalRecordStatus);
        jsonDto.put("sortingOrder", sortingOrder);

        List<MedicalRecord> medicalRecordList = ctx.getMedicalRecordDAO().getListMedicalRecordByQuery(
                jsonDto
        );
        return new Genson().serialize(medicalRecordList);
    }

    @Transaction(intent = Transaction.TYPE.EVALUATE)
    public String getListViewRequestBySenderQuery(
            MedicalRecordContext ctx,
            String jsonString
    ) {
        JSONObject jsonObject = new JSONObject(jsonString);
        String requestId = jsonObject.getString("requestId");
        String senderId = jsonObject.getString("senderId");
        String recipientId = jsonObject.getString("recipientId");
        String requestType = jsonObject.getString("requestType");
        String requestStatus = jsonObject.getString("requestStatus");
        String from = jsonObject.getString("from");
        String until = jsonObject.getString("until");
        String sortingOrder = jsonObject.getString("sortingOrder");
        authorizeRequest(ctx, senderId, "getListViewRequestBySenderQuery(validate senderId)");

        JSONObject jsonDto = new JSONObject();
        jsonDto.put("requestId", requestId);
        jsonDto.put("senderId", senderId);
        jsonDto.put("recipientId", recipientId);
        jsonDto.put("requestType", requestType);
        jsonDto.put("from", from);
        jsonDto.put("until", until);
        jsonDto.put("requestStatus", requestStatus);
        jsonDto.put("sortingOrder", sortingOrder);

        List<ViewRequest> viewRequestList = ctx.getViewRequestDAO().getListViewRequestBySenderQuery(
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
                    MedicalRecordContractErrors.EMPTY_MEDICATION_ID_ERROR.toString());
        }
        String medicationId = jsonObject.getString("medicationId");
        String manufacturerId = jsonObject.getString("manufacturerId");

        if (!ctx.getMedicationDAO().medicationExist(medicationId)) {
            throw new ChaincodeException("Medication " + medicationId + " does not exist",
                    MedicalRecordContractErrors.MEDICATION_NOT_FOUND.toString());
        }

        authorizeRequest(ctx, manufacturerId, "editMedication(validate manufacturerId)");
        JSONObject jsonDto = jsonObject;

        Medication medication = ctx.getMedicationDAO().editMedication(jsonDto);

        return new Genson().serialize(medication);
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
        JSONObject jsonObject = new JSONObject(jsonString);
        String medicationId = jsonObject.getString("medicationId");
        String manufactureDate = jsonObject.getString("manufactureDate");
        String expirationDate = jsonObject.getString("expirationDate");

        if (!ctx.getMedicationDAO().medicationExist(medicationId)) {
            throw new ChaincodeException("Medication " + medicationId + " does not exist",
                    MedicalRecordContractErrors.MEDICATION_NOT_FOUND.toString());
        }

        String manufacturerId = ctx.getMedicationDAO().getManufacturerId(medicationId);

        authorizeRequest(ctx, manufacturerId, "addDrug(validate jsonString)");

        JSONObject jsonDto = jsonObject;
        jsonDto.put("ownerId", manufacturerId);

        Drug drug = ctx.getDrugDAO().addDrug(jsonDto);
        return new Genson().serialize(drug);
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

    public Prescription addPrescription(
            MedicalRecordContext ctx,
            String jsonString
    ) {
        JSONObject jsonObject = new JSONObject(jsonString);
        String drugReaction = jsonObject.getString("drugReaction");
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
                    MedicalRecordContractErrors.UNAUTHORIZED_VIEW_PRESCRIPTION_ACCESS.toString());
        }

        Prescription prescription = ctx.getPrescriptionDAO().getPrescription(prescriptionId);

        PrescriptionDto prescriptionDto = new PrescriptionDto();
        prescriptionDto.setPrescriptionId(prescription.getPrescriptionId());
        prescriptionDto.setDrugReaction(prescription.getDrugReaction());
        prescriptionDto.setEntityName(prescription.getEntityName());
        prescriptionDto.setPrescriptionDetailsList(ctx.getPrescriptionDetailsDAO().getListPrescriptionDetails(prescriptionId));
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
        String dateModified = jsonObject.getString("dateModified");
        String prescriptionId = jsonObject.getString("prescriptionId");

        authorizeRequest(ctx, senderId, "sendViewPrescriptionRequest(validate senderId)");

        JSONObject jsonDto = new JSONObject();
        jsonDto.put("senderId", senderId);
        jsonDto.put("recipientId", recipientId);
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
                    MedicalRecordContractErrors.REQUEST_NOT_FOUND.toString());
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

    public enum MedicalRecordContractErrors {
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
        DRUG_NOT_FOUND;
    }
}

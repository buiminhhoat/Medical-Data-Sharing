package medicaldatasharing.entity;

import com.owlike.genson.Genson;
import medicaldatasharing.enumeration.RequestType;
import org.hyperledger.fabric.contract.annotation.DataType;
import org.hyperledger.fabric.contract.annotation.Property;
import com.owlike.genson.annotation.JsonProperty;

@DataType()
public class AppointmentRequest extends Request {
    @Property()
    @JsonProperty("medicalInstitutionId")
    private String medicalInstitutionId;

    public AppointmentRequest() {
        this.requestType = RequestType.APPOINTMENT;
        this.entityName = AppointmentRequest.class.getSimpleName();
    }

    public static byte[] serialize(Object object) {
        Genson genson = new Genson();
        return genson.serializeBytes(object);
    }

    public static AppointmentRequest deserialize(byte[] data) {
        Genson genson = new Genson();
        return genson.deserialize(data, AppointmentRequest.class);
    }

    public String getRequestId() {
        return requestId;
    }

    public AppointmentRequest setRequestId(String requestId) {
        this.requestId = requestId;
        return this;
    }

    public String getMedicalInstitutionId() {
        return medicalInstitutionId;
    }

    public AppointmentRequest setMedicalInstitutionId(String medicalInstitutionId) {
        this.medicalInstitutionId = medicalInstitutionId;
        return this;
    }

    public String getSenderId() {
        return senderId;
    }

    public AppointmentRequest setSenderId(String senderId) {
        this.senderId = senderId;
        return this;
    }

    public String getRecipientId() {
        return recipientId;
    }

    public AppointmentRequest setRecipientId(String recipientId) {
        this.recipientId = recipientId;
        return this;
    }

    public String getDateCreated() {
        return dateCreated;
    }

    public AppointmentRequest setDateCreated(String dateCreated) {
        this.dateCreated = dateCreated;
        return this;
    }

    public String getDateModified() {
        return dateModified;
    }

    public AppointmentRequest setDateModified(String dateModified) {
        this.dateModified = dateModified;
        return this;
    }

    public String getRequestType() {
        return requestType;
    }

    public AppointmentRequest setRequestType(String requestType) {
        this.requestType = requestType;
        return this;
    }

    public String getRequestStatus() {
        return requestStatus;
    }

    public AppointmentRequest setRequestStatus(String requestStatus) {
        this.requestStatus = requestStatus;
        return this;
    }

    public String getEntityName() {
        return entityName;
    }

    public AppointmentRequest setEntityName(String entityName) {
        this.entityName = entityName;
        return this;
    }

    public static AppointmentRequest createInstance(
            String requestId,
            String senderId,
            String recipientId,
            String medicalInstitutionId,
            String dateCreated,
            String dateModified,
            String requestType,
            String requestStatus
    ) {
        AppointmentRequest request = new AppointmentRequest();
        request.setRequestId(requestId);
        request.setSenderId(senderId);
        request.setRecipientId(recipientId);
        request.setMedicalInstitutionId(medicalInstitutionId);
        request.setDateCreated(dateCreated);
        request.setDateModified(dateModified);
        request.setRequestType(requestType);
        request.setRequestStatus(requestStatus);
        request.setEntityName(AppointmentRequest.class.getSimpleName());
        return request;
    }
}

package healthInformationSharing.entity;

import org.hyperledger.fabric.contract.annotation.DataType;
import com.owlike.genson.annotation.JsonProperty;
import org.hyperledger.fabric.contract.annotation.Property;

import static java.nio.charset.StandardCharsets.UTF_8;

@DataType()
public class ViewRequest {
    @Property()
    @JsonProperty("requestId")
    protected String requestId;

    @Property()
    @JsonProperty("senderId")
    protected String senderId;

    @Property()
    @JsonProperty("recipientId")
    protected String recipientId;

    @Property()
    @JsonProperty("dateCreated")
    protected String dateCreated;

    @Property()
    @JsonProperty("requestType")
    protected String requestType;

    @Property()
    @JsonProperty("requestStatus")
    protected String requestStatus;

    @Property()
    @JsonProperty("accessAvailableFrom")
    protected String accessAvailableFrom;

    @Property()
    @JsonProperty("accessAvailableUntil")
    protected String accessAvailableUntil;

    @Property()
    @JsonProperty("entityName")
    protected String entityName;

    @Property()
    @JsonProperty("medicalRecordId")
    protected String medicalRecordId;

    public ViewRequest() {

    }

    public String getRequestId() {
        return requestId;
    }

    public ViewRequest setRequestId(String requestId) {
        this.requestId = requestId;
        return this;
    }

    public String getSenderId() {
        return senderId;
    }

    public ViewRequest setSenderId(String senderId) {
        this.senderId = senderId;
        return this;
    }

    public String getRecipientId() {
        return recipientId;
    }

    public ViewRequest setRecipientId(String recipientId) {
        this.recipientId = recipientId;
        return this;
    }

    public String getDateCreated() {
        return dateCreated;
    }

    public ViewRequest setDateCreated(String dateCreated) {
        this.dateCreated = dateCreated;
        return this;
    }

    public String getRequestType() {
        return requestType;
    }

    public ViewRequest setRequestType(String requestType) {
        this.requestType = requestType;
        return this;
    }

    public String getRequestStatus() {
        return requestStatus;
    }

    public ViewRequest setRequestStatus(String requestStatus) {
        this.requestStatus = requestStatus;
        return this;
    }

    public String getAccessAvailableFrom() {
        return accessAvailableFrom;
    }

    public ViewRequest setAccessAvailableFrom(String accessAvailableFrom) {
        this.accessAvailableFrom = accessAvailableFrom;
        return this;
    }

    public String getAccessAvailableUntil() {
        return accessAvailableUntil;
    }

    public ViewRequest setAccessAvailableUntil(String accessAvailableUntil) {
        this.accessAvailableUntil = accessAvailableUntil;
        return this;
    }

    public String getEntityName() {
        return entityName;
    }

    public ViewRequest setEntityName(String entityName) {
        this.entityName = entityName;
        return this;
    }

    public String getMedicalRecordId() {
        return medicalRecordId;
    }

    public ViewRequest setMedicalRecordId(String medicalRecordId) {
        this.medicalRecordId = medicalRecordId;
        return this;
    }

    public static ViewRequest createInstance(
            String requestId,
            String senderId,
            String recipientId,
            String dateCreated,
            String requestType,
            String requestStatus,
            String accessAvailableFrom,
            String accessAvailableUntil,
            String medicalRecordId
    ) {
        ViewRequest request = new ViewRequest();
        request.setRequestId(requestId);
        request.setSenderId(senderId);
        request.setRecipientId(recipientId);
        request.setDateCreated(dateCreated);
        request.setRequestType(requestType);
        request.setRequestStatus(requestStatus);
        request.setAccessAvailableFrom(accessAvailableFrom);
        request.setAccessAvailableUntil(accessAvailableUntil);
        request.setMedicalRecordId(medicalRecordId);
        request.setEntityName(ViewRequest.class.getSimpleName());
        return request;
    }
}

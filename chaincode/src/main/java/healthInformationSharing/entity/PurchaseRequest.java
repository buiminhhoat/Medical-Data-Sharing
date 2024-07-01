package healthInformationSharing.entity;

import com.owlike.genson.Genson;
import com.owlike.genson.annotation.JsonProperty;
import healthInformationSharing.enumeration.RequestType;
import org.hyperledger.fabric.contract.annotation.DataType;
import org.hyperledger.fabric.contract.annotation.Property;

@DataType()
public class PurchaseRequest extends Request {
    @Property()
    @JsonProperty("insuranceProductId")
    private String insuranceProductId;

    @Property()
    @JsonProperty("startDate")
    private String startDate;

    @Property()
    @JsonProperty("endDate")
    private String endDate;

    @Property
    @JsonProperty("hashFile")
    private String hashFile;

    public PurchaseRequest() {
        this.requestType = RequestType.PURCHASE;
        this.entityName = PurchaseRequest.class.getSimpleName();
    }

    public static byte[] serialize(Object object) {
        Genson genson = new Genson();
        return genson.serializeBytes(object);
    }

    public static PurchaseRequest deserialize(byte[] data) {
        Genson genson = new Genson();
        return genson.deserialize(data, PurchaseRequest.class);
    }

    public String getRequestId() {
        return requestId;
    }

    public PurchaseRequest setRequestId(String requestId) {
        this.requestId = requestId;
        return this;
    }

    public String getSenderId() {
        return senderId;
    }

    public PurchaseRequest setSenderId(String senderId) {
        this.senderId = senderId;
        return this;
    }

    public String getRecipientId() {
        return recipientId;
    }

    public PurchaseRequest setRecipientId(String recipientId) {
        this.recipientId = recipientId;
        return this;
    }

    public String getDateModified() {
        return dateModified;
    }

    public PurchaseRequest setDateModified(String dateModified) {
        this.dateModified = dateModified;
        return this;
    }

    public String getRequestType() {
        return requestType;
    }

    public PurchaseRequest setRequestType(String requestType) {
        this.requestType = requestType;
        return this;
    }

    public String getRequestStatus() {
        return requestStatus;
    }

    public PurchaseRequest setRequestStatus(String requestStatus) {
        this.requestStatus = requestStatus;
        return this;
    }

    public String getEntityName() {
        return entityName;
    }

    public PurchaseRequest setEntityName(String entityName) {
        this.entityName = entityName;
        return this;
    }

    public String getInsuranceProductId() {
        return insuranceProductId;
    }

    public PurchaseRequest setInsuranceProductId(String insuranceProductId) {
        this.insuranceProductId = insuranceProductId;
        return this;
    }

    public String getHashFile() {
        return hashFile;
    }

    public PurchaseRequest setHashFile(String hashFile) {
        this.hashFile = hashFile;
        return this;
    }

    public String getStartDate() {
        return startDate;
    }

    public PurchaseRequest setStartDate(String startDate) {
        this.startDate = startDate;
        return this;
    }

    public String getEndDate() {
        return endDate;
    }

    public PurchaseRequest setEndDate(String endDate) {
        this.endDate = endDate;
        return this;
    }

    public static PurchaseRequest createInstance(
            String requestId,
            String senderId,
            String recipientId,
            String dateModified,
            String requestType,
            String requestStatus,
            String insuranceProductId,
            String startDate,
            String endDate,
            String hashFile
    ) {
        PurchaseRequest request = new PurchaseRequest();
        request.setRequestId(requestId);
        request.setSenderId(senderId);
        request.setRecipientId(recipientId);
        request.setDateModified(dateModified);
        request.setRequestType(requestType);
        request.setRequestStatus(requestStatus);
        request.setInsuranceProductId(insuranceProductId);
        request.setStartDate(startDate);
        request.setEndDate(endDate);
        request.setHashFile(hashFile);
        request.setEntityName(PurchaseRequest.class.getSimpleName());
        return request;
    }
}

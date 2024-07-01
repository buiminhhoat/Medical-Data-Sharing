package healthInformationSharing.entity;

import com.owlike.genson.Genson;
import com.owlike.genson.annotation.JsonProperty;
import healthInformationSharing.enumeration.RequestType;
import org.hyperledger.fabric.contract.annotation.DataType;
import org.hyperledger.fabric.contract.annotation.Property;

@DataType()
public class ConfirmPaymentRequest extends Request {
    @Property()
    @JsonProperty("paymentRequestId")
    private String paymentRequestId;

    public ConfirmPaymentRequest() {
        this.requestType = RequestType.CONFIRM_PAYMENT;
        this.entityName = ConfirmPaymentRequest.class.getSimpleName();
    }

    public static byte[] serialize(Object object) {
        Genson genson = new Genson();
        return genson.serializeBytes(object);
    }

    public static ConfirmPaymentRequest deserialize(byte[] data) {
        Genson genson = new Genson();
        return genson.deserialize(data, ConfirmPaymentRequest.class);
    }

    public String getRequestId() {
        return requestId;
    }

    public ConfirmPaymentRequest setRequestId(String requestId) {
        this.requestId = requestId;
        return this;
    }

    public String getSenderId() {
        return senderId;
    }

    public ConfirmPaymentRequest setSenderId(String senderId) {
        this.senderId = senderId;
        return this;
    }

    public String getRecipientId() {
        return recipientId;
    }

    public ConfirmPaymentRequest setRecipientId(String recipientId) {
        this.recipientId = recipientId;
        return this;
    }

    public String getDateModified() {
        return dateModified;
    }

    public ConfirmPaymentRequest setDateModified(String dateModified) {
        this.dateModified = dateModified;
        return this;
    }

    public String getRequestType() {
        return requestType;
    }

    public ConfirmPaymentRequest setRequestType(String requestType) {
        this.requestType = requestType;
        return this;
    }

    public String getRequestStatus() {
        return requestStatus;
    }

    public ConfirmPaymentRequest setRequestStatus(String requestStatus) {
        this.requestStatus = requestStatus;
        return this;
    }

    public String getEntityName() {
        return entityName;
    }

    public ConfirmPaymentRequest setEntityName(String entityName) {
        this.entityName = entityName;
        return this;
    }

    public String getPaymentRequestId() {
        return paymentRequestId;
    }

    public ConfirmPaymentRequest setPaymentRequestId(String paymentRequestId) {
        this.paymentRequestId = paymentRequestId;
        return this;
    }

    public static ConfirmPaymentRequest createInstance(
            String requestId,
            String senderId,
            String recipientId,
            String dateModified,
            String requestType,
            String requestStatus,
            String paymentRequestId
    ) {
        ConfirmPaymentRequest request = new ConfirmPaymentRequest();
        request.setRequestId(requestId);
        request.setSenderId(senderId);
        request.setRecipientId(recipientId);
        request.setDateModified(dateModified);
        request.setRequestType(requestType);
        request.setRequestStatus(requestStatus);
        request.setPaymentRequestId(paymentRequestId);
        request.setEntityName(ConfirmPaymentRequest.class.getSimpleName());
        return request;
    }

    @Override
    public String toString() {
        return "ConfirmPaymentRequest{" +
                "paymentRequestId='" + paymentRequestId + '\'' +
                ", requestId='" + requestId + '\'' +
                ", senderId='" + senderId + '\'' +
                ", recipientId='" + recipientId + '\'' +
                ", dateModified='" + dateModified + '\'' +
                ", requestType='" + requestType + '\'' +
                ", requestStatus='" + requestStatus + '\'' +
                ", entityName='" + entityName + '\'' +
                '}';
    }
}

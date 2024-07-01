package healthInformationSharing.entity;

import com.owlike.genson.Genson;
import com.owlike.genson.annotation.JsonProperty;
import healthInformationSharing.enumeration.RequestType;
import org.hyperledger.fabric.contract.annotation.DataType;
import org.hyperledger.fabric.contract.annotation.Property;

@DataType()
public class PaymentRequest extends Request {
    @Property()
    @JsonProperty("insuranceContractId")
    private String insuranceContractId;

    @Property()
    @JsonProperty("medicalRecordId")
    private String medicalRecordId;


    public PaymentRequest() {
        this.requestType = RequestType.PAYMENT;
        this.entityName = PaymentRequest.class.getSimpleName();
    }

    public static byte[] serialize(Object object) {
        Genson genson = new Genson();
        return genson.serializeBytes(object);
    }

    public static PaymentRequest deserialize(byte[] data) {
        Genson genson = new Genson();
        return genson.deserialize(data, PaymentRequest.class);
    }

    public String getRequestId() {
        return requestId;
    }

    public PaymentRequest setRequestId(String requestId) {
        this.requestId = requestId;
        return this;
    }

    public String getSenderId() {
        return senderId;
    }

    public PaymentRequest setSenderId(String senderId) {
        this.senderId = senderId;
        return this;
    }

    public String getRecipientId() {
        return recipientId;
    }

    public PaymentRequest setRecipientId(String recipientId) {
        this.recipientId = recipientId;
        return this;
    }

    public String getDateModified() {
        return dateModified;
    }

    public PaymentRequest setDateModified(String dateModified) {
        this.dateModified = dateModified;
        return this;
    }

    public String getRequestType() {
        return requestType;
    }

    public PaymentRequest setRequestType(String requestType) {
        this.requestType = requestType;
        return this;
    }

    public String getRequestStatus() {
        return requestStatus;
    }

    public PaymentRequest setRequestStatus(String requestStatus) {
        this.requestStatus = requestStatus;
        return this;
    }

    public String getEntityName() {
        return entityName;
    }

    public PaymentRequest setEntityName(String entityName) {
        this.entityName = entityName;
        return this;
    }

    public String getMedicalRecordId() {
        return medicalRecordId;
    }

    public PaymentRequest setMedicalRecordId(String medicalRecordId) {
        this.medicalRecordId = medicalRecordId;
        return this;
    }

    public String getInsuranceContractId() {
        return insuranceContractId;
    }

    public PaymentRequest setInsuranceContractId(String insuranceContractId) {
        this.insuranceContractId = insuranceContractId;
        return this;
    }

    public static PaymentRequest createInstance(
            String requestId,
            String senderId,
            String recipientId,
            String dateModified,
            String requestType,
            String requestStatus,
            String insuranceContractId,
            String medicalRecordId
    ) {
        PaymentRequest request = new PaymentRequest();
        request.setRequestId(requestId);
        request.setSenderId(senderId);
        request.setRecipientId(recipientId);
        request.setDateModified(dateModified);
        request.setRequestType(requestType);
        request.setRequestStatus(requestStatus);
        request.setInsuranceContractId(insuranceContractId);
        request.setMedicalRecordId(medicalRecordId);
        request.setEntityName(PaymentRequest.class.getSimpleName());
        return request;
    }


}

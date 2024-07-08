package com.medicaldatasharing.chaincode.dto;

import com.owlike.genson.Genson;
import com.owlike.genson.annotation.JsonProperty;
import org.json.JSONObject;

public class ConfirmPaymentRequest extends Request {
    @JsonProperty("paymentRequestId")
    private String paymentRequestId;

    public ConfirmPaymentRequest() {
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

    public static ConfirmPaymentRequest parseRequest(JSONObject jsonObject) {
        String requestId = jsonObject.getString("requestId");
        String senderId = jsonObject.getString("senderId");
        String recipientId = jsonObject.getString("recipientId");
        String dateCreated = jsonObject.getString("dateCreated");
        String dateModified = jsonObject.getString("dateModified");
        String requestType = jsonObject.getString("requestType");
        String requestStatus = jsonObject.getString("requestStatus");
        String paymentRequestId = jsonObject.getString("paymentRequestId");
        return createInstance(
                requestId,
                senderId,
                recipientId,
                dateCreated,
                dateModified,
                requestType,
                requestStatus,
                paymentRequestId
        );
    }

    public static ConfirmPaymentRequest createInstance(
            String requestId,
            String senderId,
            String recipientId,
            String dateCreated,
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
        request.setEntityName(ConfirmPaymentRequest.class.getSimpleName());
        request.setPaymentRequestId(paymentRequestId);
        return request;
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
}

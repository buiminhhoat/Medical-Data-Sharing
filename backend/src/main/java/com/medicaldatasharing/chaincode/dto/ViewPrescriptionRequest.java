package com.medicaldatasharing.chaincode.dto;

import com.owlike.genson.Genson;
import com.owlike.genson.annotation.JsonProperty;

public class ViewPrescriptionRequest extends Request {
    @JsonProperty("prescriptionId")
    protected String prescriptionId;

    public ViewPrescriptionRequest() {
        this.entityName = ViewPrescriptionRequest.class.getSimpleName();
    }

    public static byte[] serialize(Object object) {
        Genson genson = new Genson();
        return genson.serializeBytes(object);
    }

    public static ViewPrescriptionRequest deserialize(byte[] data) {
        Genson genson = new Genson();
        return genson.deserialize(data, ViewPrescriptionRequest.class);
    }

    public static ViewPrescriptionRequest createInstance(
            String requestId,
            String senderId,
            String recipientId,
            String dateModified,
            String requestType,
            String requestStatus,
            String prescriptionId
    ) {
        ViewPrescriptionRequest request = new ViewPrescriptionRequest();
        request.setRequestId(requestId);
        request.setSenderId(senderId);
        request.setRecipientId(recipientId);
        request.setDateModified(dateModified);
        request.setRequestType(requestType);
        request.setRequestStatus(requestStatus);
        request.setPrescriptionId(prescriptionId);
        request.setEntityName(ViewPrescriptionRequest.class.getSimpleName());
        return request;
    }

    public String getRequestId() {
        return requestId;
    }

    public ViewPrescriptionRequest setRequestId(String requestId) {
        this.requestId = requestId;
        return this;
    }

    public String getSenderId() {
        return senderId;
    }

    public ViewPrescriptionRequest setSenderId(String senderId) {
        this.senderId = senderId;
        return this;
    }

    public String getRecipientId() {
        return recipientId;
    }

    public ViewPrescriptionRequest setRecipientId(String recipientId) {
        this.recipientId = recipientId;
        return this;
    }

    public String getDateModified() {
        return dateModified;
    }

    public ViewPrescriptionRequest setDateModified(String dateModified) {
        this.dateModified = dateModified;
        return this;
    }

    public String getRequestType() {
        return requestType;
    }

    public ViewPrescriptionRequest setRequestType(String requestType) {
        this.requestType = requestType;
        return this;
    }

    public String getRequestStatus() {
        return requestStatus;
    }

    public ViewPrescriptionRequest setRequestStatus(String requestStatus) {
        this.requestStatus = requestStatus;
        return this;
    }

    public String getEntityName() {
        return entityName;
    }

    public ViewPrescriptionRequest setEntityName(String entityName) {
        this.entityName = entityName;
        return this;
    }

    public String getPrescriptionId() {
        return prescriptionId;
    }

    public ViewPrescriptionRequest setPrescriptionId(String prescriptionId) {
        this.prescriptionId = prescriptionId;
        return this;
    }
}

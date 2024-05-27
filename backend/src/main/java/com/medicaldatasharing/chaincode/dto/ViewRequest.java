package com.medicaldatasharing.chaincode.dto;

import com.owlike.genson.Genson;
import com.owlike.genson.annotation.JsonProperty;
import org.json.JSONObject;

import static java.nio.charset.StandardCharsets.UTF_8;

public class ViewRequest {
    @JsonProperty("requestId")
    protected String requestId;

    @JsonProperty("senderId")
    protected String senderId;

    @JsonProperty("recipientId")
    protected String recipientId;

    @JsonProperty("dateCreated")
    protected String dateCreated;

    @JsonProperty("requestType")
    protected String requestType;

    @JsonProperty("requestStatus")
    protected String requestStatus;

    @JsonProperty("accessAvailableFrom")
    protected String accessAvailableFrom;

    @JsonProperty("accessAvailableUntil")
    protected String accessAvailableUntil;

    @JsonProperty("entityName")
    protected String entityName;

    @JsonProperty("medicalRecord")
    private String medicalRecord;

    public ViewRequest() {

    }

    public String getMedicalRecord() {
        return medicalRecord;
    }

    public ViewRequest setMedicalRecord(String medicalRecord) {
        this.medicalRecord = medicalRecord;
        return this;
    }

    public static byte[] serialize(Object object) {
        Genson genson = new Genson();
        return genson.serializeBytes(object);
    }

    public static ViewRequest deserialize(byte[] data) {
        Genson genson = new Genson();
        return genson.deserialize(data, ViewRequest.class);
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
            String medicalRecord
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
        request.setEntityName(ViewRequest.class.getSimpleName());
        request.setMedicalRecord(medicalRecord);
        return request;
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
}

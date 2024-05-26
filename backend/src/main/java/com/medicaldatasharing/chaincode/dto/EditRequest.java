package com.medicaldatasharing.chaincode.dto;

import com.owlike.genson.Genson;
import com.owlike.genson.annotation.JsonProperty;
import org.json.JSONObject;

import java.nio.charset.StandardCharsets;

import static java.nio.charset.StandardCharsets.UTF_8;

public class EditRequest {
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

    public EditRequest() {

    }

    public String getMedicalRecord() {
        return medicalRecord;
    }

    public EditRequest setMedicalRecord(String medicalRecord) {
        this.medicalRecord = medicalRecord;
        return this;
    }

    public static byte[] serialize(EditRequest editRequest) {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("requestId", editRequest.getRequestId());
        jsonObject.put("senderId", editRequest.getSenderId());
        jsonObject.put("recipientId", editRequest.getRecipientId());
        jsonObject.put("dateCreated", editRequest.getDateCreated());
        jsonObject.put("requestType", editRequest.getRequestType());
        jsonObject.put("requestStatus", editRequest.getRequestStatus());
        jsonObject.put("accessAvailableFrom", editRequest.getAccessAvailableFrom());
        jsonObject.put("accessAvailableUntil", editRequest.getAccessAvailableUntil());
        jsonObject.put("medicalRecord", new JSONObject(editRequest.getMedicalRecord()));

        String jsonStr = jsonObject.toString();
        return jsonStr.getBytes(UTF_8);
    }

    public static EditRequest deserialize(byte[] data) {
        Genson genson = new Genson();
        return genson.deserialize(data, EditRequest.class);
    }

    public static EditRequest parseRequest(JSONObject jsonObject) {
        String requestId = jsonObject.getString("requestId");
        String senderId = jsonObject.getString("senderId");
        String recipientId = jsonObject.getString("recipientId");
        String dateCreated = jsonObject.getString("dateCreated");
        String requestType = jsonObject.getString("requestType");
        String requestStatus = jsonObject.getString("requestStatus");
        String accessAvailableFrom = jsonObject.getString("accessAvailableFrom");
        String accessAvailableUntil = jsonObject.getString("accessAvailableUntil");
        String medicalRecord = jsonObject.getString("medicalRecord");
        return createInstance(
                requestId,
                senderId,
                recipientId,
                dateCreated,
                requestType,
                requestStatus,
                accessAvailableFrom,
                accessAvailableUntil,
                medicalRecord
        );
    }

    public static EditRequest createInstance(
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
        EditRequest request = new EditRequest();
        request.setRequestId(requestId);
        request.setSenderId(senderId);
        request.setRecipientId(recipientId);
        request.setDateCreated(dateCreated);
        request.setRequestType(requestType);
        request.setRequestStatus(requestStatus);
        request.setAccessAvailableFrom(accessAvailableFrom);
        request.setAccessAvailableUntil(accessAvailableUntil);
        request.setEntityName(EditRequest.class.getSimpleName());
        request.setMedicalRecord(medicalRecord);
        return request;
    }

    public String getRequestId() {
        return requestId;
    }

    public EditRequest setRequestId(String requestId) {
        this.requestId = requestId;
        return this;
    }

    public String getSenderId() {
        return senderId;
    }

    public EditRequest setSenderId(String senderId) {
        this.senderId = senderId;
        return this;
    }

    public String getRecipientId() {
        return recipientId;
    }

    public EditRequest setRecipientId(String recipientId) {
        this.recipientId = recipientId;
        return this;
    }

    public String getDateCreated() {
        return dateCreated;
    }

    public EditRequest setDateCreated(String dateCreated) {
        this.dateCreated = dateCreated;
        return this;
    }

    public String getRequestType() {
        return requestType;
    }

    public EditRequest setRequestType(String requestType) {
        this.requestType = requestType;
        return this;
    }

    public String getRequestStatus() {
        return requestStatus;
    }

    public EditRequest setRequestStatus(String requestStatus) {
        this.requestStatus = requestStatus;
        return this;
    }

    public String getAccessAvailableFrom() {
        return accessAvailableFrom;
    }

    public EditRequest setAccessAvailableFrom(String accessAvailableFrom) {
        this.accessAvailableFrom = accessAvailableFrom;
        return this;
    }

    public String getAccessAvailableUntil() {
        return accessAvailableUntil;
    }

    public EditRequest setAccessAvailableUntil(String accessAvailableUntil) {
        this.accessAvailableUntil = accessAvailableUntil;
        return this;
    }

    public String getEntityName() {
        return entityName;
    }

    public EditRequest setEntityName(String entityName) {
        this.entityName = entityName;
        return this;
    }
}

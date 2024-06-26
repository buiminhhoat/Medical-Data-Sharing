package com.medicaldatasharing.chaincode.dto;

import com.owlike.genson.Genson;
import com.owlike.genson.annotation.JsonProperty;
import org.json.JSONObject;

import java.nio.charset.StandardCharsets;

import static java.nio.charset.StandardCharsets.UTF_8;

public class EditRequest extends Request {
    @JsonProperty("medicalRecord")
    private String medicalRecord;

    public EditRequest() {
        this.entityName = EditRequest.class.getSimpleName();
    }

    public String getMedicalRecord() {
        return medicalRecord;
    }

    public EditRequest setMedicalRecord(String medicalRecord) {
        this.medicalRecord = medicalRecord;
        return this;
    }

    public static byte[] serialize(Object object) {
        Genson genson = new Genson();
        return genson.serializeBytes(object);
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
        String medicalRecord = jsonObject.getString("medicalRecord");
        return createInstance(
                requestId,
                senderId,
                recipientId,
                dateCreated,
                requestType,
                requestStatus,
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
            String medicalRecord
    ) {
        EditRequest request = new EditRequest();
        request.setRequestId(requestId);
        request.setSenderId(senderId);
        request.setRecipientId(recipientId);
        request.setDateCreated(dateCreated);
        request.setRequestType(requestType);
        request.setRequestStatus(requestStatus);
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

    public String getEntityName() {
        return entityName;
    }

    public EditRequest setEntityName(String entityName) {
        this.entityName = entityName;
        return this;
    }
}

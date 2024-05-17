package com.medicaldatasharing.chaincode.dto;

import org.json.JSONObject;

import java.util.Objects;

import static java.nio.charset.StandardCharsets.UTF_8;

public class Request {
    private String requestId;

    private String senderId;

    private String recipientId;

    private String dateCreated;

    private String requestType;

    private String requestStatus;

    private String accessAvailableFrom;

    private String accessAvailableUntil;

    private String medicalRecordId;

    private String testName;

    private String entityName;

    public Request() {
    }

    public static byte[] serialize(Object object) {
        String jsonStr = new JSONObject(object).toString();
        return jsonStr.getBytes(UTF_8);
    }

    public static Request deserialize(byte[] data) {
        JSONObject jsonObject = new JSONObject(new String(data, UTF_8));
        return parseRequest(jsonObject);
    }

    public static Request parseRequest(JSONObject jsonObject) {
        String requestId = jsonObject.getString("requestId");
        String senderId = jsonObject.getString("senderId");
        String recipientId = jsonObject.getString("recipientId");
        String dateCreated = jsonObject.getString("dateCreated");
        String requestType = jsonObject.getString("requestType");
        String requestStatus = jsonObject.getString("requestStatus");
        String accessAvailableFrom = jsonObject.getString("accessAvailableFrom");
        String accessAvailableUntil = jsonObject.getString("accessAvailableUntil");
        String medicalRecordId = jsonObject.getString("medicalRecordId");
        String testName = jsonObject.getString("testName");

        return createInstance(
                requestId,
                senderId,
                recipientId,
                dateCreated,
                requestType,
                requestStatus,
                accessAvailableFrom,
                accessAvailableUntil,
                medicalRecordId,
                testName
        );
    }

    public static Request createInstance(
            String requestId,
            String senderId,
            String recipientId,
            String dateCreated,
            String requestType,
            String requestStatus,
            String accessAvailableFrom,
            String accessAvailableUntil,
            String medicalRecordJsonObject,
            String testName
    ) {
        Request request = new Request();
        request.setSenderId(senderId);
        request.setRequestId(requestId);
        request.setRecipientId(recipientId);
        request.setDateCreated(dateCreated);
        request.setRequestType(requestType);
        request.setRequestStatus(requestStatus);
        request.setAccessAvailableFrom(accessAvailableFrom);
        request.setAccessAvailableUntil(accessAvailableUntil);
        request.setMedicalRecordId(medicalRecordJsonObject);
        request.setTestName(testName);
        request.setEntityName(Request.class.getSimpleName());
        return request;
    }

    public String getRequestId() {
        return requestId;
    }

    public Request setRequestId(String requestId) {
        this.requestId = requestId;
        return this;
    }

    public String getSenderId() {
        return senderId;
    }

    public Request setSenderId(String senderId) {
        this.senderId = senderId;
        return this;
    }

    public String getRecipientId() {
        return recipientId;
    }

    public Request setRecipientId(String recipientId) {
        this.recipientId = recipientId;
        return this;
    }

    public String getDateCreated() {
        return dateCreated;
    }

    public Request setDateCreated(String dateCreated) {
        this.dateCreated = dateCreated;
        return this;
    }

    public String getRequestType() {
        return requestType;
    }

    public Request setRequestType(String requestType) {
        this.requestType = requestType;
        return this;
    }

    public String getRequestStatus() {
        return requestStatus;
    }

    public Request setRequestStatus(String requestStatus) {
        this.requestStatus = requestStatus;
        return this;
    }

    public String getAccessAvailableFrom() {
        return accessAvailableFrom;
    }

    public Request setAccessAvailableFrom(String accessAvailableFrom) {
        this.accessAvailableFrom = accessAvailableFrom;
        return this;
    }

    public String getAccessAvailableUntil() {
        return accessAvailableUntil;
    }

    public Request setAccessAvailableUntil(String accessAvailableUntil) {
        this.accessAvailableUntil = accessAvailableUntil;
        return this;
    }

    public String getMedicalRecordId() {
        return medicalRecordId;
    }

    public Request setMedicalRecordId(String medicalRecordId) {
        this.medicalRecordId = medicalRecordId;
        return this;
    }

    public String getTestName() {
        return testName;
    }

    public Request setTestName(String testName) {
        this.testName = testName;
        return this;
    }

    public String getEntityName() {
        return entityName;
    }

    public Request setEntityName(String entityName) {
        this.entityName = entityName;
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Request request = (Request) o;
        return Objects.equals(requestId, request.requestId) && Objects.equals(senderId, request.senderId) && Objects.equals(recipientId, request.recipientId) && Objects.equals(dateCreated, request.dateCreated) && Objects.equals(requestType, request.requestType) && Objects.equals(requestStatus, request.requestStatus) && Objects.equals(accessAvailableFrom, request.accessAvailableFrom) && Objects.equals(accessAvailableUntil, request.accessAvailableUntil) && Objects.equals(medicalRecordId, request.medicalRecordId) && Objects.equals(testName, request.testName) && Objects.equals(entityName, request.entityName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(requestId, senderId, recipientId, dateCreated, requestType, requestStatus, accessAvailableFrom, accessAvailableUntil, medicalRecordId, testName, entityName);
    }

    @Override
    public String toString() {
        return "Request{" +
                "requestId='" + requestId + '\'' +
                ", senderId='" + senderId + '\'' +
                ", recipientId='" + recipientId + '\'' +
                ", dateCreated='" + dateCreated + '\'' +
                ", requestType='" + requestType + '\'' +
                ", requestStatus='" + requestStatus + '\'' +
                ", accessAvailableFrom='" + accessAvailableFrom + '\'' +
                ", accessAvailableUntil='" + accessAvailableUntil + '\'' +
                ", medicalRecordId='" + medicalRecordId + '\'' +
                ", testName='" + testName + '\'' +
                ", entityName='" + entityName + '\'' +
                '}';
    }
}

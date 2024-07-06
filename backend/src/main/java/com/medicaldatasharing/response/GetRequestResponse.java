package com.medicaldatasharing.response;

import com.medicaldatasharing.chaincode.dto.Request;
import com.owlike.genson.annotation.JsonProperty;

public class GetRequestResponse {
    @JsonProperty("requestId")
    protected String requestId;

    @JsonProperty("senderId")
    protected String senderId;

    @JsonProperty("senderName")
    protected String senderName;

    @JsonProperty("recipientId")
    protected String recipientId;

    @JsonProperty("recipientName")
    protected String recipientName;

    @JsonProperty("dateModified")
    protected String dateModified;

    @JsonProperty("requestType")
    protected String requestType;

    @JsonProperty("requestStatus")
    protected String requestStatus;

    @JsonProperty("entityName")
    protected String entityName;

    public GetRequestResponse(Request request) {
        this.requestId = request.getRequestId();
        this.senderId = request.getSenderId();
        this.recipientId = request.getRecipientId();
        this.dateModified = request.getDateModified();
        this.requestType = request.getRequestType();
        this.requestStatus = request.getRequestStatus();
        this.entityName = request.getEntityName();
    }

    public String getRequestId() {
        return requestId;
    }

    public GetRequestResponse setRequestId(String requestId) {
        this.requestId = requestId;
        return this;
    }

    public String getSenderId() {
        return senderId;
    }

    public GetRequestResponse setSenderId(String senderId) {
        this.senderId = senderId;
        return this;
    }

    public String getSenderName() {
        return senderName;
    }

    public GetRequestResponse setSenderName(String senderName) {
        this.senderName = senderName;
        return this;
    }

    public String getRecipientId() {
        return recipientId;
    }

    public GetRequestResponse setRecipientId(String recipientId) {
        this.recipientId = recipientId;
        return this;
    }

    public String getRecipientName() {
        return recipientName;
    }

    public GetRequestResponse setRecipientName(String recipientName) {
        this.recipientName = recipientName;
        return this;
    }

    public String getDateModified() {
        return dateModified;
    }

    public GetRequestResponse setDateModified(String dateModified) {
        this.dateModified = dateModified;
        return this;
    }

    public String getRequestType() {
        return requestType;
    }

    public GetRequestResponse setRequestType(String requestType) {
        this.requestType = requestType;
        return this;
    }

    public String getRequestStatus() {
        return requestStatus;
    }

    public GetRequestResponse setRequestStatus(String requestStatus) {
        this.requestStatus = requestStatus;
        return this;
    }

    public String getEntityName() {
        return entityName;
    }

    public GetRequestResponse setEntityName(String entityName) {
        this.entityName = entityName;
        return this;
    }
}

package com.medicaldatasharing.chaincode.dto;

import com.owlike.genson.annotation.JsonProperty;

public abstract class Request {
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

    @JsonProperty("entityName")
    protected String entityName;
}

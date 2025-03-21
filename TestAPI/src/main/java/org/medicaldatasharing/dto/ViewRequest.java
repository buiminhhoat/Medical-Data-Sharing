package org.medicaldatasharing.dto;

import com.owlike.genson.Genson;

public class ViewRequest extends Request {
    public ViewRequest() {
        this.entityName = ViewRequest.class.getSimpleName();
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
            String dateModified,
            String requestType,
            String requestStatus
    ) {
        ViewRequest request = new ViewRequest();
        request.setRequestId(requestId);
        request.setSenderId(senderId);
        request.setRecipientId(recipientId);
        request.setDateCreated(dateCreated);
        request.setDateModified(dateModified);
        request.setRequestType(requestType);
        request.setRequestStatus(requestStatus);
        request.setEntityName(ViewRequest.class.getSimpleName());
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

    public String getDateModified() {
        return dateModified;
    }

    public ViewRequest setDateModified(String dateModified) {
        this.dateModified = dateModified;
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

    public String getEntityName() {
        return entityName;
    }

    public ViewRequest setEntityName(String entityName) {
        this.entityName = entityName;
        return this;
    }
}

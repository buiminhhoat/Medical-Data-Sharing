package com.medicaldatasharing.chaincode.dto;

import org.json.JSONObject;

import java.util.Objects;

import static java.nio.charset.StandardCharsets.UTF_8;

public class AppointmentRequest extends Request {
    public AppointmentRequest() {
    }

    public static byte[] serialize(Object object) {
        String jsonStr = new JSONObject(object).toString();
        return jsonStr.getBytes(UTF_8);
    }

    public static AppointmentRequest deserialize(byte[] data) {
        JSONObject jsonObject = new JSONObject(new String(data, UTF_8));
        return parseRequest(jsonObject);
    }

    public static AppointmentRequest parseRequest(JSONObject jsonObject) {
        String requestId = jsonObject.getString("requestId");
        String senderId = jsonObject.getString("senderId");
        String recipientId = jsonObject.getString("recipientId");
        String dateCreated = jsonObject.getString("dateCreated");
        String requestType = jsonObject.getString("requestType");
        String requestStatus = jsonObject.getString("requestStatus");
        String accessAvailableFrom = jsonObject.getString("accessAvailableFrom");
        String accessAvailableUntil = jsonObject.getString("accessAvailableUntil");

        return createInstance(
                requestId,
                senderId,
                recipientId,
                dateCreated,
                requestType,
                requestStatus,
                accessAvailableFrom,
                accessAvailableUntil
        );
    }

    public static AppointmentRequest createInstance(
            String requestId,
            String senderId,
            String recipientId,
            String dateCreated,
            String requestType,
            String requestStatus,
            String accessAvailableFrom,
            String accessAvailableUntil
    ) {
        AppointmentRequest appointmentRequest = new AppointmentRequest();
        appointmentRequest.setSenderId(senderId);
        appointmentRequest.setRequestId(requestId);
        appointmentRequest.setRecipientId(recipientId);
        appointmentRequest.setDateCreated(dateCreated);
        appointmentRequest.setRequestType(requestType);
        appointmentRequest.setRequestStatus(requestStatus);
        appointmentRequest.setAccessAvailableFrom(accessAvailableFrom);
        appointmentRequest.setAccessAvailableUntil(accessAvailableUntil);
        appointmentRequest.setEntityName(AppointmentRequest.class.getSimpleName());
        return appointmentRequest;
    }
}

package com.medicaldatasharing.response;

import com.medicaldatasharing.chaincode.dto.*;
import com.medicaldatasharing.enumeration.RequestType;
import com.owlike.genson.annotation.JsonProperty;

import java.util.Objects;

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

    @JsonProperty("dateCreated")
    protected String dateCreated;

    @JsonProperty("dateModified")
    protected String dateModified;

    @JsonProperty("requestType")
    protected String requestType;

    @JsonProperty("requestStatus")
    protected String requestStatus;

    @JsonProperty("entityName")
    protected String entityName;

    @JsonProperty("paymentRequestId")
    private String paymentRequestId;

    @JsonProperty("medicalRecord")
    private String medicalRecord;

    @JsonProperty("insuranceContractId")
    private String insuranceContractId;

    @JsonProperty("medicalRecordId")
    private String medicalRecordId;

    @JsonProperty("insuranceProductId")
    private String insuranceProductId;

    @JsonProperty("startDate")
    private String startDate;

    @JsonProperty("endDate")
    private String endDate;

    @JsonProperty("hashFile")
    private String hashFile;

    @JsonProperty("prescriptionId")
    private String prescriptionId;

    public GetRequestResponse(Request request) {
        this.requestId = request.getRequestId();
        this.senderId = request.getSenderId();
        this.recipientId = request.getRecipientId();
        this.dateCreated = request.getDateCreated();
        this.dateModified = request.getDateModified();
        this.requestType = request.getRequestType();
        this.requestStatus = request.getRequestStatus();
        this.entityName = request.getEntityName();

        if (request instanceof ConfirmPaymentRequest && Objects.equals(requestType, RequestType.CONFIRM_PAYMENT.toString())) {
            ConfirmPaymentRequest confirmPaymentRequest = (ConfirmPaymentRequest) request;
            this.paymentRequestId = confirmPaymentRequest.getPaymentRequestId();
        }

        if (request instanceof EditRequest && Objects.equals(requestType, RequestType.EDIT_RECORD.toString())) {
            EditRequest editRequest = (EditRequest) request;
            this.medicalRecord = editRequest.getMedicalRecord();
        }

        if (request instanceof PaymentRequest && Objects.equals(requestType, RequestType.PAYMENT.toString())) {
            PaymentRequest paymentRequest = (PaymentRequest) request;
            this.insuranceContractId = paymentRequest.getInsuranceContractId();
            this.medicalRecordId = paymentRequest.getMedicalRecordId();
        }

        if (request instanceof PurchaseRequest && Objects.equals(requestType, RequestType.PURCHASE.toString())) {
            PurchaseRequest purchaseRequest =(PurchaseRequest) request;
            this.insuranceProductId = purchaseRequest.getInsuranceProductId();
            this.startDate = purchaseRequest.getStartDate();
            this.endDate = purchaseRequest.getEndDate();
            this.hashFile = purchaseRequest.getHashFile();
        }

        if (request instanceof ViewPrescriptionRequest && Objects.equals(requestType, RequestType.VIEW_PRESCRIPTION.toString())) {
            ViewPrescriptionRequest viewPrescriptionRequest = (ViewPrescriptionRequest) request;
            this.prescriptionId = viewPrescriptionRequest.getPrescriptionId();
        }
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

    public String getPaymentRequestId() {
        return paymentRequestId;
    }

    public GetRequestResponse setPaymentRequestId(String paymentRequestId) {
        this.paymentRequestId = paymentRequestId;
        return this;
    }

    public String getMedicalRecord() {
        return medicalRecord;
    }

    public GetRequestResponse setMedicalRecord(String medicalRecord) {
        this.medicalRecord = medicalRecord;
        return this;
    }

    public String getInsuranceContractId() {
        return insuranceContractId;
    }

    public GetRequestResponse setInsuranceContractId(String insuranceContractId) {
        this.insuranceContractId = insuranceContractId;
        return this;
    }

    public String getMedicalRecordId() {
        return medicalRecordId;
    }

    public GetRequestResponse setMedicalRecordId(String medicalRecordId) {
        this.medicalRecordId = medicalRecordId;
        return this;
    }

    public String getInsuranceProductId() {
        return insuranceProductId;
    }

    public GetRequestResponse setInsuranceProductId(String insuranceProductId) {
        this.insuranceProductId = insuranceProductId;
        return this;
    }

    public String getStartDate() {
        return startDate;
    }

    public GetRequestResponse setStartDate(String startDate) {
        this.startDate = startDate;
        return this;
    }

    public String getEndDate() {
        return endDate;
    }

    public GetRequestResponse setEndDate(String endDate) {
        this.endDate = endDate;
        return this;
    }

    public String getHashFile() {
        return hashFile;
    }

    public GetRequestResponse setHashFile(String hashFile) {
        this.hashFile = hashFile;
        return this;
    }

    public String getPrescriptionId() {
        return prescriptionId;
    }

    public GetRequestResponse setPrescriptionId(String prescriptionId) {
        this.prescriptionId = prescriptionId;
        return this;
    }
}

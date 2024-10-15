package org.medicaldatasharing.dto;

import com.owlike.genson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.Objects;

@Getter
@Setter
@AllArgsConstructor
public class RequestResponse {
    @JsonProperty("requestId")
    protected String requestId;

    @JsonProperty("senderId")
    protected String senderId;

    @JsonProperty("senderName")
    protected String senderName;

    @JsonProperty("medicalInstitutionId")
    protected String medicalInstitutionId;

    @JsonProperty("medicalInstitutionName")
    protected String medicalInstitutionName;

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

    public RequestResponse() {

    }
    
    public RequestResponse(Request request) {
        this.requestId = request.getRequestId();
        this.senderId = request.getSenderId();
        this.recipientId = request.getRecipientId();
        this.dateCreated = request.getDateCreated();
        this.dateModified = request.getDateModified();
        this.requestType = request.getRequestType();
        this.requestStatus = request.getRequestStatus();
        this.entityName = request.getEntityName();

        if (request instanceof AppointmentRequest && Objects.equals(requestType, RequestType.APPOINTMENT.toString())) {
            AppointmentRequest appointmentRequest = (AppointmentRequest) request;
            this.medicalInstitutionId = appointmentRequest.getMedicalInstitutionId();
        }

        if (request instanceof ViewPrescriptionRequest && Objects.equals(requestType, RequestType.VIEW_PRESCRIPTION.toString())) {
            ViewPrescriptionRequest viewPrescriptionRequest = (ViewPrescriptionRequest) request;
            this.prescriptionId = viewPrescriptionRequest.getPrescriptionId();
        }
    }

    public String getRequestId() {
        return requestId;
    }

    public RequestResponse setRequestId(String requestId) {
        this.requestId = requestId;
        return this;
    }

    public String getSenderId() {
        return senderId;
    }

    public RequestResponse setSenderId(String senderId) {
        this.senderId = senderId;
        return this;
    }

    public String getSenderName() {
        return senderName;
    }

    public RequestResponse setSenderName(String senderName) {
        this.senderName = senderName;
        return this;
    }

    public String getRecipientId() {
        return recipientId;
    }

    public RequestResponse setRecipientId(String recipientId) {
        this.recipientId = recipientId;
        return this;
    }

    public String getRecipientName() {
        return recipientName;
    }

    public RequestResponse setRecipientName(String recipientName) {
        this.recipientName = recipientName;
        return this;
    }

    public String getDateModified() {
        return dateModified;
    }

    public RequestResponse setDateModified(String dateModified) {
        this.dateModified = dateModified;
        return this;
    }

    public String getRequestType() {
        return requestType;
    }

    public RequestResponse setRequestType(String requestType) {
        this.requestType = requestType;
        return this;
    }

    public String getRequestStatus() {
        return requestStatus;
    }

    public RequestResponse setRequestStatus(String requestStatus) {
        this.requestStatus = requestStatus;
        return this;
    }

    public String getEntityName() {
        return entityName;
    }

    public RequestResponse setEntityName(String entityName) {
        this.entityName = entityName;
        return this;
    }

    public String getPaymentRequestId() {
        return paymentRequestId;
    }

    public RequestResponse setPaymentRequestId(String paymentRequestId) {
        this.paymentRequestId = paymentRequestId;
        return this;
    }

    public String getMedicalRecord() {
        return medicalRecord;
    }

    public RequestResponse setMedicalRecord(String medicalRecord) {
        this.medicalRecord = medicalRecord;
        return this;
    }

    public String getInsuranceContractId() {
        return insuranceContractId;
    }

    public RequestResponse setInsuranceContractId(String insuranceContractId) {
        this.insuranceContractId = insuranceContractId;
        return this;
    }

    public String getMedicalRecordId() {
        return medicalRecordId;
    }

    public RequestResponse setMedicalRecordId(String medicalRecordId) {
        this.medicalRecordId = medicalRecordId;
        return this;
    }

    public String getInsuranceProductId() {
        return insuranceProductId;
    }

    public RequestResponse setInsuranceProductId(String insuranceProductId) {
        this.insuranceProductId = insuranceProductId;
        return this;
    }

    public String getStartDate() {
        return startDate;
    }

    public RequestResponse setStartDate(String startDate) {
        this.startDate = startDate;
        return this;
    }

    public String getEndDate() {
        return endDate;
    }

    public RequestResponse setEndDate(String endDate) {
        this.endDate = endDate;
        return this;
    }

    public String getHashFile() {
        return hashFile;
    }

    public RequestResponse setHashFile(String hashFile) {
        this.hashFile = hashFile;
        return this;
    }

    public String getPrescriptionId() {
        return prescriptionId;
    }

    public RequestResponse setPrescriptionId(String prescriptionId) {
        this.prescriptionId = prescriptionId;
        return this;
    }

    public String getMedicalInstitutionId() {
        return medicalInstitutionId;
    }

    public RequestResponse setMedicalInstitutionId(String medicalInstitutionId) {
        this.medicalInstitutionId = medicalInstitutionId;
        return this;
    }

    public String getMedicalInstitutionName() {
        return medicalInstitutionName;
    }

    public RequestResponse setMedicalInstitutionName(String medicalInstitutionName) {
        this.medicalInstitutionName = medicalInstitutionName;
        return this;
    }

    public String getDateCreated() {
        return dateCreated;
    }

    public RequestResponse setDateCreated(String dateCreated) {
        this.dateCreated = dateCreated;
        return this;
    }
}

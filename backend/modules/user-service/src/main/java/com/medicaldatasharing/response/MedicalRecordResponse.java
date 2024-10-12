package com.medicaldatasharing.response;

import com.medicaldatasharing.chaincode.dto.MedicalRecord;
import com.owlike.genson.Genson;
import com.owlike.genson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class MedicalRecordResponse {

    @JsonProperty("medicalRecordId")
    private String medicalRecordId;

    @JsonProperty("patientId")
    private String patientId;

    @JsonProperty("patientName")
    private String patientName;

    @JsonProperty("doctorId")
    private String doctorId;

    @JsonProperty("doctorName")
    private String doctorName;

    @JsonProperty("medicalInstitutionId")
    private String medicalInstitutionId;

    @JsonProperty("medicalInstitutionName")
    private String medicalInstitutionName;

    @JsonProperty("dateCreated")
    private String dateCreated;

    @JsonProperty("dateModified")
    private String dateModified;

    @JsonProperty("testName")
    private String testName;

    @JsonProperty("details")
    private String details;

    @JsonProperty("prescriptionId")
    private String prescriptionId;

    @JsonProperty("hashFile")
    private String hashFile;

    @JsonProperty("medicalRecordStatus")
    private String medicalRecordStatus;

    @JsonProperty("entityName")
    private String entityName;

    public MedicalRecordResponse() {
        this.entityName = MedicalRecordResponse.class.getSimpleName();
    }

    public MedicalRecordResponse(MedicalRecord medicalRecord) {
        super();
        this.medicalRecordId = medicalRecord.getMedicalRecordId();
        this.patientId = medicalRecord.getPatientId();
        this.doctorId = medicalRecord.getDoctorId();
        this.medicalInstitutionId = medicalRecord.getMedicalInstitutionId();
        this.dateCreated = medicalRecord.getDateCreated();
        this.dateModified = medicalRecord.getDateModified();
        this.testName = medicalRecord.getTestName();
        this.details = medicalRecord.getDetails();
        this.prescriptionId = medicalRecord.getPrescriptionId();
        this.hashFile = medicalRecord.getHashFile();
        this.medicalRecordStatus = medicalRecord.getMedicalRecordStatus();
    }

    public static byte[] serialize(Object object) {
        Genson genson = new Genson();
        return genson.serializeBytes(object);
    }

    public static MedicalRecordResponse deserialize(byte[] data) {
        Genson genson = new Genson();
        return genson.deserialize(data, MedicalRecordResponse.class);
    }

    public String getTestName() {
        return testName;
    }

    public MedicalRecordResponse setTestName(String testName) {
        this.testName = testName;
        return this;
    }

    public String getMedicalRecordId() {
        return medicalRecordId;
    }

    public MedicalRecordResponse setMedicalRecordId(String medicalRecordId) {
        this.medicalRecordId = medicalRecordId;
        return this;
    }

    public String getPatientId() {
        return patientId;
    }

    public MedicalRecordResponse setPatientId(String patientId) {
        this.patientId = patientId;
        return this;
    }

    public String getDoctorId() {
        return doctorId;
    }

    public MedicalRecordResponse setDoctorId(String doctorId) {
        this.doctorId = doctorId;
        return this;
    }

    public String getMedicalInstitutionId() {
        return medicalInstitutionId;
    }

    public MedicalRecordResponse setMedicalInstitutionId(String medicalInstitutionId) {
        this.medicalInstitutionId = medicalInstitutionId;
        return this;
    }

    public String getDateModified() {
        return dateModified;
    }

    public MedicalRecordResponse setDateModified(String dateModified) {
        this.dateModified = dateModified;
        return this;
    }

    public String getDetails() {
        return details;
    }

    public MedicalRecordResponse setDetails(String details) {
        this.details = details;
        return this;
    }

    public String getMedicalRecordStatus() {
        return medicalRecordStatus;
    }

    public MedicalRecordResponse setMedicalRecordStatus(String medicalRecordStatus) {
        this.medicalRecordStatus = medicalRecordStatus;
        return this;
    }

    public String getEntityName() {
        return entityName;
    }

    public MedicalRecordResponse setEntityName(String entityName) {
        this.entityName = entityName;
        return this;
    }

    public String getHashFile() {
        return hashFile;
    }

    public MedicalRecordResponse setHashFile(String hashFile) {
        this.hashFile = hashFile;
        return this;
    }

    public String getPrescriptionId() {
        return prescriptionId;
    }

    public MedicalRecordResponse setPrescriptionId(String prescriptionId) {
        this.prescriptionId = prescriptionId;
        return this;
    }

    public String getPatientName() {
        return patientName;
    }

    public MedicalRecordResponse setPatientName(String patientName) {
        this.patientName = patientName;
        return this;
    }

    public String getDoctorName() {
        return doctorName;
    }

    public MedicalRecordResponse setDoctorName(String doctorName) {
        this.doctorName = doctorName;
        return this;
    }

    public String getMedicalInstitutionName() {
        return medicalInstitutionName;
    }

    public MedicalRecordResponse setMedicalInstitutionName(String medicalInstitutionName) {
        this.medicalInstitutionName = medicalInstitutionName;
        return this;
    }

    public String getDateCreated() {
        return dateCreated;
    }

    public MedicalRecordResponse setDateCreated(String dateCreated) {
        this.dateCreated = dateCreated;
        return this;
    }
}

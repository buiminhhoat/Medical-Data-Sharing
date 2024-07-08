package com.medicaldatasharing.response;

import com.owlike.genson.Genson;
import com.owlike.genson.annotation.JsonProperty;

public class MedicalRecordResponse {

    @JsonProperty("medicalRecordId")
    private String medicalRecordId;

    @JsonProperty("patientId")
    private String patientId;

    @JsonProperty("doctorId")
    private String doctorId;

    @JsonProperty("medicalInstitutionId")
    private String medicalInstitutionId;

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

    public static byte[] serialize(Object object) {
        Genson genson = new Genson();
        return genson.serializeBytes(object);
    }

    public static MedicalRecordResponse deserialize(byte[] data) {
        Genson genson = new Genson();
        return genson.deserialize(data, MedicalRecordResponse.class);
    }

    public static MedicalRecordResponse createInstance(
            String medicalRecordId,
            String patientId,
            String doctorId,
            String medicalInstitutionId,
            String dateModified,
            String testName,
            String details,
            String prescriptionId,
            String hashFile,
            String medicalRecordStatus
    ) {
        MedicalRecordResponse medicalRecord = new MedicalRecordResponse();
        medicalRecord.setMedicalRecordId(medicalRecordId);
        medicalRecord.setPatientId(patientId);
        medicalRecord.setDoctorId(doctorId);
        medicalRecord.setMedicalInstitutionId(medicalInstitutionId);
        medicalRecord.setDateModified(dateModified);
        medicalRecord.setTestName(testName);
        medicalRecord.setDetails(details);
        medicalRecord.setPrescriptionId(prescriptionId);
        medicalRecord.setHashFile(hashFile);
        medicalRecord.setMedicalRecordStatus(medicalRecordStatus);
        medicalRecord.setEntityName(MedicalRecordResponse.class.getSimpleName());
        return medicalRecord;
    }

    @Override
    public String toString() {
        return "MedicalRecord{" +
                "medicalRecordId='" + medicalRecordId + '\'' +
                ", patientId='" + patientId + '\'' +
                ", doctorId='" + doctorId + '\'' +
                ", medicalInstitutionId='" + medicalInstitutionId + '\'' +
                ", dateModified='" + dateModified + '\'' +
                ", testName='" + testName + '\'' +
                ", details='" + details + '\'' +
                ", hashFile='" + hashFile + '\'' +
                ", prescriptionId='" + prescriptionId + '\'' +
                ", medicalRecordStatus='" + medicalRecordStatus + '\'' +
                ", entityName='" + entityName + '\'' +
                '}';
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
}

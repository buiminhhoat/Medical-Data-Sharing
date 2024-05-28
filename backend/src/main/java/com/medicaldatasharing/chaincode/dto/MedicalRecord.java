package com.medicaldatasharing.chaincode.dto;

import com.owlike.genson.Genson;
import com.owlike.genson.annotation.JsonProperty;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import static java.nio.charset.StandardCharsets.UTF_8;

public class MedicalRecord {

    @JsonProperty("medicalRecordId")
    private String medicalRecordId;

    @JsonProperty("patientId")
    private String patientId;

    @JsonProperty("doctorId")
    private String doctorId;

    @JsonProperty("medicalInstitutionId")
    private String medicalInstitutionId;

    @JsonProperty("dateCreated")
    private String dateCreated;

    @JsonProperty("testName")
    private String testName;

    @JsonProperty("details")
    private String details;

    @JsonProperty("medicalRecordStatus")
    private String medicalRecordStatus;

    @JsonProperty("entityName")
    private String entityName;

    public static byte[] serialize(Object object) {
        Genson genson = new Genson();
        return genson.serializeBytes(object);
    }

    public static MedicalRecord deserialize(byte[] data) {
        Genson genson = new Genson();
        return genson.deserialize(data, MedicalRecord.class);
    }

    public static MedicalRecord parseMedicalRecord(JSONObject jsonObject) {
        String medicalRecordId = jsonObject.getString("medicalRecordId");
        String patientId = jsonObject.getString("patientId");
        String doctorId = jsonObject.getString("doctorId");
        String medicalInstitutionId = jsonObject.getString("medicalInstitutionId");
        String dateCreated = jsonObject.getString("dateCreated");
        String testName = jsonObject.getString("testName");
        String details = jsonObject.getString("details");
        String medicalRecordStatus = jsonObject.getString("medicalRecordStatus");

        return createInstance(
                medicalRecordId,
                patientId,
                doctorId,
                medicalInstitutionId,
                dateCreated,
                testName,
                details,
                medicalRecordStatus
        );
    }

    public static MedicalRecord createInstance(
            String medicalRecordId,
            String patientId,
            String doctorId,
            String medicalInstitutionId,
            String dateCreated,
            String testName,
            String details,
            String medicalRecordStatus
    ) {
        MedicalRecord medicalRecord = new MedicalRecord();
        medicalRecord.setMedicalRecordId(medicalRecordId);
        medicalRecord.setPatientId(patientId);
        medicalRecord.setDoctorId(doctorId);
        medicalRecord.setMedicalInstitutionId(medicalInstitutionId);
        medicalRecord.setDateCreated(dateCreated);
        medicalRecord.setTestName(testName);
        medicalRecord.setDetails(details);
        medicalRecord.setMedicalRecordStatus(medicalRecordStatus);
        medicalRecord.setEntityName(MedicalRecord.class.getSimpleName());
        return medicalRecord;
    }

    @Override
    public String toString() {
        return "MedicalRecord{" +
                "medicalRecordId='" + medicalRecordId + '\'' +
                ", patientId='" + patientId + '\'' +
                ", doctorId='" + doctorId + '\'' +
                ", medicalInstitutionId='" + medicalInstitutionId + '\'' +
                ", dateCreated='" + dateCreated + '\'' +
                ", testName='" + testName + '\'' +
                ", details='" + details + '\'' +
                ", medicalRecordStatus='" + medicalRecordStatus + '\'' +
                ", entityName='" + entityName + '\'' +
                '}';
    }

    public String getTestName() {
        return testName;
    }

    public MedicalRecord setTestName(String testName) {
        this.testName = testName;
        return this;
    }

    public String getMedicalRecordId() {
        return medicalRecordId;
    }

    public MedicalRecord setMedicalRecordId(String medicalRecordId) {
        this.medicalRecordId = medicalRecordId;
        return this;
    }

    public String getPatientId() {
        return patientId;
    }

    public MedicalRecord setPatientId(String patientId) {
        this.patientId = patientId;
        return this;
    }

    public String getDoctorId() {
        return doctorId;
    }

    public MedicalRecord setDoctorId(String doctorId) {
        this.doctorId = doctorId;
        return this;
    }

    public String getMedicalInstitutionId() {
        return medicalInstitutionId;
    }

    public MedicalRecord setMedicalInstitutionId(String medicalInstitutionId) {
        this.medicalInstitutionId = medicalInstitutionId;
        return this;
    }

    public String getDateCreated() {
        return dateCreated;
    }

    public MedicalRecord setDateCreated(String dateCreated) {
        this.dateCreated = dateCreated;
        return this;
    }

    public String getDetails() {
        return details;
    }

    public MedicalRecord setDetails(String details) {
        this.details = details;
        return this;
    }

    public String getMedicalRecordStatus() {
        return medicalRecordStatus;
    }

    public MedicalRecord setMedicalRecordStatus(String medicalRecordStatus) {
        this.medicalRecordStatus = medicalRecordStatus;
        return this;
    }

    public String getEntityName() {
        return entityName;
    }

    public MedicalRecord setEntityName(String entityName) {
        this.entityName = entityName;
        return this;
    }
}

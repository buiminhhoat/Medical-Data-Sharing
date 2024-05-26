package com.medicaldatasharing.chaincode.dto;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import static java.nio.charset.StandardCharsets.UTF_8;

public class MedicalRecord {

    private String medicalRecordId;

    private String patientId;

    private String doctorId;

    private String medicalInstitutionId;

    private String dateCreated;

    private String testName;

    private String details;

    private String medicalRecordStatus;

    private String changeHistory;

    private String entityName;

    public static byte[] serialize(Object object) {
        String jsonStr = new JSONObject(object).toString();
        return jsonStr.getBytes(UTF_8);
    }

    public static MedicalRecord deserialize(byte[] data) {
        JSONObject json = new JSONObject(new String(data, UTF_8));
        return parseMedicalRecord(json);
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
        String changeHistory = jsonObject.getString("changeHistory");

        return createInstance(
                medicalRecordId,
                patientId,
                doctorId,
                medicalInstitutionId,
                dateCreated,
                testName,
                details,
                medicalRecordStatus,
                changeHistory
        );
    }

    public static List<MedicalRecord> parseChangeHistory(JSONArray changeHistoryJson) {
        List<MedicalRecord> changeHistory = new ArrayList<>();
        for (int i = 0; i < changeHistoryJson.length(); i++) {
            JSONObject medicalRecordJson = changeHistoryJson.getJSONObject(i);
            MedicalRecord medicalRecord = parseMedicalRecord(medicalRecordJson);
            changeHistory.add(medicalRecord);
        }
        return changeHistory;
    }

    public static MedicalRecord createInstance(
            String medicalRecordId,
            String patientId,
            String doctorId,
            String medicalInstitutionId,
            String dateCreated,
            String testName,
            String details,
            String medicalRecordStatus,
            String changeHistory
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
        medicalRecord.setChangeHistory(changeHistory);
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
                ", changeHistory=" + changeHistory +
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

    public String getChangeHistory() {
        return changeHistory;
    }

    public MedicalRecord setChangeHistory(String changeHistory) {
        this.changeHistory = changeHistory;
        return this;
    }
}

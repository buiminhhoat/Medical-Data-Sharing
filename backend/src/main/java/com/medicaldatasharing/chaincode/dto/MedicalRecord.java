package com.medicaldatasharing.chaincode.dto;

import org.json.JSONObject;

import static java.nio.charset.StandardCharsets.UTF_8;

public class MedicalRecord {

    private String medicalRecordId;

    private String patientId;

    private String doctorId;

    private String medicalInstitutionId;

    private String dateCreated;

    private String testName;

    private String relevantParameters;

    private String medicalRecordStatus;

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
        String medicalRecordStatus = jsonObject.getString("medicalRecordStatus");
        String relevantParameters = jsonObject.getString("relevantParameters");

        return createInstance(
                medicalRecordId,
                patientId,
                doctorId,
                medicalInstitutionId,
                dateCreated,
                testName,
                medicalRecordStatus,
                relevantParameters
        );
    }

    public static MedicalRecord createInstance(
            String medicalRecordId,
            String patientId,
            String doctorId,
            String medicalInstitutionId,
            String dateCreated,
            String testName,
            String medicalRecordStatus,
            String relevantParameters
    ) {
        MedicalRecord medicalRecord = new MedicalRecord();
        medicalRecord.setMedicalRecordId(medicalRecordId);
        medicalRecord.setPatientId(patientId);
        medicalRecord.setDoctorId(doctorId);
        medicalRecord.setMedicalInstitutionId(medicalInstitutionId);
        medicalRecord.setDateCreated(dateCreated);
        medicalRecord.setTestName(testName);
        medicalRecord.setRelevantParameters(relevantParameters);
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
                ", relevantParameters='" + relevantParameters + '\'' +
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

    public String getRelevantParameters() {
        return relevantParameters;
    }

    public MedicalRecord setRelevantParameters(String relevantParameters) {
        this.relevantParameters = relevantParameters;
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

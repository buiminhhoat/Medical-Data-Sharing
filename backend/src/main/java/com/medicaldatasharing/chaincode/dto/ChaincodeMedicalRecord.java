package com.medicaldatasharing.chaincode.dto;

import org.json.JSONObject;

import static java.nio.charset.StandardCharsets.UTF_8;

public class ChaincodeMedicalRecord {

    private String medicalRecordId;

    private String patientId;

    private String doctorId;

    private String medicalInstitutionId;

    private String time;

    private String testName;

    private String relevantParameters;

    private String entityName;

    public static byte[] serialize(Object object) {
        String jsonStr = new JSONObject(object).toString();
        return jsonStr.getBytes(UTF_8);
    }

    public static ChaincodeMedicalRecord deserialize(byte[] data) {
        JSONObject json = new JSONObject(new String(data, UTF_8));
        return parseMedicalRecord(json);
    }

    public static ChaincodeMedicalRecord parseMedicalRecord(JSONObject jsonObject) {
        String medicalRecordId = jsonObject.getString("medicalRecordId");
        String patientId = jsonObject.getString("patientId");
        String doctorId = jsonObject.getString("doctorId");
        String medicalInstitutionId = jsonObject.getString("medicalInstitutionId");
        String time = jsonObject.getString("time");
        String testName = jsonObject.getString("testName");
        String relevantParameters = jsonObject.getString("relevantParameters");

        return createInstance(
                medicalRecordId,
                patientId,
                doctorId,
                medicalInstitutionId,
                time,
                testName,
                relevantParameters
        );
    }

    public static ChaincodeMedicalRecord createInstance(
            String medicalRecordId,
            String patientId,
            String doctorId,
            String medicalInstitutionId,
            String time,
            String testName,
            String relevantParameters
    ) {
        ChaincodeMedicalRecord chaincodeMedicalRecord = new ChaincodeMedicalRecord();
        chaincodeMedicalRecord.setMedicalRecordId(medicalRecordId);
        chaincodeMedicalRecord.setPatientId(patientId);
        chaincodeMedicalRecord.setDoctorId(doctorId);
        chaincodeMedicalRecord.setMedicalInstitutionId(medicalInstitutionId);
        chaincodeMedicalRecord.setTime(time);
        chaincodeMedicalRecord.setTestName(testName);
        chaincodeMedicalRecord.setRelevantParameters(relevantParameters);
        chaincodeMedicalRecord.setEntityName(ChaincodeMedicalRecord.class.getSimpleName());
        return chaincodeMedicalRecord;
    }

    @Override
    public String toString() {
        return "ChaincodeMedicalRecord{" +
                "medicalRecordId='" + medicalRecordId + '\'' +
                ", patientId='" + patientId + '\'' +
                ", doctorId='" + doctorId + '\'' +
                ", medicalInstitutionId='" + medicalInstitutionId + '\'' +
                ", time='" + time + '\'' +
                ", testName='" + testName + '\'' +
                ", relevantParameters='" + relevantParameters + '\'' +
                ", entityName='" + entityName + '\'' +
                '}';
    }

    public String getTestName() {
        return testName;
    }

    public ChaincodeMedicalRecord setTestName(String testName) {
        this.testName = testName;
        return this;
    }

    public String getMedicalRecordId() {
        return medicalRecordId;
    }

    public ChaincodeMedicalRecord setMedicalRecordId(String medicalRecordId) {
        this.medicalRecordId = medicalRecordId;
        return this;
    }

    public String getPatientId() {
        return patientId;
    }

    public ChaincodeMedicalRecord setPatientId(String patientId) {
        this.patientId = patientId;
        return this;
    }

    public String getDoctorId() {
        return doctorId;
    }

    public ChaincodeMedicalRecord setDoctorId(String doctorId) {
        this.doctorId = doctorId;
        return this;
    }

    public String getMedicalInstitutionId() {
        return medicalInstitutionId;
    }

    public ChaincodeMedicalRecord setMedicalInstitutionId(String medicalInstitutionId) {
        this.medicalInstitutionId = medicalInstitutionId;
        return this;
    }

    public String getTime() {
        return time;
    }

    public ChaincodeMedicalRecord setTime(String time) {
        this.time = time;
        return this;
    }

    public String getRelevantParameters() {
        return relevantParameters;
    }

    public ChaincodeMedicalRecord setRelevantParameters(String relevantParameters) {
        this.relevantParameters = relevantParameters;
        return this;
    }

    public String getEntityName() {
        return entityName;
    }

    public ChaincodeMedicalRecord setEntityName(String entityName) {
        this.entityName = entityName;
        return this;
    }
}

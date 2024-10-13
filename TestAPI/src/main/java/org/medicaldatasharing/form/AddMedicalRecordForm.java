package org.medicaldatasharing.form;

import org.json.JSONObject;

import java.lang.reflect.Field;

public class AddMedicalRecordForm {
    private String requestId;

    private String medicalRecordId;

    private String patientId;

    private String doctorId;

    private String medicalInstitutionId;

    private String dateCreated;

    private String dateModified;

    private String testName;

    private String details;

    private String addPrescription;

    private String prescriptionId;

    private String hashFile;

    private String medicalRecordStatus;

    public String getRequestId() {
        return requestId;
    }

    public AddMedicalRecordForm setRequestId(String requestId) {
        this.requestId = requestId;
        return this;
    }

    public String getMedicalRecordId() {
        return medicalRecordId;
    }

    public AddMedicalRecordForm setMedicalRecordId(String medicalRecordId) {
        this.medicalRecordId = medicalRecordId;
        return this;
    }

    public String getPatientId() {
        return patientId;
    }

    public AddMedicalRecordForm setPatientId(String patientId) {
        this.patientId = patientId;
        return this;
    }

    public String getDoctorId() {
        return doctorId;
    }

    public AddMedicalRecordForm setDoctorId(String doctorId) {
        this.doctorId = doctorId;
        return this;
    }

    public String getMedicalInstitutionId() {
        return medicalInstitutionId;
    }

    public AddMedicalRecordForm setMedicalInstitutionId(String medicalInstitutionId) {
        this.medicalInstitutionId = medicalInstitutionId;
        return this;
    }

    public String getDateCreated() {
        return dateCreated;
    }

    public AddMedicalRecordForm setDateCreated(String dateCreated) {
        this.dateCreated = dateCreated;
        return this;
    }

    public String getDateModified() {
        return dateModified;
    }

    public AddMedicalRecordForm setDateModified(String dateModified) {
        this.dateModified = dateModified;
        return this;
    }

    public String getTestName() {
        return testName;
    }

    public AddMedicalRecordForm setTestName(String testName) {
        this.testName = testName;
        return this;
    }

    public String getDetails() {
        return details;
    }

    public AddMedicalRecordForm setDetails(String details) {
        this.details = details;
        return this;
    }

    public String getAddPrescription() {
        return addPrescription;
    }

    public AddMedicalRecordForm setAddPrescription(String addPrescription) {
        this.addPrescription = addPrescription;
        return this;
    }

    public String getPrescriptionId() {
        return prescriptionId;
    }

    public AddMedicalRecordForm setPrescriptionId(String prescriptionId) {
        this.prescriptionId = prescriptionId;
        return this;
    }

    public String getHashFile() {
        return hashFile;
    }

    public AddMedicalRecordForm setHashFile(String hashFile) {
        this.hashFile = hashFile;
        return this;
    }

    public String getMedicalRecordStatus() {
        return medicalRecordStatus;
    }

    public AddMedicalRecordForm setMedicalRecordStatus(String medicalRecordStatus) {
        this.medicalRecordStatus = medicalRecordStatus;
        return this;
    }

    public JSONObject toJSONObject() {
        JSONObject jsonObj = new JSONObject();
        Field[] fields = this.getClass().getDeclaredFields();

        for (Field field : fields) {
            field.setAccessible(true);
            try {
                jsonObj.put(field.getName(), field.get(this));
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
        return jsonObj;
    }
}

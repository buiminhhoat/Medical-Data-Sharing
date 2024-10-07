package com.medicaldatasharing.form;

import com.medicaldatasharing.util.AESUtil;
import org.json.JSONObject;

import javax.validation.constraints.NotBlank;
import java.lang.reflect.Field;

public class AddMedicalRecordForm {
    @NotBlank
    private String requestId;

    @NotBlank
    private String medicalRecordId;

    @NotBlank
    private String patientId;

    @NotBlank
    private String doctorId;

    @NotBlank
    private String medicalInstitutionId;

    @NotBlank
    private String dateCreated;

    @NotBlank
    private String dateModified;

    @NotBlank
    private String testName;

    @NotBlank
    private String details;

    private String addPrescription;

    @NotBlank
    private String prescriptionId;

    @NotBlank
    private String hashFile;

    @NotBlank
    private String medicalRecordStatus;

    public @NotBlank String getRequestId() {
        return requestId;
    }

    public AddMedicalRecordForm setRequestId(@NotBlank String requestId) {
        this.requestId = requestId;
        return this;
    }

    public @NotBlank String getMedicalRecordId() {
        return medicalRecordId;
    }

    public AddMedicalRecordForm setMedicalRecordId(@NotBlank String medicalRecordId) {
        this.medicalRecordId = medicalRecordId;
        return this;
    }

    public @NotBlank String getPatientId() {
        return patientId;
    }

    public AddMedicalRecordForm setPatientId(@NotBlank String patientId) {
        this.patientId = patientId;
        return this;
    }

    public @NotBlank String getDoctorId() {
        return doctorId;
    }

    public AddMedicalRecordForm setDoctorId(@NotBlank String doctorId) {
        this.doctorId = doctorId;
        return this;
    }

    public @NotBlank String getMedicalInstitutionId() {
        return medicalInstitutionId;
    }

    public AddMedicalRecordForm setMedicalInstitutionId(@NotBlank String medicalInstitutionId) {
        this.medicalInstitutionId = medicalInstitutionId;
        return this;
    }

    public @NotBlank String getDateCreated() {
        return dateCreated;
    }

    public AddMedicalRecordForm setDateCreated(@NotBlank String dateCreated) {
        this.dateCreated = dateCreated;
        return this;
    }

    public @NotBlank String getDateModified() {
        return dateModified;
    }

    public AddMedicalRecordForm setDateModified(@NotBlank String dateModified) {
        this.dateModified = dateModified;
        return this;
    }

    public @NotBlank String getTestName() {
        return testName;
    }

    public AddMedicalRecordForm setTestName(@NotBlank String testName) {
        this.testName = testName;
        return this;
    }

    public @NotBlank String getDetails() {
        return details;
    }

    public AddMedicalRecordForm setDetails(@NotBlank String details) {
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

    public @NotBlank String getPrescriptionId() {
        return prescriptionId;
    }

    public AddMedicalRecordForm setPrescriptionId(@NotBlank String prescriptionId) {
        this.prescriptionId = prescriptionId;
        return this;
    }

    public @NotBlank String getHashFile() {
        return hashFile;
    }

    public AddMedicalRecordForm setHashFile(@NotBlank String hashFile) {
        this.hashFile = hashFile;
        return this;
    }

    public @NotBlank String getMedicalRecordStatus() {
        return medicalRecordStatus;
    }

    public AddMedicalRecordForm setMedicalRecordStatus(@NotBlank String medicalRecordStatus) {
        this.medicalRecordStatus = medicalRecordStatus;
        return this;
    }

    public void encrypt() throws Exception {
        this.testName = AESUtil.encrypt(this.testName);
        this.details = AESUtil.encrypt(this.details);
        this.hashFile = AESUtil.encrypt(this.hashFile);
    }

    public void decrypt() throws Exception {
        this.testName = AESUtil.decrypt(this.testName);
        this.details = AESUtil.decrypt(this.details);
        this.hashFile = AESUtil.decrypt(this.hashFile);
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

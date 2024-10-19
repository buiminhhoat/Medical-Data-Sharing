package com.medicaldatasharing.form;

import com.medicaldatasharing.util.AESUtil;
import org.json.JSONObject;

import javax.validation.constraints.Max;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.lang.reflect.Field;

public class AddMedicalRecordForm {
    @NotBlank
    private String requestId;

    private String medicalRecordId;

    @NotBlank
    private String patientId;

    @NotBlank
    private String doctorId;

    @NotBlank
    private String medicalInstitutionId;

    private String dateCreated;

    private String dateModified;

    @NotBlank
    @Size(max = 100)
    private String testName;

    @NotBlank
    @Size(max = 100)
    private String details;

    private String addPrescription;

    private String prescriptionId;

    private String hashFile;

    private String medicalRecordStatus;

    public @NotBlank String getRequestId() {
        return requestId;
    }

    public AddMedicalRecordForm setRequestId(@NotBlank String requestId) {
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

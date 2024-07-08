package com.medicaldatasharing.dto;

import lombok.*;
import org.json.JSONArray;
import org.json.JSONObject;

import javax.validation.constraints.NotBlank;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class MedicalRecordDto {
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

    public static MedicalRecordDto parseMedicalRecordDto(JSONObject jsonObject) {
        String medicalRecordId = jsonObject.getString("medicalRecordId");
        String patientId = jsonObject.getString("patientId");
        String doctorId = jsonObject.getString("doctorId");
        String medicalInstitutionId = jsonObject.getString("medicalInstitutionId");
        String dateCreated = jsonObject.getString("dateCreated");
        String dateModified = jsonObject.getString("dateModified");
        String testName = jsonObject.getString("testName");
        String details = jsonObject.getString("details");
        String prescriptionId = jsonObject.getString("prescriptionId");
        String hashFile = jsonObject.getString("hashFile");
        String medicalRecordStatus = jsonObject.getString("medicalRecordStatus");

        return createInstance(
                medicalRecordId,
                patientId,
                doctorId,
                medicalInstitutionId,
                dateCreated,
                dateModified,
                testName,
                details,
                prescriptionId,
                hashFile,
                medicalRecordStatus
        );
    }

    private static List<MedicalRecordDto> parseChangeHistory(JSONArray changeHistoryJson) {
        List<MedicalRecordDto> changeHistory = new ArrayList<>();
        for (int i = 0; i < changeHistoryJson.length(); i++) {
            JSONObject medicalRecordJson = changeHistoryJson.getJSONObject(i);
            MedicalRecordDto medicalRecord = parseMedicalRecordDto(medicalRecordJson);
            changeHistory.add(medicalRecord);
        }
        return changeHistory;
    }

    public static MedicalRecordDto createInstance(
            String medicalRecordId,
            String patientId,
            String doctorId,
            String medicalInstitutionId,
            String dateCreated,
            String dateModified,
            String testName,
            String details,
            String addPrescription,
            String hashFile,
            String medicalRecordStatus
    ) {
        MedicalRecordDto medicalRecord = new MedicalRecordDto();
        medicalRecord.setMedicalRecordId(medicalRecordId);
        medicalRecord.setPatientId(patientId);
        medicalRecord.setDoctorId(doctorId);
        medicalRecord.setMedicalInstitutionId(medicalInstitutionId);
        medicalRecord.setDateCreated(dateCreated);
        medicalRecord.setDateModified(dateModified);
        medicalRecord.setTestName(testName);
        medicalRecord.setDetails(details);
        medicalRecord.setAddPrescription(addPrescription);
        medicalRecord.setHashFile(hashFile);
        medicalRecord.setMedicalRecordStatus(medicalRecordStatus);
        return medicalRecord;
    }

    public @NotBlank String getPatientId() {
        return patientId;
    }

    public MedicalRecordDto setPatientId(@NotBlank String patientId) {
        this.patientId = patientId;
        return this;
    }

    public @NotBlank String getDoctorId() {
        return doctorId;
    }

    public MedicalRecordDto setDoctorId(@NotBlank String doctorId) {
        this.doctorId = doctorId;
        return this;
    }

    public @NotBlank String getMedicalInstitutionId() {
        return medicalInstitutionId;
    }

    public MedicalRecordDto setMedicalInstitutionId(@NotBlank String medicalInstitutionId) {
        this.medicalInstitutionId = medicalInstitutionId;
        return this;
    }

    public @NotBlank String getDateCreated() {
        return dateCreated;
    }

    public MedicalRecordDto setDateCreated(@NotBlank String dateCreated) {
        this.dateCreated = dateCreated;
        return this;
    }

    public String getAddPrescription() {
        return addPrescription;
    }

    public MedicalRecordDto setAddPrescription(String addPrescription) {
        this.addPrescription = addPrescription;
        return this;
    }

    public @NotBlank String getPrescriptionId() {
        return prescriptionId;
    }

    public MedicalRecordDto setPrescriptionId(@NotBlank String prescriptionId) {
        this.prescriptionId = prescriptionId;
        return this;
    }

    public @NotBlank String getHashFile() {
        return hashFile;
    }

    public MedicalRecordDto setHashFile(@NotBlank String hashFile) {
        this.hashFile = hashFile;
        return this;
    }

    public @NotBlank String getDateModified() {
        return dateModified;
    }

    public MedicalRecordDto setDateModified(@NotBlank String dateModified) {
        this.dateModified = dateModified;
        return this;
    }

    public @NotBlank String getTestName() {
        return testName;
    }

    public MedicalRecordDto setTestName(@NotBlank String testName) {
        this.testName = testName;
        return this;
    }

    public @NotBlank String getDetails() {
        return details;
    }

    public MedicalRecordDto setDetails(@NotBlank String details) {
        this.details = details;
        return this;
    }

    public @NotBlank String getMedicalRecordId() {
        return medicalRecordId;
    }

    public MedicalRecordDto setMedicalRecordId(@NotBlank String medicalRecordId) {
        this.medicalRecordId = medicalRecordId;
        return this;
    }

    public @NotBlank String getMedicalRecordStatus() {
        return medicalRecordStatus;
    }

    public MedicalRecordDto setMedicalRecordStatus(@NotBlank String medicalRecordStatus) {
        this.medicalRecordStatus = medicalRecordStatus;
        return this;
    }

    public @NotBlank String getRequestId() {
        return requestId;
    }

    public MedicalRecordDto setRequestId(@NotBlank String requestId) {
        this.requestId = requestId;
        return this;
    }
}

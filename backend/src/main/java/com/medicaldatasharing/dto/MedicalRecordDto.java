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
    private String testName;

    @NotBlank
    private String details;

    @NotBlank
    private String medicalRecordStatus;

    @NotBlank
    private List<MedicalRecordDto> changeHistory;

    public static MedicalRecordDto parseMedicalRecordDto(JSONObject jsonObject) {
        String medicalRecordId = jsonObject.getString("medicalRecordId");
        String patientId = jsonObject.getString("patientId");
        String doctorId = jsonObject.getString("doctorId");
        String medicalInstitutionId = jsonObject.getString("medicalInstitutionId");
        String dateCreated = jsonObject.getString("dateCreated");
        String testName = jsonObject.getString("testName");
        String details = jsonObject.getString("details");
        String medicalRecordStatus = jsonObject.getString("medicalRecordStatus");
        List<MedicalRecordDto> changeHistory = parseChangeHistory(jsonObject.getJSONArray("changeHistory"));

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
            String testName,
            String details,
            String medicalRecordStatus,
            List<MedicalRecordDto> changeHistory
    ) {
        MedicalRecordDto medicalRecord = new MedicalRecordDto();
        medicalRecord.setMedicalRecordId(medicalRecordId);
        medicalRecord.setPatientId(patientId);
        medicalRecord.setDoctorId(doctorId);
        medicalRecord.setMedicalInstitutionId(medicalInstitutionId);
        medicalRecord.setDateCreated(dateCreated);
        medicalRecord.setTestName(testName);
        medicalRecord.setDetails(details);
        medicalRecord.setMedicalRecordStatus(medicalRecordStatus);
        medicalRecord.setChangeHistory(changeHistory);
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

    public @NotBlank List<MedicalRecordDto> getChangeHistory() {
        return changeHistory;
    }

    public MedicalRecordDto setChangeHistory(@NotBlank List<MedicalRecordDto> changeHistory) {
        this.changeHistory = changeHistory;
        return this;
    }
}

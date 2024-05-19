package com.medicaldatasharing.dto;

import lombok.*;

import javax.validation.constraints.NotBlank;

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
}

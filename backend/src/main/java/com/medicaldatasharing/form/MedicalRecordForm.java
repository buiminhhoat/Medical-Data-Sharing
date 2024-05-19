package com.medicaldatasharing.form;

import lombok.*;

import javax.validation.constraints.NotBlank;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MedicalRecordForm {
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


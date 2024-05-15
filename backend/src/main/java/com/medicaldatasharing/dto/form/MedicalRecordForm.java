package com.medicaldatasharing.dto.form;

import lombok.*;

import javax.validation.constraints.NotBlank;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MedicalRecordForm {
    @NotBlank
    private String patientId;

    private String doctorId;

    private String medicalInstitutionId;

    private String time;

    private String testName;

    private String relevantParameters;
}


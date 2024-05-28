package com.medicaldatasharing.dto;

import lombok.*;

import javax.validation.constraints.NotBlank;
import java.util.List;

@Getter
@Setter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class MedicalRecordPreviewDto {
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
}

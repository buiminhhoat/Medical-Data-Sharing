package com.medicaldatasharing.dto;

import lombok.*;

@Getter
@Setter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class MedicalRecordDto {
    private String patientId;

    private String doctorId;

    private String medicalInstitutionId;

    private String dateCreated;

    private String testName;

    private String relevantParameters;
}

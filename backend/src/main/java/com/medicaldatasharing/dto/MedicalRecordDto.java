package com.medicaldatasharing.dto;

import lombok.*;

import java.util.Date;

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

    private String time;

    private String testName;

    private String relevantParameters;
}

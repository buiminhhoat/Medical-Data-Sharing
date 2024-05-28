package com.medicaldatasharing.form;

import lombok.*;

import java.util.Date;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SearchMedicalRecordForm {
    private String medicalRecordId;
    private String patientId;
    private String doctorId;
    private String medicalInstitutionId;
    private Date from;
    private Date until;
    private String testName;
    private String medicalRecordStatus;
    private String details;
    private String sortingOrder;
}


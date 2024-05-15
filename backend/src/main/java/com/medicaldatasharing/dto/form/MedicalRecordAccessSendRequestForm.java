package com.medicaldatasharing.dto.form;

import lombok.*;

import javax.validation.constraints.NotBlank;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MedicalRecordAccessSendRequestForm {
    private String medicalRecordAccessRequestId;
    @NotBlank
    private String medicalRecordId;
    private String patientId;
    private String requesterId;
    private String dateCreated;
}


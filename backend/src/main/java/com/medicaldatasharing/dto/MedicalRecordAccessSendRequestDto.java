package com.medicaldatasharing.dto;

import lombok.*;

import javax.validation.constraints.NotBlank;

@Getter
@Setter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class MedicalRecordAccessSendRequestDto {
    @NotBlank
    private String medicalRecordId;
    private String patientId;
    private String requesterId;
    private String dateCreated;
    private String medicalRecordAccessRequestId;
}

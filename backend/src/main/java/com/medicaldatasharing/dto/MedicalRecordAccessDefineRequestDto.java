package com.medicaldatasharing.dto;

import lombok.*;

import javax.validation.constraints.NotBlank;

@Getter
@Setter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class MedicalRecordAccessDefineRequestDto {
    @NotBlank
    private String medicalRecordAccessRequestId;
    private String decision;
    private String accessAvailableFrom;
    private String accessAvailableUntil;
}

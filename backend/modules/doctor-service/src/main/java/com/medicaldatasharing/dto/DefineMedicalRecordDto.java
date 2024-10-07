package com.medicaldatasharing.dto;

import lombok.*;

import javax.validation.constraints.NotBlank;

@Getter
@Setter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class DefineMedicalRecordDto {
    @NotBlank
    String medicalRecordId;

    @NotBlank
    String medicalRecordStatus;
}

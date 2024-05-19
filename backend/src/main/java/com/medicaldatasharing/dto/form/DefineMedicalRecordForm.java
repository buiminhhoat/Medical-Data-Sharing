package com.medicaldatasharing.dto.form;

import lombok.*;

import javax.validation.constraints.NotBlank;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DefineMedicalRecordForm {
    @NotBlank
    String medicalRecordId;

    @NotBlank
    String medicalRecordStatus;
}


package com.medicaldatasharing.dto.form;

import lombok.*;

import javax.validation.constraints.NotBlank;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MedicalRecordAccessDefineRequestForm {
    @NotBlank
    private String medicalRecordAccessRequestId;
    private String decision;
    private String accessAvailableFrom;
    private String accessAvailableUntil;
}


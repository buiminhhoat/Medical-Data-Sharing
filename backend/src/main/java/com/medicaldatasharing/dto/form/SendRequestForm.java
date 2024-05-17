package com.medicaldatasharing.dto.form;

import lombok.*;

import javax.validation.constraints.NotBlank;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SendRequestForm {
    @NotBlank
    private String senderId;
    @NotBlank
    private String recipientId;
    @NotBlank
    private String medicalRecordId;
    @NotBlank
    private String dateCreated;
    @NotBlank
    private String requestType;
}


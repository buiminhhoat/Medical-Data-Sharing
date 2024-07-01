package com.medicaldatasharing.form;

import lombok.*;

import javax.validation.constraints.NotBlank;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SendPaymentRequestForm {
    @NotBlank
    private String senderId;

    @NotBlank
    private String recipientId;

    @NotBlank
    private String dateModified;

    @NotBlank
    private String insuranceContractId;

    @NotBlank
    private String medicalRecordId;
}


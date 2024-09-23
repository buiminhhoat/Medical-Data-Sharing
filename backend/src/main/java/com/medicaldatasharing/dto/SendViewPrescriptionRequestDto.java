package com.medicaldatasharing.dto;

import lombok.*;

import javax.validation.constraints.NotBlank;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor

public class SendViewPrescriptionRequestDto {
    @NotBlank
    private String senderId;

    @NotBlank
    private String recipientId;

    @NotBlank
    private String dateCreated;

    @NotBlank
    private String dateModified;

    @NotBlank
    private String prescriptionId;
}

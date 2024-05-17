package com.medicaldatasharing.dto;

import lombok.*;

import javax.validation.constraints.NotBlank;

@Getter
@Setter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class SendRequestDto {
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

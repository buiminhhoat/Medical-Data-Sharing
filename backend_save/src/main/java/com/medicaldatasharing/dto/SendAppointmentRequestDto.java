package com.medicaldatasharing.dto;

import lombok.*;

import javax.validation.constraints.NotBlank;

@Getter
@Setter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class SendAppointmentRequestDto {
    @NotBlank
    private String senderId;

    @NotBlank
    private String recipientId;

    @NotBlank
    private String dateCreated;

    @NotBlank
    private String dateModified;
}

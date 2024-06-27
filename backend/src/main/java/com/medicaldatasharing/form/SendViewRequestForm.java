package com.medicaldatasharing.form;

import lombok.*;

import javax.validation.constraints.NotBlank;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SendViewRequestForm {
    @NotBlank
    private String senderId;

    @NotBlank
    private String recipientId;

    @NotBlank
    private String dateModified;
}

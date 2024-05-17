package com.medicaldatasharing.dto.form;

import lombok.*;

import javax.validation.constraints.NotBlank;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DefineRequestForm {
    @NotBlank
    private String requestId;
    @NotBlank
    private String requestStatus;
    @NotBlank
    private String accessAvailableFrom;
    @NotBlank
    private String accessAvailableUntil;
}


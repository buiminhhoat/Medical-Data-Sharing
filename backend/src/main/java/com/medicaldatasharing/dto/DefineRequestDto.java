package com.medicaldatasharing.dto;

import lombok.*;

import javax.validation.constraints.NotBlank;

@Getter
@Setter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class DefineRequestDto {
    @NotBlank
    private String requestId;
    @NotBlank
    private String requestStatus;
    @NotBlank
    private String accessAvailableFrom;
    @NotBlank
    private String accessAvailableUntil;
}

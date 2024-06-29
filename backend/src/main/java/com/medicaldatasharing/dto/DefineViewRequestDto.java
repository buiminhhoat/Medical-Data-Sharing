package com.medicaldatasharing.dto;

import lombok.*;

import javax.validation.constraints.NotBlank;

@Getter
@Setter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class DefineViewRequestDto {
    @NotBlank
    private String requestId;
    @NotBlank
    private String requestStatus;
}

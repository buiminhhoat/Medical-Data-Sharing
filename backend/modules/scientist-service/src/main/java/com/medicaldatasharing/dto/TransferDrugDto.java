package com.medicaldatasharing.dto;

import lombok.*;

import javax.validation.constraints.NotBlank;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TransferDrugDto {
    @NotBlank
    private String drugId;

    @NotBlank
    private String newOwnerId;
}

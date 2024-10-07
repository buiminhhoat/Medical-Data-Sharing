package com.medicaldatasharing.form;

import lombok.*;

import javax.validation.constraints.NotBlank;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SearchDrugForm {
    private String drugId;

    private String medicationId;

    private String manufactureDate;

    private String expirationDate;

    @NotBlank
    private String ownerId;
}

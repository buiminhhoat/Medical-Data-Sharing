package com.medicaldatasharing.form;

import lombok.*;

import java.util.Date;

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

    private String ownerId;
}

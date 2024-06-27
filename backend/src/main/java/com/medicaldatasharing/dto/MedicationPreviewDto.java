package com.medicaldatasharing.dto;

import lombok.*;

@Getter
@Setter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class MedicationPreviewDto {
    private String medicationId;

    private String manufacturerId;

    private String medicationName;

    private String description;

    private String dateModified;
}

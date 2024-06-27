package com.medicaldatasharing.form;

import com.owlike.genson.annotation.JsonProperty;
import lombok.*;

import java.util.Date;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SearchMedicationForm {
    private String medicationId;

    private String manufacturerId;

    private String medicationName;

    private String description;

    private Date from;

    private Date until;

    private String sortingOrder;
}

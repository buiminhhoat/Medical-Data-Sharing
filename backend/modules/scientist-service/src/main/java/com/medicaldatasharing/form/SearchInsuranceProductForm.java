package com.medicaldatasharing.form;

import lombok.*;

import java.util.Date;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SearchInsuranceProductForm {
    private String insuranceProductId;

    private String insuranceProductName;

    private String insuranceCompanyId;

    private String dateCreated;
    private String dateModified;

    private String description;

    private String hashFile;

    private Date from;

    private Date until;

    private String sortingOrder;
}

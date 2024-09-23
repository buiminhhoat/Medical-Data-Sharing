package com.medicaldatasharing.form;

import lombok.*;

import java.util.Date;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SearchInsuranceContractForm {
    private String insuranceContractId;
    private String insuranceProductId;
    private String patientId;
    private String insuranceCompanyId;
    private String startDate;
    private String endDate;
    private String hashFile;
    private String sortingOrder;
    private Date from;
    private Date until;
}


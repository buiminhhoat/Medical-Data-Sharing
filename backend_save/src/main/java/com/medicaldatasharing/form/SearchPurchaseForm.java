package com.medicaldatasharing.form;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SearchPurchaseForm {
    private String purchaseId;

    private String prescriptionId;

    private String patientId;

    private String drugStoreId;
}

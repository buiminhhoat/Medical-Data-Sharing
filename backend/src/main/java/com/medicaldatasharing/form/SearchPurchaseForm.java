package com.medicaldatasharing.form;

import com.owlike.genson.annotation.JsonProperty;
import lombok.*;

import java.util.Date;

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

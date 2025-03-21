package org.medicaldatasharing.dto;

import com.owlike.genson.annotation.JsonProperty;
import lombok.*;

import java.util.List;

@Getter
@Setter
@Builder
@ToString
@AllArgsConstructor
public class MedicationPurchaseDto {
    @JsonProperty("medicationId")
    private String medicationId;

    @JsonProperty("prescriptionDetailId")
    private String prescriptionDetailId;

    @JsonProperty("drugIdList")
    private List<String> drugIdList;

    public MedicationPurchaseDto() {

    }
}

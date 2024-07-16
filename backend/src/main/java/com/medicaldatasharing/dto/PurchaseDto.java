package com.medicaldatasharing.dto;

import com.owlike.genson.annotation.JsonProperty;
import lombok.*;
import org.json.JSONObject;

import java.lang.reflect.Field;
import java.util.List;

@Getter
@Setter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class PurchaseDto {
    @JsonProperty("purchaseId")
    private String purchaseId;
    @JsonProperty("prescriptionId")
    private String prescriptionId;

    @JsonProperty("medicationPurchaseList")
    private String medicationPurchaseList;

    @JsonProperty("patientId")
    private String patientId;

    @JsonProperty("drugStoreId")
    private String drugStoreId;

    @JsonProperty("dateCreated")
    private String dateCreated;

    @JsonProperty("dateModified")
    private String dateModified;

    @JsonProperty("purchaseDetailsDtoList")
    private List<PurchaseDetailsDto> purchaseDetailsDtoList;

    public JSONObject toJSONObject() {
        JSONObject jsonObj = new JSONObject();
        Field[] fields = this.getClass().getDeclaredFields();

        for (Field field : fields) {
            field.setAccessible(true);
            try {
                jsonObj.put(field.getName(), field.get(this));
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
        return jsonObj;
    }
}

package com.medicaldatasharing.form;

import org.json.JSONObject;

import javax.validation.constraints.NotBlank;
import java.lang.reflect.Field;

public class GetPrescriptionForm {
    @NotBlank
    private String prescriptionId;

    @NotBlank
    private String drugStoreId;

    public @NotBlank String getPrescriptionId() {
        return prescriptionId;
    }

    public GetPrescriptionForm setPrescriptionId(@NotBlank String prescriptionId) {
        this.prescriptionId = prescriptionId;
        return this;
    }

    public @NotBlank String getDrugStoreId() {
        return drugStoreId;
    }

    public GetPrescriptionForm setDrugStoreId(@NotBlank String drugStoreId) {
        this.drugStoreId = drugStoreId;
        return this;
    }

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

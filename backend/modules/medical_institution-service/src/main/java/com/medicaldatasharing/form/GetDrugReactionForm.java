package com.medicaldatasharing.form;

import org.json.JSONObject;

import javax.validation.constraints.NotBlank;
import java.lang.reflect.Field;

public class GetDrugReactionForm {
    @NotBlank
    private String manufacturerId;

    public @NotBlank String getManufacturerId() {
        return manufacturerId;
    }

    public GetDrugReactionForm setManufacturerId(@NotBlank String manufacturerId) {
        this.manufacturerId = manufacturerId;
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

package com.medicaldatasharing.form;

import lombok.*;
import org.json.JSONObject;

import javax.validation.constraints.NotBlank;
import java.lang.reflect.Field;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EditInsuranceProductForm {
    @NotBlank
    String insuranceProductId;

    @NotBlank
    String insuranceProductName;

    @NotBlank
    String insuranceCompanyId;

    @NotBlank
    String dateModified;
    
    @NotBlank
    String description;

    @NotBlank
    String hashFile;


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

package com.medicaldatasharing.form;

import lombok.*;
import org.checkerframework.checker.units.qual.N;
import org.json.JSONObject;

import javax.validation.constraints.NotBlank;
import java.lang.reflect.Field;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EditMedicationForm {
    @NotBlank
    String medicationId;

    @NotBlank
    String manufacturerId;

    @NotBlank
    String medicationName;

    @NotBlank
    String dateCreated;

    @NotBlank
    String dateModified;
    
    @NotBlank
    String description;


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

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
public class SendViewPrescriptionRequestForm {
    @NotBlank
    private String senderId;

    @NotBlank
    private String recipientId;

    @NotBlank
    private String dateCreated;

    @NotBlank
    private String dateModified;

    @NotBlank
    private String prescriptionId;

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


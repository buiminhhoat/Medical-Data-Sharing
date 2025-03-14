package org.medicaldatasharing.form;

import lombok.*;
import org.json.JSONObject;

import java.lang.reflect.Field;


@Getter
@Setter
@Builder
@AllArgsConstructor
public class DefineRequestForm {
    private String requestId;
    private String requestStatus;
    private String requestType;

    public DefineRequestForm() {

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


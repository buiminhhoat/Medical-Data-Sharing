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
public class DefineConfirmPaymentRequestForm {
    @NotBlank
    private String requestId;

    @NotBlank
    private String requestStatus;

    @NotBlank
    private String dateCreated;

    @NotBlank
    private String dateModified;

    public DefineConfirmPaymentRequestForm(DefineRequestForm defineRequestForm) {
        this.requestId = defineRequestForm.getRequestId();
        this.requestStatus = defineRequestForm.getRequestStatus();
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


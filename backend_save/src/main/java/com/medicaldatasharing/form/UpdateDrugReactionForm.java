package com.medicaldatasharing.form;

import com.medicaldatasharing.util.AESUtil;
import lombok.*;
import org.json.JSONObject;

import javax.validation.constraints.NotBlank;
import java.lang.reflect.Field;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UpdateDrugReactionForm {
    @NotBlank
    String prescriptionId;

    String drugReaction;

    String dateCreated;

    String dateModified;

    public void encrypt() throws Exception {
        this.drugReaction = AESUtil.encrypt(this.drugReaction);
    }

    public void decrypt() throws Exception {
        this.drugReaction = AESUtil.decrypt(this.drugReaction);
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


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
public class AddMedicationForm {
    @NotBlank
    String manufacturerId;

    @NotBlank
    String medicationName;

    @NotBlank
    String description;

    @NotBlank
    String dateCreated;

    @NotBlank
    String dateModified;

    String hashFile;

    public void encrypt() throws Exception {
        this.medicationName = AESUtil.encrypt(this.medicationName);
        this.description = AESUtil.encrypt(this.description);
        this.hashFile = AESUtil.encrypt(this.hashFile);
    }

    public void decrypt() throws Exception {
        this.medicationName = AESUtil.decrypt(this.medicationName);
        this.description = AESUtil.decrypt(this.description);
        this.hashFile = AESUtil.decrypt(this.hashFile);
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

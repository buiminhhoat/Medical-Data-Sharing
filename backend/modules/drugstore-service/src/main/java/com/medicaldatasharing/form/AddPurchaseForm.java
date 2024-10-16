package com.medicaldatasharing.form;

import lombok.*;
import org.json.JSONObject;
import org.springframework.web.bind.annotation.RequestParam;

import javax.validation.constraints.NotBlank;
import java.lang.reflect.Field;

@Getter
@Setter
@Builder
@AllArgsConstructor
public class AddPurchaseForm {
    @NotBlank
    String sellingPrescriptionDrug;
    @NotBlank
    String prescriptionId;
    @NotBlank
    String patientId;

    public AddPurchaseForm() {

    }

    public @NotBlank String getSellingPrescriptionDrug() {
        return sellingPrescriptionDrug;
    }

    public void setSellingPrescriptionDrug(@NotBlank String sellingPrescriptionDrug) {
        this.sellingPrescriptionDrug = sellingPrescriptionDrug;
    }

    public @NotBlank String getPrescriptionId() {
        return prescriptionId;
    }

    public void setPrescriptionId(@NotBlank String prescriptionId) {
        this.prescriptionId = prescriptionId;
    }

    public @NotBlank String getPatientId() {
        return patientId;
    }

    public void setPatientId(@NotBlank String patientId) {
        this.patientId = patientId;
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

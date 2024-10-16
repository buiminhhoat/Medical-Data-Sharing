package org.medicaldatasharing.form;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.json.JSONObject;

import java.lang.reflect.Field;

@Getter
@Setter
@Builder
@AllArgsConstructor
public class AddPurchaseForm {
    String sellingPrescriptionDrug;
    String prescriptionId;
    String patientId;

    public AddPurchaseForm() {

    }

    public String getSellingPrescriptionDrug() {
        return sellingPrescriptionDrug;
    }

    public void setSellingPrescriptionDrug(String sellingPrescriptionDrug) {
        this.sellingPrescriptionDrug = sellingPrescriptionDrug;
    }

    public String getPrescriptionId() {
        return prescriptionId;
    }

    public void setPrescriptionId(String prescriptionId) {
        this.prescriptionId = prescriptionId;
    }

    public String getPatientId() {
        return patientId;
    }

    public void setPatientId(String patientId) {
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

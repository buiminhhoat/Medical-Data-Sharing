package com.medicaldatasharing.form;

import org.json.JSONObject;

import javax.validation.constraints.NotBlank;
import java.lang.reflect.Field;

public class GetPrescriptionForm {
    @NotBlank
    private String prescriptionId;

    private String patientId;

    private String drugStoreId;

    private String manufacturerId;

    private String scientistId;

    public @NotBlank String getPrescriptionId() {
        return prescriptionId;
    }

    public GetPrescriptionForm setPrescriptionId(@NotBlank String prescriptionId) {
        this.prescriptionId = prescriptionId;
        return this;
    }

    public String getPatientId() {
        return patientId;
    }

    public GetPrescriptionForm setPatientId(String patientId) {
        this.patientId = patientId;
        return this;
    }

    public String getDrugStoreId() {
        return drugStoreId;
    }

    public GetPrescriptionForm setDrugStoreId(String drugStoreId) {
        this.drugStoreId = drugStoreId;
        return this;
    }

    public String getManufacturerId() {
        return manufacturerId;
    }

    public GetPrescriptionForm setManufacturerId(String manufacturerId) {
        this.manufacturerId = manufacturerId;
        return this;
    }

    public String getScientistId() {
        return scientistId;
    }

    public GetPrescriptionForm setScientistId(String scientistId) {
        this.scientistId = scientistId;
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

package com.medicaldatasharing.chaincode.dto;

import com.owlike.genson.Genson;
import com.owlike.genson.annotation.JsonProperty;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import static java.nio.charset.StandardCharsets.UTF_8;

public class Medication {
    @JsonProperty("medicationId")
    private String medicationId;

    @JsonProperty("manufacturerId")
    private String manufacturerId;

    @JsonProperty("medicationName")
    private String medicationName;

    @JsonProperty("description")
    private String description;

    @JsonProperty("dateModified")
    private String dateModified;

    @JsonProperty("entityName")
    private String entityName;

    public Medication() {
        this.entityName = Medication.class.getSimpleName();
    }

    public String getMedicationId() {
        return medicationId;
    }

    public Medication setMedicationId(String medicationId) {
        this.medicationId = medicationId;
        return this;
    }

    public String getManufacturerId() {
        return manufacturerId;
    }

    public Medication setManufacturerId(String manufacturerId) {
        this.manufacturerId = manufacturerId;
        return this;
    }

    public String getMedicationName() {
        return medicationName;
    }

    public Medication setMedicationName(String medicationName) {
        this.medicationName = medicationName;
        return this;
    }

    public String getDescription() {
        return description;
    }

    public Medication setDescription(String description) {
        this.description = description;
        return this;
    }

    public static byte[] serialize(Object object) {
        Genson genson = new Genson();
        return genson.serializeBytes(object);
    }

    public static Medication deserialize(byte[] data) {
        Genson genson = new Genson();
        return genson.deserialize(data, Medication.class);
    }

    public String getDateModified() {
        return dateModified;
    }

    public Medication setDateModified(String dateModified) {
        this.dateModified = dateModified;
        return this;
    }

    public String getEntityName() {
        return entityName;
    }

    public Medication setEntityName(String entityName) {
        this.entityName = entityName;
        return this;
    }

    public static Medication createInstance(String medicationId,
                                            String manufacturerId,
                                            String medicationName,
                                            String description,
                                            String dateModified) {
        Medication medication = new Medication();
        medication.setMedicationId(medicationId);
        medication.setManufacturerId(manufacturerId);
        medication.setMedicationName(medicationName);
        medication.setDescription(description);
        medication.setDateModified(dateModified);
        medication.setEntityName(Medication.class.getSimpleName());
        return medication;
    }
}

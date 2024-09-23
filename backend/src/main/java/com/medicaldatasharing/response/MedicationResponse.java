package com.medicaldatasharing.response;

import com.medicaldatasharing.chaincode.dto.Medication;
import com.owlike.genson.annotation.JsonProperty;

import java.util.List;

public class MedicationResponse {
    @JsonProperty("medicationId")
    private String medicationId;

    @JsonProperty("manufacturerId")
    private String manufacturerId;

    @JsonProperty("medicationName")
    private String medicationName;

    @JsonProperty("description")
    private String description;

    @JsonProperty("dateCreated")
    private String dateCreated;

    @JsonProperty("dateModified")
    private String dateModified;

    public MedicationResponse(Medication medication) {
        this.medicationId = medication.getMedicationId();
        this.manufacturerId = medication.getManufacturerId();
        this.medicationName = medication.getMedicationName();
        this.description = medication.getDescription();
        this.dateCreated = medication.getDateCreated();
        this.dateModified = medication.getDateModified();
    }

    public String getMedicationId() {
        return medicationId;
    }

    public MedicationResponse setMedicationId(String medicationId) {
        this.medicationId = medicationId;
        return this;
    }

    public String getManufacturerId() {
        return manufacturerId;
    }

    public MedicationResponse setManufacturerId(String manufacturerId) {
        this.manufacturerId = manufacturerId;
        return this;
    }

    public String getMedicationName() {
        return medicationName;
    }

    public MedicationResponse setMedicationName(String medicationName) {
        this.medicationName = medicationName;
        return this;
    }

    public String getDescription() {
        return description;
    }

    public MedicationResponse setDescription(String description) {
        this.description = description;
        return this;
    }

    public String getDateCreated() {
        return dateCreated;
    }

    public MedicationResponse setDateCreated(String dateCreated) {
        this.dateCreated = dateCreated;
        return this;
    }

    public String getDateModified() {
        return dateModified;
    }

    public MedicationResponse setDateModified(String dateModified) {
        this.dateModified = dateModified;
        return this;
    }
}

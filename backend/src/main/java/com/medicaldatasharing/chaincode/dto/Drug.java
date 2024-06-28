package com.medicaldatasharing.chaincode.dto;

import com.owlike.genson.annotation.JsonProperty;

public class Drug {
    @JsonProperty("drugId")
    private String drugId;

    @JsonProperty("medicationId")
    private String medicationId;

    @JsonProperty("manufactureDate")
    private String manufactureDate;

    @JsonProperty("expirationDate")
    private String expirationDate;

    @JsonProperty("ownerId")
    private String ownerId;

    @JsonProperty("entityName")
    private String entityName;

    public String getDrugId() {
        return drugId;
    }

    public Drug setDrugId(String drugId) {
        this.drugId = drugId;
        return this;
    }

    public String getMedicationId() {
        return medicationId;
    }

    public Drug setMedicationId(String medicationId) {
        this.medicationId = medicationId;
        return this;
    }

    public String getManufactureDate() {
        return manufactureDate;
    }

    public Drug setManufactureDate(String manufactureDate) {
        this.manufactureDate = manufactureDate;
        return this;
    }

    public String getExpirationDate() {
        return expirationDate;
    }

    public Drug setExpirationDate(String expirationDate) {
        this.expirationDate = expirationDate;
        return this;
    }

    public String getOwnerId() {
        return ownerId;
    }

    public Drug setOwnerId(String ownerId) {
        this.ownerId = ownerId;
        return this;
    }

    public String getEntityName() {
        return entityName;
    }

    public Drug setEntityName(String entityName) {
        this.entityName = entityName;
        return this;
    }
}

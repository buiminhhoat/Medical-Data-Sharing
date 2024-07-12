package com.medicaldatasharing.response;

import com.medicaldatasharing.chaincode.dto.Medication;
import com.owlike.genson.annotation.JsonProperty;

import java.util.List;

public class ManufacturerResponse {
    @JsonProperty("manufacturerId")
    private String manufacturerId;

    @JsonProperty("manufacturerName")
    private String manufacturerName;

    @JsonProperty("medicationList")
    private List<Medication> medicationList;

    public String getManufacturerId() {
        return manufacturerId;
    }

    public ManufacturerResponse setManufacturerId(String manufacturerId) {
        this.manufacturerId = manufacturerId;
        return this;
    }

    public String getManufacturerName() {
        return manufacturerName;
    }

    public ManufacturerResponse setManufacturerName(String manufacturerName) {
        this.manufacturerName = manufacturerName;
        return this;
    }

    public List<Medication> getMedicationList() {
        return medicationList;
    }

    public ManufacturerResponse setMedicationList(List<Medication> medicationList) {
        this.medicationList = medicationList;
        return this;
    }
}

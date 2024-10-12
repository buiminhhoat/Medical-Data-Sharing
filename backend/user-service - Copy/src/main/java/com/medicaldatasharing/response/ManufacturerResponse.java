package com.medicaldatasharing.response;

import com.medicaldatasharing.chaincode.dto.Medication;
import com.medicaldatasharing.model.Manufacturer;
import com.owlike.genson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class ManufacturerResponse extends UserResponse {
    @JsonProperty("manufacturerId")
    private String manufacturerId;

    @JsonProperty("manufacturerName")
    private String manufacturerName;

    @JsonProperty("businessLicenseNumber")
    private String businessLicenseNumber;

    @JsonProperty("medicationList")
    private List<Medication> medicationList;


    public ManufacturerResponse() {

    }

    public ManufacturerResponse(Manufacturer manufacturer) {
        this.id = manufacturer.getId();
        this.manufacturerId = manufacturer.getId();
        this.email = manufacturer.getEmail();
        this.fullName = manufacturer.getFullName();
        this.manufacturerName = manufacturer.getFullName();
        this.avatar = manufacturer.getAvatar();
        this.role = manufacturer.getRole();
        this.address = manufacturer.getAddress();
        this.enabled = String.valueOf(manufacturer.isEnabled());
        this.businessLicenseNumber = manufacturer.getBusinessLicenseNumber();
    }

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

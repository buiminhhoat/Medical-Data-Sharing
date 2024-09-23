package com.medicaldatasharing.dto;

import com.medicaldatasharing.chaincode.dto.PrescriptionDetails;
import com.owlike.genson.Genson;
import com.owlike.genson.annotation.JsonProperty;
import lombok.*;

import java.util.List;

@Getter
@Setter
@Builder
@ToString
@AllArgsConstructor
public class PrescriptionDetailsDto {
    @JsonProperty("prescriptionDetailId")
    private String prescriptionDetailId;

    @JsonProperty("prescriptionId")
    private String prescriptionId;

    @JsonProperty("medicationId")
    private String medicationId;

    @JsonProperty("medicationName")
    private String medicationName;

    @JsonProperty("quantity")
    private String quantity;

    @JsonProperty("purchasedQuantity")
    private String purchasedQuantity;

    @JsonProperty("details")
    private String details;

    @JsonProperty("entityName")
    private String entityName;

    public PrescriptionDetailsDto() {
        this.purchasedQuantity = "0";
        this.entityName = PrescriptionDetails.class.getSimpleName();
    }

    public String getPrescriptionDetailId() {
        return prescriptionDetailId;
    }

    public PrescriptionDetailsDto setPrescriptionDetailId(String prescriptionDetailId) {
        this.prescriptionDetailId = prescriptionDetailId;
        return this;
    }

    public String getPrescriptionId() {
        return prescriptionId;
    }

    public PrescriptionDetailsDto setPrescriptionId(String prescriptionId) {
        this.prescriptionId = prescriptionId;
        return this;
    }

    public String getMedicationId() {
        return medicationId;
    }

    public PrescriptionDetailsDto setMedicationId(String medicationId) {
        this.medicationId = medicationId;
        return this;
    }

    public String getMedicationName() {
        return medicationName;
    }

    public PrescriptionDetailsDto setMedicationName(String medicationName) {
        this.medicationName = medicationName;
        return this;
    }

    public String getQuantity() {
        return quantity;
    }

    public PrescriptionDetailsDto setQuantity(String quantity) {
        this.quantity = quantity;
        return this;
    }

    public String getPurchasedQuantity() {
        return purchasedQuantity;
    }

    public PrescriptionDetailsDto setPurchasedQuantity(String purchasedQuantity) {
        this.purchasedQuantity = purchasedQuantity;
        return this;
    }

    public String getDetails() {
        return details;
    }

    public PrescriptionDetailsDto setDetails(String details) {
        this.details = details;
        return this;
    }

    public String getEntityName() {
        return entityName;
    }

    public PrescriptionDetailsDto setEntityName(String entityName) {
        this.entityName = entityName;
        return this;
    }

    public static byte[] serialize(Object object) {
        Genson genson = new Genson();
        return genson.serializeBytes(object);
    }

    public static PrescriptionDetailsDto deserialize(byte[] data) {
        Genson genson = new Genson();
        return genson.deserialize(data, PrescriptionDetailsDto.class);
    }
}

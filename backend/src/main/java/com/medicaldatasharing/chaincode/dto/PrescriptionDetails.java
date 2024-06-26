package com.medicaldatasharing.chaincode.dto;

import com.owlike.genson.annotation.JsonProperty;

public class PrescriptionDetails {
    @JsonProperty("prescriptionDetailId")
    private String prescriptionDetailId;

    @JsonProperty("prescriptionId")
    private String prescriptionId;

    @JsonProperty("medicationId")
    private String medicationId;

    @JsonProperty("quantity")
    private String quantity;

    @JsonProperty("details")
    private String details;

    @JsonProperty("entityName")
    private String entityName;

    public PrescriptionDetails() {
        this.entityName = PrescriptionDetails.class.getSimpleName();
    }

    public PrescriptionDetails(String prescriptionDetailId,
                               String prescriptionId,
                               String medicationId,
                               String quantity,
                               String details) {
        this.prescriptionDetailId = prescriptionDetailId;
        this.prescriptionId = prescriptionId;
        this.medicationId = medicationId;
        this.quantity = quantity;
        this.details = details;
    }

    public String getPrescriptionDetailId() {
        return prescriptionDetailId;
    }

    public PrescriptionDetails setPrescriptionDetailId(String prescriptionDetailId) {
        this.prescriptionDetailId = prescriptionDetailId;
        return this;
    }

    public String getPrescriptionId() {
        return prescriptionId;
    }

    public PrescriptionDetails setPrescriptionId(String prescriptionId) {
        this.prescriptionId = prescriptionId;
        return this;
    }

    public String getMedicationId() {
        return medicationId;
    }

    public PrescriptionDetails setMedicationId(String medicationId) {
        this.medicationId = medicationId;
        return this;
    }

    public String getQuantity() {
        return quantity;
    }

    public PrescriptionDetails setQuantity(String quantity) {
        this.quantity = quantity;
        return this;
    }

    public String getDetails() {
        return details;
    }

    public PrescriptionDetails setDetails(String details) {
        this.details = details;
        return this;
    }
}

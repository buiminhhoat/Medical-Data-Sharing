package org.medicaldatasharing.dto;

import com.owlike.genson.annotation.JsonProperty;

import java.util.Objects;

public class PrescriptionDetails {
    @JsonProperty("prescriptionDetailId")
    private String prescriptionDetailId;

    @JsonProperty("prescriptionId")
    private String prescriptionId;

    @JsonProperty("medicationId")
    private String medicationId;

    @JsonProperty("quantity")
    private String quantity;

    @JsonProperty("purchasedQuantity")
    private String purchasedQuantity;

    @JsonProperty("details")
    private String details;

    @JsonProperty("entityName")
    private String entityName;

    public PrescriptionDetails() {
        this.purchasedQuantity = "0";
        this.entityName = PrescriptionDetails.class.getSimpleName();
    }

    public PrescriptionDetails(String prescriptionDetailId,
                               String prescriptionId,
                               String medicationId,
                               String quantity,
                               String details) {
        super();
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

    public String getEntityName() {
        return entityName;
    }

    public PrescriptionDetails setEntityName(String entityName) {
        this.entityName = entityName;
        return this;
    }

    public String getPurchasedQuantity() {
        return purchasedQuantity;
    }

    public PrescriptionDetails setPurchasedQuantity(String purchasedQuantity) {
        this.purchasedQuantity = purchasedQuantity;
        return this;
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) return true;
        if (object == null || getClass() != object.getClass()) return false;
        PrescriptionDetails that = (PrescriptionDetails) object;
        return Objects.equals(prescriptionDetailId, that.prescriptionDetailId) && Objects.equals(prescriptionId, that.prescriptionId) && Objects.equals(medicationId, that.medicationId) && Objects.equals(quantity, that.quantity) && Objects.equals(purchasedQuantity, that.purchasedQuantity) && Objects.equals(details, that.details) && Objects.equals(entityName, that.entityName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(prescriptionDetailId, prescriptionId, medicationId, quantity, purchasedQuantity, details, entityName);
    }
}

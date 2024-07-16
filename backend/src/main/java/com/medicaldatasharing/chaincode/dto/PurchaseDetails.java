package com.medicaldatasharing.chaincode.dto;

import com.owlike.genson.annotation.JsonProperty;

public class PurchaseDetails {
    @JsonProperty("purchaseDetailId")
    private String purchaseDetailId;

    @JsonProperty("prescriptionDetailId")
    private String prescriptionDetailId;

    @JsonProperty("medicationId")
    private String medicationId;

    @JsonProperty("drugId")
    private String drugId;

    @JsonProperty("entityName")
    private String entityName;

    public String getPurchaseDetailId() {
        return purchaseDetailId;
    }

    public PurchaseDetails setPurchaseDetailId(String purchaseDetailId) {
        this.purchaseDetailId = purchaseDetailId;
        return this;
    }

    public String getPrescriptionDetailId() {
        return prescriptionDetailId;
    }

    public PurchaseDetails setPrescriptionDetailId(String prescriptionDetailId) {
        this.prescriptionDetailId = prescriptionDetailId;
        return this;
    }

    public String getMedicationId() {
        return medicationId;
    }

    public PurchaseDetails setMedicationId(String medicationId) {
        this.medicationId = medicationId;
        return this;
    }

    public String getDrugId() {
        return drugId;
    }

    public PurchaseDetails setDrugId(String drugId) {
        this.drugId = drugId;
        return this;
    }

    public String getEntityName() {
        return entityName;
    }

    public PurchaseDetails setEntityName(String entityName) {
        this.entityName = entityName;
        return this;
    }
}

package com.medicaldatasharing.chaincode.dto;

import com.owlike.genson.annotation.JsonProperty;

public class Prescription {
    @JsonProperty("prescriptionId")
    private String prescriptionId;

    @JsonProperty("drugReaction")
    private String drugReaction;

    @JsonProperty("entityName")
    private String entityName;

    public Prescription() {
        this.entityName = Prescription.class.getSimpleName();
    }

    public String getPrescriptionId() {
        return prescriptionId;
    }

    public Prescription setPrescriptionId(String prescriptionId) {
        this.prescriptionId = prescriptionId;
        return this;
    }

    public String getDrugReaction() {
        return drugReaction;
    }

    public Prescription setDrugReaction(String drugReaction) {
        this.drugReaction = drugReaction;
        return this;
    }
}

package com.medicaldatasharing.dto;

import com.medicaldatasharing.chaincode.dto.PrescriptionDetails;
import com.owlike.genson.annotation.JsonProperty;
import lombok.*;

import java.util.List;

@Getter
@Setter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class PrescriptionDto {
    @JsonProperty("prescriptionId")
    private String prescriptionId;

    @JsonProperty("drugReaction")
    private String drugReaction;

    @JsonProperty("entityName")
    private String entityName;

    @JsonProperty("prescriptionDetailsList")
    List<PrescriptionDetails> prescriptionDetailsList;

    public String getPrescriptionId() {
        return prescriptionId;
    }

    public PrescriptionDto setPrescriptionId(String prescriptionId) {
        this.prescriptionId = prescriptionId;
        return this;
    }

    public String getDrugReaction() {
        return drugReaction;
    }

    public PrescriptionDto setDrugReaction(String drugReaction) {
        this.drugReaction = drugReaction;
        return this;
    }

    public String getEntityName() {
        return entityName;
    }

    public PrescriptionDto setEntityName(String entityName) {
        this.entityName = entityName;
        return this;
    }

    public List<PrescriptionDetails> getPrescriptionDetailsList() {
        return prescriptionDetailsList;
    }

    public PrescriptionDto setPrescriptionDetailsList(List<PrescriptionDetails> prescriptionDetailsList) {
        this.prescriptionDetailsList = prescriptionDetailsList;
        return this;
    }

    public PrescriptionDto addPrescriptionDetailsToPrescriptionDetailsList(PrescriptionDetails prescriptionDetails) {
        prescriptionDetailsList.add(prescriptionDetails);
        return this;
    }
}

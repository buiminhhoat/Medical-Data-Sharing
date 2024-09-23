package com.medicaldatasharing.dto;

import com.medicaldatasharing.chaincode.dto.PurchaseDetails;
import com.owlike.genson.Genson;
import com.owlike.genson.annotation.JsonProperty;
import lombok.*;
import org.json.JSONObject;

import java.lang.reflect.Field;
import java.util.List;

@Getter
@Setter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class PurchaseDetailsDto {
    @JsonProperty("purchaseDetailId")
    private String purchaseDetailId;

    @JsonProperty("purchaseId")
    private String purchaseId;

    @JsonProperty("prescriptionDetailId")
    private String prescriptionDetailId;

    @JsonProperty("medicationId")
    private String medicationId;

    @JsonProperty("drugId")
    private String drugId;

    @JsonProperty("entityName")
    private String entityName;

    public PurchaseDetailsDto(PurchaseDetails purchaseDetails) {
        this.purchaseDetailId = purchaseDetails.getPurchaseDetailId();
        this.purchaseId = purchaseDetails.getPurchaseId();
        this.prescriptionDetailId = purchaseDetails.getPrescriptionDetailId();
        this.medicationId = purchaseDetails.getMedicationId();
        this.drugId = purchaseDetails.getDrugId();
        this.entityName = purchaseDetails.getEntityName();
    }

    public String getPurchaseDetailId() {
        return purchaseDetailId;
    }

    public PurchaseDetailsDto setPurchaseDetailId(String purchaseDetailId) {
        this.purchaseDetailId = purchaseDetailId;
        return this;
    }

    public String getPrescriptionDetailId() {
        return prescriptionDetailId;
    }

    public PurchaseDetailsDto setPrescriptionDetailId(String prescriptionDetailId) {
        this.prescriptionDetailId = prescriptionDetailId;
        return this;
    }

    public String getMedicationId() {
        return medicationId;
    }

    public PurchaseDetailsDto setMedicationId(String medicationId) {
        this.medicationId = medicationId;
        return this;
    }

    public String getDrugId() {
        return drugId;
    }

    public PurchaseDetailsDto setDrugId(String drugId) {
        this.drugId = drugId;
        return this;
    }

    public String getEntityName() {
        return entityName;
    }

    public PurchaseDetailsDto setEntityName(String entityName) {
        this.entityName = entityName;
        return this;
    }

    public static byte[] serialize(Object object) {
        Genson genson = new Genson();
        return genson.serializeBytes(object);
    }

    public static PurchaseDetailsDto deserialize(byte[] data) {
        Genson genson = new Genson();
        return genson.deserialize(data, PurchaseDetailsDto.class);
    }
}

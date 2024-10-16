package org.medicaldatasharing.dto;

import com.owlike.genson.annotation.JsonProperty;

public class Purchase {
    @JsonProperty("purchaseId")
    private String purchaseId;

    @JsonProperty("prescriptionId")
    private String prescriptionId;

    @JsonProperty("patientId")
    private String patientId;

    @JsonProperty("drugStoreId")
    private String drugStoreId;

    @JsonProperty("dateCreated")
    private String dateCreated;

    @JsonProperty("dateModified")
    private String dateModified;

    @JsonProperty("entityName")
    private String entityName;

    public Purchase() {

    }

    public String getPurchaseId() {
        return purchaseId;
    }

    public Purchase setPurchaseId(String purchaseId) {
        this.purchaseId = purchaseId;
        return this;
    }

    public String getPrescriptionId() {
        return prescriptionId;
    }

    public Purchase setPrescriptionId(String prescriptionId) {
        this.prescriptionId = prescriptionId;
        return this;
    }

    public String getPatientId() {
        return patientId;
    }

    public Purchase setPatientId(String patientId) {
        this.patientId = patientId;
        return this;
    }

    public String getDrugStoreId() {
        return drugStoreId;
    }

    public Purchase setDrugStoreId(String drugStoreId) {
        this.drugStoreId = drugStoreId;
        return this;
    }

    public String getDateModified() {
        return dateModified;
    }

    public Purchase setDateModified(String dateModified) {
        this.dateModified = dateModified;
        return this;
    }

    public String getEntityName() {
        return entityName;
    }

    public Purchase setEntityName(String entityName) {
        this.entityName = entityName;
        return this;
    }

    public String getDateCreated() {
        return dateCreated;
    }

    public Purchase setDateCreated(String dateCreated) {
        this.dateCreated = dateCreated;
        return this;
    }
}

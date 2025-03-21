package com.medicaldatasharing.response;

import com.medicaldatasharing.chaincode.dto.Purchase;
import com.medicaldatasharing.dto.PurchaseDetailsDto;
import com.medicaldatasharing.dto.PurchaseDto;
import com.owlike.genson.annotation.JsonProperty;

import java.util.List;

public class PurchaseResponse {
    @JsonProperty("purchaseId")
    private String purchaseId;

    @JsonProperty("prescriptionId")
    private String prescriptionId;

    @JsonProperty("patientId")
    private String patientId;

    @JsonProperty("patientName")
    private String patientName;

    @JsonProperty("drugStoreId")
    private String drugStoreId;

    @JsonProperty("drugStoreName")
    private String drugStoreName;

    @JsonProperty("dateCreated")
    private String dateCreated;

    @JsonProperty("dateModified")
    private String dateModified;

    @JsonProperty("purchaseDetailsList")
    private List<PurchaseDetailsDto> purchaseDetailsList;

    public PurchaseResponse(Purchase purchase) {
        this.purchaseId = purchase.getPurchaseId();
        this.prescriptionId = purchase.getPrescriptionId();
        this.patientId = purchase.getPatientId();
        this.drugStoreId = purchase.getDrugStoreId();
        this.dateCreated = purchase.getDateCreated();
        this.dateModified = purchase.getDateModified();
    }

    public PurchaseResponse(PurchaseDto purchaseDto) {
        this.purchaseId = purchaseDto.getPurchaseId();
        this.prescriptionId = purchaseDto.getPrescriptionId();
        this.patientId = purchaseDto.getPatientId();
        this.drugStoreId = purchaseDto.getDrugStoreId();
        this.dateCreated = purchaseDto.getDateCreated();
        this.dateModified = purchaseDto.getDateModified();
        this.purchaseDetailsList = purchaseDto.getPurchaseDetailsDtoList();
    }

    public String getPurchaseId() {
        return purchaseId;
    }

    public PurchaseResponse setPurchaseId(String purchaseId) {
        this.purchaseId = purchaseId;
        return this;
    }

    public String getPrescriptionId() {
        return prescriptionId;
    }

    public PurchaseResponse setPrescriptionId(String prescriptionId) {
        this.prescriptionId = prescriptionId;
        return this;
    }

    public String getPatientId() {
        return patientId;
    }

    public PurchaseResponse setPatientId(String patientId) {
        this.patientId = patientId;
        return this;
    }

    public String getPatientName() {
        return patientName;
    }

    public PurchaseResponse setPatientName(String patientName) {
        this.patientName = patientName;
        return this;
    }

    public String getDrugStoreId() {
        return drugStoreId;
    }

    public PurchaseResponse setDrugStoreId(String drugStoreId) {
        this.drugStoreId = drugStoreId;
        return this;
    }

    public String getDrugStoreName() {
        return drugStoreName;
    }

    public PurchaseResponse setDrugStoreName(String drugStoreName) {
        this.drugStoreName = drugStoreName;
        return this;
    }

    public String getDateCreated() {
        return dateCreated;
    }

    public PurchaseResponse setDateCreated(String dateCreated) {
        this.dateCreated = dateCreated;
        return this;
    }

    public String getDateModified() {
        return dateModified;
    }

    public PurchaseResponse setDateModified(String dateModified) {
        this.dateModified = dateModified;
        return this;
    }

    public List<PurchaseDetailsDto> getPurchaseDetailsList() {
        return purchaseDetailsList;
    }

    public PurchaseResponse setPurchaseDetailsList(List<PurchaseDetailsDto> purchaseDetailsList) {
        this.purchaseDetailsList = purchaseDetailsList;
        return this;
    }
}

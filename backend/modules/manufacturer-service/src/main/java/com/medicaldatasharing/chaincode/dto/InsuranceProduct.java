package com.medicaldatasharing.chaincode.dto;

import com.owlike.genson.Genson;
import com.owlike.genson.annotation.JsonProperty;

import java.util.Objects;

public class InsuranceProduct {
    @JsonProperty("insuranceProductId")
    private String insuranceProductId;

    @JsonProperty("insuranceProductName")
    private String insuranceProductName;

    @JsonProperty("insuranceCompanyId")
    private String insuranceCompanyId;

    @JsonProperty("dateCreated")
    private String dateCreated;

    @JsonProperty("dateModified")
    private String dateModified;

    @JsonProperty("description")
    private String description;

    @JsonProperty("hashFile")
    private String hashFile;

    @JsonProperty("entityName")
    private String entityName;

    public InsuranceProduct() {
        this.entityName = InsuranceProduct.class.getSimpleName();
    }

    public InsuranceProduct(String insuranceProductId,
                            String insuranceProductName,
                            String insuranceCompanyId,
                            String dateCreated,
                            String dateModified,
                            String description,
                            String hashFile) {
        super();
        this.insuranceProductId = insuranceProductId;
        this.insuranceProductName = insuranceProductName;
        this.insuranceCompanyId = insuranceCompanyId;
        this.dateCreated = dateCreated;
        this.dateModified = dateModified;
        this.description = description;
        this.hashFile = hashFile;
    }

    public static InsuranceProduct createInstance(String insuranceProductId,
                                                  String insuranceProductName,
                                                  String insuranceCompanyId,
                                                  String dateCreated,
                                                  String dateModified,
                                                  String description,
                                                  String hashFile) {
        InsuranceProduct insuranceProduct = new InsuranceProduct();
        insuranceProduct.setInsuranceProductId(insuranceProductId);
        insuranceProduct.setInsuranceProductName(insuranceProductName);
        insuranceProduct.setInsuranceCompanyId(insuranceCompanyId);
        insuranceProduct.setDateCreated(dateCreated);
        insuranceProduct.setDateModified(dateModified);
        insuranceProduct.setDescription(description);
        insuranceProduct.setHashFile(hashFile);
        return insuranceProduct;
    }

    public String getInsuranceProductId() {
        return insuranceProductId;
    }

    public InsuranceProduct setInsuranceProductId(String insuranceProductId) {
        this.insuranceProductId = insuranceProductId;
        return this;
    }

    public String getInsuranceProductName() {
        return insuranceProductName;
    }

    public InsuranceProduct setInsuranceProductName(String insuranceProductName) {
        this.insuranceProductName = insuranceProductName;
        return this;
    }

    public String getInsuranceCompanyId() {
        return insuranceCompanyId;
    }

    public InsuranceProduct setInsuranceCompanyId(String insuranceCompanyId) {
        this.insuranceCompanyId = insuranceCompanyId;
        return this;
    }

    public String getDescription() {
        return description;
    }

    public InsuranceProduct setDescription(String description) {
        this.description = description;
        return this;
    }

    public static byte[] serialize(Object object) {
        Genson genson = new Genson();
        return genson.serializeBytes(object);
    }

    public static InsuranceProduct deserialize(byte[] data) {
        Genson genson = new Genson();
        return genson.deserialize(data, InsuranceProduct.class);
    }

    public String getEntityName() {
        return entityName;
    }

    public InsuranceProduct setEntityName(String entityName) {
        this.entityName = entityName;
        return this;
    }

    public String getDateCreated() {
        return dateCreated;
    }

    public InsuranceProduct setDateCreated(String dateCreated) {
        this.dateCreated = dateCreated;
        return this;
    }

    public String getDateModified() {
        return dateModified;
    }

    public InsuranceProduct setDateModified(String dateModified) {
        this.dateModified = dateModified;
        return this;
    }

    public String getHashFile() {
        return hashFile;
    }

    public InsuranceProduct setHashFile(String hashFile) {
        this.hashFile = hashFile;
        return this;
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) return true;
        if (object == null || getClass() != object.getClass()) return false;
        InsuranceProduct that = (InsuranceProduct) object;
        return Objects.equals(insuranceProductId, that.insuranceProductId) && Objects.equals(insuranceProductName, that.insuranceProductName) && Objects.equals(insuranceCompanyId, that.insuranceCompanyId) && Objects.equals(dateModified, that.dateModified) && Objects.equals(description, that.description) && Objects.equals(hashFile, that.hashFile) && Objects.equals(entityName, that.entityName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(insuranceProductId, insuranceProductName, insuranceCompanyId, dateModified, description, hashFile, entityName);
    }
}

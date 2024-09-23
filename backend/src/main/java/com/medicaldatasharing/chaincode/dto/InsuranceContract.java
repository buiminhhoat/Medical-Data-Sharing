package com.medicaldatasharing.chaincode.dto;

import com.owlike.genson.Genson;
import com.owlike.genson.annotation.JsonProperty;

import java.util.Objects;

public class InsuranceContract {
    @JsonProperty("insuranceContractId")
    private String insuranceContractId;

    @JsonProperty("insuranceProductId")
    private String insuranceProductId;

    @JsonProperty("patientId")
    private String patientId;

    @JsonProperty("insuranceCompanyId")
    private String insuranceCompanyId;

    @JsonProperty("startDate")
    private String startDate;

    @JsonProperty("endDate")
    private String endDate;

    @JsonProperty("dateCreated")
    private String dateCreated;

    @JsonProperty("dateModified")
    private String dateModified;

    @JsonProperty("hashFile")
    private String hashFile;

    @JsonProperty("entityName")
    private String entityName;

    public InsuranceContract() {
        this.entityName = InsuranceContract.class.getSimpleName();
    }

    public InsuranceContract(String insuranceContractId,
                             String patientId,
                             String insuranceCompanyId,
                             String startDate,
                             String endDate,
                             String dateCreated,
                             String dateModified,
                             String hashFile) {
        super();
        this.insuranceContractId = insuranceContractId;
        this.patientId = patientId;
        this.insuranceCompanyId = insuranceCompanyId;
        this.startDate = startDate;
        this.endDate = endDate;
        this.dateCreated = dateCreated;
        this.dateModified = dateModified;
        this.hashFile = hashFile;
    }

    public static InsuranceContract createInstance(String insuranceContractId,
                                                   String insuranceProductId,
                                                   String patientId,
                                                   String insuranceCompanyId,
                                                   String startDate,
                                                   String endDate,
                                                   String dateCreated,
                                                   String dateModified,
                                                   String hashFile) {
        InsuranceContract insuranceContract = new InsuranceContract();
        insuranceContract.setInsuranceProductId(insuranceContractId);
        insuranceContract.setInsuranceProductId(insuranceProductId);
        insuranceContract.setPatientId(patientId);
        insuranceContract.setInsuranceCompanyId(insuranceCompanyId);
        insuranceContract.setStartDate(startDate);
        insuranceContract.setEndDate(endDate);
        insuranceContract.setDateModified(dateModified);
        insuranceContract.setHashFile(hashFile);
        return insuranceContract;
    }


    public String getInsuranceContractId() {
        return insuranceContractId;
    }

    public InsuranceContract setInsuranceContractId(String insuranceContractId) {
        this.insuranceContractId = insuranceContractId;
        return this;
    }

    public String getInsuranceProductId() {
        return insuranceProductId;
    }

    public InsuranceContract setInsuranceProductId(String insuranceProductId) {
        this.insuranceProductId = insuranceProductId;
        return this;
    }

    public String getPatientId() {
        return patientId;
    }

    public InsuranceContract setPatientId(String patientId) {
        this.patientId = patientId;
        return this;
    }

    public String getInsuranceCompanyId() {
        return insuranceCompanyId;
    }

    public InsuranceContract setInsuranceCompanyId(String insuranceCompanyId) {
        this.insuranceCompanyId = insuranceCompanyId;
        return this;
    }

    public static byte[] serialize(Object object) {
        Genson genson = new Genson();
        return genson.serializeBytes(object);
    }

    public static InsuranceContract deserialize(byte[] data) {
        Genson genson = new Genson();
        return genson.deserialize(data, InsuranceContract.class);
    }

    public String getEntityName() {
        return entityName;
    }

    public InsuranceContract setEntityName(String entityName) {
        this.entityName = entityName;
        return this;
    }

    public String getDateCreated() {
        return dateCreated;
    }

    public InsuranceContract setDateCreated(String dateCreated) {
        this.dateCreated = dateCreated;
        return this;
    }

    public String getDateModified() {
        return dateModified;
    }

    public InsuranceContract setDateModified(String dateModified) {
        this.dateModified = dateModified;
        return this;
    }

    public String getHashFile() {
        return hashFile;
    }

    public InsuranceContract setHashFile(String hashFile) {
        this.hashFile = hashFile;
        return this;
    }

    public String getStartDate() {
        return startDate;
    }

    public InsuranceContract setStartDate(String startDate) {
        this.startDate = startDate;
        return this;
    }

    public String getEndDate() {
        return endDate;
    }

    public InsuranceContract setEndDate(String endDate) {
        this.endDate = endDate;
        return this;
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) return true;
        if (object == null || getClass() != object.getClass()) return false;
        InsuranceContract that = (InsuranceContract) object;
        return Objects.equals(insuranceContractId, that.insuranceContractId) && Objects.equals(insuranceProductId, that.insuranceProductId) && Objects.equals(patientId, that.patientId) && Objects.equals(insuranceCompanyId, that.insuranceCompanyId) && Objects.equals(startDate, that.startDate) && Objects.equals(endDate, that.endDate) && Objects.equals(dateModified, that.dateModified) && Objects.equals(hashFile, that.hashFile) && Objects.equals(entityName, that.entityName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(insuranceContractId, insuranceProductId, patientId, insuranceCompanyId, startDate, endDate, dateModified, hashFile, entityName);
    }
}

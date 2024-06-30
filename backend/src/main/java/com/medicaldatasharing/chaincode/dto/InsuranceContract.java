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
                             String dateModified,
                             String hashFile) {
        super();
        this.insuranceContractId = insuranceContractId;
        this.patientId = patientId;
        this.insuranceCompanyId = insuranceCompanyId;
        this.dateModified = dateModified;
        this.hashFile = hashFile;
    }

    public static InsuranceContract createInstance(String insuranceContractId,
                                                   String insuranceProductId,
                                                   String patientId,
                                                   String insuranceCompanyId,
                                                   String dateModified,
                                                   String hashFile) {
        InsuranceContract insuranceContract = new InsuranceContract();
        insuranceContract.setInsuranceProductId(insuranceContractId);
        insuranceContract.setInsuranceProductId(insuranceProductId);
        insuranceContract.setPatientId(patientId);
        insuranceContract.setInsuranceCompanyId(insuranceCompanyId);
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

    @Override
    public boolean equals(Object object) {
        if (this == object) return true;
        if (object == null || getClass() != object.getClass()) return false;
        InsuranceContract that = (InsuranceContract) object;
        return Objects.equals(insuranceContractId, that.insuranceContractId) && Objects.equals(insuranceProductId, that.insuranceProductId) && Objects.equals(patientId, that.patientId) && Objects.equals(insuranceCompanyId, that.insuranceCompanyId) && Objects.equals(dateModified, that.dateModified) && Objects.equals(hashFile, that.hashFile) && Objects.equals(entityName, that.entityName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(insuranceContractId, insuranceProductId, patientId, insuranceCompanyId, dateModified, hashFile, entityName);
    }
}

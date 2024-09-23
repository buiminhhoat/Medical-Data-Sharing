package com.medicaldatasharing.chaincode.dto;

import com.owlike.genson.Genson;
import com.owlike.genson.annotation.JsonProperty;

public class Medication {
    @JsonProperty("medicationId")
    private String medicationId;

    @JsonProperty("manufacturerId")
    private String manufacturerId;

    @JsonProperty("medicationName")
    private String medicationName;

    @JsonProperty("description")
    private String description;

    @JsonProperty("hashFile")
    private String hashFile;

    @JsonProperty("dateCreated")
    private String dateCreated;

    @JsonProperty("dateModified")
    private String dateModified;

    @JsonProperty("entityName")
    private String entityName;

    public Medication() {
        this.entityName = Medication.class.getSimpleName();
    }

    public static byte[] serialize(Object object) {
        Genson genson = new Genson();
        return genson.serializeBytes(object);
    }

    public static Medication deserialize(byte[] data) {
        Genson genson = new Genson();
        return genson.deserialize(data, Medication.class);
    }

    public static Medication createInstance(String medicationId,
                                            String manufacturerId,
                                            String medicationName,
                                            String dateCreated,
                                            String dateModified,
                                            String description,
                                            String hashFile
                                            ) {
        Medication medication = new Medication();
        medication.setMedicationId(medicationId);
        medication.setManufacturerId(manufacturerId);
        medication.setMedicationName(medicationName);
        medication.setDateCreated(dateCreated);
        medication.setDateModified(dateModified);
        medication.setDescription(description);
        medication.setHashFile(hashFile);
        medication.setEntityName(Medication.class.getSimpleName());
        return medication;
    }

    public String getMedicationId() {
        return medicationId;
    }

    public Medication setMedicationId(String medicationId) {
        this.medicationId = medicationId;
        return this;
    }

    public String getManufacturerId() {
        return manufacturerId;
    }

    public Medication setManufacturerId(String manufacturerId) {
        this.manufacturerId = manufacturerId;
        return this;
    }

    public String getMedicationName() {
        return medicationName;
    }

    public Medication setMedicationName(String medicationName) {
        this.medicationName = medicationName;
        return this;
    }

    public String getDescription() {
        return description;
    }

    public Medication setDescription(String description) {
        this.description = description;
        return this;
    }

    public String getDateCreated() {
        return dateCreated;
    }

    public Medication setDateCreated(String dateCreated) {
        this.dateCreated = dateCreated;
        return this;
    }

    public String getDateModified() {
        return dateModified;
    }

    public Medication setDateModified(String dateModified) {
        this.dateModified = dateModified;
        return this;
    }

    public String getEntityName() {
        return entityName;
    }

    public Medication setEntityName(String entityName) {
        this.entityName = entityName;
        return this;
    }

    public String getHashFile() {
        return hashFile;
    }

    public Medication setHashFile(String hashFile) {
        this.hashFile = hashFile;
        return this;
    }
}

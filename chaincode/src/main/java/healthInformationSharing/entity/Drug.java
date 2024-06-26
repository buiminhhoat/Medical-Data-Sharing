package healthInformationSharing.entity;

import com.owlike.genson.Genson;
import org.hyperledger.fabric.contract.annotation.DataType;
import org.hyperledger.fabric.contract.annotation.Property;
import com.owlike.genson.annotation.JsonProperty;

import java.util.Objects;

@DataType
public class Drug {
    @Property
    @JsonProperty("drugId")
    private String drugId;

    @Property
    @JsonProperty("medicationId")
    private String medicationId;

    @Property
    @JsonProperty("manufactureDate")
    private String manufactureDate;

    @Property
    @JsonProperty("expirationDate")
    private String expirationDate;

    @Property
    @JsonProperty("ownerId")
    private String ownerId;

    @Property()
    @JsonProperty("entityName")
    private String entityName;

    public Drug() {
        this.entityName = Drug.class.getSimpleName();
    }

    public static Drug createInstance(
            String drugId,
            String medicationId,
            String manufactureDate,
            String expirationDate,
            String ownerId) {
        Drug drug = new Drug();
        drug.setDrugId(drugId);
        drug.setMedicationId(medicationId);
        drug.setManufactureDate(manufactureDate);
        drug.setExpirationDate(expirationDate);
        drug.setOwnerId(ownerId);
        return drug;
    }

    public String getDrugId() {
        return drugId;
    }

    public Drug setDrugId(String drugId) {
        this.drugId = drugId;
        return this;
    }

    public String getMedicationId() {
        return medicationId;
    }

    public Drug setMedicationId(String medicationId) {
        this.medicationId = medicationId;
        return this;
    }

    public String getManufactureDate() {
        return manufactureDate;
    }

    public Drug setManufactureDate(String manufactureDate) {
        this.manufactureDate = manufactureDate;
        return this;
    }

    public String getExpirationDate() {
        return expirationDate;
    }

    public Drug setExpirationDate(String expirationDate) {
        this.expirationDate = expirationDate;
        return this;
    }

    public String getOwnerId() {
        return ownerId;
    }

    public Drug setOwnerId(String ownerId) {
        this.ownerId = ownerId;
        return this;
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) return true;
        if (object == null || getClass() != object.getClass()) return false;
        Drug drug = (Drug) object;
        return Objects.equals(drugId, drug.drugId) && Objects.equals(medicationId, drug.medicationId) && Objects.equals(manufactureDate, drug.manufactureDate) && Objects.equals(expirationDate, drug.expirationDate) && Objects.equals(ownerId, drug.ownerId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(drugId, medicationId, manufactureDate, expirationDate, ownerId);
    }

    @Override
    public String toString() {
        return "Drug{" +
                "drugId='" + drugId + '\'' +
                ", medicationId='" + medicationId + '\'' +
                ", manufactureDate='" + manufactureDate + '\'' +
                ", expirationDate='" + expirationDate + '\'' +
                ", ownerId='" + ownerId + '\'' +
                '}';
    }

    public static byte[] serialize(Object object) {
        Genson genson = new Genson();
        return genson.serializeBytes(object);
    }

    public static Drug deserialize(byte[] data) {
        Genson genson = new Genson();
        return genson.deserialize(data, Drug.class);
    }
}

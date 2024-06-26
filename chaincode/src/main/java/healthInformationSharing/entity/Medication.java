package healthInformationSharing.entity;

import com.owlike.genson.Genson;
import org.hyperledger.fabric.contract.annotation.DataType;
import org.hyperledger.fabric.contract.annotation.Property;
import com.owlike.genson.annotation.JsonProperty;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static java.nio.charset.StandardCharsets.UTF_8;

@DataType()
public class Medication {
    @Property
    @JsonProperty("medicationId")
    private String medicationId;

    @Property
    @JsonProperty("manufacturerId")
    private String manufacturerId;

    @Property
    @JsonProperty("medicationName")
    private String medicationName;

    @Property
    @JsonProperty("description")
    private String description;

    public Medication() {
    }

    public Medication(String medicationId, String manufacturerId, String medicationName, String description) {
        this.medicationId = medicationId;
        this.manufacturerId = manufacturerId;
        this.medicationName = medicationName;
        this.description = description;
    }

    public static Medication createInstance(String medicationId,
                                            String manufacturerId,
                                            String medicationName,
                                            String description) {
        Medication medication = new Medication();
        medication.setMedicationId(medicationId);
        medication.setManufacturerId(manufacturerId);
        medication.setMedicationName(medicationName);
        medication.setDescription(description);
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

    public static byte[] serialize(Object object) {
        Genson genson = new Genson();
        return genson.serializeBytes(object);
    }

    public static Medication deserialize(byte[] data) {
        Genson genson = new Genson();
        return genson.deserialize(data, Medication.class);
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) return true;
        if (object == null || getClass() != object.getClass()) return false;
        Medication that = (Medication) object;
        return Objects.equals(medicationId, that.medicationId) && Objects.equals(manufacturerId, that.manufacturerId) && Objects.equals(medicationName, that.medicationName) && Objects.equals(description, that.description);
    }

    @Override
    public int hashCode() {
        return Objects.hash(medicationId, manufacturerId, medicationName, description);
    }
}

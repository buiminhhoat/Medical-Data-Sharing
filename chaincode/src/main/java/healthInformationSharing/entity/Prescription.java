package healthInformationSharing.entity;

import com.owlike.genson.Genson;
import healthInformationSharing.enumeration.DrugReactionStatus;
import org.hyperledger.fabric.contract.annotation.DataType;
import org.hyperledger.fabric.contract.annotation.Property;
import com.owlike.genson.annotation.JsonProperty;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static java.nio.charset.StandardCharsets.UTF_8;

public class Prescription {
    @Property
    @JsonProperty("prescriptionId")
    private String prescriptionId;

    @Property
    @JsonProperty("drugReaction")
    private String drugReaction;

    @Property()
    @JsonProperty("entityName")
    private String entityName;

    public Prescription() {
        this.drugReaction = DrugReactionStatus.NO_INFORMATION;
        this.entityName = Prescription.class.getSimpleName();
    }

    public static Prescription createInstance(
            String prescriptionId,
            String drugReaction) {
        Prescription prescription = new Prescription();
        prescription.setPrescriptionId(prescriptionId);
        prescription.setDrugReaction(drugReaction);
        return prescription;
    }

    public String getPrescriptionId() {
        return prescriptionId;
    }

    public Prescription setPrescriptionId(String prescriptionId) {
        this.prescriptionId = prescriptionId;
        return this;
    }

    public String getDrugReaction() {
        return drugReaction;
    }

    public Prescription setDrugReaction(String drugReaction) {
        this.drugReaction = drugReaction;
        return this;
    }

    public String getEntityName() {
        return entityName;
    }

    public Prescription setEntityName(String entityName) {
        this.entityName = entityName;
        return this;
    }

    public static byte[] serialize(Object object) {
        Genson genson = new Genson();
        return genson.serializeBytes(object);
    }

    public static Prescription deserialize(byte[] data) {
        Genson genson = new Genson();
        return genson.deserialize(data, Prescription.class);
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) return true;
        if (object == null || getClass() != object.getClass()) return false;
        Prescription that = (Prescription) object;
        return Objects.equals(prescriptionId, that.prescriptionId) && Objects.equals(drugReaction, that.drugReaction) && Objects.equals(entityName, that.entityName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(prescriptionId, drugReaction, entityName);
    }
}

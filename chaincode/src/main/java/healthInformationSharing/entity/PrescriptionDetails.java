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

@DataType
public class PrescriptionDetails {
    @Property
    @JsonProperty("prescriptionDetailId")
    private String prescriptionDetailId;

    @Property
    @JsonProperty("prescriptionId")
    private String prescriptionId;

    @Property
    @JsonProperty("medicationId")
    private String medicationId;

    @Property
    @JsonProperty("quantity")
    private String quantity;

    @Property
    @JsonProperty("purchasedQuantity")
    private String purchasedQuantity;

    @Property
    @JsonProperty("details")
    private String details;

    @Property()
    @JsonProperty("entityName")
    private String entityName;

    public PrescriptionDetails() {
        this.entityName = PrescriptionDetails.class.getSimpleName();
    }

    public String getPrescriptionDetailId() {
        return prescriptionDetailId;
    }

    public PrescriptionDetails setPrescriptionDetailId(String prescriptionDetailId) {
        this.prescriptionDetailId = prescriptionDetailId;
        return this;
    }

    public String getPrescriptionId() {
        return prescriptionId;
    }

    public PrescriptionDetails setPrescriptionId(String prescriptionId) {
        this.prescriptionId = prescriptionId;
        return this;
    }

    public String getMedicationId() {
        return medicationId;
    }

    public PrescriptionDetails setMedicationId(String medicationId) {
        this.medicationId = medicationId;
        return this;
    }

    public String getQuantity() {
        return quantity;
    }

    public PrescriptionDetails setQuantity(String quantity) {
        this.quantity = quantity;
        return this;
    }

    public String getDetails() {
        return details;
    }

    public PrescriptionDetails setDetails(String details) {
        this.details = details;
        return this;
    }

    public String getEntityName() {
        return entityName;
    }

    public PrescriptionDetails setEntityName(String entityName) {
        this.entityName = entityName;
        return this;
    }

    public String getPurchasedQuantity() {
        return purchasedQuantity;
    }

    public PrescriptionDetails setPurchasedQuantity(String purchasedQuantity) {
        this.purchasedQuantity = purchasedQuantity;
        return this;
    }

    public static byte[] serialize(Object object) {
        Genson genson = new Genson();
        return genson.serializeBytes(object);
    }

    public static PrescriptionDetails deserialize(byte[] data) {
        Genson genson = new Genson();
        return genson.deserialize(data, PrescriptionDetails.class);
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) return true;
        if (object == null || getClass() != object.getClass()) return false;
        PrescriptionDetails that = (PrescriptionDetails) object;
        return Objects.equals(prescriptionDetailId, that.prescriptionDetailId) && Objects.equals(prescriptionId, that.prescriptionId) && Objects.equals(medicationId, that.medicationId) && Objects.equals(quantity, that.quantity) && Objects.equals(purchasedQuantity, that.purchasedQuantity) && Objects.equals(details, that.details) && Objects.equals(entityName, that.entityName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(prescriptionDetailId, prescriptionId, medicationId, quantity, purchasedQuantity, details, entityName);
    }
}

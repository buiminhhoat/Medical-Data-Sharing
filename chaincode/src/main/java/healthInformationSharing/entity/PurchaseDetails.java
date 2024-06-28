package healthInformationSharing.entity;

import com.owlike.genson.Genson;
import com.owlike.genson.annotation.JsonProperty;
import org.hyperledger.fabric.contract.annotation.Property;

import java.util.Objects;

public class PurchaseDetails {
    @Property
    @JsonProperty("purchaseDetailId")
    private String purchaseDetailId;

    @Property
    @JsonProperty("prescriptionDetailId")
    private String prescriptionDetailId;

    @Property
    @JsonProperty("drugId")
    private String drugId;

    @Property()
    @JsonProperty("entityName")
    private String entityName;

    public PurchaseDetails() {
        this.entityName = PurchaseDetails.class.getSimpleName();
    }

    public PurchaseDetails(String purchaseDetailId, String prescriptionDetailId, String drugId) {
        super();
        this.purchaseDetailId = purchaseDetailId;
        this.prescriptionDetailId = prescriptionDetailId;
        this.drugId = drugId;
    }

    public String getPurchaseDetailId() {
        return purchaseDetailId;
    }

    public PurchaseDetails setPurchaseDetailId(String purchaseDetailId) {
        this.purchaseDetailId = purchaseDetailId;
        return this;
    }

    public String getPrescriptionDetailId() {
        return prescriptionDetailId;
    }

    public PurchaseDetails setPrescriptionDetailId(String prescriptionDetailId) {
        this.prescriptionDetailId = prescriptionDetailId;
        return this;
    }

    public String getDrugId() {
        return drugId;
    }

    public PurchaseDetails setDrugId(String drugId) {
        this.drugId = drugId;
        return this;
    }

    public String getEntityName() {
        return entityName;
    }

    public PurchaseDetails setEntityName(String entityName) {
        this.entityName = entityName;
        return this;
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) return true;
        if (object == null || getClass() != object.getClass()) return false;
        PurchaseDetails that = (PurchaseDetails) object;
        return Objects.equals(purchaseDetailId, that.purchaseDetailId) && Objects.equals(prescriptionDetailId, that.prescriptionDetailId) && Objects.equals(drugId, that.drugId) && Objects.equals(entityName, that.entityName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(purchaseDetailId, prescriptionDetailId, drugId, entityName);
    }

    public static byte[] serialize(Object object) {
        Genson genson = new Genson();
        return genson.serializeBytes(object);
    }

    public static PurchaseDetails deserialize(byte[] data) {
        Genson genson = new Genson();
        return genson.deserialize(data, PurchaseDetails.class);
    }
}

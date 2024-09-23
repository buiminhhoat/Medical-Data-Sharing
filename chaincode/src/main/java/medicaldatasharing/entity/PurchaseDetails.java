package medicaldatasharing.entity;

import com.owlike.genson.Genson;
import com.owlike.genson.annotation.JsonProperty;
import org.hyperledger.fabric.contract.annotation.Property;
import org.json.JSONObject;

import java.lang.reflect.Field;
import java.util.Objects;

public class PurchaseDetails {
    @Property
    @JsonProperty("purchaseDetailId")
    private String purchaseDetailId;

    @Property
    @JsonProperty("purchaseId")
    private String purchaseId;

    @Property
    @JsonProperty("prescriptionDetailId")
    private String prescriptionDetailId;

    @Property
    @JsonProperty("medicationId")
    private String medicationId;

    @Property
    @JsonProperty("drugId")
    private String drugId;

    @Property()
    @JsonProperty("entityName")
    private String entityName;

    public PurchaseDetails() {
        this.entityName = PurchaseDetails.class.getSimpleName();
    }

    public static PurchaseDetails createInstance(
            String purchaseDetailId,
            String purchaseId,
            String prescriptionDetailId,
            String medicationId,
            String drugId
    ) {
        PurchaseDetails purchaseDetails = new PurchaseDetails();
        purchaseDetails.setPurchaseDetailId(purchaseDetailId);
        purchaseDetails.setPurchaseId(purchaseId);
        purchaseDetails.setPrescriptionDetailId(prescriptionDetailId);
        purchaseDetails.setMedicationId(medicationId);
        purchaseDetails.setDrugId(drugId);
        return purchaseDetails;
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

    public String getMedicationId() {
        return medicationId;
    }

    public PurchaseDetails setMedicationId(String medicationId) {
        this.medicationId = medicationId;
        return this;
    }

    public String getPurchaseId() {
        return purchaseId;
    }

    public PurchaseDetails setPurchaseId(String purchaseId) {
        this.purchaseId = purchaseId;
        return this;
    }

    public static byte[] serialize(Object object) {
        Genson genson = new Genson();
        return genson.serializeBytes(object);
    }

    public static PurchaseDetails deserialize(byte[] data) {
        Genson genson = new Genson();
        return genson.deserialize(data, PurchaseDetails.class);
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) return true;
        if (object == null || getClass() != object.getClass()) return false;
        PurchaseDetails that = (PurchaseDetails) object;
        return Objects.equals(purchaseDetailId, that.purchaseDetailId) && Objects.equals(purchaseId, that.purchaseId) && Objects.equals(prescriptionDetailId, that.prescriptionDetailId) && Objects.equals(medicationId, that.medicationId) && Objects.equals(drugId, that.drugId) && Objects.equals(entityName, that.entityName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(purchaseDetailId, purchaseId, prescriptionDetailId, medicationId, drugId, entityName);
    }

    public JSONObject toJSONObject() {
        JSONObject jsonObj = new JSONObject();
        Field[] fields = this.getClass().getDeclaredFields();

        for (Field field : fields) {
            field.setAccessible(true);
            try {
                jsonObj.put(field.getName(), field.get(this));
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
        return jsonObj;
    }
}

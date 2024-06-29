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
public class Purchase {
    @Property
    @JsonProperty("purchaseId")
    private String purchaseId;

    @Property
    @JsonProperty("prescriptionId")
    private String prescriptionId;

    @Property
    @JsonProperty("patientId")
    private String patientId;

    @Property
    @JsonProperty("drugStoreId")
    private String drugStoreId;

    @Property
    @JsonProperty("dateModified")
    private String dateModified;

    @Property()
    @JsonProperty("entityName")
    private String entityName;

    public Purchase() {
        this.entityName = Purchase.class.getSimpleName();
    }

    public static Purchase createInstance(
            String purchaseId,
            String prescriptionId,
            String patientId,
            String drugStoreId,
            String dateModified
    ) {
        Purchase purchase = new Purchase();
        purchase.setPurchaseId(purchaseId);
        purchase.setPrescriptionId(prescriptionId);
        purchase.setPatientId(patientId);
        purchase.setDrugStoreId(drugStoreId);
        purchase.setDateModified(dateModified);
        return purchase;
    }

    public String getPurchaseId() {
        return purchaseId;
    }

    public Purchase setPurchaseId(String purchaseId) {
        this.purchaseId = purchaseId;
        return this;
    }

    public String getPrescriptionId() {
        return prescriptionId;
    }

    public Purchase setPrescriptionId(String prescriptionId) {
        this.prescriptionId = prescriptionId;
        return this;
    }

    public String getEntityName() {
        return entityName;
    }

    public Purchase setEntityName(String entityName) {
        this.entityName = entityName;
        return this;
    }

    public static byte[] serialize(Object object) {
        Genson genson = new Genson();
        return genson.serializeBytes(object);
    }

    public static Purchase deserialize(byte[] data) {
        Genson genson = new Genson();
        return genson.deserialize(data, Purchase.class);
    }

    public String getPatientId() {
        return patientId;
    }

    public Purchase setPatientId(String patientId) {
        this.patientId = patientId;
        return this;
    }

    public String getDrugStoreId() {
        return drugStoreId;
    }

    public Purchase setDrugStoreId(String drugStoreId) {
        this.drugStoreId = drugStoreId;
        return this;
    }

    public String getDateModified() {
        return dateModified;
    }

    public Purchase setDateModified(String dateModified) {
        this.dateModified = dateModified;
        return this;
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) return true;
        if (object == null || getClass() != object.getClass()) return false;
        Purchase purchase = (Purchase) object;
        return Objects.equals(purchaseId, purchase.purchaseId) && Objects.equals(prescriptionId, purchase.prescriptionId) && Objects.equals(patientId, purchase.patientId) && Objects.equals(drugStoreId, purchase.drugStoreId) && Objects.equals(dateModified, purchase.dateModified) && Objects.equals(entityName, purchase.entityName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(purchaseId, prescriptionId, patientId, drugStoreId, dateModified, entityName);
    }
}

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

    @Property()
    @JsonProperty("entityName")
    private String entityName;

    public Purchase() {
        this.entityName = Purchase.class.getSimpleName();
    }

    public Purchase(String purchaseId, String prescriptionId, String entityName) {
        super();
        this.purchaseId = purchaseId;
        this.prescriptionId = prescriptionId;
        this.entityName = entityName;
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

    @Override
    public boolean equals(Object object) {
        if (this == object) return true;
        if (object == null || getClass() != object.getClass()) return false;
        Purchase purchase = (Purchase) object;
        return Objects.equals(purchaseId, purchase.purchaseId) && Objects.equals(prescriptionId, purchase.prescriptionId) && Objects.equals(entityName, purchase.entityName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(purchaseId, prescriptionId, entityName);
    }

    @Override
    public String toString() {
        return "Purchase{" +
                "purchaseId='" + purchaseId + '\'' +
                ", prescriptionId='" + prescriptionId + '\'' +
                ", entityName='" + entityName + '\'' +
                '}';
    }
}

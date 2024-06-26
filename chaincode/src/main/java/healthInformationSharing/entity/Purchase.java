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
    @JsonProperty("prescriptionDetailId")
    private String prescriptionDetailId;

    @Property
    @JsonProperty("drugId")
    private String drugId;

    @Property()
    @JsonProperty("entityName")
    private String entityName;

    public Purchase() {
        this.entityName = Purchase.class.getSimpleName();
    }

    public Purchase(String purchaseId, String prescriptionDetailId, String drugId) {
        this.purchaseId = purchaseId;
        this.prescriptionDetailId = prescriptionDetailId;
        this.drugId = drugId;
    }

    public String getPurchaseId() {
        return purchaseId;
    }

    public Purchase setPurchaseId(String purchaseId) {
        this.purchaseId = purchaseId;
        return this;
    }

    public String getPrescriptionDetailId() {
        return prescriptionDetailId;
    }

    public Purchase setPrescriptionDetailId(String prescriptionDetailId) {
        this.prescriptionDetailId = prescriptionDetailId;
        return this;
    }

    public String getDrugId() {
        return drugId;
    }

    public Purchase setDrugId(String drugId) {
        this.drugId = drugId;
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
}

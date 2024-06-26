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

public class Prescription {
    @Property
    @JsonProperty("prescriptionId")
    private String prescriptionId;

    @Property
    @JsonProperty("usageCount")
    private String usageCount;

    @Property
    @JsonProperty("drugReaction")
    private String drugReaction;

    @Property()
    @JsonProperty("entityName")
    private String entityName;

    public Prescription() {
        this.entityName = Prescription.class.getSimpleName();
    }

    public Prescription(String prescriptionId, String medicalRecordId, String usageCount, String drugReaction) {
        this.prescriptionId = prescriptionId;
        this.usageCount = usageCount;
        this.drugReaction = drugReaction;
    }

    public static Prescription createInstance(
            String prescriptionId,
            String usageCount,
            String drugReaction) {
        Prescription prescription = new Prescription();
        prescription.setPrescriptionId(prescriptionId);
        prescription.setUsageCount(usageCount);
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

    public String getUsageCount() {
        return usageCount;
    }

    public Prescription setUsageCount(String usageCount) {
        this.usageCount = usageCount;
        return this;
    }

    public String getDrugReaction() {
        return drugReaction;
    }

    public Prescription setDrugReaction(String drugReaction) {
        this.drugReaction = drugReaction;
        return this;
    }
}

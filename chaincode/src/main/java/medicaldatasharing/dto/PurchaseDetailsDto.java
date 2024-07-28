package medicaldatasharing.dto;

import com.owlike.genson.Genson;
import com.owlike.genson.annotation.JsonProperty;
import medicaldatasharing.entity.PurchaseDetails;
import org.hyperledger.fabric.contract.annotation.DataType;
import org.hyperledger.fabric.contract.annotation.Property;

@DataType
public class PurchaseDetailsDto {
    @Property
    @JsonProperty("purchaseDetailId")
    private String purchaseDetailId;

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

    public PurchaseDetailsDto(PurchaseDetails purchaseDetails) {
        this.purchaseDetailId = purchaseDetails.getPurchaseDetailId();
        this.prescriptionDetailId = purchaseDetails.getPrescriptionDetailId();
        this.medicationId = purchaseDetails.getMedicationId();
        this.drugId = purchaseDetails.getDrugId();
        this.entityName = purchaseDetails.getEntityName();
    }

    public String getPurchaseDetailId() {
        return purchaseDetailId;
    }

    public PurchaseDetailsDto setPurchaseDetailId(String purchaseDetailId) {
        this.purchaseDetailId = purchaseDetailId;
        return this;
    }

    public String getPrescriptionDetailId() {
        return prescriptionDetailId;
    }

    public PurchaseDetailsDto setPrescriptionDetailId(String prescriptionDetailId) {
        this.prescriptionDetailId = prescriptionDetailId;
        return this;
    }

    public String getMedicationId() {
        return medicationId;
    }

    public PurchaseDetailsDto setMedicationId(String medicationId) {
        this.medicationId = medicationId;
        return this;
    }

    public String getDrugId() {
        return drugId;
    }

    public PurchaseDetailsDto setDrugId(String drugId) {
        this.drugId = drugId;
        return this;
    }

    public String getEntityName() {
        return entityName;
    }

    public PurchaseDetailsDto setEntityName(String entityName) {
        this.entityName = entityName;
        return this;
    }

    public static byte[] serialize(Object object) {
        Genson genson = new Genson();
        return genson.serializeBytes(object);
    }

    public static PurchaseDetailsDto deserialize(byte[] data) {
        Genson genson = new Genson();
        return genson.deserialize(data, PurchaseDetailsDto.class);
    }
}

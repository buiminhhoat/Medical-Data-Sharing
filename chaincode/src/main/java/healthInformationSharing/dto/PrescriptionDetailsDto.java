package healthInformationSharing.dto;

import com.owlike.genson.Genson;
import com.owlike.genson.annotation.JsonProperty;
import healthInformationSharing.entity.PrescriptionDetails;
import org.hyperledger.fabric.contract.annotation.DataType;
import org.hyperledger.fabric.contract.annotation.Property;

@DataType
public class PrescriptionDetailsDto {
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
    @JsonProperty("medicationName")
    private String medicationName;

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

    public PrescriptionDetailsDto(PrescriptionDetails prescriptionDetails) {
        this.prescriptionDetailId = prescriptionDetails.getPrescriptionDetailId();
        this.prescriptionId = prescriptionDetails.getPrescriptionId();
        this.medicationId = prescriptionDetails.getMedicationId();
        this.quantity = prescriptionDetails.getQuantity();
        this.purchasedQuantity = prescriptionDetails.getPurchasedQuantity();
        this.details = prescriptionDetails.getDetails();
    }

    public String getPrescriptionDetailId() {
        return prescriptionDetailId;
    }

    public PrescriptionDetailsDto setPrescriptionDetailId(String prescriptionDetailId) {
        this.prescriptionDetailId = prescriptionDetailId;
        return this;
    }

    public String getPrescriptionId() {
        return prescriptionId;
    }

    public PrescriptionDetailsDto setPrescriptionId(String prescriptionId) {
        this.prescriptionId = prescriptionId;
        return this;
    }

    public String getMedicationId() {
        return medicationId;
    }

    public PrescriptionDetailsDto setMedicationId(String medicationId) {
        this.medicationId = medicationId;
        return this;
    }

    public String getMedicationName() {
        return medicationName;
    }

    public PrescriptionDetailsDto setMedicationName(String medicationName) {
        this.medicationName = medicationName;
        return this;
    }

    public String getQuantity() {
        return quantity;
    }

    public PrescriptionDetailsDto setQuantity(String quantity) {
        this.quantity = quantity;
        return this;
    }

    public String getPurchasedQuantity() {
        return purchasedQuantity;
    }

    public PrescriptionDetailsDto setPurchasedQuantity(String purchasedQuantity) {
        this.purchasedQuantity = purchasedQuantity;
        return this;
    }

    public String getDetails() {
        return details;
    }

    public PrescriptionDetailsDto setDetails(String details) {
        this.details = details;
        return this;
    }

    public String getEntityName() {
        return entityName;
    }

    public PrescriptionDetailsDto setEntityName(String entityName) {
        this.entityName = entityName;
        return this;
    }

    public static byte[] serialize(Object object) {
        Genson genson = new Genson();
        return genson.serializeBytes(object);
    }

    public static PrescriptionDetailsDto deserialize(byte[] data) {
        Genson genson = new Genson();
        return genson.deserialize(data, PrescriptionDetailsDto.class);
    }
}

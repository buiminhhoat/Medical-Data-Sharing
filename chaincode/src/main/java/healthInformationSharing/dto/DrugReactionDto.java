package healthInformationSharing.dto;

import com.owlike.genson.annotation.JsonProperty;
import org.hyperledger.fabric.contract.annotation.DataType;

import java.util.List;

@DataType
public class DrugReactionDto {
    @JsonProperty("medicationId")
    private String medicationId;

    @JsonProperty("prescriptionId")
    private String prescriptionId;

    @JsonProperty("drugReaction")
    private String drugReaction;

    public String getMedicationId() {
        return medicationId;
    }

    public DrugReactionDto setMedicationId(String medicationId) {
        this.medicationId = medicationId;
        return this;
    }

    public String getPrescriptionId() {
        return prescriptionId;
    }

    public DrugReactionDto setPrescriptionId(String prescriptionId) {
        this.prescriptionId = prescriptionId;
        return this;
    }

    public String getDrugReaction() {
        return drugReaction;
    }

    public DrugReactionDto setDrugReaction(String drugReaction) {
        this.drugReaction = drugReaction;
        return this;
    }
}

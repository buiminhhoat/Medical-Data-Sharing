package medicaldatasharing.dto;

import com.owlike.genson.annotation.JsonProperty;
import org.hyperledger.fabric.contract.annotation.DataType;

import java.util.List;

@DataType
public class MedicationPurchaseDto {
    @JsonProperty("medicationId")
    private String medicationId;

    @JsonProperty("prescriptionDetailId")
    private String prescriptionDetailId;

    @JsonProperty("drugIdList")
    private List<String> drugIdList;

    public String getMedicationId() {
        return medicationId;
    }

    public MedicationPurchaseDto setMedicationId(String medicationId) {
        this.medicationId = medicationId;
        return this;
    }

    public List<String> getDrugIdList() {
        return drugIdList;
    }

    public MedicationPurchaseDto setDrugIdList(List<String> drugIdList) {
        this.drugIdList = drugIdList;
        return this;
    }

    public String getPrescriptionDetailId() {
        return prescriptionDetailId;
    }

    public MedicationPurchaseDto setPrescriptionDetailId(String prescriptionDetailId) {
        this.prescriptionDetailId = prescriptionDetailId;
        return this;
    }
}

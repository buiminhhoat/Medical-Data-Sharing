package medicaldatasharing.dto;

import com.owlike.genson.annotation.JsonProperty;
import org.hyperledger.fabric.contract.annotation.DataType;

import java.util.List;

@DataType
public class PrescriptionDto {
    @JsonProperty("prescriptionId")
    private String prescriptionId;

    @JsonProperty("drugReaction")
    private String drugReaction;

    @JsonProperty("entityName")
    private String entityName;

    @JsonProperty("prescriptionDetailsListDto")
    List<PrescriptionDetailsDto> prescriptionDetailsListDto;

    public String getPrescriptionId() {
        return prescriptionId;
    }

    public PrescriptionDto setPrescriptionId(String prescriptionId) {
        this.prescriptionId = prescriptionId;
        return this;
    }

    public String getDrugReaction() {
        return drugReaction;
    }

    public PrescriptionDto setDrugReaction(String drugReaction) {
        this.drugReaction = drugReaction;
        return this;
    }

    public String getEntityName() {
        return entityName;
    }

    public PrescriptionDto setEntityName(String entityName) {
        this.entityName = entityName;
        return this;
    }

    public List<PrescriptionDetailsDto> getPrescriptionDetailsListDto() {
        return prescriptionDetailsListDto;
    }

    public PrescriptionDto setPrescriptionDetailsListDto(List<PrescriptionDetailsDto> prescriptionDetailsListDto) {
        this.prescriptionDetailsListDto = prescriptionDetailsListDto;
        return this;
    }

    public PrescriptionDto addPrescriptionDetailsToPrescriptionDetailsListDto(PrescriptionDetailsDto prescriptionDetailsDto) {
        prescriptionDetailsListDto.add(prescriptionDetailsDto);
        return this;
    }
}

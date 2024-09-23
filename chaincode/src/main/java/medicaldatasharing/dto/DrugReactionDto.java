package medicaldatasharing.dto;

import com.owlike.genson.annotation.JsonProperty;
import org.hyperledger.fabric.contract.annotation.DataType;

@DataType
public class DrugReactionDto {
    @JsonProperty("medicationId")
    private String medicationId;

    @JsonProperty("prescriptionId")
    private String prescriptionId;

    @JsonProperty("drugReaction")
    private String drugReaction;

    @JsonProperty("patientId")
    private String patientId;

    @JsonProperty("medicalRecordId")
    private String medicalRecordId;

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

    public String getPatientId() {
        return patientId;
    }

    public DrugReactionDto setPatientId(String patientId) {
        this.patientId = patientId;
        return this;
    }

    public String getMedicalRecordId() {
        return medicalRecordId;
    }

    public DrugReactionDto setMedicalRecordId(String medicalRecordId) {
        this.medicalRecordId = medicalRecordId;
        return this;
    }
}

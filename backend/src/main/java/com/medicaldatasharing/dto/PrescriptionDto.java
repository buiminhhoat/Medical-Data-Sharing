package com.medicaldatasharing.dto;

import com.medicaldatasharing.chaincode.dto.Prescription;
import com.medicaldatasharing.chaincode.dto.PrescriptionDetails;
import com.medicaldatasharing.enumeration.DrugReactionStatus;
import com.medicaldatasharing.util.AESUtil;
import com.owlike.genson.annotation.JsonProperty;
import lombok.*;

import java.util.List;

@Getter
@Setter
@Builder
@ToString
@AllArgsConstructor
public class PrescriptionDto {
    @JsonProperty("prescriptionId")
    private String prescriptionId;

    @JsonProperty("drugReaction")
    private String drugReaction;

    @JsonProperty("entityName")
    private String entityName;

    @JsonProperty("prescriptionDetailsListDto")
    List<PrescriptionDetailsDto> prescriptionDetailsListDto;

    public PrescriptionDto() throws Exception {
        this.drugReaction = AESUtil.encrypt(DrugReactionStatus.NO_INFORMATION);
        this.entityName = PrescriptionDto.class.getSimpleName();
    }

    public void encrypt() throws Exception {
        this.drugReaction = AESUtil.encrypt(this.drugReaction);
        for (PrescriptionDetailsDto prescriptionDetailsDto: prescriptionDetailsListDto) {
            prescriptionDetailsDto.encrypt();
        }
    }

    public void decrypt() throws Exception {
        this.drugReaction = AESUtil.decrypt(this.drugReaction);
        for (PrescriptionDetailsDto prescriptionDetailsDto: prescriptionDetailsListDto) {
            prescriptionDetailsDto.decrypt();
        }
    }
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

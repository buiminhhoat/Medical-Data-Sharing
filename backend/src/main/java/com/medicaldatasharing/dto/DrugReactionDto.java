package com.medicaldatasharing.dto;

import com.medicaldatasharing.util.AESUtil;
import com.owlike.genson.annotation.JsonProperty;
import lombok.*;

import javax.validation.constraints.NotBlank;

@Getter
@Setter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
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

    public void encrypt() throws Exception {
        this.drugReaction = AESUtil.encrypt(this.drugReaction);
    }

    public void decrypt() throws Exception {
        this.drugReaction = AESUtil.decrypt(this.drugReaction);
    }
}

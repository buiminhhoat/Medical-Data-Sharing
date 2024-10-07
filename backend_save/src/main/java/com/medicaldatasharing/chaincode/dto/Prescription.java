package com.medicaldatasharing.chaincode.dto;

import com.medicaldatasharing.enumeration.DrugReactionStatus;
import com.medicaldatasharing.util.AESUtil;
import com.owlike.genson.annotation.JsonProperty;

public class Prescription {
    @JsonProperty("prescriptionId")
    private String prescriptionId;

    @JsonProperty("drugReaction")
    private String drugReaction;

    @JsonProperty("entityName")
    private String entityName;

    public Prescription() throws Exception {
        this.drugReaction = AESUtil.encrypt(DrugReactionStatus.NO_INFORMATION);
        this.entityName = Prescription.class.getSimpleName();
    }

    public void encrypt() throws Exception {
        this.drugReaction = AESUtil.encrypt(this.drugReaction);
    }

    public void decrypt() throws Exception {
        this.drugReaction = AESUtil.decrypt(this.drugReaction);
    }
    public String getPrescriptionId() {
        return prescriptionId;
    }

    public Prescription setPrescriptionId(String prescriptionId) {
        this.prescriptionId = prescriptionId;
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

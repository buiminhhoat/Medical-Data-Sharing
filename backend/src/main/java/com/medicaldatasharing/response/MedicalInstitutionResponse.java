package com.medicaldatasharing.response;

import com.medicaldatasharing.chaincode.dto.Medication;
import com.medicaldatasharing.model.Manufacturer;
import com.medicaldatasharing.model.MedicalInstitution;
import com.owlike.genson.annotation.JsonProperty;

import java.util.List;

public class MedicalInstitutionResponse extends UserResponse {
    public MedicalInstitutionResponse(MedicalInstitution medicalInstitution) {
        this.id = medicalInstitution.getId();
        this.email = medicalInstitution.getEmail();
        this.fullName = medicalInstitution.getFullName();
        this.avatar = medicalInstitution.getAvatar();
        this.role = medicalInstitution.getRole();
        this.address = medicalInstitution.getAddress();
        this.enabled = String.valueOf(medicalInstitution.isEnabled());
    }
}

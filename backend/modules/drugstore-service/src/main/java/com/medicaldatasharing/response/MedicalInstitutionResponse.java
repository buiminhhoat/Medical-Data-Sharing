package com.medicaldatasharing.response;

import com.medicaldatasharing.model.MedicalInstitution;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
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

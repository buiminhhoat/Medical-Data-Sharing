package com.medicaldatasharing.response;

import com.medicaldatasharing.model.ResearchCenter;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class ResearchCenterResponse extends UserResponse {
    public ResearchCenterResponse(ResearchCenter researchCenter) {
        this.id = researchCenter.getId();
        this.email = researchCenter.getEmail();
        this.fullName = researchCenter.getFullName();
        this.avatar = researchCenter.getAvatar();
        this.role = researchCenter.getRole();
        this.address = researchCenter.getAddress();
        this.enabled = String.valueOf(researchCenter.isEnabled());
    }
}

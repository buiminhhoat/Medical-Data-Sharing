package com.medicaldatasharing.response;

import com.medicaldatasharing.model.Doctor;
import com.medicaldatasharing.model.Scientist;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ScientistResponse extends UserResponse {
    private String doctorId;

    private String avatar;
    private String researchCenterId;

    public ScientistResponse(Scientist scientist) {
        this.id = scientist.getId();
        this.doctorId = scientist.getId();
        this.email = scientist.getEmail();
        this.fullName = scientist.getFullName();
        this.researchCenterId = scientist.getResearchCenterId();
        this.avatar = scientist.getAvatar();
        this.role = scientist.getRole();
        this.address = scientist.getAddress();
        this.enabled = String.valueOf(scientist.isEnabled());
    }

    public String getDoctorId() {
        return doctorId;
    }

    public ScientistResponse setDoctorId(String doctorId) {
        this.doctorId = doctorId;
        return this;
    }
}

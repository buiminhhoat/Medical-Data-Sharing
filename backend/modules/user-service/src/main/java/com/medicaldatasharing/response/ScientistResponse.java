package com.medicaldatasharing.response;

import com.medicaldatasharing.model.Scientist;
import com.medicaldatasharing.util.StringUtil;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class ScientistResponse extends UserResponse {
    private String doctorId;

    private String avatar;
    private String researchCenterId;
    private String researchCenterName;
    private String dateBirthday;
    private String gender;

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
        this.dateBirthday = StringUtil.parseDate(scientist.getDateBirthday());
        this.gender = scientist.getGender();
    }

    public String getDoctorId() {
        return doctorId;
    }

    public ScientistResponse setDoctorId(String doctorId) {
        this.doctorId = doctorId;
        return this;
    }
}

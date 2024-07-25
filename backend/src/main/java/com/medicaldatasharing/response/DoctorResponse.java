package com.medicaldatasharing.response;

import com.medicaldatasharing.model.Doctor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DoctorResponse extends UserResponse {
    private String doctorId;

    private String department;

    private String avatar;
    private String medicalInstitutionId;
    private String medicalInstitutionName;

    public DoctorResponse(Doctor doctor) {
        this.id = doctor.getId();
        this.doctorId = doctor.getId();
        this.email = doctor.getEmail();
        this.fullName = doctor.getFullName();
        this.department = doctor.getDepartment();
        this.medicalInstitutionId = doctor.getMedicalInstitutionId();
        this.avatar = doctor.getAvatar();
        this.role = doctor.getRole();
        this.address = doctor.getAddress();
        this.enabled = String.valueOf(doctor.isEnabled());
    }

    public String getDoctorId() {
        return doctorId;
    }

    public DoctorResponse setDoctorId(String doctorId) {
        this.doctorId = doctorId;
        return this;
    }
}

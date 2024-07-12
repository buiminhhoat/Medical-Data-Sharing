package com.medicaldatasharing.response;

import com.medicaldatasharing.model.Doctor;

public class DoctorResponse extends UserResponse {
    private String doctorId;

    private String department;

    private String avatar;
    private String medicalInstitutionId;
    private String medicalInstitutionName;

    public DoctorResponse(Doctor doctor) {
        this.doctorId = doctor.getId();
        this.email = doctor.getEmail();
        this.fullName = doctor.getFullName();
        this.department = doctor.getDepartment();
        this.medicalInstitutionId = doctor.getMedicalInstitutionId();
        this.avatar = doctor.getAvatar();
    }

    public String getDoctorId() {
        return doctorId;
    }

    public DoctorResponse setDoctorId(String doctorId) {
        this.doctorId = doctorId;
        return this;
    }

    public String getEmail() {
        return email;
    }

    public DoctorResponse setEmail(String email) {
        this.email = email;
        return this;
    }

    public String getFullName() {
        return fullName;
    }

    public DoctorResponse setFullName(String fullName) {
        this.fullName = fullName;
        return this;
    }

    public String getDepartment() {
        return department;
    }

    public DoctorResponse setDepartment(String department) {
        this.department = department;
        return this;
    }

    public String getMedicalInstitutionId() {
        return medicalInstitutionId;
    }

    public DoctorResponse setMedicalInstitutionId(String medicalInstitutionId) {
        this.medicalInstitutionId = medicalInstitutionId;
        return this;
    }

    public String getMedicalInstitutionName() {
        return medicalInstitutionName;
    }

    public DoctorResponse setMedicalInstitutionName(String medicalInstitutionName) {
        this.medicalInstitutionName = medicalInstitutionName;
        return this;
    }

    public String getAvatar() {
        return avatar;
    }

    public DoctorResponse setAvatar(String avatar) {
        this.avatar = avatar;
        return this;
    }
}

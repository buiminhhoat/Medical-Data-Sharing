package com.medicaldatasharing.response;

import com.medicaldatasharing.model.Doctor;

public class DoctorDataResponse {
    private String doctorId;
    private String email;
    private String fullName;

    private String department;

    private String avatar;
    private String medicalInstitutionId;
    private String medicalInstitutionName;

    public DoctorDataResponse(Doctor doctor) {
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

    public DoctorDataResponse setDoctorId(String doctorId) {
        this.doctorId = doctorId;
        return this;
    }

    public String getEmail() {
        return email;
    }

    public DoctorDataResponse setEmail(String email) {
        this.email = email;
        return this;
    }

    public String getFullName() {
        return fullName;
    }

    public DoctorDataResponse setFullName(String fullName) {
        this.fullName = fullName;
        return this;
    }

    public String getDepartment() {
        return department;
    }

    public DoctorDataResponse setDepartment(String department) {
        this.department = department;
        return this;
    }

    public String getMedicalInstitutionId() {
        return medicalInstitutionId;
    }

    public DoctorDataResponse setMedicalInstitutionId(String medicalInstitutionId) {
        this.medicalInstitutionId = medicalInstitutionId;
        return this;
    }

    public String getMedicalInstitutionName() {
        return medicalInstitutionName;
    }

    public DoctorDataResponse setMedicalInstitutionName(String medicalInstitutionName) {
        this.medicalInstitutionName = medicalInstitutionName;
        return this;
    }

    public String getAvatar() {
        return avatar;
    }

    public DoctorDataResponse setAvatar(String avatar) {
        this.avatar = avatar;
        return this;
    }
}

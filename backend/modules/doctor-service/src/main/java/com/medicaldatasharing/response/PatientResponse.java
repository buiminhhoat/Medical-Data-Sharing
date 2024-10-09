package com.medicaldatasharing.response;

import com.medicaldatasharing.model.Patient;
import com.medicaldatasharing.util.StringUtil;
import com.owlike.genson.annotation.JsonProperty;

public class PatientResponse extends UserResponse {
    @JsonProperty("patientId")
    protected String patientId;

    @JsonProperty("patientName")
    protected String patientName;

    @JsonProperty("gender")
    protected String gender;

    @JsonProperty("dateBirthday")
    protected String dateBirthday;

    @JsonProperty("address")
    protected String address;

    public PatientResponse() {

    }

    public PatientResponse(Patient patient) {
        this.id = patient.getId();
        this.patientId = patient.getId();
        this.fullName = patient.getFullName();
        this.patientName = patient.getFullName();
        this.role = patient.getRole();
        this.gender = patient.getGender();
        this.email = patient.getEmail();
        this.address = patient.getAddress();
        this.dateBirthday = StringUtil.parseDate(patient.getDateBirthday());
        this.enabled = String.valueOf(patient.isEnabled());
    }
}

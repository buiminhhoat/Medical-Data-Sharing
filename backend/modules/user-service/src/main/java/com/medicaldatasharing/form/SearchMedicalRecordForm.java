package com.medicaldatasharing.form;

import com.medicaldatasharing.util.AESUtil;
import lombok.*;

import java.util.Date;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SearchMedicalRecordForm {
    private String medicalRecordId;
    private String patientId;
    private String doctorId;
    private String medicalInstitutionId;
    private Date from;
    private Date until;
    private String testName;
    private String medicalRecordStatus;
    private String details;
    private String sortingOrder;
    private String prescriptionId;
    private String hashFile;

    public void encrypt() throws Exception {
        this.testName = AESUtil.encrypt(this.testName);
        this.details = AESUtil.encrypt(this.details);
        this.hashFile = AESUtil.encrypt(this.hashFile);
    }

    public void decrypt() throws Exception {
        this.testName = AESUtil.decrypt(this.testName);
        this.details = AESUtil.decrypt(this.details);
        this.hashFile = AESUtil.decrypt(this.hashFile);
    }
}


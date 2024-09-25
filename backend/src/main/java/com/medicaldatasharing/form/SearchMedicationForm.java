package com.medicaldatasharing.form;

import com.medicaldatasharing.util.AESUtil;
import lombok.*;

import java.util.Date;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SearchMedicationForm {
    private String medicationId;

    private String manufacturerId;

    private String medicationName;

    private String description;

    private Date from;

    private Date until;

    private String sortingOrder;

    public void encrypt() throws Exception {
        this.medicationName = AESUtil.encrypt(this.medicationName);
        this.description = AESUtil.encrypt(this.description);
    }

    public void decrypt() throws Exception {
        this.medicationName = AESUtil.decrypt(this.medicationName);
        this.description = AESUtil.decrypt(this.description);
    }
}

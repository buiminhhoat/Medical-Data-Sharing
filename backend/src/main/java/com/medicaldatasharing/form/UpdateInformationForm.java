package com.medicaldatasharing.form;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UpdateInformationForm {
    private String fullName;

    private String address;

    private Date dateBirthday;

    private String department;

    private String businessLicenseNumber;

    private String gender;
}

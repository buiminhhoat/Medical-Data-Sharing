package com.medicaldatasharing.security.dto;

import lombok.Getter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.Date;

@Getter
public class RegisterDto {
    @NotBlank
    @Size(max = 100)
    private String email;

    @NotBlank
    @Size(min = 8, max = 40)
    private String password;

    @NotBlank
    private String fullName;

    @NotBlank
    private String gender;

    @NotBlank
    private Date birthday;
}

package com.medicaldatasharing.security.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LoginDto {
    @NotBlank
    @Size(min = 8, max = 60)
    private String email;

    @NotBlank
    @Size(min = 8, max = 60)
    private String password;
}

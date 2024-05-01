package com.healthcare.security.controller;

import com.healthcare.chaincode.Config;
import com.healthcare.chaincode.client.RegisterUserHyperledger;
import com.healthcare.model.Patient;
import com.healthcare.repository.PatientRepository;
import com.healthcare.security.dto.RegisterDto;
import com.healthcare.security.service.UserDetailsServiceImpl;
import com.healthcare.util.Constants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.security.auth.message.AuthException;
import javax.validation.Valid;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/auth")
public class AuthRestAPIs {
    @Autowired
    private PatientRepository patientRepository;

    @Autowired
    private PasswordEncoder userPasswordEncoder;

    @Autowired
    private UserDetailsServiceImpl userDetailsService;

    @PostMapping(value = "/register")
    public String register(@Valid @RequestBody RegisterDto registerDto, BindingResult result) throws AuthException {
        if (result.hasErrors()) {
            throw new AuthException("Error while register");
        }

        if (userDetailsService.getUser(registerDto.getEmail()) != null) {
            throw new AuthException("Username already exists");
        }

        Patient patient = Patient
                .builder()
                .firstName(registerDto.getFirstName())
                .lastName(registerDto.getLastName())
                .gender(registerDto.getGender())
                .birthday(registerDto.getBirthday())
                .username(registerDto.getEmail())
                .email(registerDto.getEmail())
                .password(userPasswordEncoder.encode(registerDto.getPassword()))
                .enabled(true)
                .role(Constants.ROLE_PATIENT)
                .build();
        Patient patientSaved = patientRepository.save(patient);

        try {
            String userIdentityId = patientSaved.getId();
            RegisterUserHyperledger.enrollOrgAppUsers(patientSaved.getEmail(), Config.PATIENT_ORG, userIdentityId);
        } catch (Exception e) {
            throw new AuthException("Error while register in Hyperledger");
        }
        return "Success";
    }
}


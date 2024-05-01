package com.healthcare.security.controller;

import com.healthcare.chaincode.Config;
import com.healthcare.chaincode.client.RegisterUserHyperledger;
import com.healthcare.model.Patient;
import com.healthcare.model.User;
import com.healthcare.repository.PatientRepository;
import com.healthcare.security.dto.ErrorResponse;
import com.healthcare.security.dto.JwtResponse;
import com.healthcare.security.dto.LoginDto;
import com.healthcare.security.dto.RegisterDto;
import com.healthcare.security.jwt.JwtProvider;
import com.healthcare.security.service.UserDetailsServiceImpl;
import com.healthcare.util.Constants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
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

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtProvider jwtProvider;

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
            patientRepository.delete(patient);
            throw new AuthException("Error while register in Hyperledger");
        }
        return "Success";
    }

    @PostMapping("/login")
    public ResponseEntity<?> authenticateUser(@RequestBody LoginDto loginDto) {
        User user = userDetailsService.getUser(loginDto.getUsername());

        if (user == null) {
            return new ResponseEntity<>(new ErrorResponse("Invalid username or password", HttpStatus.UNAUTHORIZED), HttpStatus.UNAUTHORIZED);
        }

        if (!user.isEnabled()) {
            return new ResponseEntity<>(new ErrorResponse("Invalid username or password", HttpStatus.UNAUTHORIZED), HttpStatus.UNAUTHORIZED);
        }

        Authentication authentication = null;
        try {
            authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            loginDto.getUsername(),
                            loginDto.getPassword()
                    )
            );
        } catch (AuthenticationException e) {
            System.out.println("Not validation");
            return new ResponseEntity<>(new ErrorResponse("Invalid username or password", HttpStatus.UNAUTHORIZED), HttpStatus.UNAUTHORIZED);
        }

        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = jwtProvider.generateJwtToken(authentication);
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();

        return ResponseEntity.ok(new JwtResponse(jwt, userDetails.getUsername(), userDetails.getAuthorities()));
    }
}


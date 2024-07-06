package com.medicaldatasharing.security.controller;

import com.medicaldatasharing.chaincode.Config;
import com.medicaldatasharing.chaincode.client.RegisterUserHyperledger;
import com.medicaldatasharing.model.Patient;
import com.medicaldatasharing.model.User;
import com.medicaldatasharing.repository.PatientRepository;
import com.medicaldatasharing.response.GetUserDataResponse;
import com.medicaldatasharing.security.dto.ErrorResponse;
import com.medicaldatasharing.security.dto.JwtResponse;
import com.medicaldatasharing.security.dto.LoginDto;
import com.medicaldatasharing.security.dto.RegisterDto;
import com.medicaldatasharing.security.jwt.JwtProvider;
import com.medicaldatasharing.security.service.UserDetailsServiceImpl;
import com.medicaldatasharing.util.Constants;
import org.json.JSONObject;
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
@RequestMapping("/public")
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
                .fullName(registerDto.getFullName())
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
    public ResponseEntity<?> authenticateUser(@ModelAttribute LoginDto loginDto) {
        User user = userDetailsService.getUser(loginDto.getEmail());

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
                            loginDto.getEmail(),
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

    @GetMapping("/get-user-data")
    public ResponseEntity<?> getUserData(@RequestHeader("Authorization") String access_token) {
        User user = null;
        if (access_token != null && access_token.startsWith("Bearer ")) {
            access_token = access_token.replace("Bearer ", "");
            String username = jwtProvider.getUserNameFromJwtToken(access_token);
            user = userDetailsService.getUser(username);
        }

        if (user == null) {
            return new ResponseEntity<>(new ErrorResponse("Invalid access token", HttpStatus.UNAUTHORIZED), HttpStatus.UNAUTHORIZED);
        }

        if (!user.isEnabled()) {
            return new ResponseEntity<>(new ErrorResponse("Invalid access token", HttpStatus.UNAUTHORIZED), HttpStatus.UNAUTHORIZED);
        }

        return ResponseEntity.ok(new GetUserDataResponse(user.getFullName(), user.getRole()));
    }
}


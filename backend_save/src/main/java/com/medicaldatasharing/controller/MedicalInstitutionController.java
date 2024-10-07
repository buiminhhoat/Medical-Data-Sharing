package com.medicaldatasharing.controller;

import com.medicaldatasharing.form.RegisterForm;
import com.medicaldatasharing.security.dto.Response;
import com.medicaldatasharing.service.MedicalInstitutionService;
import com.medicaldatasharing.util.Constants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.Objects;

@RestController
@RequestMapping("/medical_institution")
public class MedicalInstitutionController {
    @Autowired
    private MedicalInstitutionService medicalInstitutionService;

    @PostMapping("/get-all-doctor-by-medical-institution")
    public ResponseEntity<?> getAllDoctorByMedicalInstitution() throws Exception {
        try {
            String getAllDoctorByMedicalInstitution = medicalInstitutionService.getAllDoctorByMedicalInstitution();
            return ResponseEntity.status(HttpStatus.OK).body(getAllDoctorByMedicalInstitution);
        }
        catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }

    @PostMapping("/get-user-info")
    public ResponseEntity<?> getUserInfo(HttpServletRequest httpServletRequest) throws Exception {
        try {
            String id = httpServletRequest.getParameter("id");
            String getUserInfo = medicalInstitutionService.getUserInfo(id);
            return ResponseEntity.status(HttpStatus.OK).body(getUserInfo);
        }
        catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }

    @PostMapping("/register-user")
    public ResponseEntity<?> registerUser(@Valid @ModelAttribute RegisterForm registerForm,
                                          BindingResult result) {
        try {
            String registerUser = "";

            if (Objects.equals(registerForm.getRole(), Constants.ROLE_DOCTOR)) {
                if (registerForm.getDepartment().isEmpty()) {
                    return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new Response("Chuyên khoa không được bỏ trống!"));
                }
                registerUser = medicalInstitutionService.registerDoctor(registerForm);
            }
            else {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new Response("Vai trò của người dùng phải là bác sỹ!"));
            }

            return ResponseEntity.status(HttpStatus.OK).body(registerUser);
        }
        catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }
}

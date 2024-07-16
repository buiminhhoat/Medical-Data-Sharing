package com.medicaldatasharing.controller;

import com.medicaldatasharing.chaincode.dto.PrescriptionDetails;
import com.medicaldatasharing.dto.GetListAuthorizedMedicalRecordByDoctorQueryDto;
import com.medicaldatasharing.form.AddMedicalRecordForm;
import com.medicaldatasharing.form.AddPrescriptionForm;
import com.medicaldatasharing.form.RegisterForm;
import com.medicaldatasharing.form.SendViewRequestForm;
import com.medicaldatasharing.security.dto.ErrorResponse;
import com.medicaldatasharing.security.service.UserDetailsServiceImpl;
import com.medicaldatasharing.service.AdminService;
import com.medicaldatasharing.service.DoctorService;
import com.medicaldatasharing.service.UserService;
import com.medicaldatasharing.util.StringUtil;
import com.owlike.genson.GenericType;
import com.owlike.genson.Genson;
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
import java.util.Date;
import java.util.List;
import java.util.Objects;

@RestController
@RequestMapping("/admin")
public class AdminController {
    @Autowired
    private UserService userService;

    @Autowired
    private DoctorService doctorService;

    @Autowired
    private AdminService adminService;

    @Autowired
    private UserDetailsServiceImpl userDetailsService;

    @PostMapping("/get-all-user-by-admin")
    public ResponseEntity<?> getAllUserByAdmin() throws Exception {
        try {
            String getAllUserByAdmin = adminService.getAllUserByAdmin();
            return ResponseEntity.status(HttpStatus.OK).body(getAllUserByAdmin);
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
            if (Objects.equals(registerForm.getRole(), "Cơ sở y tế")) {
                registerUser = adminService.registerMedicalInstitution(registerForm);
            }

            if (Objects.equals(registerForm.getRole(), "Công ty sản xuất thuốc")) {
                if (registerForm.getBusinessLicenseNumber().isEmpty()) {
                    return new ResponseEntity<>(new ErrorResponse("Business License Number is empty",
                            HttpStatus.UNAUTHORIZED), HttpStatus.UNAUTHORIZED);
                }
                registerUser = adminService.registerManufacturer(registerForm);
            }

            if (Objects.equals(registerForm.getRole(), "Trung tâm nghiên cứu")) {
                if (registerForm.getBusinessLicenseNumber().isEmpty()) {
                    return new ResponseEntity<>(new ErrorResponse("Business License Number is empty",
                            HttpStatus.UNAUTHORIZED), HttpStatus.UNAUTHORIZED);
                }
                registerUser = adminService.registerResearchCenter(registerForm);
            }

            return ResponseEntity.status(HttpStatus.OK).body(registerUser);
        }
        catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }
}

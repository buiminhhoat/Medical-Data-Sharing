package com.medicaldatasharing.controller;

import com.medicaldatasharing.chaincode.dto.PrescriptionDetails;
import com.medicaldatasharing.dto.GetListAuthorizedMedicalRecordByDoctorQueryDto;
import com.medicaldatasharing.form.AddMedicalRecordForm;
import com.medicaldatasharing.form.AddPrescriptionForm;
import com.medicaldatasharing.form.SendViewRequestForm;
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
}

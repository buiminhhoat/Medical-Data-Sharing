package com.medicaldatasharing.controller;

import com.medicaldatasharing.form.RegisterForm;
import com.medicaldatasharing.security.dto.Response;
import com.medicaldatasharing.service.ResearchCenterService;
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
@RequestMapping("/research_center")
public class ResearchCenterController {
    @Autowired
    private ResearchCenterService researchCenterService;

    @PostMapping("/get-all-scientist-by-research-center")
    public ResponseEntity<?> getAllScientistByResearchCenter() throws Exception {
        try {
            String getAllScientistByResearchCenter = researchCenterService.getAllScientistByResearchCenter();
            return ResponseEntity.status(HttpStatus.OK).body(getAllScientistByResearchCenter);
        }
        catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }

    @PostMapping("/get-user-info")
    public ResponseEntity<?> getUserInfo(HttpServletRequest httpServletRequest) throws Exception {
        try {
            String id = httpServletRequest.getParameter("id");
            String getUserInfo = researchCenterService.getUserInfo(id);
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

            if (Objects.equals(registerForm.getRole(), Constants.ROLE_SCIENTIST)) {
                registerUser = researchCenterService.registerScientist(registerForm);
            }
            else {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(
                        new Response("Vai trò của người dùng phải là Nhà khoa học"));
            }

            return ResponseEntity.status(HttpStatus.OK).body(registerUser);
        }
        catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }
}

package com.medicaldatasharing.controller;

import com.medicaldatasharing.form.ChangePasswordForm;
import com.medicaldatasharing.form.DefineRequestForm;
import com.medicaldatasharing.form.UpdateInformationForm;
import com.medicaldatasharing.security.dto.Response;
import com.medicaldatasharing.service.UserService;
import com.medicaldatasharing.util.ValidationUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.security.auth.message.AuthException;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import javax.validation.ValidationException;

@RestController
@RequestMapping("/user")
public class UserController {
    @Autowired
    private UserService userService;

    @GetMapping("/get-full-name/{id}")
    public String getFullName(@PathVariable String id) throws Exception {
        try {
            return userService.getFullName(id);
        }
        catch (Exception e) {
            return "No found";
        }
    }
}

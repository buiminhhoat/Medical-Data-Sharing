package com.medicaldatasharing.controller;

import com.medicaldatasharing.service.DoctorService;
import com.medicaldatasharing.service.HyperledgerService;
import com.medicaldatasharing.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/public")
public class UserController {
    @Autowired
    private DoctorService doctorService;

    @Autowired
    private UserService userService;

    @Autowired
    private HyperledgerService hyperledgerService;

    @GetMapping("/get-all-request")
    public ResponseEntity<?> getAllRequest() throws Exception {
        try {
            String getAllRequest = userService.getAllRequest();
            return ResponseEntity.status(HttpStatus.OK).body(getAllRequest);
        }
        catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }

//    @PostMapping("/sendRequest")
//    public SendRequestDto sendRequest(
//            @Valid @ModelAttribute SendRequestForm sendRequestForm,
//            BindingResult result) throws Exception {
//        if (result.hasErrors()) {
//            String errorMsg = ValidationUtil.formatValidationErrorMessages(result.getAllErrors());
//            throw new ValidationException(errorMsg);
//        }
//
//        return doctorService.sendRequest(sendRequestForm);
//        return null;
//    }
}

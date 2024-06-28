package com.medicaldatasharing.controller;

import com.medicaldatasharing.service.DoctorService;
import com.medicaldatasharing.service.PatientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/patient")
public class PatientController {
    @Autowired
    private DoctorService doctorService;

    @Autowired
    private PatientService patientService;

//    @PostMapping("/defineRequest")
//    public DefineRequestDto defineRequest(
//            @Valid @ModelAttribute DefineRequestForm defineRequestForm,
//            BindingResult result) throws Exception {
//        if (result.hasErrors()) {
//            String errorMsg = ValidationUtil.formatValidationErrorMessages(result.getAllErrors());
//            throw new ValidationException(errorMsg);
//        }
//
//        return patientService.defineRequest(defineRequestForm);
//    }
}

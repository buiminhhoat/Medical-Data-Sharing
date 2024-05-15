package com.medicaldatasharing.controller;

import com.medicaldatasharing.dto.MedicalRecordAccessDefineRequestDto;
import com.medicaldatasharing.dto.MedicalRecordDto;
import com.medicaldatasharing.dto.form.MedicalRecordAccessDefineRequestForm;
import com.medicaldatasharing.dto.form.MedicalRecordForm;
import com.medicaldatasharing.service.DoctorService;
import com.medicaldatasharing.service.PatientService;
import com.medicaldatasharing.util.ValidationUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import javax.validation.ValidationException;

@RestController
@RequestMapping("/patient")
public class PatientController {
    @Autowired
    private DoctorService doctorService;

    @Autowired
    private PatientService patientService;

    @PostMapping("/defineMedicalRecordAccessRequest")
    public MedicalRecordAccessDefineRequestDto defineMedicalRecordAccessRequest(
            @Valid @ModelAttribute MedicalRecordAccessDefineRequestForm medicalRecordAccessDefineRequestForm,
            BindingResult result) throws Exception {
        if (result.hasErrors()) {
            String errorMsg = ValidationUtil.formatValidationErrorMessages(result.getAllErrors());
            throw new ValidationException(errorMsg);
        }

        return patientService.defineMedicalRecordAccessRequest(medicalRecordAccessDefineRequestForm);
    }
}

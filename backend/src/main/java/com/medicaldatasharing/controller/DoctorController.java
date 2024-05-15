package com.medicaldatasharing.controller;

import com.medicaldatasharing.dto.MedicalRecordDto;
import com.medicaldatasharing.dto.form.MedicalRecordForm;
import com.medicaldatasharing.service.DoctorService;
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
@RequestMapping("/doctor")
public class DoctorController {
    @Autowired
    private DoctorService doctorService;

    @PostMapping("/addMedicalRecord")
    public MedicalRecordDto addMedicalRecord(@Valid @ModelAttribute MedicalRecordForm medicalRecordForm, BindingResult result) throws Exception {
        if (result.hasErrors()) {
            String errorMsg = ValidationUtil.formatValidationErrorMessages(result.getAllErrors());
            throw new ValidationException(errorMsg);
        }

        return doctorService.addMedicalRecord(medicalRecordForm);
    }
}

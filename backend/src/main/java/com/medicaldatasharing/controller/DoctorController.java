package com.medicaldatasharing.controller;

import com.medicaldatasharing.dto.MedicalRecordDto;
import com.medicaldatasharing.form.MedicalRecordForm;
import com.medicaldatasharing.form.SearchMedicalRecordForm;
import com.medicaldatasharing.security.service.UserDetailsServiceImpl;
import com.medicaldatasharing.service.DoctorService;
import com.medicaldatasharing.service.UserService;
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

@RestController
@RequestMapping("/doctor")
public class DoctorController {
    @Autowired
    private UserService userService;

    @Autowired
    private DoctorService doctorService;

    @Autowired
    private UserDetailsServiceImpl userDetailsService;

    @PostMapping("/get-list-medical-record-by-patientId")
    public ResponseEntity<?> getListMedicalRecord(HttpServletRequest httpServletRequest) {
        try {
            String patientId = httpServletRequest.getParameter("patientId");
            SearchMedicalRecordForm searchMedicalRecordForm = new SearchMedicalRecordForm();
            searchMedicalRecordForm.setDoctorId(userDetailsService.getLoggedUser().getId());
            searchMedicalRecordForm.setPatientId(patientId);
            String getListMedicalRecord = doctorService.getListMedicalRecord(searchMedicalRecordForm);
            return ResponseEntity.status(HttpStatus.OK).body(getListMedicalRecord);
        }
        catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }

    @PostMapping("/addMedicalRecord")
    public MedicalRecordDto addMedicalRecord(@Valid @ModelAttribute MedicalRecordForm medicalRecordForm, BindingResult result) throws Exception {
//        if (result.hasErrors()) {
//            String errorMsg = ValidationUtil.formatValidationErrorMessages(result.getAllErrors());
//            throw new ValidationException(errorMsg);
//        }
//
//        return doctorService.addMedicalRecord(medicalRecordForm);
        return null;
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

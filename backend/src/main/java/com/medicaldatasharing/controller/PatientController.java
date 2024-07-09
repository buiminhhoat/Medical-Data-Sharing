package com.medicaldatasharing.controller;

import com.medicaldatasharing.form.SearchMedicalRecordForm;
import com.medicaldatasharing.security.service.UserDetailsServiceImpl;
import com.medicaldatasharing.service.DoctorService;
import com.medicaldatasharing.service.PatientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/patient")
public class PatientController {
    @Autowired
    private DoctorService doctorService;

    @Autowired
    private PatientService patientService;

    @Autowired
    private UserDetailsServiceImpl userDetailsService;

    @PostMapping("/get-list-medical-record-by-patientId")
    public ResponseEntity<?> getListMedicalRecordByPatientId(HttpServletRequest httpServletRequest) {
        try {
            String patientId = httpServletRequest.getParameter("patientId");
            SearchMedicalRecordForm searchMedicalRecordForm = new SearchMedicalRecordForm();
            searchMedicalRecordForm.setPatientId(userDetailsService.getLoggedUser().getId());
            String getListMedicalRecord = patientService.getListMedicalRecordByPatientId(searchMedicalRecordForm);
            return ResponseEntity.status(HttpStatus.OK).body(getListMedicalRecord);
        }
        catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }

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

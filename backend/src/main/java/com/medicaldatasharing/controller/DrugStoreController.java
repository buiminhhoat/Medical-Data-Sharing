package com.medicaldatasharing.controller;

import com.medicaldatasharing.dto.SendViewPrescriptionRequestDto;
import com.medicaldatasharing.form.GetPrescriptionForm;
import com.medicaldatasharing.form.SendAppointmentRequestForm;
import com.medicaldatasharing.form.SendViewPrescriptionRequestForm;
import com.medicaldatasharing.model.Doctor;
import com.medicaldatasharing.model.DrugStore;
import com.medicaldatasharing.security.service.UserDetailsServiceImpl;
import com.medicaldatasharing.service.DoctorService;
import com.medicaldatasharing.service.DrugStoreService;
import com.medicaldatasharing.service.PatientService;
import com.medicaldatasharing.util.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.Date;

@RestController
@RequestMapping("/drugstore")
public class DrugStoreController {
    @Autowired
    private DoctorService doctorService;

    @Autowired
    private PatientService patientService;

    @Autowired
    private DrugStoreService drugStoreService;

    @Autowired
    private UserDetailsServiceImpl userDetailsService;

    @PostMapping("/get-prescription-by-drugstore")
    public ResponseEntity<?> getPrescriptionByDrugStore(@Valid @ModelAttribute GetPrescriptionForm getPrescriptionForm,
                                                             BindingResult result) throws Exception {
        try {
            String getPrescriptionByPrescriptionId = drugStoreService.getPrescriptionByDrugStore(getPrescriptionForm);
            return ResponseEntity.status(HttpStatus.OK).body(getPrescriptionByPrescriptionId);
        }
        catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }

    @PostMapping("/send-view-prescription-request")
    public ResponseEntity<?> sendViewPrescriptionRequest(@Valid @ModelAttribute SendViewPrescriptionRequestForm sendViewPrescriptionRequestForm,
                                                         BindingResult result) throws Exception {
        try {
            sendViewPrescriptionRequestForm.setDateCreated(StringUtil.parseDate(new Date()));
            sendViewPrescriptionRequestForm.setDateModified(StringUtil.parseDate(new Date()));
            sendViewPrescriptionRequestForm.setSenderId(userDetailsService.getLoggedUser().getId());
            String viewPrescriptionRequestStr = drugStoreService.sendViewPrescriptionRequest(sendViewPrescriptionRequestForm);
            return ResponseEntity.status(HttpStatus.OK).body(viewPrescriptionRequestStr);
        }
        catch (Exception exception) {
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

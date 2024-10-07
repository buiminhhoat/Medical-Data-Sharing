package com.medicaldatasharing.controller;

import com.medicaldatasharing.dto.GetListAuthorizedMedicalRecordByScientistQueryDto;
import com.medicaldatasharing.form.GetPrescriptionForm;
import com.medicaldatasharing.form.SendViewRequestForm;
import com.medicaldatasharing.service.ScientistService;
import com.medicaldatasharing.util.StringUtil;
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

@RestController
@RequestMapping("/scientist")
public class ScientistController {
    @Autowired
    private ScientistService scientistService;

    @PostMapping("/get-list-medical-record-by-scientistId")
    public ResponseEntity<?> getListMedicalRecord(HttpServletRequest httpServletRequest) {
        try {
            String patientId = httpServletRequest.getParameter("patientId");
            GetListAuthorizedMedicalRecordByScientistQueryDto getListAuthorizedMedicalRecordByScientistQueryDto = new GetListAuthorizedMedicalRecordByScientistQueryDto();
            getListAuthorizedMedicalRecordByScientistQueryDto.setScientistId(scientistService.getLoggedUser().getId());
            getListAuthorizedMedicalRecordByScientistQueryDto.setPatientId(patientId);
            String getListMedicalRecord = scientistService.getListMedicalRecord(getListAuthorizedMedicalRecordByScientistQueryDto);
            return ResponseEntity.status(HttpStatus.OK).body(getListMedicalRecord);
        }
        catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }

    @PostMapping("/get-prescription-by-scientist")
    public ResponseEntity<?> getPrescriptionByScientist(@Valid @ModelAttribute GetPrescriptionForm getPrescriptionForm,
                                                        BindingResult result) throws Exception {
        try {
            String getPrescriptionByPrescriptionId = scientistService.getPrescriptionByScientist(getPrescriptionForm);
            return ResponseEntity.status(HttpStatus.OK).body(getPrescriptionByPrescriptionId);
        }
        catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }

    @PostMapping("/get-all-patient-managed-by-scientistId")
    public ResponseEntity<?> getAllPatientManagedByScientistId() throws Exception {
        try {
            String getAllPatientManagedByScientistId = scientistService.getAllPatientManagedByScientistId();
            return ResponseEntity.status(HttpStatus.OK).body(getAllPatientManagedByScientistId);
        }
        catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }

    @PostMapping("/send-view-request")
    public ResponseEntity<?> sendViewRequest(@Valid @ModelAttribute SendViewRequestForm sendViewRequestForm,
                                             BindingResult result) throws Exception {
        try {
            sendViewRequestForm.setDateCreated(StringUtil.parseDate(new Date()));
            sendViewRequestForm.setDateModified(StringUtil.parseDate(new Date()));
            String viewRequest = scientistService.sendViewRequest(sendViewRequestForm);
            return ResponseEntity.status(HttpStatus.OK).body(viewRequest);
        }
        catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }
}

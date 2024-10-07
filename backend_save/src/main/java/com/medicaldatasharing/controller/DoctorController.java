package com.medicaldatasharing.controller;

import com.medicaldatasharing.chaincode.dto.PrescriptionDetails;
import com.medicaldatasharing.dto.GetListAuthorizedMedicalRecordByDoctorQueryDto;
import com.medicaldatasharing.form.*;
import com.medicaldatasharing.service.DoctorService;
import com.medicaldatasharing.util.StringUtil;
import com.medicaldatasharing.util.ValidationUtil;
import com.owlike.genson.GenericType;
import com.owlike.genson.Genson;
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
import javax.validation.ValidationException;
import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/doctor")
public class DoctorController {
    @Autowired
    private DoctorService doctorService;

    @PostMapping("/get-list-medical-record-by-patientId")
    public ResponseEntity<?> getListMedicalRecord(HttpServletRequest httpServletRequest) {
        try {
            String patientId = httpServletRequest.getParameter("patientId");
            GetListAuthorizedMedicalRecordByDoctorQueryDto getListAuthorizedMedicalRecordByDoctorQueryDto = new GetListAuthorizedMedicalRecordByDoctorQueryDto();
            getListAuthorizedMedicalRecordByDoctorQueryDto.setDoctorId(doctorService.getLoggedUser().getId());
            getListAuthorizedMedicalRecordByDoctorQueryDto.setPatientId(patientId);
            String getListMedicalRecord = doctorService.getListMedicalRecord(getListAuthorizedMedicalRecordByDoctorQueryDto);
            return ResponseEntity.status(HttpStatus.OK).body(getListMedicalRecord);
        }
        catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }

    @PostMapping("/get-all-medication")
    public ResponseEntity<?> getAllMedication(HttpServletRequest httpServletRequest) {
        try {
            String getAllMedication = doctorService.getAllMedication();
            return ResponseEntity.status(HttpStatus.OK).body(getAllMedication);
        }
        catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }

    @PostMapping("/add-medical-record")
    public ResponseEntity<?> addMedicalRecord(@Valid @ModelAttribute AddMedicalRecordForm addMedicalRecordForm, BindingResult result) throws Exception {
        try {
            addMedicalRecordForm.setDateCreated(StringUtil.parseDate(new Date()));
            addMedicalRecordForm.setDateModified(StringUtil.parseDate(new Date()));
            String addPrescription = addMedicalRecordForm.getAddPrescription();
            List<PrescriptionDetails>  prescriptionDetailsList = new Genson().deserialize(addPrescription,
                            new GenericType<List<PrescriptionDetails>>() {});
            AddPrescriptionForm addPrescriptionForm = new AddPrescriptionForm();
            addPrescriptionForm.setPrescriptionDetailsList(new Genson().serialize(prescriptionDetailsList));
            addMedicalRecordForm.setAddPrescription(addPrescriptionForm.toJSONObject().toString());
            String medicalRecord = doctorService.addMedicalRecord(addMedicalRecordForm);
            return ResponseEntity.status(HttpStatus.OK).body(medicalRecord);
        }
        catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }

    @PostMapping("/send-view-request")
    public ResponseEntity<?> sendViewRequest(@Valid @ModelAttribute SendViewRequestForm sendViewRequestForm, BindingResult result) throws Exception {
        try {
            sendViewRequestForm.setDateCreated(StringUtil.parseDate(new Date()));
            sendViewRequestForm.setDateModified(StringUtil.parseDate(new Date()));
            String viewRequest = doctorService.sendViewRequest(sendViewRequestForm);
            return ResponseEntity.status(HttpStatus.OK).body(viewRequest);
        }
        catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }

    @PostMapping("/get-all-patient-managed-by-doctorId")
    public ResponseEntity<?> getAllPatientManagedByDoctorId() throws Exception {
        try {
            String getAllPatientManagedByDoctorId = doctorService.getAllPatientManagedByDoctorId();
            return ResponseEntity.status(HttpStatus.OK).body(getAllPatientManagedByDoctorId);
        }
        catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }

    @PostMapping("/define-medical-record")
    public ResponseEntity<?> defineMedicalRecord(@Valid @ModelAttribute DefineMedicalRecordForm defineMedicalRecordForm,
                                                 BindingResult result) {
        if (result.hasErrors()) {
            String errorMsg = ValidationUtil.formatValidationErrorMessages(result.getAllErrors());
            throw new ValidationException(errorMsg);
        }

        try {
            String defineMedicalRecord = doctorService.defineMedicalRecord(defineMedicalRecordForm);
            return ResponseEntity.status(HttpStatus.OK).body(defineMedicalRecord);
        }
        catch (Exception exception) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }

    @PostMapping("/get-prescription-by-doctor")
    public ResponseEntity<?> getPrescriptionByDoctor(@Valid @ModelAttribute GetPrescriptionForm getPrescriptionForm,
                                                           BindingResult result) throws Exception {
        try {
            String getPrescriptionByPrescriptionId = doctorService.getPrescriptionByDoctor(getPrescriptionForm);
            return ResponseEntity.status(HttpStatus.OK).body(getPrescriptionByPrescriptionId);
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

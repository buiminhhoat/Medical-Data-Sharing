package com.medicaldatasharing.controller;

import com.medicaldatasharing.form.*;
import com.medicaldatasharing.model.Doctor;
import com.medicaldatasharing.security.dto.Response;
import com.medicaldatasharing.security.service.UserDetailsServiceImpl;
import com.medicaldatasharing.service.PatientService;
import com.medicaldatasharing.util.StringUtil;
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
import java.util.Date;
import com.medicaldatasharing.util.*;

@RestController
@RequestMapping("/patient")
public class PatientController {
    @Autowired
    private PatientService patientService;

    @Autowired
    private UserDetailsServiceImpl userDetailsService;

    @GetMapping("/permit-all/check-health")
    public String checkHealth() {
        return "OK";
    }

    @GetMapping("/permit-all/get-full-name/{id}")
    public String getFullName(@PathVariable String id) throws Exception {
        try {
            return patientService.getFullName(id);
        }
        catch (Exception e) {
            return "Không tìm thấy thông tin của người dùng " + id;
        }
    }

    @PostMapping("/get-list-medical-record-by-patientId")
    public ResponseEntity<?> getListMedicalRecordByPatientId(HttpServletRequest httpServletRequest) {
        try {
            String patientId = httpServletRequest.getParameter("patientId");
            SearchMedicalRecordForm searchMedicalRecordForm = new SearchMedicalRecordForm();
            searchMedicalRecordForm.setPatientId(patientService.getLoggedUser().getId());
            String getListMedicalRecord = patientService.getListMedicalRecordByPatientId(searchMedicalRecordForm);
            return ResponseEntity.status(HttpStatus.OK).body(getListMedicalRecord);
        }
        catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }

    @PostMapping("/get-medical-record-by-medicalRecordId")
    public ResponseEntity<?> getMedicalRecordByMedicalRecordId(HttpServletRequest httpServletRequest) {
        try {
            String medicalRecordId = httpServletRequest.getParameter("medicalRecordId");
            SearchMedicalRecordForm searchMedicalRecordForm = new SearchMedicalRecordForm();
            searchMedicalRecordForm.setPatientId(patientService.getLoggedUser().getId());
            searchMedicalRecordForm.setMedicalRecordId(medicalRecordId);
            String getListMedicalRecord = patientService.getListMedicalRecordByPatientId(searchMedicalRecordForm);
            return ResponseEntity.status(HttpStatus.OK).body(getListMedicalRecord);
        }
        catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }

    @PostMapping("/get-prescription-by-prescriptionId")
    public ResponseEntity<?> getPrescriptionByPrescriptionId(@Valid @ModelAttribute GetPrescriptionForm getPrescriptionForm, BindingResult result) throws Exception {
        try {
            String getPrescriptionByPrescriptionId = patientService.getPrescriptionByPrescriptionId(getPrescriptionForm);
            return ResponseEntity.status(HttpStatus.OK).body(getPrescriptionByPrescriptionId);
        }
        catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }

    // to-do
    @PostMapping("/send-appointment-request")
    public ResponseEntity<?> sendAppointmentRequest(@Valid @ModelAttribute SendAppointmentRequestForm sendAppointmentRequestForm, BindingResult result) throws Exception {
        try {
            sendAppointmentRequestForm.setDateCreated(StringUtil.parseDate(new Date()));
            sendAppointmentRequestForm.setDateModified(StringUtil.parseDate(new Date()));
//            Doctor doctor = (Doctor) userDetailsService.getUserByUserId(sendAppointmentRequestForm.getRecipientId());
//            sendAppointmentRequestForm.setMedicalInstitutionId(doctor.getMedicalInstitutionId());
            String appointmentRequestStr = patientService.sendAppointmentRequest(sendAppointmentRequestForm);
            return ResponseEntity.status(HttpStatus.OK).body(appointmentRequestStr);
        }
        catch (Exception exception) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }

    @PostMapping("/share-prescription-by-patient")
    public ResponseEntity<?> sharePrescriptionByPatient(@Valid @ModelAttribute SendViewPrescriptionRequestForm sendViewPrescriptionRequestForm, BindingResult result) throws Exception {
        try {
            sendViewPrescriptionRequestForm.setDateCreated(StringUtil.parseDate(new Date()));
            sendViewPrescriptionRequestForm.setDateModified(StringUtil.parseDate(new Date()));
            sendViewPrescriptionRequestForm.setRecipientId(patientService.getLoggedUser().getId());
            String sharePrescriptionByPatient = patientService.sharePrescriptionByPatient(sendViewPrescriptionRequestForm);
            return ResponseEntity.status(HttpStatus.OK).body(sharePrescriptionByPatient);
        }
        catch (Exception exception) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }

    @PostMapping("/get-list-purchase-by-patientId")
    public ResponseEntity<?> getListPurchaseByPatientId(@Valid @ModelAttribute SearchPurchaseForm searchPurchaseForm,
                                                        BindingResult result) {
        try {
            String getListPurchaseByPatientId = patientService.getListPurchaseByPatientId(searchPurchaseForm);
            return ResponseEntity.status(HttpStatus.OK).body(getListPurchaseByPatientId);
        }
        catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }

    @PostMapping("/get-purchase-by-purchaseId")
    public ResponseEntity<?> getPurchaseByPurchaseId(HttpServletRequest httpServletRequest) {
        try {
            String purchaseId = httpServletRequest.getParameter("purchaseId");
            String getPurchaseByPurchaseId = patientService.getPurchaseByPurchaseId(purchaseId);
            return ResponseEntity.status(HttpStatus.OK).body(getPurchaseByPurchaseId);
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
            String defineMedicalRecord = patientService.defineMedicalRecord(defineMedicalRecordForm);
            return ResponseEntity.status(HttpStatus.OK).body(defineMedicalRecord);
        }
        catch (Exception exception) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }

    @PostMapping("/update-drug-reaction-by-patient")
    public ResponseEntity<?> updateDrugReactionByPatient(
            @Valid @ModelAttribute UpdateDrugReactionForm updateDrugReactionForm,
                                                 BindingResult result) {
        if (result.hasErrors()) {
            String errorMsg = ValidationUtil.formatValidationErrorMessages(result.getAllErrors());
            throw new ValidationException(errorMsg);
        }

        try {
            updateDrugReactionForm.setDateCreated(StringUtil.parseDate(new Date()));
            updateDrugReactionForm.setDateModified(StringUtil.parseDate(new Date()));
            String updateDrugReactionByPatient = patientService.updateDrugReactionByPatient(updateDrugReactionForm);
            return ResponseEntity.status(HttpStatus.OK).body(updateDrugReactionByPatient);
        }
        catch (Exception exception) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }

    /*  OK  */
    @GetMapping("/get-all-request")
    public ResponseEntity<?> getAllRequest() throws Exception {
        try {
            String getAllRequest = patientService.getAllRequest();
            return ResponseEntity.status(HttpStatus.OK).body(getAllRequest);
        }
        catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }

    /*  OK  */
    @PostMapping("/get-request")
    public ResponseEntity<?> getRequest(HttpServletRequest httpServletRequest) throws Exception {
        String requestId = httpServletRequest.getParameter("requestId");
        String requestType = httpServletRequest.getParameter("requestType");
        if (requestId.isEmpty() || requestType.isEmpty()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        try {
            String request = patientService.getRequest(requestId, requestType);
            return ResponseEntity.status(HttpStatus.OK).body(request);
        }
        catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }

    /*  OK  */
    @PostMapping("/define-request")
    public ResponseEntity<?> defineRequest(
            @Valid @ModelAttribute DefineRequestForm defineRequestForm,
            BindingResult result) throws Exception {
        if (result.hasErrors()) {
            String errorMsg = ValidationUtil.formatValidationErrorMessages(result.getAllErrors());
            throw new ValidationException(errorMsg);
        }

        try {
            String defineRequest = patientService.defineRequest(defineRequestForm);
            return ResponseEntity.status(HttpStatus.OK).body(defineRequest);
        }
        catch (Exception exception) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }

    /*  OK  */
    @GetMapping("/get-list-drug-by-ownerId")
    public ResponseEntity<?> getListDrugByOwnerId() throws Exception {
        try {
            String getListDrugByOwnerId = patientService.getListDrugByOwnerId();
            return ResponseEntity.status(HttpStatus.OK).body(getListDrugByOwnerId);
        }
        catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }

    /*  OK  */
    @PostMapping("/change-password")
    public ResponseEntity<?> changePassword(@ModelAttribute ChangePasswordForm changePasswordForm, BindingResult result) throws AuthException {
        if (result.hasErrors()) {
            String errorMsg = ValidationUtil.formatValidationErrorMessages(result.getAllErrors());
            throw new AuthException(errorMsg);
        }

        if (changePasswordForm.getOldPassword().isEmpty()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new Response("Mật khẩu cũ không được bỏ trống!"));
        }

        if (changePasswordForm.getPassword().isEmpty()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new Response("Mật khẩu mới không được bỏ trống!"));
        }

        try {
            String changePassword = patientService.changePassword(changePasswordForm);
            return ResponseEntity.status(HttpStatus.OK).body(changePassword);
        }
        catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }

    /*  OK  */
    @PostMapping("/update-information")
    public ResponseEntity<?> updateInformation(@ModelAttribute UpdateInformationForm updateInformationForm, BindingResult result) throws AuthException {
        if (result.hasErrors()) {
            String errorMsg = ValidationUtil.formatValidationErrorMessages(result.getAllErrors());
//            throw new AuthException(errorMsg);
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        try {
            String updateInformation = patientService.updateInformation(updateInformationForm);
            return ResponseEntity.status(HttpStatus.OK).body(updateInformation);
        }
        catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }
}

package com.medicaldatasharing.controller;

import com.medicaldatasharing.chaincode.dto.PrescriptionDetails;
import com.medicaldatasharing.dto.GetListAuthorizedMedicalRecordByDoctorQueryDto;
import com.medicaldatasharing.form.*;
import com.medicaldatasharing.response.DoctorResponse;
import com.medicaldatasharing.response.PatientResponse;
import com.medicaldatasharing.security.dto.Response;
import com.medicaldatasharing.service.DoctorService;
import com.medicaldatasharing.util.StringUtil;
import com.medicaldatasharing.util.ValidationUtil;
import com.owlike.genson.GenericType;
import com.owlike.genson.Genson;
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
import java.util.List;

@RestController
@RequestMapping("/doctor")
public class DoctorController {
    @Autowired
    private DoctorService doctorService;

    @GetMapping("/permit-all/get-doctor-response/{id}")
    public DoctorResponse getDoctorResponse(@PathVariable String id) {
        return doctorService.getDoctorResponse(id);
    }

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

    /*  OK  */
    @GetMapping("/get-all-request")
    public ResponseEntity<?> getAllRequest() throws Exception {
        try {
            String getAllRequest = doctorService.getAllRequest();
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
            String request = doctorService.getRequest(requestId, requestType);
            return ResponseEntity.status(HttpStatus.OK).body(request);
        }
        catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }

    /*  OK  */
    @PostMapping("/get-user-info")
    public ResponseEntity<?> getUserInfo(HttpServletRequest httpServletRequest) throws Exception {
        try {
            String id = httpServletRequest.getParameter("id");
            String getUserInfo = doctorService.getUserInfo(id);
            return ResponseEntity.status(HttpStatus.OK).body(getUserInfo);
        }
        catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }

    @PostMapping("/get-full-name")
    public ResponseEntity<?> getFullName(HttpServletRequest httpServletRequest) throws Exception {
        try {
            String id = httpServletRequest.getParameter("id");
            String getFullName = doctorService.getFullName(id);
            return ResponseEntity.status(HttpStatus.OK).body(getFullName);
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
            String defineRequest = doctorService.defineRequest(defineRequestForm);
            return ResponseEntity.status(HttpStatus.OK).body(defineRequest);
        }
        catch (Exception exception) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }

    @PostMapping("/permit-all/get-all-doctor")
    public String getAllDoctor() throws Exception {
        try {
            String getAllDoctor = doctorService.getAllDoctor();
            return getAllDoctor;
        }
        catch (Exception e) {
            return "";
        }
    }

    /*  OK  */
    @GetMapping("/get-list-drug-by-ownerId")
    public ResponseEntity<?> getListDrugByOwnerId() throws Exception {
        try {
            String getListDrugByOwnerId = doctorService.getListDrugByOwnerId();
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
            String changePassword = doctorService.changePassword(changePasswordForm);
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
            String updateInformation = doctorService.updateInformation(updateInformationForm);
            return ResponseEntity.status(HttpStatus.OK).body(updateInformation);
        }
        catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }
}

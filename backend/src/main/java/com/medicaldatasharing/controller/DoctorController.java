package com.medicaldatasharing.controller;

import com.medicaldatasharing.chaincode.dto.Medication;
import com.medicaldatasharing.chaincode.dto.PrescriptionDetails;
import com.medicaldatasharing.chaincode.dto.ViewRequest;
import com.medicaldatasharing.dto.GetListAuthorizedMedicalRecordByDoctorQueryDto;
import com.medicaldatasharing.dto.MedicalRecordDto;
import com.medicaldatasharing.form.AddMedicalRecordForm;
import com.medicaldatasharing.form.AddPrescriptionForm;
import com.medicaldatasharing.form.SearchMedicalRecordForm;
import com.medicaldatasharing.security.service.UserDetailsServiceImpl;
import com.medicaldatasharing.service.DoctorService;
import com.medicaldatasharing.service.UserService;
import com.medicaldatasharing.util.StringUtil;
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
import java.util.Date;
import java.util.List;

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
            GetListAuthorizedMedicalRecordByDoctorQueryDto getListAuthorizedMedicalRecordByDoctorQueryDto = new GetListAuthorizedMedicalRecordByDoctorQueryDto();
            getListAuthorizedMedicalRecordByDoctorQueryDto.setDoctorId(userDetailsService.getLoggedUser().getId());
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

    @PostMapping("/get-all-authorized-patient-by-doctorId")
    public ResponseEntity<?> getAllAuthorizedPatientByDoctorId() throws Exception {
        try {
            String getAllAuthorizedPatientByDoctorId = doctorService.getAllAuthorizedPatientByDoctorId();
            return ResponseEntity.status(HttpStatus.OK).body(getAllAuthorizedPatientByDoctorId);
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

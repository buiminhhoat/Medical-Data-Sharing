package com.medicaldatasharing.controller;

import com.medicaldatasharing.chaincode.Config;
import com.medicaldatasharing.chaincode.client.RegisterUserHyperledger;
import com.medicaldatasharing.form.*;
import com.medicaldatasharing.model.Doctor;
import com.medicaldatasharing.model.Patient;
import com.medicaldatasharing.model.User;
import com.medicaldatasharing.repository.PatientRepository;
import com.medicaldatasharing.response.DoctorResponse;
import com.medicaldatasharing.response.GetUserDataResponse;
import com.medicaldatasharing.response.PatientResponse;
import com.medicaldatasharing.security.dto.JwtResponse;
import com.medicaldatasharing.security.dto.LoginDto;
import com.medicaldatasharing.security.dto.RegisterDto;
import com.medicaldatasharing.security.dto.Response;
import com.medicaldatasharing.security.jwt.JwtProvider;
import com.medicaldatasharing.security.service.UserDetailsServiceImpl;
import com.medicaldatasharing.service.PatientService;
import com.medicaldatasharing.util.StringUtil;
import com.medicaldatasharing.util.ValidationUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
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

    @Autowired
    private PatientRepository patientRepository;

    @Autowired
    private PasswordEncoder userPasswordEncoder;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtProvider jwtProvider;

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

    @GetMapping("/permit-all/get-patient-response/{id}")
    public PatientResponse getPatientResponse(@PathVariable String id) {
        return patientService.getPatientResponse(id);
    }

    @PostMapping("/permit-all/register")
    public ResponseEntity<?> register(@ModelAttribute RegisterDto registerDto, BindingResult result) {
        if (result.hasErrors()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new Response("Đã gặp lỗi trong quá trình đăng ký"));
        }

        if (userDetailsService.getUser(registerDto.getEmail()) != null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new Response("Email đã tồn tại!"));
        }

        Patient patient = Patient
                .builder()
                .fullName(registerDto.getFullName())
                .gender(registerDto.getGender())
                .dateBirthday(registerDto.getDateBirthday())
                .username(registerDto.getEmail())
                .email(registerDto.getEmail())
                .password(userPasswordEncoder.encode(registerDto.getPassword()))
                .enabled(true)
                .role(Constants.ROLE_PATIENT)
                .build();

        try {
            Patient patientSaved = patientRepository.save(patient);

            String userIdentityId = patientSaved.getId();
            RegisterUserHyperledger.enrollOrgAppUsers(patientSaved.getEmail(), Config.PATIENT_ORG, userIdentityId);
        } catch (Exception e) {
            patientRepository.delete(patient);
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new Response("Đã gặp lỗi trong quá trình đăng ký với Hyperledger Fabric"));
        }
        return ResponseEntity.status(HttpStatus.OK).body(new Response("Đăng ký thành công"));
    }

    @PostMapping("/permit-all/login")
    public ResponseEntity<?> authenticateUser(@ModelAttribute LoginDto loginDto) {
        User user = userDetailsService.getUser(loginDto.getEmail());

        if (user == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new Response("Email hoặc mật khẩu không đúng!"));
        }

        if (!user.isEnabled()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new Response("Email hoặc mật khẩu không đúng!"));
        }

        Authentication authentication = null;
        try {
            authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            loginDto.getEmail(),
                            loginDto.getPassword()
                    )
            );
        } catch (AuthenticationException e) {
            System.out.println("Not validation");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new Response("Email hoặc mật khẩu không đúng!"));
        }

        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = jwtProvider.generateJwtToken(authentication);
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();

        return ResponseEntity.ok(new JwtResponse(jwt, user.getId(), userDetails.getUsername(), userDetails.getAuthorities()));
    }

    @GetMapping("/permit-all/get-user-data")
    public ResponseEntity<?> getUserData(@RequestHeader("Authorization") String access_token) {
        try {
            User user = null;
            if (access_token != null && access_token.startsWith("Bearer ")) {
                access_token = access_token.replace("Bearer ", "");
                String username = jwtProvider.getUserNameFromJwtToken(access_token);
                user = userDetailsService.getUser(username);
            }

            if (user == null) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new Response("Access Token không hợp lệ!"));
            }

            if (!user.isEnabled()) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new Response("Access Token không hợp lệ!"));
            }
            return ResponseEntity.ok(new GetUserDataResponse(user.getFullName(), user.getRole()));
        }
        catch (Exception exception) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new Response("Access Token không hợp lệ!"));
        }
    }

    @PostMapping("/get-full-name")
    public ResponseEntity<?> getFullName(HttpServletRequest httpServletRequest) throws Exception {
        try {
            String id = httpServletRequest.getParameter("id");
            String getFullName = patientService.getFullName(id);
            return ResponseEntity.status(HttpStatus.OK).body(getFullName);
        }
        catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }

    @PostMapping("/get-user-info")
    public ResponseEntity<?> getUserInfo(HttpServletRequest httpServletRequest) throws Exception {
        try {
            String id = httpServletRequest.getParameter("id");
            String getUserInfo = patientService.getUserInfo(id);
            return ResponseEntity.status(HttpStatus.OK).body(getUserInfo);
        }
        catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
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

    @PostMapping("/send-appointment-request")
    public ResponseEntity<?> sendAppointmentRequest(@Valid @ModelAttribute SendAppointmentRequestForm sendAppointmentRequestForm, BindingResult result) throws Exception {
        try {
            sendAppointmentRequestForm.setDateCreated(StringUtil.parseDate(new Date()));
            sendAppointmentRequestForm.setDateModified(StringUtil.parseDate(new Date()));
//            Doctor doctor = (Doctor) userDetailsService.getUserByUserId(sendAppointmentRequestForm.getRecipientId());
//            sendAppointmentRequestForm.setMedicalInstitutionId(doctor.getMedicalInstitutionId());
            DoctorResponse doctorResponse = patientService.getDoctorResponseFromDoctorService(sendAppointmentRequestForm.getRecipientId());
            sendAppointmentRequestForm.setMedicalInstitutionId(doctorResponse.getMedicalInstitutionId());
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

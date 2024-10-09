package com.medicaldatasharing.controller;

import com.medicaldatasharing.chaincode.Config;
import com.medicaldatasharing.chaincode.client.RegisterUserHyperledger;
import com.medicaldatasharing.chaincode.dto.PrescriptionDetails;
import com.medicaldatasharing.dto.GetListAuthorizedMedicalRecordByDoctorQueryDto;
import com.medicaldatasharing.form.*;
import com.medicaldatasharing.model.Doctor;
import com.medicaldatasharing.model.User;
import com.medicaldatasharing.repository.DoctorRepository;
import com.medicaldatasharing.response.DoctorResponse;
import com.medicaldatasharing.response.GetUserDataResponse;
import com.medicaldatasharing.response.PatientResponse;
import com.medicaldatasharing.security.dto.JwtResponse;
import com.medicaldatasharing.security.dto.LoginDto;
import com.medicaldatasharing.security.dto.Response;
import com.medicaldatasharing.security.jwt.JwtProvider;
import com.medicaldatasharing.security.service.UserDetailsServiceImpl;
import com.medicaldatasharing.service.DoctorService;
import com.medicaldatasharing.util.Constants;
import com.medicaldatasharing.util.StringUtil;
import com.medicaldatasharing.util.ValidationUtil;
import com.owlike.genson.GenericType;
import com.owlike.genson.Genson;
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
import java.util.List;

@RestController
@RequestMapping("/doctor")
public class DoctorController {
    @Autowired
    private DoctorService doctorService;

    @Autowired
    private JwtProvider jwtProvider;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private PasswordEncoder userPasswordEncoder;

    @Autowired
    private UserDetailsServiceImpl userDetailsService;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private DoctorRepository doctorRepository;

    @GetMapping("/permit-all/check-health")
    public String checkHealth() {
        return "OK";
    }

    @GetMapping("/permit-all/get-full-name/{id}")
    public String getFullName(@PathVariable String id) throws Exception {
        try {
            return doctorService.getFullName(id);
        }
        catch (Exception e) {
            return "Không tìm thấy thông tin của người dùng " + id;
        }
    }

    @GetMapping("/permit-all/get-doctor-response/{id}")
    public DoctorResponse getDoctorResponse(@PathVariable String id) {
        return doctorService.getDoctorResponse(id);
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

    @PostMapping("/permit-all/get-all-doctor-by-medical-institution-id/{medicalInstitutionId}")
    public String getAllDoctorByMedicalInstitutionId(@PathVariable String medicalInstitutionId) throws Exception {
        try {
            String getAllDoctorByMedicalInstitutionId = doctorService.getAllDoctorByMedicalInstitutionId(medicalInstitutionId);
            return getAllDoctorByMedicalInstitutionId;
        }
        catch (Exception e) {
            return "";
        }
    }

    @PostMapping("/permit-all/register-user")
    public ResponseEntity<String> registerUser(@RequestBody RegisterForm registerForm,
                                               @RequestHeader("Authorization") String accessToken) throws Exception {
        // Xử lý RegisterForm và access_token
        accessToken = accessToken.replace("Bearer ", "");

        // Kiểm tra token và xử lý đăng ký
        if (jwtProvider.validateJwtToken(accessToken)) {
            String medicalInstitutionId = jwtProvider.getUserNameFromJwtToken(accessToken);
            try {
                Doctor doctor = Doctor
                        .builder()
                        .fullName(registerForm.getFullName())
                        .email(registerForm.getEmail())
                        .role(Constants.ROLE_DOCTOR)
                        .username(registerForm.getEmail())
                        .password(passwordEncoder.encode(registerForm.getPassword()))
                        .enabled(true)
                        .department(registerForm.getDepartment())
                        .medicalInstitutionId(medicalInstitutionId)
                        .address(registerForm.getAddress())
                        .build();
                doctorRepository.save(doctor);

                String appUserIdentityId = doctor.getEmail();
                String org = Config.DOCTOR_ORG;
                String userIdentityId = doctor.getId();
                RegisterUserHyperledger.enrollOrgAppUsers(appUserIdentityId, org, userIdentityId);
                return ResponseEntity.ok(new Genson().serialize(doctor));
            } catch (Exception e) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Error while signUp in hyperledger");
            }
            // Xử lý đăng ký với thông tin trong registerForm
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Token không hợp lệ");
        }
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
            String getFullName = doctorService.getFullName(id);
            return ResponseEntity.status(HttpStatus.OK).body(getFullName);
        }
        catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
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

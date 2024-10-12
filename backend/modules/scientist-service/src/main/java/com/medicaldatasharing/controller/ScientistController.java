package com.medicaldatasharing.controller;

import com.medicaldatasharing.chaincode.Config;
import com.medicaldatasharing.chaincode.client.RegisterUserHyperledger;
import com.medicaldatasharing.dto.GetListAuthorizedMedicalRecordByScientistQueryDto;
import com.medicaldatasharing.form.*;
import com.medicaldatasharing.model.Doctor;
import com.medicaldatasharing.model.Scientist;
import com.medicaldatasharing.model.User;
import com.medicaldatasharing.repository.ScientistRepository;
import com.medicaldatasharing.response.DoctorResponse;
import com.medicaldatasharing.response.GetUserDataResponse;
import com.medicaldatasharing.response.ScientistResponse;
import com.medicaldatasharing.security.dto.JwtResponse;
import com.medicaldatasharing.security.dto.LoginDto;
import com.medicaldatasharing.security.dto.Response;
import com.medicaldatasharing.security.jwt.JwtProvider;
import com.medicaldatasharing.security.service.UserDetailsServiceImpl;
import com.medicaldatasharing.service.ScientistService;
import com.medicaldatasharing.util.Constants;
import com.medicaldatasharing.util.StringUtil;
import com.medicaldatasharing.util.ValidationUtil;
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

@RestController
@RequestMapping("/scientist")
public class ScientistController {
    @Autowired
    private ScientistService scientistService;

    @Autowired
    private JwtProvider jwtProvider;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private UserDetailsServiceImpl userDetailsService;

    @Autowired
    private ScientistRepository scientistRepository;

    @Autowired
    private AuthenticationManager authenticationManager;

    @GetMapping("/permit-all/check-health")
    public String checkHealth() {
        return "OK";
    }

    @GetMapping("/permit-all/get-full-name/{id}")
    public String getFullName(@PathVariable String id) throws Exception {
        try {
            return scientistService.getFullName(id);
        }
        catch (Exception e) {
            return "Không tìm thấy thông tin của người dùng " + id;
        }
    }

    @GetMapping("/permit-all/get-scientist-response/{id}")
    public String getScientistResponse(@PathVariable String id) {
        return scientistService.getScientistResponse(id);
    }

    @PostMapping("/admin-service/get-all-user-response")
    public String getAllUserResponse() {
        return scientistService.getAllUserResponse();
    }

    @PostMapping("/permit-all/get-all-scientist-by-research-center-id/{researchCenterId}")
    public String getAllScientistByResearchCenterId(@PathVariable String researchCenterId) throws Exception {
        try {
            String getAllScientistByResearchCenterId = scientistService.getAllScientistByResearchCenterId(researchCenterId);
            return getAllScientistByResearchCenterId;
        }
        catch (Exception e) {
            return "";
        }
    }

    @PostMapping("/permit-all/register-user")
    public ResponseEntity<String> registerUser(@RequestBody RegisterForm registerForm,
                                               @RequestHeader("Authorization-Service") String accessToken) throws Exception {
        // Xử lý RegisterForm và access_token
        accessToken = accessToken.replace("Bearer ", "");

        // Kiểm tra token và xử lý đăng ký
        if (jwtProvider.validateJwtToken(accessToken)) {
            String researchCenterId = jwtProvider.getUserNameFromJwtToken(accessToken);
            try {
                Scientist scientist = Scientist
                        .builder()
                        .fullName(registerForm.getFullName())
                        .email(registerForm.getEmail())
                        .role(Constants.ROLE_SCIENTIST)
                        .username(registerForm.getEmail())
                        .password(passwordEncoder.encode(registerForm.getPassword()))
                        .enabled(true)
                        .researchCenterId(researchCenterId)
                        .address(registerForm.getAddress())
                        .build();
                scientistRepository.save(scientist);

                String appUserIdentityId = scientist.getEmail();
                String org = Config.SCIENTIST_ORG;
                String userIdentityId = scientist.getId();
                RegisterUserHyperledger.enrollOrgAppUsers(appUserIdentityId, org, userIdentityId);
                return ResponseEntity.ok(new Genson().serialize(scientist));
            } catch (Exception e) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Error while signUp in hyperledger");
            }
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

    @GetMapping("/get-all-request")
    public ResponseEntity<?> getAllRequest() throws Exception {
        try {
            String getAllRequest = scientistService.getAllRequest();
            return ResponseEntity.status(HttpStatus.OK).body(getAllRequest);
        }
        catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }

    @PostMapping("/get-request")
    public ResponseEntity<?> getRequest(HttpServletRequest httpServletRequest) throws Exception {
        String requestId = httpServletRequest.getParameter("requestId");
        String requestType = httpServletRequest.getParameter("requestType");
        if (requestId.isEmpty() || requestType.isEmpty()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        try {
            String request = scientistService.getRequest(requestId, requestType);
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
            String getUserInfo = scientistService.getUserInfo(id);
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
            String getFullName = scientistService.getFullName(id);
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
            String defineRequest = scientistService.defineRequest(defineRequestForm);
            return ResponseEntity.status(HttpStatus.OK).body(defineRequest);
        }
        catch (Exception exception) {
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
            String changePassword = scientistService.changePassword(changePasswordForm);
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
            String updateInformation = scientistService.updateInformation(updateInformationForm);
            return ResponseEntity.status(HttpStatus.OK).body(updateInformation);
        }
        catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }
}

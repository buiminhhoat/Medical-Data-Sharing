package com.medicaldatasharing.controller;

import com.medicaldatasharing.chaincode.Config;
import com.medicaldatasharing.chaincode.client.RegisterUserHyperledger;
import com.medicaldatasharing.dto.GetListAuthorizedMedicalRecordByManufacturerQueryDto;
import com.medicaldatasharing.form.*;
import com.medicaldatasharing.model.Manufacturer;
import com.medicaldatasharing.model.Scientist;
import com.medicaldatasharing.model.User;
import com.medicaldatasharing.repository.ManufacturerRepository;
import com.medicaldatasharing.response.GetUserDataResponse;
import com.medicaldatasharing.response.ManufacturerResponse;
import com.medicaldatasharing.security.dto.JwtResponse;
import com.medicaldatasharing.security.dto.LoginDto;
import com.medicaldatasharing.security.dto.Response;
import com.medicaldatasharing.security.jwt.JwtProvider;
import com.medicaldatasharing.security.service.UserDetailsServiceImpl;
import com.medicaldatasharing.service.ManufacturerService;
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
@RequestMapping("/manufacturer")
public class ManufacturerController {
    @Autowired
    private ManufacturerService manufacturerService;

    @Autowired
    private JwtProvider jwtProvider;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private UserDetailsServiceImpl userDetailsService;

    @Autowired
    private ManufacturerRepository manufacturerRepository;

    @Autowired
    private AuthenticationManager authenticationManager;

    @GetMapping("/permit-all/check-health")
    public String checkHealth() {
        return "OK";
    }

    @GetMapping("/permit-all/get-full-name/{id}")
    public String getFullName(@PathVariable String id) throws Exception {
        try {
            return manufacturerService.getFullName(id);
        }
        catch (Exception e) {
            return "Không tìm thấy thông tin của người dùng " + id;
        }
    }

    @GetMapping("/permit-all/get-manufacturer-response/{id}")
    public String getManufacturerResponse(@PathVariable String id) {
        return manufacturerService.getManufacturerResponse(id);
    }

    @PostMapping("/admin-service/get-all-user-response")
    public String getAllUserResponse() {
        return manufacturerService.getAllUserResponse();
    }

    @PostMapping("/admin-service/register-user")
    public ResponseEntity<String> registerUser(@RequestBody RegisterForm registerForm,
                                               @RequestHeader("Authorization-Service") String accessToken) throws Exception {
        // Xử lý RegisterForm và access_token
        accessToken = accessToken.replace("Bearer ", "");

        // Kiểm tra token và xử lý đăng ký
        if (jwtProvider.validateJwtToken(accessToken)) {
            String researchCenterId = jwtProvider.getUserNameFromJwtToken(accessToken);
            try {
                Manufacturer manufacturer = Manufacturer
                        .builder()
                        .fullName(registerForm.getFullName())
                        .email(registerForm.getEmail())
                        .businessLicenseNumber(registerForm.getBusinessLicenseNumber())
                        .role(Constants.ROLE_MANUFACTURER)
                        .username(registerForm.getEmail())
                        .password(passwordEncoder.encode(registerForm.getPassword()))
                        .enabled(true)
                        .build();
                manufacturerRepository.save(manufacturer);

                String appUserIdentityId = manufacturer.getEmail();
                String org = Config.MANUFACTURER_ORG;
                String userIdentityId = manufacturer.getId();
                RegisterUserHyperledger.enrollOrgAppUsers(appUserIdentityId, org, userIdentityId);
                return ResponseEntity.ok(new Genson().serialize(manufacturer));
            } catch (Exception e) {
                throw new AuthException("Error while signUp in hyperledger");
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

    @GetMapping("/get-list-drug-by-ownerId")
    public ResponseEntity<?> getListDrugByOwnerId() throws Exception {
        try {
            String getListDrugByOwnerId = manufacturerService.getListDrugByOwnerId();
            return ResponseEntity.status(HttpStatus.OK).body(getListDrugByOwnerId);
        }
        catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }

    @GetMapping("/get-all-request")
    public ResponseEntity<?> getAllRequest() throws Exception {
        try {
            String getAllRequest = manufacturerService.getAllRequest();
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
            String request = manufacturerService.getRequest(requestId, requestType);
            return ResponseEntity.status(HttpStatus.OK).body(request);
        }
        catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }

    @PostMapping("/get-user-info")
    public ResponseEntity<?> getUserInfo(HttpServletRequest httpServletRequest) throws Exception {
        try {
            String id = httpServletRequest.getParameter("id");
            String getUserInfo = manufacturerService.getUserInfo(id);
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
            String getFullName = manufacturerService.getFullName(id);
            return ResponseEntity.status(HttpStatus.OK).body(getFullName);
        }
        catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }

    @PostMapping("/define-request")
    public ResponseEntity<?> defineRequest(
            @Valid @ModelAttribute DefineRequestForm defineRequestForm,
            BindingResult result) throws Exception {
        if (result.hasErrors()) {
            String errorMsg = ValidationUtil.formatValidationErrorMessages(result.getAllErrors());
            throw new ValidationException(errorMsg);
        }

        try {
            String defineRequest = manufacturerService.defineRequest(defineRequestForm);
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
            String changePassword = manufacturerService.changePassword(changePasswordForm);
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
            String updateInformation = manufacturerService.updateInformation(updateInformationForm);
            return ResponseEntity.status(HttpStatus.OK).body(updateInformation);
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
            String viewRequest = manufacturerService.sendViewRequest(sendViewRequestForm);
            return ResponseEntity.status(HttpStatus.OK).body(viewRequest);
        }
        catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }

    @GetMapping("/get-list-medication-by-manufacturerId")
    public ResponseEntity<?> getListMedicationByManufacturerId(@Valid @ModelAttribute SearchMedicationForm searchMedicationForm) throws Exception {
        try {
            String getListMedication = manufacturerService.getListMedicationByManufacturerId(searchMedicationForm);
            return ResponseEntity.status(HttpStatus.OK).body(getListMedication);
        }
        catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }

    @PostMapping("/get-list-medical-record-by-manufacturerId")
    public ResponseEntity<?> getListMedicalRecord(HttpServletRequest httpServletRequest) {
        try {
            String patientId = httpServletRequest.getParameter("patientId");
            GetListAuthorizedMedicalRecordByManufacturerQueryDto getListAuthorizedMedicalRecordByManufacturerQueryDto = new GetListAuthorizedMedicalRecordByManufacturerQueryDto();
            getListAuthorizedMedicalRecordByManufacturerQueryDto.setManufacturerId(manufacturerService.getLoggedUser().getId());
            getListAuthorizedMedicalRecordByManufacturerQueryDto.setPatientId(patientId);
            String getListMedicalRecord = manufacturerService.getListMedicalRecord(getListAuthorizedMedicalRecordByManufacturerQueryDto);
            return ResponseEntity.status(HttpStatus.OK).body(getListMedicalRecord);
        }
        catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }

    @PostMapping("/add-medication")
    public ResponseEntity<?> addMedication(@Valid @ModelAttribute AddMedicationForm addMedicationForm, BindingResult result) throws Exception {
        try {
            addMedicationForm.setDateCreated(StringUtil.parseDate(new Date()));
            addMedicationForm.setDateModified(StringUtil.parseDate(new Date()));
            String addMedication = manufacturerService.addMedication(addMedicationForm);
            return ResponseEntity.status(HttpStatus.OK).body(addMedication);
        }
        catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }

    @PostMapping("/add-drug")
    public ResponseEntity<?> addDrug(@Valid @ModelAttribute AddDrugForm addDrugForm, BindingResult result) throws Exception {
        try {
            String addDrug = manufacturerService.addDrug(addDrugForm);
            return ResponseEntity.status(HttpStatus.OK).body(addDrug);
        }
        catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }

    @PostMapping("/get-list-drug-reaction-by-manufacturer")
    public ResponseEntity<?> getListDrugReactionByManufacturer() throws Exception {
        try {
            String getListMedication = manufacturerService.getListDrugReactionByManufacturer();
            return ResponseEntity.status(HttpStatus.OK).body(getListMedication);
        }
        catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }

    @PostMapping("/get-prescription-by-manufacturer")
    public ResponseEntity<?> getPrescriptionByManufacturer(@Valid @ModelAttribute GetPrescriptionForm getPrescriptionForm,
                                                        BindingResult result) throws Exception {
        try {
            String getPrescriptionByPrescriptionId = manufacturerService.getPrescriptionByManufacturer(getPrescriptionForm);
            return ResponseEntity.status(HttpStatus.OK).body(getPrescriptionByPrescriptionId);
        }
        catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }

    @PostMapping("/get-all-patient-managed-by-manufacturerId")
    public ResponseEntity<?> getAllPatientManagedByManufacturerId() throws Exception {
        try {
            String getAllPatientManagedByManufacturerId = manufacturerService.getAllPatientManagedByManufacturerId();
            return ResponseEntity.status(HttpStatus.OK).body(getAllPatientManagedByManufacturerId);
        }
        catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }
}

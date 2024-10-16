package com.medicaldatasharing.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.medicaldatasharing.chaincode.Config;
import com.medicaldatasharing.chaincode.client.RegisterUserHyperledger;
import com.medicaldatasharing.dto.MedicationPurchaseDto;
import com.medicaldatasharing.dto.PurchaseDto;
import com.medicaldatasharing.form.*;
import com.medicaldatasharing.model.DrugStore;
import com.medicaldatasharing.model.User;
import com.medicaldatasharing.repository.DrugStoreRepository;
import com.medicaldatasharing.response.GetUserDataResponse;
import com.medicaldatasharing.security.dto.JwtResponse;
import com.medicaldatasharing.security.dto.LoginDto;
import com.medicaldatasharing.security.dto.Response;
import com.medicaldatasharing.security.jwt.JwtProvider;
import com.medicaldatasharing.security.service.UserDetailsServiceImpl;
import com.medicaldatasharing.service.DrugStoreService;
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
import java.util.List;

@RestController
@RequestMapping("/drugstore")
public class DrugStoreController {
    @Autowired
    private DrugStoreService drugStoreService;

    @Autowired
    private JwtProvider jwtProvider;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private UserDetailsServiceImpl userDetailsService;

    @Autowired
    private DrugStoreRepository drugStoreRepository;

    @Autowired
    private AuthenticationManager authenticationManager;

    @GetMapping("/permit-all/check-health")
    public String checkHealth() {
        return "OK";
    }

    @GetMapping("/permit-all/get-full-name/{id}")
    public String getFullName(@PathVariable String id) throws Exception {
        try {
            return drugStoreService.getFullName(id);
        }
        catch (Exception e) {
            return "Không tìm thấy thông tin của người dùng " + id;
        }
    }

    @GetMapping("/permit-all/get-drugstore-response/{id}")
    public String getDrugStoreResponse(@PathVariable String id) {
        return drugStoreService.getDrugStoreResponse(id);
    }

    @PostMapping("/admin-service/get-all-user-response")
    public String getAllUserResponse() {
        return drugStoreService.getAllUserResponse();
    }

    @PostMapping("/permit-all/register-user")
    public ResponseEntity<String> registerUser(@RequestBody RegisterForm registerForm,
                                               @RequestHeader("Authorization-Service") String accessToken) throws Exception {
        // Xử lý RegisterForm và access_token
        accessToken = accessToken.replace("Bearer ", "");

        // Kiểm tra token và xử lý đăng ký
        if (jwtProvider.validateJwtToken(accessToken)) {
            try {
                DrugStore drugstore = DrugStore
                        .builder()
                        .fullName(registerForm.getFullName())
                        .email(registerForm.getEmail())
                        .role(Constants.ROLE_SCIENTIST)
                        .username(registerForm.getEmail())
                        .password(passwordEncoder.encode(registerForm.getPassword()))
                        .enabled(true)
                        .address(registerForm.getAddress())
                        .build();
                drugStoreRepository.save(drugstore);

                String appUserIdentityId = drugstore.getEmail();
                String org = Config.SCIENTIST_ORG;
                String userIdentityId = drugstore.getId();
                RegisterUserHyperledger.enrollOrgAppUsers(appUserIdentityId, org, userIdentityId);
                return ResponseEntity.ok(new Genson().serialize(drugstore));
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
            sendViewPrescriptionRequestForm.setSenderId(drugStoreService.getLoggedUser().getId());
            String viewPrescriptionRequestStr = drugStoreService.sendViewPrescriptionRequest(sendViewPrescriptionRequestForm);
            return ResponseEntity.status(HttpStatus.OK).body(viewPrescriptionRequestStr);
        }
        catch (Exception exception) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }

    @PostMapping("/get-list-drug-by-medicationId-and-ownerId")
    public ResponseEntity<?> getListDrugByMedicationIdAndOwnerId(@Valid @ModelAttribute SearchDrugForm searchDrugForm,
                                                       BindingResult result) throws Exception {
        try {
            String getListDrugByMedicationIdAndOwnerId = drugStoreService.getListDrugByMedicationIdAndOwnerId(searchDrugForm);
            return ResponseEntity.status(HttpStatus.OK).body(getListDrugByMedicationIdAndOwnerId);
        }
        catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }

    @PostMapping("/add-purchase")
    public ResponseEntity<?> addPurchase(@Valid @ModelAttribute AddPurchaseForm addPurchaseForm, BindingResult result) throws Exception {
        try {
            List<MedicationPurchaseDto> medicationPurchaseDtoList = new ObjectMapper().readValue(addPurchaseForm.getSellingPrescriptionDrug(),
                    new TypeReference<List<MedicationPurchaseDto>>() {});

            PurchaseDto purchaseDto = new PurchaseDto();
            purchaseDto.setPrescriptionId(addPurchaseForm.getPrescriptionId());
            purchaseDto.setMedicationPurchaseList(new Genson().serialize(medicationPurchaseDtoList));
            purchaseDto.setPatientId(addPurchaseForm.getPatientId());
            purchaseDto.setDrugStoreId(drugStoreService.getLoggedUser().getId());
            purchaseDto.setDateCreated(StringUtil.parseDate(new Date()));
            purchaseDto.setDateModified(StringUtil.parseDate(new Date()));

            String addPurchase = drugStoreService.addPurchase(purchaseDto);
            return ResponseEntity.status(HttpStatus.OK).body(addPurchase);
        }
        catch (Exception exception) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }

    @PostMapping("/get-list-purchase-by-drugStoreId")
    public ResponseEntity<?> getListPurchaseByDrugStoreId(@Valid @ModelAttribute SearchPurchaseForm searchPurchaseForm,
                                                        BindingResult result) {
        try {
            String getListPurchaseByDrugStoreId = drugStoreService.getListPurchaseByDrugStoreId(searchPurchaseForm);
            return ResponseEntity.status(HttpStatus.OK).body(getListPurchaseByDrugStoreId);
        }
        catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }

    @PostMapping("/get-purchase-by-purchaseId")
    public ResponseEntity<?> getPurchaseByPurchaseId(HttpServletRequest httpServletRequest) {
        try {
            String purchaseId = httpServletRequest.getParameter("purchaseId");
            String getPurchaseByPurchaseId = drugStoreService.getPurchaseByPurchaseId(purchaseId);
            return ResponseEntity.status(HttpStatus.OK).body(getPurchaseByPurchaseId);
        }
        catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }

    @GetMapping("/get-all-request")
    public ResponseEntity<?> getAllRequest() throws Exception {
        try {
            String getAllRequest = drugStoreService.getAllRequest();
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
            String request = drugStoreService.getRequest(requestId, requestType);
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
            String getUserInfo = drugStoreService.getUserInfo(id);
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
            String getFullName = drugStoreService.getFullName(id);
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
            String defineRequest = drugStoreService.defineRequest(defineRequestForm);
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
            String changePassword = drugStoreService.changePassword(changePasswordForm);
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
            String updateInformation = drugStoreService.updateInformation(updateInformationForm);
            return ResponseEntity.status(HttpStatus.OK).body(updateInformation);
        }
        catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }
}

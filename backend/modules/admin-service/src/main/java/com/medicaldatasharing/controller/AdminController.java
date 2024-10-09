package com.medicaldatasharing.controller;

import com.medicaldatasharing.chaincode.Config;
import com.medicaldatasharing.chaincode.client.RegisterUserHyperledger;
import com.medicaldatasharing.form.ChangePasswordForm;
import com.medicaldatasharing.form.RegisterForm;
import com.medicaldatasharing.form.UpdateInformationForm;
import com.medicaldatasharing.model.Admin;
import com.medicaldatasharing.model.User;
import com.medicaldatasharing.repository.AdminRepository;
import com.medicaldatasharing.security.dto.JwtResponse;
import com.medicaldatasharing.security.dto.LoginDto;
import com.medicaldatasharing.security.dto.Response;
import com.medicaldatasharing.security.jwt.JwtProvider;
import com.medicaldatasharing.security.service.UserDetailsServiceImpl;
import com.medicaldatasharing.service.AdminService;
import com.medicaldatasharing.util.Constants;
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
import com.medicaldatasharing.response.GetUserDataResponse;
import javax.security.auth.message.AuthException;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.Objects;

@RestController
@RequestMapping("/admin")
public class AdminController {
    @Autowired
    private AdminService adminService;

    @Autowired
    private JwtProvider jwtProvider;

    @Autowired
    private AdminRepository adminRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private UserDetailsServiceImpl userDetailsService;

    @GetMapping("/permit-all/check-health")
    public String checkHealth() {
        return "OK";
    }

    @GetMapping("/permit-all/get-full-name/{id}")
    public String getFullName(@PathVariable String id) throws Exception {
        try {
            return adminService.getFullName(id);
        }
        catch (Exception e) {
            return "Không tìm thấy thông tin của người dùng " + id;
        }
    }

    @PostMapping("/permit-all/register-user")
    public ResponseEntity<String> registerUser(@RequestBody RegisterForm registerForm,
                                               @RequestHeader("Authorization-Service") String accessToken) throws Exception {
        // Xử lý RegisterForm và access_token
        accessToken = accessToken.replace("Bearer ", "");

        // Kiểm tra token và xử lý đăng ký
        if (jwtProvider.validateJwtToken(accessToken)) {
            try {
                Admin drugstore = Admin
                        .builder()
                        .fullName(registerForm.getFullName())
                        .email(registerForm.getEmail())
                        .role(Constants.ROLE_SCIENTIST)
                        .username(registerForm.getEmail())
                        .password(passwordEncoder.encode(registerForm.getPassword()))
                        .enabled(true)
                        .address(registerForm.getAddress())
                        .build();
                adminRepository.save(drugstore);

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
        User user = adminService.getUser(loginDto.getEmail());

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
    
    @PostMapping("/get-all-user-by-admin")
    public ResponseEntity<?> getAllUserByAdmin() throws Exception {
        try {
            String getAllUserByAdmin = adminService.getAllUserByAdmin();
            return ResponseEntity.status(HttpStatus.OK).body(getAllUserByAdmin);
        }
        catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }

    @PostMapping("/get-user-info")
    public ResponseEntity<?> getUserInfo(HttpServletRequest httpServletRequest) throws Exception {
        try {
            String id = httpServletRequest.getParameter("id");
            String getUserInfo = adminService.getUserInfo(id);
            return ResponseEntity.status(HttpStatus.OK).body(getUserInfo);
        }
        catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }

    // todo: Need check medical institution, research center
    @PostMapping("/register-user")
    public ResponseEntity<?> registerUser(@Valid @ModelAttribute RegisterForm registerForm,
                                          BindingResult result) {
        try {
            String registerUser = "";
            if (Objects.equals(registerForm.getRole(), "Cơ sở y tế")) {
                registerUser = adminService.registerMedicalInstitution(registerForm);
            }

            if (Objects.equals(registerForm.getRole(), "Công ty sản xuất thuốc")) {
                if (registerForm.getBusinessLicenseNumber().isEmpty()) {
                    return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new Response("Mã số giấy phép kinh doanh không được bỏ trống!"));
                }
                registerUser = adminService.registerManufacturer(registerForm);
            }

            if (Objects.equals(registerForm.getRole(), "Trung tâm nghiên cứu")) {
                if (registerForm.getBusinessLicenseNumber().isEmpty()) {
                    return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new Response("Mã số giấy phép kinh doanh không được bỏ trống!"));
                }
                registerUser = adminService.registerResearchCenter(registerForm);
            }

            return ResponseEntity.status(HttpStatus.OK).body(registerUser);
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
            String changePassword = adminService.changePassword(changePasswordForm);
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
            String updateInformation = adminService.updateInformation(updateInformationForm);
            return ResponseEntity.status(HttpStatus.OK).body(updateInformation);
        }
        catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
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
}

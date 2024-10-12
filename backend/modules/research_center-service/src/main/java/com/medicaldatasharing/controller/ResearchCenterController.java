package com.medicaldatasharing.controller;

import com.medicaldatasharing.form.ChangePasswordForm;
import com.medicaldatasharing.form.RegisterForm;
import com.medicaldatasharing.form.UpdateInformationForm;
import com.medicaldatasharing.model.User;
import com.medicaldatasharing.response.GetUserDataResponse;
import com.medicaldatasharing.response.PatientResponse;
import com.medicaldatasharing.response.ResearchCenterResponse;
import com.medicaldatasharing.response.ScientistResponse;
import com.medicaldatasharing.security.dto.JwtResponse;
import com.medicaldatasharing.security.dto.LoginDto;
import com.medicaldatasharing.security.dto.Response;
import com.medicaldatasharing.security.jwt.JwtProvider;
import com.medicaldatasharing.security.service.UserDetailsServiceImpl;
import com.medicaldatasharing.service.ResearchCenterService;
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
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.security.auth.message.AuthException;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.Objects;

@RestController
@RequestMapping("/research_center")
public class ResearchCenterController {
    @Autowired
    private ResearchCenterService researchCenterService;

    @Autowired
    private UserDetailsServiceImpl userDetailsService;

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
            return researchCenterService.getFullName(id);
        }
        catch (Exception e) {
            return "Không tìm thấy thông tin của người dùng " + id;
        }
    }

    @GetMapping("/permit-all/get-research_center-response/{id}")
    public String getResearchCenterResponse(@PathVariable String id) {
        return researchCenterService.getResearchCenterResponse(id);
    }

    @PostMapping("/admin-service/get-all-user-response")
    public String getAllUserResponse() {
        return researchCenterService.getAllUserResponse();
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

    @PostMapping("/get-all-scientist-by-research-center")
    public ResponseEntity<?> getAllScientistByResearchCenter() throws Exception {
        try {
            String getAllScientistByResearchCenter = researchCenterService.getAllScientistByResearchCenter(researchCenterService.getLoggedUser().getId());
            return ResponseEntity.status(HttpStatus.OK).body(getAllScientistByResearchCenter);
        }
        catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }

    @PostMapping("/get-user-info")
    public ResponseEntity<?> getUserInfo(HttpServletRequest httpServletRequest) throws Exception {
        try {
            String id = httpServletRequest.getParameter("id");
            String getUserInfo = researchCenterService.getUserInfo(id);
            return ResponseEntity.status(HttpStatus.OK).body(getUserInfo);
        }
        catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }

    @PostMapping("/register-user")
    public ResponseEntity<?> registerUser(@Valid @ModelAttribute RegisterForm registerForm,
                                          BindingResult result) {
        try {
            String registerUser = "";

            if (Objects.equals(registerForm.getRole(), Constants.ROLE_SCIENTIST)) {
                registerUser = researchCenterService.registerScientist(registerForm);
            }
            else {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(
                        new Response("Vai trò của người dùng phải là Nhà khoa học"));
            }

            return ResponseEntity.status(HttpStatus.OK).body(registerUser);
        }
        catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }

    @PostMapping("/get-full-name")
    public ResponseEntity<?> getFullName(HttpServletRequest httpServletRequest) throws Exception {
        try {
            String id = httpServletRequest.getParameter("id");
            String getFullName = researchCenterService.getFullName(id);
            return ResponseEntity.status(HttpStatus.OK).body(getFullName);
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
            String changePassword = researchCenterService.changePassword(changePasswordForm);
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
            String updateInformation = researchCenterService.updateInformation(updateInformationForm);
            return ResponseEntity.status(HttpStatus.OK).body(updateInformation);
        }
        catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }
}

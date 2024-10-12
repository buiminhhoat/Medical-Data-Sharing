package com.medicaldatasharing.service;

import com.medicaldatasharing.chaincode.Config;
import com.medicaldatasharing.chaincode.client.RegisterUserHyperledger;
import com.medicaldatasharing.form.ChangePasswordForm;
import com.medicaldatasharing.form.RegisterForm;
import com.medicaldatasharing.form.UpdateInformationForm;
import com.medicaldatasharing.model.*;
import com.medicaldatasharing.repository.*;
import com.medicaldatasharing.response.*;
import com.medicaldatasharing.security.jwt.JwtProvider;
import com.medicaldatasharing.security.service.UserDetailsServiceImpl;
import com.medicaldatasharing.util.Constants;
import com.owlike.genson.GenericType;
import com.owlike.genson.Genson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import javax.security.auth.message.AuthException;
import java.util.ArrayList;
import java.util.List;

@Service
public class AdminService {
    @Autowired
    private AdminRepository adminRepository;

    @Autowired
    private UserDetailsServiceImpl userDetailsService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtProvider jwtProvider;

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private AuthenticationManager authenticationManager;

    public List<PatientResponse> getAllPatientResponseFromPatientService() {
        try {
            String url = "http://localhost:8000/api/patient/admin-service/admin-service/get-all-patient-response/";
            HttpHeaders headers = new HttpHeaders();
            headers.set("apikey", "admin-service");
            HttpEntity<String> entity = new HttpEntity<>(headers);
            String patientResponseStr = restTemplate.postForObject(url, entity, String.class);

            List<PatientResponse> patientResponseList = new Genson().deserialize(patientResponseStr,
                    new GenericType<List<PatientResponse>>() {
                    });
            return patientResponseList;
        }
        catch (Exception exception) {
            System.out.println(exception);
            return null;
        }
    }

    public List<UserResponse> getAllUserResponseFromOtherService(String org) {
        try {
            String url = "http://localhost:8000/api/" + org + "/admin-service/admin-service/get-all-user-response/";
            HttpHeaders headers = new HttpHeaders();
            headers.set("apikey", "admin-service");
            HttpEntity<String> entity = new HttpEntity<>(headers);
            String userResponseStr = restTemplate.postForObject(url, entity, String.class);

            List<UserResponse> userResponseList = new Genson().deserialize(userResponseStr,
                    new GenericType<List<UserResponse>>() {
                    });
            return userResponseList;
        }
        catch (Exception exception) {
            System.out.println(exception);
            return null;
        }
    }

    public UserResponse getUserResponse(String id) {
        String url = "http://localhost:8000/api/user/get-user-response/" + id;
        try {
            String userResponseStr = restTemplate.getForObject(url, String.class);
            return new Genson().deserialize(userResponseStr, UserResponse.class);
        }
        catch (Exception exception) {
            return null;
        }
    }

    public String getAllUserByAdmin() throws Exception {
        List<UserResponse> userResponseList = new ArrayList<>();
        User user = getLoggedUser();
        userResponseList.addAll(getAllUserResponseFromOtherService("patient"));
        userResponseList.addAll(getAllUserResponseFromOtherService("doctor"));
        userResponseList.addAll(getAllUserResponseFromOtherService("drugstore"));
        userResponseList.addAll(getAllUserResponseFromOtherService("manufacturer"));
        userResponseList.addAll(getAllUserResponseFromOtherService("medical_institution"));
        userResponseList.addAll(getAllUserResponseFromOtherService("research_center"));
        userResponseList.addAll(getAllUserResponseFromOtherService("scientist"));

        try {
            return new Genson().serialize(userResponseList);
        }
        catch (Exception e) {
            throw e;
        }
    }

    public String getUserInfo(String id) throws Exception {
        List<UserResponse> userResponseList = new ArrayList<>();
        User user = getLoggedUser();
        userResponseList.add(getUserResponse(id));

        try {
            if (userResponseList.size() == 1) {
                return new Genson().serialize(userResponseList.get(0));
            }
            else {
                throw new Exception("Không tìm thấy thông tin của user " + id);
            }
        }
        catch (Exception e) {
            throw e;
        }
    }

    public String registerMedicalInstitution(RegisterForm registerForm) throws AuthException {
        try {
            User user = getLoggedUser();
            String accessToken = jwtProvider.generateJwtToken(user.getId());
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.set("Authorization-Service", "Bearer " + accessToken);
            headers.set("apikey", "admin-service");

            HttpEntity<RegisterForm> request = new HttpEntity<>(registerForm, headers);
            String url = "http://localhost:8000/api/medical_institution/admin-service/admin-service/register-user";
            ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.POST, request, String.class);

            return response.getBody();
        } catch (Exception e) {
            throw new AuthException("Error while signUp in hyperledger");
        }
    }

    public String registerManufacturer(RegisterForm registerForm) throws AuthException {
        try {
            User user = getLoggedUser();
            String accessToken = jwtProvider.generateJwtToken(user.getId());
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.set("Authorization-Service", "Bearer " + accessToken);
            headers.set("apikey", "admin-service");

            HttpEntity<RegisterForm> request = new HttpEntity<>(registerForm, headers);

            String url = "http://localhost:8000/api/manufacturer/admin-service/admin-service/register-user";
            ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.POST, request, String.class);

            return response.getBody();
        } catch (Exception e) {
            throw new AuthException("Error while signUp in hyperledger");
        }
    }

    public String registerResearchCenter(RegisterForm registerForm) throws AuthException {
        try {
            User user = getLoggedUser();
            String accessToken = jwtProvider.generateJwtToken(user.getId());
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.set("Authorization-Service", "Bearer " + accessToken);
            headers.set("apikey", "admin-service");

            HttpEntity<RegisterForm> request = new HttpEntity<>(registerForm, headers);

            String url = "http://localhost:8000/api/research_center/admin-service/admin-service/register-user";
            ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.POST, request, String.class);

            return response.getBody();
        } catch (Exception e) {
            throw new AuthException("Error while signUp in hyperledger");
        }
    }

    public User getUser(String email) {
        Admin admin = adminRepository.findAdminByEmail(email);
        if (admin != null) {
            return admin;
        }
        return null;
    }

    public User getLoggedUser() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String username = "";
        User user;
        if (principal instanceof UserDetails) {
            username = ((UserDetails) principal).getUsername();
            user = getUser(username);
            return user;
        } else {
            return null;
        }
    }

    public String changePassword(ChangePasswordForm changePasswordForm) throws Exception {
        User user = getLoggedUser();
        try {
            Authentication authentication = null;
            try {
                authentication = authenticationManager.authenticate(
                        new UsernamePasswordAuthenticationToken(
                                user.getEmail(),
                                changePasswordForm.getOldPassword()
                        )
                );
            } catch (AuthenticationException e) {
                throw new Exception("Mật khẩu cũ không đúng");
            }

            user.setPassword(passwordEncoder.encode(changePasswordForm.getPassword()));
            switch (user.getRole()) {
                case Constants.ROLE_ADMIN:
                    adminRepository.save((Admin) user);
                    break;
            }
            return "Thành công";
        }
        catch (Exception e) {
            throw e;
        }
    }

    public String updateInformation(UpdateInformationForm updateInformationForm) throws Exception {
        User user = getLoggedUser();
        try {
            user.setFullName(updateInformationForm.getFullName());
            user.setAddress(updateInformationForm.getAddress());

            switch (user.getRole()) {
                case Constants.ROLE_ADMIN:
                    adminRepository.save((Admin) user);
                    break;
            }
            return "Thành công";
        }
        catch (Exception e) {
            throw e;
        }
    }

    public String getFullNameFromUserService(String id) {
        String url = "http://localhost:9000/api/user/get-full-name/" + id;
        return restTemplate.getForObject(url, String.class);
    }

    public String getFullName(String id) {
        String org = id.substring(0, id.indexOf("-"));
        if (org.equals("Admin")) {
            Admin admin = adminRepository.findAdminById(id);
            if (admin != null) {
                return admin.getFullName();
            }
        }

        String fullName = getFullNameFromUserService(id);
        return fullName;
    }
}

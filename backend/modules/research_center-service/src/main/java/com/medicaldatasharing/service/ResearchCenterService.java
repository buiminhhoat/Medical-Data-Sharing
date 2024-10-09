package com.medicaldatasharing.service;

import com.medicaldatasharing.form.ChangePasswordForm;
import com.medicaldatasharing.form.RegisterForm;
import com.medicaldatasharing.form.UpdateInformationForm;
import com.medicaldatasharing.model.ResearchCenter;
import com.medicaldatasharing.model.Scientist;
import com.medicaldatasharing.model.User;
import com.medicaldatasharing.repository.ResearchCenterRepository;
import com.medicaldatasharing.response.ResearchCenterResponse;
import com.medicaldatasharing.response.ScientistResponse;
import com.medicaldatasharing.response.UserResponse;
import com.medicaldatasharing.security.jwt.JwtProvider;
import com.medicaldatasharing.security.service.UserDetailsServiceImpl;
import com.medicaldatasharing.util.Constants;
import com.owlike.genson.GenericType;
import com.owlike.genson.Genson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
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
import java.util.Objects;

@Service
public class ResearchCenterService {
    @Autowired
    private ResearchCenterRepository researchCenterRepository;

    @Autowired
    private UserDetailsServiceImpl userDetailsService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtProvider jwtProvider;

    @Autowired
    private RestTemplate restTemplate;

    public User getUser(String email) {
        ResearchCenter researchCenter = researchCenterRepository.findResearchCenterByEmail(email);
        if (researchCenter != null) {
            return researchCenter;
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

    public String getFullNameFromUserService(String id) {
        String url = "http://localhost:9000/api/user/get-full-name/" + id;
        return restTemplate.getForObject(url, String.class);
    }

    public String getResearchCenterResponse(String id) {
        ResearchCenter researchCenter = researchCenterRepository.findResearchCenterById(id);
        if (researchCenter == null) return null;
        ResearchCenterResponse researchCenterResponse = new ResearchCenterResponse(researchCenter);
        return new Genson().serialize(researchCenterResponse);
    }

    public ScientistResponse getScientistResponseResponseFromScientistResponseService(String id) {
        String url = "http://localhost:9004/api/scientist/permit-all/get-scientist-response/" + id;
        String scientistResponseStr = restTemplate.getForObject(url, String.class);
        return new Genson().deserialize(scientistResponseStr, ScientistResponse.class);
    }

    public String getAllScientistByResearchCenter(String researchCenterId) throws Exception {
        String url = "http://localhost:9004/api/scientist/permit-all/get-all-scientist-by-research-center-id/{researchCenterId}";
        ResponseEntity<String> response = restTemplate.exchange(
                url,
                HttpMethod.POST,
                HttpEntity.EMPTY,
                new ParameterizedTypeReference<String>() {},
                researchCenterId
        );
        List<ScientistResponse> scientistResponseList = new Genson().deserialize(response.getBody(),
                new GenericType<List<ScientistResponse>>() {});
        return new Genson().serialize(scientistResponseList);
    }

    public String registerScientist(RegisterForm registerForm) throws AuthException {
        try {
            User user = getLoggedUser();
            String accessToken = jwtProvider.generateJwtToken(user.getId());
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.set("Authorization-Service", "Bearer " + accessToken);

            HttpEntity<RegisterForm> request = new HttpEntity<>(registerForm, headers);

            String url = "http://localhost:9004/api/scientist/permit-all/register-user";
            ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.POST, request, String.class);

            return response.getBody();
        } catch (Exception e) {
            throw new AuthException("Error while signUp in hyperledger");
        }
    }

    public String getUserInfo(String id) throws Exception {
        List<UserResponse> userResponseList = new ArrayList<>();
        User user = getLoggedUser();

        String org = id.substring(0, id.indexOf("-"));

        if (org.equals("Scientist")) {
            ScientistResponse scientistResponse = getScientistResponseResponseFromScientistResponseService(id);
            if (Objects.equals(scientistResponse.getResearchCenterId(), user.getId())) {
                userResponseList.add(scientistResponse);
            }
        }
        else if (!Objects.equals(id, user.getId())) {
            throw new Exception("Lỗi xác thực!!!");
        }

        List<ResearchCenter> researchCenterList = researchCenterRepository.findAllById(id);
        for (ResearchCenter researchCenter: researchCenterList) {
            ResearchCenterResponse userResponse = new ResearchCenterResponse(researchCenter);
            userResponseList.add(userResponse);
        }

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

    public String getFullName(String id) throws Exception {
        String org = id.substring(0, id.indexOf("-"));
        if (org.equals("ResearchCenter")) {
            ResearchCenter researchCenter = researchCenterRepository.findResearchCenterById(id);
            if (researchCenter != null) {
                return researchCenter.getFullName();
            }
        }

        String fullName = getFullNameFromUserService(id);
        return fullName;
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
                case Constants.ROLE_RESEARCH_CENTER:
                    researchCenterRepository.save((ResearchCenter) user);
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
                case Constants.ROLE_RESEARCH_CENTER:
                    researchCenterRepository.save((ResearchCenter) user);
                    break;
            }
            return "Thành công";
        }
        catch (Exception e) {
            throw e;
        }
    }

}

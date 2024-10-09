package com.medicaldatasharing.service;

import com.medicaldatasharing.chaincode.Config;
import com.medicaldatasharing.chaincode.client.RegisterUserHyperledger;
import com.medicaldatasharing.chaincode.dto.Drug;
import com.medicaldatasharing.chaincode.dto.Request;
import com.medicaldatasharing.enumeration.RequestType;
import com.medicaldatasharing.form.*;
import com.medicaldatasharing.model.Doctor;
import com.medicaldatasharing.model.Manufacturer;
import com.medicaldatasharing.model.MedicalInstitution;
import com.medicaldatasharing.model.User;
import com.medicaldatasharing.repository.*;
import com.medicaldatasharing.response.*;
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
public class MedicalInstitutionService {
    @Autowired
    private MedicalInstitutionRepository medicalInstitutionRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private JwtProvider jwtProvider;

    public String getFullNameFromUserService(String id) {
        String url = "http://localhost:9000/api/user/get-full-name/" + id;
        return restTemplate.getForObject(url, String.class);
    }

    public List<DoctorResponse> getAllDoctorByMedicalInstitutionId(String medicalInstitutionId) {
        String url = "http://localhost:9002/api/doctor/permit-all/get-all-doctor-by-medical-institution-id/{medicalInstitutionId}";
        ResponseEntity<String> response = restTemplate.exchange(
                url,
                HttpMethod.POST,
                HttpEntity.EMPTY,
                new ParameterizedTypeReference<String>() {},
                medicalInstitutionId
        );
        List<DoctorResponse> doctorResponseList = new Genson().deserialize(response.getBody(),
                new GenericType<List<DoctorResponse>>() {});
        return doctorResponseList;
    }

    public String registerDoctor(RegisterForm registerForm) throws AuthException {
        try {
            User user = getLoggedUser();
            String accessToken = jwtProvider.generateJwtToken(user.getId());
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.set("Authorization-Service", "Bearer " + accessToken);

            HttpEntity<RegisterForm> request = new HttpEntity<>(registerForm, headers);

            String url = "http://localhost:9002/api/doctor/permit-all/register-user";
            ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.POST, request, String.class);

            return response.getBody();
        } catch (Exception e) {
            throw new AuthException("Error while signUp in hyperledger");
        }
    }
    
    public String getFullName(String id) {
        String org = id.substring(0, id.indexOf("-"));
        if (org.equals("MedicalInstitution")) {
            MedicalInstitution medicalInstitution = medicalInstitutionRepository.findMedicalInstitutionById(id);
            if (medicalInstitution != null) {
                return medicalInstitution.getFullName();
            }
        }

        String fullName = getFullNameFromUserService(id);
        return fullName;

    }

    public User getUser(String email) {
        MedicalInstitution medicalInstitution = medicalInstitutionRepository.findMedicalInstitutionByEmail(email);
        if (medicalInstitution != null) {
            return medicalInstitution;
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
    public String getAllDoctorByMedicalInstitution() throws Exception {
        User user = getLoggedUser();
        
        List<DoctorResponse> doctorResponseList = getAllDoctorByMedicalInstitutionId(user.getId());

        try {
            return new Genson().serialize(doctorResponseList);
        }
        catch (Exception e) {
            throw e;
        }
    }

    public String getUserInfo(String id) throws Exception {
        List<UserResponse> userResponseList = new ArrayList<>();
        User user = getLoggedUser();

        MedicalInstitution medicalInstitution = (MedicalInstitution) user;
        List<DoctorResponse> doctorResponses = getAllDoctorByMedicalInstitutionId(medicalInstitution.getId());

        for (DoctorResponse doctorResponse: doctorResponses) {
            if (!Objects.equals(doctorResponse.getDoctorId(), id)) continue;
            doctorResponse.setMedicalInstitutionName(getFullName(doctorResponse.getMedicalInstitutionId()));
            userResponseList.add(doctorResponse);
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
                case Constants.ROLE_MEDICAL_INSTITUTION:
                    medicalInstitutionRepository.save((MedicalInstitution) user);
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
                case Constants.ROLE_MEDICAL_INSTITUTION:
                    medicalInstitutionRepository.save((MedicalInstitution) user);
                    break;
            }
            return "Thành công";
        }
        catch (Exception e) {
            throw e;
        }
    }

    public MedicalInstitutionResponse getMedicalInstitutionResponse(String id) {
        MedicalInstitution medicalInstitution = medicalInstitutionRepository.findMedicalInstitutionById(id);
        if (medicalInstitution == null) return null;
        MedicalInstitutionResponse medicalInstitutionResponse = new MedicalInstitutionResponse(medicalInstitution);
        return medicalInstitutionResponse;
    }
}

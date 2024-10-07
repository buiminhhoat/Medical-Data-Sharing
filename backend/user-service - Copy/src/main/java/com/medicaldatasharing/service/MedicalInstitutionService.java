package com.medicaldatasharing.service;

import com.medicaldatasharing.chaincode.Config;
import com.medicaldatasharing.chaincode.client.RegisterUserHyperledger;
import com.medicaldatasharing.form.RegisterForm;
import com.medicaldatasharing.model.Doctor;
import com.medicaldatasharing.model.MedicalInstitution;
import com.medicaldatasharing.model.User;
import com.medicaldatasharing.repository.*;
import com.medicaldatasharing.response.DoctorResponse;
import com.medicaldatasharing.response.UserResponse;
import com.medicaldatasharing.security.service.UserDetailsServiceImpl;
import com.medicaldatasharing.util.Constants;
import com.owlike.genson.Genson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.security.auth.message.AuthException;
import java.util.ArrayList;
import java.util.List;

@Service
public class MedicalInstitutionService {
    @Autowired
    private DoctorRepository doctorRepository;

    @Autowired
    private MedicalInstitutionRepository medicalInstitutionRepository;

    @Autowired
    private UserDetailsServiceImpl userDetailsService;

    @Autowired
    private PasswordEncoder passwordEncoder;

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
        List<UserResponse> userResponseList = new ArrayList<>();
        User user = getLoggedUser();

        List<Doctor> doctorList = doctorRepository.findAllByMedicalInstitutionId(user.getId());
        for (Doctor doctor: doctorList) {
            DoctorResponse userResponse = new DoctorResponse(doctor);
            userResponseList.add(userResponse);
        }

        try {
            return new Genson().serialize(userResponseList);
        }
        catch (Exception e) {
            throw e;
        }
    }

    public String registerDoctor(RegisterForm registerForm) throws AuthException {
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
                    .medicalInstitutionId(getLoggedUser().getId())
                    .address(registerForm.getAddress())
                    .build();
            doctorRepository.save(doctor);

            String appUserIdentityId = doctor.getEmail();
            String org = Config.DOCTOR_ORG;
            String userIdentityId = doctor.getId();
            RegisterUserHyperledger.enrollOrgAppUsers(appUserIdentityId, org, userIdentityId);
            return new Genson().serialize(doctor);
        } catch (Exception e) {
            throw new AuthException("Error while signUp in hyperledger");
        }
    }

    public String getUserInfo(String id) throws Exception {
        List<UserResponse> userResponseList = new ArrayList<>();
        User user = getLoggedUser();

        MedicalInstitution medicalInstitution = (MedicalInstitution) user;
        List<Doctor> doctorList = doctorRepository.findDoctorByIdAndMedicalInstitutionId(id, medicalInstitution.getId());

        for (Doctor doctor: doctorList) {
            DoctorResponse userResponse = new DoctorResponse(doctor);
            userResponse.setMedicalInstitutionName(userDetailsService.getUserByUserId(userResponse.getMedicalInstitutionId()).getFullName());
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

}

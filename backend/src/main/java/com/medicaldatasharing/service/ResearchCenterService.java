package com.medicaldatasharing.service;

import com.medicaldatasharing.chaincode.Config;
import com.medicaldatasharing.chaincode.client.RegisterUserHyperledger;
import com.medicaldatasharing.form.RegisterForm;
import com.medicaldatasharing.model.*;
import com.medicaldatasharing.repository.*;
import com.medicaldatasharing.response.DoctorResponse;
import com.medicaldatasharing.response.ScientistResponse;
import com.medicaldatasharing.response.UserResponse;
import com.medicaldatasharing.security.service.UserDetailsServiceImpl;
import com.medicaldatasharing.util.Constants;
import com.owlike.genson.Genson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.security.auth.message.AuthException;
import java.util.ArrayList;
import java.util.List;

@Service
public class ResearchCenterService {
    @Autowired
    private AdminRepository adminRepository;

    @Autowired
    private DoctorRepository doctorRepository;

    @Autowired
    private DrugStoreRepository drugStoreRepository;

    @Autowired
    private InsuranceCompanyRepository insuranceCompanyRepository;

    @Autowired
    private ManufacturerRepository manufacturerRepository;

    @Autowired
    private ResearchCenterRepository researchCenterRepository;

    @Autowired
    private ScientistRepository scientistRepository;

    @Autowired
    private MedicalInstitutionRepository medicalInstitutionRepository;

    @Autowired
    private PatientRepository patientRepository;

    @Autowired
    private UserDetailsServiceImpl userDetailsService;

    @Autowired
    private HyperledgerService hyperledgerService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public String getAllScientistByResearchCenter() throws Exception {
        List<UserResponse> userResponseList = new ArrayList<>();
        User user = userDetailsService.getLoggedUser();

        List<Scientist> scientistList = scientistRepository.findAllByResearchCenterId(user.getId());
        for (Scientist scientist: scientistList) {
            ScientistResponse userResponse = new ScientistResponse(scientist);
            userResponseList.add(userResponse);
        }

        try {
            return new Genson().serialize(userResponseList);
        }
        catch (Exception e) {
            throw e;
        }
    }

    public String registerScientist(RegisterForm registerForm) throws AuthException {
        try {
            Scientist scientist = Scientist
                    .builder()
                    .fullName(registerForm.getFullName())
                    .email(registerForm.getEmail())
                    .role(Constants.ROLE_SCIENTIST)
                    .username(registerForm.getEmail())
                    .password(passwordEncoder.encode(registerForm.getPassword()))
                    .enabled(true)
                    .researchCenterId(userDetailsService.getLoggedUser().getId())
                    .address(registerForm.getAddress())
                    .build();
            scientistRepository.save(scientist);

            String appUserIdentityId = scientist.getEmail();
            String org = Config.SCIENTIST_ORG;
            String userIdentityId = scientist.getId();
            RegisterUserHyperledger.enrollOrgAppUsers(appUserIdentityId, org, userIdentityId);
            return new Genson().serialize(scientist);
        } catch (Exception e) {
            throw new AuthException("Error while signUp in hyperledger");
        }
    }

    public String getUserInfo(String id) throws Exception {
        List<UserResponse> userResponseList = new ArrayList<>();
        User user = userDetailsService.getLoggedUser();

        ResearchCenter researchCenter = (ResearchCenter) user;
        List<Scientist> scientistList = scientistRepository.findScientistByIdAndResearchCenterId(id, researchCenter.getId());

        for (Scientist scientist: scientistList) {
            ScientistResponse userResponse = new ScientistResponse(scientist);
            userResponse.setResearchCenterName(userDetailsService.getUserByUserId(userResponse.getResearchCenterId()).getFullName());
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

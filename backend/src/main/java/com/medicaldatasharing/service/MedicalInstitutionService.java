package com.medicaldatasharing.service;

import com.medicaldatasharing.chaincode.Config;
import com.medicaldatasharing.chaincode.client.RegisterUserHyperledger;
import com.medicaldatasharing.form.RegisterForm;
import com.medicaldatasharing.model.*;
import com.medicaldatasharing.repository.*;
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
public class MedicalInstitutionService {
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

    public String getAllDoctorByMedicalInstitution() throws Exception {
        List<UserResponse> userResponseList = new ArrayList<>();
        User user = userDetailsService.getLoggedUser();

        List<Doctor> doctorList = doctorRepository.findAllByMedicalInstitutionId(user.getId());
        for (Doctor doctor: doctorList) {
            UserResponse userResponse = new UserResponse(doctor);
            userResponseList.add(userResponse);
        }

        try {
            return new Genson().serialize(userResponseList);
        }
        catch (Exception e) {
            throw e;
        }
    }

    public String registerMedicalInstitution(RegisterForm registerForm) {
        MedicalInstitution medicalInstitution = null;
        try {
            medicalInstitution = MedicalInstitution
                    .builder()
                    .fullName(registerForm.getFullName())
                    .email(registerForm.getEmail())
                    .role(Constants.ROLE_MEDICAL_INSTITUTION)
                    .username(registerForm.getEmail())
                    .password(passwordEncoder.encode(registerForm.getPassword()))
                    .address(registerForm.getAddress())
                    .enabled(true)
                    .build();
            medicalInstitutionRepository.save(medicalInstitution);
            return new Genson().serialize(medicalInstitution);
        }
        catch (Exception exception) {
            medicalInstitutionRepository.delete(medicalInstitution);
            throw exception;
        }
    }

    public String registerManufacturer(RegisterForm registerForm) throws AuthException {
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
            return new Genson().serialize(manufacturer);
        } catch (Exception e) {
            throw new AuthException("Error while signUp in hyperledger");
        }
    }

    public String registerResearchCenter(RegisterForm registerForm) throws AuthException {
        ResearchCenter researchCenter = null;
        try {
            researchCenter = ResearchCenter
                    .builder()
                    .fullName(registerForm.getFullName())
                    .email(registerForm.getEmail())
                    .username(registerForm.getEmail())
                    .password(passwordEncoder.encode(registerForm.getPassword()))
                    .address(registerForm.getAddress())
                    .role(Constants.ROLE_RESEARCH_CENTER)
                    .enabled(true)
                    .build();

            researchCenterRepository.save(researchCenter);
            return new Genson().serialize(researchCenter);
        } catch (Exception e) {
            researchCenterRepository.delete(researchCenter);
            throw e;
        }
    }
}

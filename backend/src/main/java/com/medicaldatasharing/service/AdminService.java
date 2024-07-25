package com.medicaldatasharing.service;

import com.medicaldatasharing.chaincode.Config;
import com.medicaldatasharing.chaincode.dto.Drug;
import com.medicaldatasharing.form.RegisterForm;
import com.medicaldatasharing.model.*;
import com.medicaldatasharing.repository.*;
import com.medicaldatasharing.response.*;
import com.medicaldatasharing.security.service.UserDetailsServiceImpl;
import com.medicaldatasharing.util.Constants;
import com.owlike.genson.Genson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.security.crypto.password.PasswordEncoder;
import com.medicaldatasharing.chaincode.client.RegisterUserHyperledger;

import javax.print.Doc;
import javax.security.auth.message.AuthException;
import java.util.*;

@Service
public class AdminService {
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

    public String getAllUserByAdmin() throws Exception {
        List<UserResponse> userResponseList = new ArrayList<>();
        User user = userDetailsService.getLoggedUser();
        List<Patient> patientList = patientRepository.findAll();
        for (Patient patient: patientList) {
            PatientResponse userResponse = new PatientResponse(patient);
            userResponseList.add(userResponse);
        }

        List<Doctor> doctorList = doctorRepository.findAll();
        for (Doctor doctor: doctorList) {
            DoctorResponse userResponse = new DoctorResponse(doctor);
            userResponseList.add(userResponse);
        }

        List<DrugStore> drugStoreList = drugStoreRepository.findAll();
        for (DrugStore drugStore: drugStoreList) {
            DrugStoreResponse userResponse = new DrugStoreResponse(drugStore);
            userResponseList.add(userResponse);
        }

        List<Manufacturer> manufacturerList = manufacturerRepository.findAll();
        for (Manufacturer manufacturer: manufacturerList) {
            ManufacturerResponse userResponse = new ManufacturerResponse(manufacturer);
            userResponseList.add(userResponse);
        }

        List<MedicalInstitution> medicalInstitutionList = medicalInstitutionRepository.findAll();
        for (MedicalInstitution medicalInstitution: medicalInstitutionList) {
            MedicalInstitutionResponse userResponse = new MedicalInstitutionResponse(medicalInstitution);
            userResponseList.add(userResponse);
        }

        List<ResearchCenter> researchCenterList = researchCenterRepository.findAll();
        for (ResearchCenter researchCenter: researchCenterList) {
            ResearchCenterResponse userResponse = new ResearchCenterResponse(researchCenter);
            userResponseList.add(userResponse);
        }

        List<Scientist> scientistList = scientistRepository.findAll();
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

    public String getUserInfo(String id) throws Exception {
        List<UserResponse> userResponseList = new ArrayList<>();
        User user = userDetailsService.getLoggedUser();
        List<Patient> patientList = patientRepository.findAllById(id);
        for (Patient patient: patientList) {
            PatientResponse userResponse = new PatientResponse(patient);
            userResponseList.add(userResponse);
        }

        List<Doctor> doctorList = doctorRepository.findAllById(id);
        for (Doctor doctor: doctorList) {
            DoctorResponse userResponse = new DoctorResponse(doctor);
            userResponse.setMedicalInstitutionName(userDetailsService.getUserByUserId(userResponse.getMedicalInstitutionId()).getFullName());
            userResponseList.add(userResponse);
        }

        List<DrugStore> drugStoreList = drugStoreRepository.findAllById(id);
        for (DrugStore drugStore: drugStoreList) {
            DrugStoreResponse userResponse = new DrugStoreResponse(drugStore);
            userResponseList.add(userResponse);
        }

        List<Manufacturer> manufacturerList = manufacturerRepository.findAllById(id);
        for (Manufacturer manufacturer: manufacturerList) {
            ManufacturerResponse userResponse = new ManufacturerResponse(manufacturer);
            userResponseList.add(userResponse);
        }

        List<MedicalInstitution> medicalInstitutionList = medicalInstitutionRepository.findAllById(id);
        for (MedicalInstitution medicalInstitution: medicalInstitutionList) {
            MedicalInstitutionResponse userResponse = new MedicalInstitutionResponse(medicalInstitution);
            userResponseList.add(userResponse);
        }

        List<ResearchCenter> researchCenterList = researchCenterRepository.findAllById(id);
        for (ResearchCenter researchCenter: researchCenterList) {
            ResearchCenterResponse userResponse = new ResearchCenterResponse(researchCenter);
            userResponseList.add(userResponse);
        }

        List<Scientist> scientistList = scientistRepository.findAllById(id);
        for (Scientist scientist: scientistList) {
            ScientistResponse userResponse = new ScientistResponse(scientist);
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

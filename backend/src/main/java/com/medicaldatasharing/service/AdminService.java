package com.medicaldatasharing.service;

import com.medicaldatasharing.chaincode.dto.MedicalRecord;
import com.medicaldatasharing.chaincode.dto.Medication;
import com.medicaldatasharing.chaincode.dto.ViewRequest;
import com.medicaldatasharing.dto.GetListAuthorizedMedicalRecordByDoctorQueryDto;
import com.medicaldatasharing.enumeration.RequestStatus;
import com.medicaldatasharing.enumeration.RequestType;
import com.medicaldatasharing.form.AddMedicalRecordForm;
import com.medicaldatasharing.form.SearchViewRequestForm;
import com.medicaldatasharing.form.SendViewRequestForm;
import com.medicaldatasharing.model.*;
import com.medicaldatasharing.repository.*;
import com.medicaldatasharing.response.MedicalRecordResponse;
import com.medicaldatasharing.response.MedicationResponse;
import com.medicaldatasharing.response.PatientResponse;
import com.medicaldatasharing.response.UserResponse;
import com.medicaldatasharing.security.service.UserDetailsServiceImpl;
import com.owlike.genson.Genson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

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

    public String getAllUserByAdmin() throws Exception {
        List<UserResponse> userResponseList = new ArrayList<>();
        User user = userDetailsService.getLoggedUser();
        List<Patient> patientList = patientRepository.findAll();
        for (Patient patient: patientList) {
            UserResponse userResponse = new UserResponse(patient);
            userResponseList.add(userResponse);
        }

        List<Doctor> doctorList = doctorRepository.findAll();
        for (Doctor doctor: doctorList) {
            UserResponse userResponse = new UserResponse(doctor);
            userResponseList.add(userResponse);
        }

        List<DrugStore> drugStoreList = drugStoreRepository.findAll();
        for (DrugStore drugStore: drugStoreList) {
            UserResponse userResponse = new UserResponse(drugStore);
            userResponseList.add(userResponse);
        }

        List<InsuranceCompany> insuranceCompanyList = insuranceCompanyRepository.findAll();
        for (InsuranceCompany insuranceCompany: insuranceCompanyList) {
            UserResponse userResponse = new UserResponse(insuranceCompany);
            userResponseList.add(userResponse);
        }

        List<Manufacturer> manufacturerList = manufacturerRepository.findAll();
        for (Manufacturer manufacturer: manufacturerList) {
            UserResponse userResponse = new UserResponse(manufacturer);
            userResponseList.add(userResponse);
        }

        List<MedicalInstitution> medicalInstitutionList = medicalInstitutionRepository.findAll();
        for (MedicalInstitution medicalInstitution: medicalInstitutionList) {
            UserResponse userResponse = new UserResponse(medicalInstitution);
            userResponseList.add(userResponse);
        }

        List<ResearchCenter> researchCenterList = researchCenterRepository.findAll();
        for (ResearchCenter researchCenter: researchCenterList) {
            UserResponse userResponse = new UserResponse(researchCenter);
            userResponseList.add(userResponse);
        }

        List<Scientist> scientistList = scientistRepository.findAll();
        for (Scientist scientist: scientistList) {
            UserResponse userResponse = new UserResponse(scientist);
            userResponseList.add(userResponse);
        }
        
        try {
            return new Genson().serialize(userResponseList);
        }
        catch (Exception e) {
            throw e;
        }
    }
}

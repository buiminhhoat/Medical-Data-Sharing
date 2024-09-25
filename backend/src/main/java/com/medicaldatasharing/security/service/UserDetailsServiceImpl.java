package com.medicaldatasharing.security.service;

import com.medicaldatasharing.model.*;
import com.medicaldatasharing.repository.*;
import com.medicaldatasharing.security.dto.UserPrinciple;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {
    @Autowired
    private PatientRepository patientRepository;

    @Autowired
    private DoctorRepository doctorRepository;

    @Autowired
    private AdminRepository adminRepository;

    @Autowired
    private DrugStoreRepository drugStoreRepository;

    @Autowired
    private InsuranceCompanyRepository insuranceCompanyRepository;

    @Autowired
    private ManufacturerRepository manufacturerRepository;

    @Autowired
    private MedicalInstitutionRepository medicalInstitutionRepository;

    @Autowired
    private ResearchCenterRepository researchCenterRepository;

    @Autowired
    protected ScientistRepository scientistRepository;

    @Override
    @Transactional
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = getUser(username);
        return UserPrinciple.build(user);
    }

    public User getUserByUserId(String userId) {
        Patient patient = patientRepository.findPatientById(userId);
        if (patient != null) {
            return patient;
        }

        Doctor doctor = doctorRepository.findDoctorById(userId);
        if (doctor != null) {
            return doctor;
        }

        Admin admin = adminRepository.findAdminById(userId);
        if (admin != null) {
            return admin;
        }

        DrugStore drugStore = drugStoreRepository.findDrugStoreById(userId);
        if (drugStore != null) {
            return drugStore;
        }

        InsuranceCompany insuranceCompany = insuranceCompanyRepository.findInsuranceCompanyById(userId);
        if (insuranceCompany != null) {
            return insuranceCompany;
        }

        Manufacturer manufacturer = manufacturerRepository.findManufacturerById(userId);
        if (manufacturer != null) {
            return manufacturer;
        }

        MedicalInstitution medicalInstitution = medicalInstitutionRepository.findMedicalInstitutionById(userId);
        if (medicalInstitution != null) {
            return medicalInstitution;
        }

        ResearchCenter researchCenter = researchCenterRepository.findResearchInstituteById(userId);
        if (researchCenter != null) {
            return researchCenter;
        }

        Scientist scientist = scientistRepository.findScientistById(userId);
        if (scientist != null){
            return scientist;
        }
        return null;
    }

    public User getUser(String email) {
        Patient patient = patientRepository.findByUsername(email);
        if (patient != null) {
            return patient;
        }

        Doctor doctor = doctorRepository.findByUsername(email);
        if (doctor != null) {
            return doctor;
        }

        Admin admin = adminRepository.findAdminByEmail(email);
        if (admin != null) {
            return admin;
        }

        DrugStore drugStore = drugStoreRepository.findDrugStoreByEmail(email);
        if (drugStore != null) {
            return drugStore;
        }

        InsuranceCompany insuranceCompany = insuranceCompanyRepository.findInsuranceCompanyByEmail(email);
        if (insuranceCompany != null) {
            return insuranceCompany;
        }

        Manufacturer manufacturer = manufacturerRepository.findManufacturerByEmail(email);
        if (manufacturer != null) {
            return manufacturer;
        }

        MedicalInstitution medicalInstitution = medicalInstitutionRepository.findMedicalInstitutionByEmail(email);
        if (medicalInstitution != null) {
            return medicalInstitution;
        }

        ResearchCenter researchCenter = researchCenterRepository.findResearchInstituteByEmail(email);
        if (researchCenter != null) {
            return researchCenter;
        }

        Scientist scientist = scientistRepository.findScientistByEmail(email);
        if (scientist != null){
            return scientist;
        }
        return null;
    }
}

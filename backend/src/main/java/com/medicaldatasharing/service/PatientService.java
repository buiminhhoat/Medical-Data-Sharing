package com.medicaldatasharing.service;

import com.medicaldatasharing.repository.AdminRepository;
import com.medicaldatasharing.repository.DoctorRepository;
import com.medicaldatasharing.repository.MedicalInstitutionRepository;
import com.medicaldatasharing.repository.PatientRepository;
import com.medicaldatasharing.security.service.UserDetailsServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PatientService {
    @Autowired
    private AdminRepository adminRepository;

    @Autowired
    private DoctorRepository doctorRepository;

    @Autowired
    private MedicalInstitutionRepository medicalInstitutionRepository;

    @Autowired
    private PatientRepository patientRepository;

    @Autowired
    private UserDetailsServiceImpl userDetailsService;

    @Autowired
    private HyperledgerService hyperledgerService;

//    public DefineRequestDto defineRequest(
//            DefineRequestForm defineRequestForm
//    ) throws Exception {
//        User user = userDetailsService.getLoggedUser();
//        Request request = hyperledgerService.defineRequest(user, defineRequestForm);
//
//        DefineRequestDto defineRequestDto = new DefineRequestDto();
//        defineRequestDto.setRequestId(request.getRequestId());
//        defineRequestDto.setRequestStatus(request.getRequestStatus());
//        defineRequestDto.setAccessAvailableFrom(request.getAccessAvailableFrom());
//        defineRequestDto.setAccessAvailableUntil(request.getAccessAvailableUntil());
//        return defineRequestDto;
//    }
}

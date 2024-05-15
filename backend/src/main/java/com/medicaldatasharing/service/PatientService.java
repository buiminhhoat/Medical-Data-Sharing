package com.medicaldatasharing.service;

import com.medicaldatasharing.chaincode.dto.MedicalRecordAccessRequest;
import com.medicaldatasharing.dto.MedicalRecordAccessDefineRequestDto;
import com.medicaldatasharing.dto.MedicalRecordAccessSendRequestDto;
import com.medicaldatasharing.dto.form.MedicalRecordAccessDefineRequestForm;
import com.medicaldatasharing.model.User;
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

    public MedicalRecordAccessDefineRequestDto defineMedicalRecordAccessRequest(
            MedicalRecordAccessDefineRequestForm medicalRecordAccessDefineRequestForm
    ) throws Exception {
        User user = userDetailsService.getLoggedUser();
        MedicalRecordAccessRequest medicalRecordAccessRequest
                = hyperledgerService.defineMedicalRecordAccessRequest(user,
                medicalRecordAccessDefineRequestForm.getMedicalRecordAccessRequestId(),
                medicalRecordAccessDefineRequestForm.getDecision(),
                medicalRecordAccessDefineRequestForm.getAccessAvailableFrom(),
                medicalRecordAccessDefineRequestForm.getAccessAvailableUntil());

        MedicalRecordAccessDefineRequestDto medicalRecordAccessDefineRequestDto = new MedicalRecordAccessDefineRequestDto();
        medicalRecordAccessDefineRequestDto.setMedicalRecordAccessRequestId(medicalRecordAccessRequest.getMedicalRecordAccessRequestId());
        medicalRecordAccessDefineRequestDto.setDecision(medicalRecordAccessRequest.getDecision());
        medicalRecordAccessDefineRequestDto.setAccessAvailableFrom(medicalRecordAccessRequest.getAccessAvailableFrom());
        medicalRecordAccessDefineRequestDto.setAccessAvailableUntil(medicalRecordAccessRequest.getAccessAvailableUntil());
        return medicalRecordAccessDefineRequestDto;
    }
}

package com.medicaldatasharing.service;

import com.medicaldatasharing.chaincode.dto.MedicalRecord;
import com.medicaldatasharing.chaincode.dto.MedicalRecordAccessRequest;
import com.medicaldatasharing.dto.MedicalRecordAccessSendRequestDto;
import com.medicaldatasharing.dto.MedicalRecordDto;
import com.medicaldatasharing.dto.form.MedicalRecordAccessSendRequestForm;
import com.medicaldatasharing.dto.form.MedicalRecordForm;
import com.medicaldatasharing.model.User;
import com.medicaldatasharing.repository.AdminRepository;
import com.medicaldatasharing.repository.DoctorRepository;
import com.medicaldatasharing.repository.MedicalInstitutionRepository;
import com.medicaldatasharing.repository.PatientRepository;
import com.medicaldatasharing.security.service.UserDetailsServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DoctorService {
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

    public MedicalRecordDto addMedicalRecord(MedicalRecordForm medicalRecordForm) throws Exception {
        User user = userDetailsService.getLoggedUser();
        MedicalRecordDto medicalRecordDto = new MedicalRecordDto();
        medicalRecordDto.setPatientId(medicalRecordForm.getPatientId());
        medicalRecordDto.setDoctorId(medicalRecordForm.getDoctorId());
        medicalRecordDto.setMedicalInstitutionId(medicalRecordForm.getMedicalInstitutionId());
        medicalRecordDto.setDateCreated(medicalRecordForm.getTime());
        medicalRecordDto.setTestName(medicalRecordForm.getTestName());
        medicalRecordDto.setRelevantParameters(medicalRecordForm.getRelevantParameters());

        MedicalRecord medicalRecord = hyperledgerService.addMedicalRecord(user, medicalRecordDto);

        MedicalRecordDto result = new MedicalRecordDto();
        result.setPatientId(medicalRecord.getPatientId());
        result.setDoctorId(medicalRecord.getDoctorId());
        result.setMedicalInstitutionId(medicalRecord.getMedicalInstitutionId());
        result.setDateCreated(medicalRecord.getDateCreated());
        result.setTestName(medicalRecord.getTestName());
        result.setRelevantParameters(medicalRecord.getRelevantParameters());
        return result;
    }

    public MedicalRecordAccessSendRequestDto sendMedicalRecordAccessRequest(
            MedicalRecordAccessSendRequestForm medicalRecordAccessSendRequestForm) throws Exception {
        User user = userDetailsService.getLoggedUser();
        MedicalRecordAccessRequest medicalRecordAccessRequest = hyperledgerService.sendMedicalRecordAccessRequest(user, medicalRecordAccessSendRequestForm);

        MedicalRecordAccessSendRequestDto medicalRecordAccessSendRequestDto = new MedicalRecordAccessSendRequestDto();
        medicalRecordAccessSendRequestDto.setMedicalRecordAccessRequestId(medicalRecordAccessRequest.getMedicalRecordAccessRequestId());
        medicalRecordAccessSendRequestDto.setPatientId(medicalRecordAccessRequest.getPatientId());
        medicalRecordAccessSendRequestDto.setRequesterId(medicalRecordAccessRequest.getRequestId());
        medicalRecordAccessSendRequestDto.setMedicalRecordId(medicalRecordAccessRequest.getMedicalRecordId());
        medicalRecordAccessSendRequestDto.setDateCreated(medicalRecordAccessRequest.getDateCreated());
        return medicalRecordAccessSendRequestDto;
    }
}

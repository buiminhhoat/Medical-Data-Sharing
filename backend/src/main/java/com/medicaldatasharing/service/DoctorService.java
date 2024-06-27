package com.medicaldatasharing.service;

import com.medicaldatasharing.chaincode.dto.MedicalRecord;
import com.medicaldatasharing.dto.MedicalRecordDto;
import com.medicaldatasharing.form.MedicalRecordForm;
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
        medicalRecordDto.setDateModified(medicalRecordForm.getDateModified());
        medicalRecordDto.setTestName(medicalRecordForm.getTestName());
        medicalRecordDto.setDetails(medicalRecordForm.getDetails());

        MedicalRecord medicalRecord = hyperledgerService.addMedicalRecord(user, medicalRecordDto);

        MedicalRecordDto result = new MedicalRecordDto();
        result.setPatientId(medicalRecord.getPatientId());
        result.setDoctorId(medicalRecord.getDoctorId());
        result.setMedicalInstitutionId(medicalRecord.getMedicalInstitutionId());
        result.setDateModified(medicalRecord.getDateModified());
        result.setTestName(medicalRecord.getTestName());
        result.setDetails(medicalRecord.getDetails());
        return result;
    }

//    public SendRequestDto sendRequest(
//            SendRequestForm sendRequestForm) throws Exception {
//        User user = userDetailsService.getLoggedUser();
//        Request request = hyperledgerService.sendRequest(user, sendRequestForm);
//
//        SendRequestDto sendRequestDto = new SendRequestDto();
//        sendRequestDto.setSenderId(request.getSenderId());
//        sendRequestDto.setRecipientId(request.getRecipientId());
//        sendRequestDto.setMedicalRecordId(request.getMedicalRecordId());
//        sendRequestDto.setDateModified(request.getDateModified());
//        sendRequestDto.setRequestType(request.getRequestType());
//        return sendRequestDto;
//    }
}

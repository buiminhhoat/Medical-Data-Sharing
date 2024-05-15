package com.medicaldatasharing.service;

import com.medicaldatasharing.chaincode.dto.ChaincodeMedicalRecord;
import com.medicaldatasharing.dto.MedicalRecordDto;
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
        medicalRecordDto.setTime(medicalRecordForm.getTime());
        medicalRecordDto.setTestName(medicalRecordForm.getTestName());
        medicalRecordDto.setRelevantParameters(medicalRecordForm.getRelevantParameters());

        ChaincodeMedicalRecord chaincodeMedicalRecord = hyperledgerService.addMedicalRecord(user, medicalRecordDto);

        MedicalRecordDto result = new MedicalRecordDto();
        result.setPatientId(chaincodeMedicalRecord.getPatientId());
        result.setDoctorId(chaincodeMedicalRecord.getDoctorId());
        result.setMedicalInstitutionId(chaincodeMedicalRecord.getMedicalInstitutionId());
        result.setTime(chaincodeMedicalRecord.getTime());
        result.setTestName(chaincodeMedicalRecord.getTestName());
        result.setRelevantParameters(chaincodeMedicalRecord.getRelevantParameters());
        return result;
    }
}

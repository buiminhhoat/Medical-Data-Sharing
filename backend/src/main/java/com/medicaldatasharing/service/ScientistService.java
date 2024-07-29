package com.medicaldatasharing.service;

import com.medicaldatasharing.chaincode.dto.Drug;
import com.medicaldatasharing.chaincode.dto.MedicalRecord;
import com.medicaldatasharing.chaincode.dto.Medication;
import com.medicaldatasharing.dto.DrugReactionDto;
import com.medicaldatasharing.dto.GetListAllAuthorizedPatientForScientistDto;
import com.medicaldatasharing.dto.GetListAuthorizedMedicalRecordByScientistQueryDto;
import com.medicaldatasharing.dto.PrescriptionDto;
import com.medicaldatasharing.form.*;
import com.medicaldatasharing.model.Patient;
import com.medicaldatasharing.model.User;
import com.medicaldatasharing.repository.*;
import com.medicaldatasharing.response.MedicalRecordResponse;
import com.medicaldatasharing.response.MedicationResponse;
import com.medicaldatasharing.response.PatientResponse;
import com.medicaldatasharing.security.service.UserDetailsServiceImpl;
import com.owlike.genson.Genson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ScientistService {
    @Autowired
    private AdminRepository adminRepository;

    @Autowired
    private DoctorRepository doctorRepository;

    @Autowired
    private MedicalInstitutionRepository medicalInstitutionRepository;

    @Autowired
    private PatientRepository patientRepository;

    @Autowired
    private ScientistRepository scientistRepository;
    
    @Autowired
    private UserDetailsServiceImpl userDetailsService;

    @Autowired
    private HyperledgerService hyperledgerService;

    public String getListMedicalRecord(GetListAuthorizedMedicalRecordByScientistQueryDto getListAuthorizedMedicalRecordByScientistQueryDto) throws Exception {
        User user = userDetailsService.getLoggedUser();
        try {
            getListAuthorizedMedicalRecordByScientistQueryDto.setScientistId(user.getId());
            List<MedicalRecord> medicalRecordList = hyperledgerService.getListAuthorizedMedicalRecordByScientistQuery(user,
                    getListAuthorizedMedicalRecordByScientistQueryDto);
            List<MedicalRecordResponse> medicalRecordResponseList = new ArrayList<>();
            for (MedicalRecord medicalRecord: medicalRecordList) {
                MedicalRecordResponse medicalRecordResponse = new MedicalRecordResponse(medicalRecord);

                User patient = userDetailsService.getUserByUserId(medicalRecord.getPatientId());
                medicalRecordResponse.setPatientName(patient.getFullName());

                User doctor = userDetailsService.getUserByUserId(medicalRecord.getDoctorId());
                medicalRecordResponse.setDoctorName(doctor.getFullName());

                User medicalInstitution = userDetailsService.getUserByUserId(medicalRecord.getMedicalInstitutionId());
                medicalRecordResponse.setMedicalInstitutionName(medicalInstitution.getFullName());

                medicalRecordResponseList.add(medicalRecordResponse);
            }
            return new Genson().serialize(medicalRecordResponseList);
        }
        catch (Exception e) {
            throw e;
        }
    }

    public String getPrescriptionByScientist(GetPrescriptionForm getPrescriptionForm) throws Exception {
        User user = userDetailsService.getLoggedUser();
        try {
            getPrescriptionForm.setScientistId(user.getId());
            PrescriptionDto prescriptionDto = hyperledgerService.getPrescriptionByScientist(user,
                    getPrescriptionForm);
            return new Genson().serialize(prescriptionDto);
        }
        catch (Exception e) {
            throw e;
        }
    }

    public String getAllPatientManagedByScientistId() throws Exception {
        List<PatientResponse> patientResponseList = new ArrayList<>();
        User user = userDetailsService.getLoggedUser();
        try {
            GetListAllAuthorizedPatientForScientistDto getListAllAuthorizedPatientForScientistDto = new GetListAllAuthorizedPatientForScientistDto();
            getListAllAuthorizedPatientForScientistDto.setScientistId(user.getId());
            List<String> stringList = hyperledgerService.getListAllAuthorizedPatientForScientist(
                    user,
                    getListAllAuthorizedPatientForScientistDto
            );

            for (String patientId: stringList) {
                Patient patient = (Patient) userDetailsService.getUserByUserId(patientId);
                PatientResponse patientResponse = new PatientResponse(patient);
                patientResponseList.add(patientResponse);
            }
            return new Genson().serialize(patientResponseList);
        }
        catch (Exception e) {
            throw e;
        }
    }
}

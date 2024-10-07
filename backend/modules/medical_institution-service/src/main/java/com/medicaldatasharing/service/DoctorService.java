package com.medicaldatasharing.service;

import com.medicaldatasharing.chaincode.dto.MedicalRecord;
import com.medicaldatasharing.chaincode.dto.Medication;
import com.medicaldatasharing.chaincode.dto.ViewRequest;
import com.medicaldatasharing.dto.GetListAllAuthorizedPatientForDoctorDto;
import com.medicaldatasharing.dto.GetListAuthorizedMedicalRecordByDoctorQueryDto;
import com.medicaldatasharing.dto.PrescriptionDto;
import com.medicaldatasharing.form.AddMedicalRecordForm;
import com.medicaldatasharing.form.DefineMedicalRecordForm;
import com.medicaldatasharing.form.GetPrescriptionForm;
import com.medicaldatasharing.form.SendViewRequestForm;
import com.medicaldatasharing.model.Doctor;
import com.medicaldatasharing.model.Patient;
import com.medicaldatasharing.model.User;
import com.medicaldatasharing.repository.AdminRepository;
import com.medicaldatasharing.repository.DoctorRepository;
import com.medicaldatasharing.repository.MedicalInstitutionRepository;
import com.medicaldatasharing.repository.PatientRepository;
import com.medicaldatasharing.response.ManufacturerResponse;
import com.medicaldatasharing.response.MedicalRecordResponse;
import com.medicaldatasharing.response.PatientResponse;
import com.medicaldatasharing.security.service.UserDetailsServiceImpl;
import com.owlike.genson.Genson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class DoctorService {
    @Autowired
    private DoctorRepository doctorRepository;

    @Autowired
    private UserDetailsServiceImpl userDetailsService;

    @Autowired
    private HyperledgerService hyperledgerService;

    public User getUser(String email) {
        Doctor doctor = doctorRepository.findByUsername(email);
        if (doctor != null) {
            return doctor;
        }
        return null;
    }

    public User getLoggedUser() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String username = "";
        User user;
        if (principal instanceof UserDetails) {
            username = ((UserDetails) principal).getUsername();
            user = getUser(username);
            return user;
        } else {
            return null;
        }
    }

    public String getAllMedication() throws Exception {
        User user = getLoggedUser();
        try {
            List<Medication> medicationList = hyperledgerService.getAllMedication(user);
            List<ManufacturerResponse> manufacturerResponseList = new ArrayList<>();
            Map<String, List<Medication>> groupedByManufacturer = medicationList.stream()
                    .collect(Collectors.groupingBy(Medication::getManufacturerId));
            groupedByManufacturer.forEach((manufacturerId, medications) -> {
                ManufacturerResponse manufacturerResponse = new ManufacturerResponse();
                manufacturerResponse.setManufacturerId(manufacturerId);
                manufacturerResponse.setManufacturerName(userDetailsService.getUserByUserId(manufacturerId).getFullName());
                manufacturerResponse.setMedicationList(medications);
                manufacturerResponseList.add(manufacturerResponse);
            });
            return new Genson().serialize(manufacturerResponseList);
        }
        catch (Exception e) {
            throw e;
        }
    }

    public String getListMedicalRecord(GetListAuthorizedMedicalRecordByDoctorQueryDto getListAuthorizedMedicalRecordByDoctorQueryDto) throws Exception {
        User user = getLoggedUser();
        try {
            List<MedicalRecord> medicalRecordList = hyperledgerService.getListAuthorizedMedicalRecordByDoctorQuery(user,
                    getListAuthorizedMedicalRecordByDoctorQueryDto);
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

    public String addMedicalRecord(AddMedicalRecordForm addMedicalRecordForm) throws Exception {
        User user = getLoggedUser();

        MedicalRecord medicalRecord = hyperledgerService.addMedicalRecord(user, addMedicalRecordForm);

        return new Genson().serialize(medicalRecord);
    }

    public String getAllPatientManagedByDoctorId() throws Exception {
        List<PatientResponse> patientResponseList = new ArrayList<>();
        User user = getLoggedUser();
        try {
            GetListAllAuthorizedPatientForDoctorDto getListAllAuthorizedPatientForDoctorDto = new GetListAllAuthorizedPatientForDoctorDto();
            getListAllAuthorizedPatientForDoctorDto.setDoctorId(user.getId());
            List<String> stringList = hyperledgerService.getListAllAuthorizedPatientForDoctor(
                    user,
                    getListAllAuthorizedPatientForDoctorDto
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

    public String sendViewRequest(SendViewRequestForm sendViewRequestForm) throws Exception {
        User user = getLoggedUser();
        try {
            sendViewRequestForm.setSenderId(user.getId());
            ViewRequest viewRequest = hyperledgerService.sendViewRequest(user, sendViewRequestForm);
            return new Genson().serialize(viewRequest);
        }
        catch (Exception e) {
            throw e;
        }
    }

    public String defineMedicalRecord(DefineMedicalRecordForm defineMedicalRecordForm) throws Exception {
        User user = getLoggedUser();
        MedicalRecord defineMedicalRecord = hyperledgerService.defineMedicalRecord(user, defineMedicalRecordForm);
        return new Genson().serialize(defineMedicalRecord);
    }

    public String getPrescriptionByDoctor(GetPrescriptionForm getPrescriptionForm) throws Exception {
        User user = getLoggedUser();
        try {
            getPrescriptionForm.setDoctorId(user.getId());
            PrescriptionDto prescriptionDto = hyperledgerService.getPrescriptionByDoctor(user,
                    getPrescriptionForm);
            return new Genson().serialize(prescriptionDto);
        }
        catch (Exception e) {
            throw e;
        }
    }
}

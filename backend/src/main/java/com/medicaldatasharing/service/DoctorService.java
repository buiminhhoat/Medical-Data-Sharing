package com.medicaldatasharing.service;

import com.medicaldatasharing.chaincode.dto.MedicalRecord;
import com.medicaldatasharing.chaincode.dto.Medication;
import com.medicaldatasharing.dto.MedicalRecordDto;
import com.medicaldatasharing.form.AddMedicalRecordForm;
import com.medicaldatasharing.form.SearchMedicalRecordForm;
import com.medicaldatasharing.model.User;
import com.medicaldatasharing.repository.AdminRepository;
import com.medicaldatasharing.repository.DoctorRepository;
import com.medicaldatasharing.repository.MedicalInstitutionRepository;
import com.medicaldatasharing.repository.PatientRepository;
import com.medicaldatasharing.response.MedicalRecordResponse;
import com.medicaldatasharing.response.MedicationResponse;
import com.medicaldatasharing.security.service.UserDetailsServiceImpl;
import com.owlike.genson.Genson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.stream.Collectors;


import java.util.ArrayList;
import java.util.List;

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

    public String getAllMedication() throws Exception {
        User user = userDetailsService.getLoggedUser();
        try {
            List<Medication> medicationList = hyperledgerService.getAllMedication(user);
            List<MedicationResponse> medicationResponseList = new ArrayList<>();
            Map<String, List<Medication>> groupedByManufacturer = medicationList.stream()
                    .collect(Collectors.groupingBy(Medication::getManufacturerId));
            groupedByManufacturer.forEach((manufacturerId, medications) -> {
                MedicationResponse medicationResponse = new MedicationResponse();
                medicationResponse.setManufacturerId(manufacturerId);
                medicationResponse.setManufacturerName(userDetailsService.getUserByUserId(manufacturerId).getFullName());
                medicationResponse.setMedicationList(medications);
                medicationResponseList.add(medicationResponse);
            });
            return new Genson().serialize(medicationResponseList);
        }
        catch (Exception e) {
            throw e;
        }
    }
    public String getListMedicalRecord(SearchMedicalRecordForm searchMedicalRecordForm) throws Exception {
        User user = userDetailsService.getLoggedUser();
        try {
            List<MedicalRecord> medicalRecordList = hyperledgerService.getListMedicalRecordByDoctorQuery(user, searchMedicalRecordForm);
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
        User user = userDetailsService.getLoggedUser();

        MedicalRecord medicalRecord = hyperledgerService.addMedicalRecord(user, addMedicalRecordForm.toJSONObject());

        return new Genson().serialize(medicalRecord);
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

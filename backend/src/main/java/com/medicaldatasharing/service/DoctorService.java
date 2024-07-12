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
import com.medicaldatasharing.model.Patient;
import com.medicaldatasharing.model.User;
import com.medicaldatasharing.repository.AdminRepository;
import com.medicaldatasharing.repository.DoctorRepository;
import com.medicaldatasharing.repository.MedicalInstitutionRepository;
import com.medicaldatasharing.repository.PatientRepository;
import com.medicaldatasharing.response.MedicalRecordResponse;
import com.medicaldatasharing.response.MedicationResponse;
import com.medicaldatasharing.response.PatientResponse;
import com.medicaldatasharing.security.service.UserDetailsServiceImpl;
import com.owlike.genson.Genson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

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

    public String getListMedicalRecord(GetListAuthorizedMedicalRecordByDoctorQueryDto getListAuthorizedMedicalRecordByDoctorQueryDto) throws Exception {
        User user = userDetailsService.getLoggedUser();
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
        User user = userDetailsService.getLoggedUser();

        MedicalRecord medicalRecord = hyperledgerService.addMedicalRecord(user, addMedicalRecordForm.toJSONObject());

        return new Genson().serialize(medicalRecord);
    }

    public String getAllPatientManagedByDoctorId() throws Exception {
        List<PatientResponse> patientResponseList = new ArrayList<>();
        User user = userDetailsService.getLoggedUser();
        try {
            SearchViewRequestForm searchViewRequestForm = new SearchViewRequestForm();
            searchViewRequestForm.setSenderId(user.getId());
            searchViewRequestForm.setRequestType(RequestType.VIEW_RECORD.toString());
            searchViewRequestForm.setRequestStatus(RequestStatus.ACCEPTED.toString());
            List<ViewRequest> viewRequestList = hyperledgerService.getListViewRequestByRecipientQuery(user, searchViewRequestForm);
            Set<String> patientSet = new HashSet<>();
            for (ViewRequest viewRequest: viewRequestList) {
                patientSet.add(viewRequest.getRecipientId());
            }

            for (String patientId: patientSet) {
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
        User user = userDetailsService.getLoggedUser();
        try {
            ViewRequest viewRequest = hyperledgerService.sendViewRequest(user, sendViewRequestForm);
            return new Genson().serialize(viewRequest);
        }
        catch (Exception e) {
            throw e;
        }
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

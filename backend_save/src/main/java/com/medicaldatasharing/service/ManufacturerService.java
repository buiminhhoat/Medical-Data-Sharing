package com.medicaldatasharing.service;

import com.medicaldatasharing.chaincode.dto.Drug;
import com.medicaldatasharing.chaincode.dto.MedicalRecord;
import com.medicaldatasharing.chaincode.dto.Medication;
import com.medicaldatasharing.chaincode.dto.ViewRequest;
import com.medicaldatasharing.dto.DrugReactionDto;
import com.medicaldatasharing.dto.GetListAllAuthorizedPatientForManufacturerDto;
import com.medicaldatasharing.dto.GetListAuthorizedMedicalRecordByManufacturerQueryDto;
import com.medicaldatasharing.dto.PrescriptionDto;
import com.medicaldatasharing.form.*;
import com.medicaldatasharing.model.Manufacturer;
import com.medicaldatasharing.model.Patient;
import com.medicaldatasharing.model.User;
import com.medicaldatasharing.repository.AdminRepository;
import com.medicaldatasharing.repository.DoctorRepository;
import com.medicaldatasharing.repository.ManufacturerRepository;
import com.medicaldatasharing.repository.MedicalInstitutionRepository;
import com.medicaldatasharing.response.MedicalRecordResponse;
import com.medicaldatasharing.response.MedicationResponse;
import com.medicaldatasharing.response.PatientResponse;
import com.medicaldatasharing.security.service.UserDetailsServiceImpl;
import com.owlike.genson.Genson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ManufacturerService {
    @Autowired
    private ManufacturerRepository manufacturerRepository;

    @Autowired
    private UserDetailsServiceImpl userDetailsService;

    @Autowired
    private HyperledgerService hyperledgerService;

    public User getUser(String email) {
        Manufacturer manufacturer = manufacturerRepository.findManufacturerByEmail(email);
        if (manufacturer != null) {
            return manufacturer;
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

    public String getListMedicalRecord(GetListAuthorizedMedicalRecordByManufacturerQueryDto getListAuthorizedMedicalRecordByManufacturerQueryDto) throws Exception {
        User user = getLoggedUser();
        try {
            List<MedicalRecord> medicalRecordList = hyperledgerService.getListAuthorizedMedicalRecordByManufacturerQuery(user,
                    getListAuthorizedMedicalRecordByManufacturerQueryDto);
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

    public String getPrescriptionByManufacturer(GetPrescriptionForm getPrescriptionForm) throws Exception {
        User user = getLoggedUser();
        try {
            getPrescriptionForm.setManufacturerId(user.getId());
            PrescriptionDto prescriptionDto = hyperledgerService.getPrescriptionByManufacturer(user,
                    getPrescriptionForm);
            return new Genson().serialize(prescriptionDto);
        }
        catch (Exception e) {
            throw e;
        }
    }

    public String getListMedicationByManufacturerId(SearchMedicationForm searchMedicationForm) throws Exception {
        User user = getLoggedUser();
        try {
            searchMedicationForm.setManufacturerId(user.getId());
            List<Medication> medicationList = hyperledgerService.getListMedication(user, searchMedicationForm);
            List<MedicationResponse> medicationResponseList = new ArrayList<>();
            for (Medication medication: medicationList) {
                MedicationResponse medicationResponse = new MedicationResponse(medication);
                medicationResponseList.add(medicationResponse);
            }
            return new Genson().serialize(medicationResponseList);
        }
        catch (Exception e) {
            throw e;
        }
    }

    public String getListDrugReactionByManufacturer() throws Exception {
        User user = getLoggedUser();
        try {
            GetDrugReactionForm getDrugReactionForm = new GetDrugReactionForm();
            getDrugReactionForm.setManufacturerId(user.getId());
            List<DrugReactionDto> drugReactionDtoList = hyperledgerService.getListDrugReactionByManufacturer(user, getDrugReactionForm);
            return new Genson().serialize(drugReactionDtoList);
        }
        catch (Exception e) {
            throw e;
        }
    }

    public String addMedication(AddMedicationForm addMedicationForm) throws Exception {
        User user = getLoggedUser();
        try {
            addMedicationForm.setManufacturerId(user.getId());
            Medication medication = hyperledgerService.addMedication(user, addMedicationForm);
            return new Genson().serialize(medication);
        }
        catch (Exception e) {
            throw e;
        }
    }

    public String addDrug(AddDrugForm addDrugForm) throws Exception {
        User user = getLoggedUser();
        try {
            List<Drug> drugList = hyperledgerService.addDrug(user, addDrugForm);
            return new Genson().serialize(drugList);
        }
        catch (Exception e) {
            throw e;
        }
    }

    public String getAllPatientManagedByManufacturerId() throws Exception {
        List<PatientResponse> patientResponseList = new ArrayList<>();
        User user = getLoggedUser();
        try {
            GetListAllAuthorizedPatientForManufacturerDto getListAllAuthorizedPatientForManufacturerDto = new GetListAllAuthorizedPatientForManufacturerDto();
            getListAllAuthorizedPatientForManufacturerDto.setManufacturerId(user.getId());
            List<String> stringList = hyperledgerService.getListAllAuthorizedPatientForManufacturer(
                    user,
                    getListAllAuthorizedPatientForManufacturerDto
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
}

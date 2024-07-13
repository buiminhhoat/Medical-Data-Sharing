package com.medicaldatasharing.service;

import com.medicaldatasharing.chaincode.dto.Drug;
import com.medicaldatasharing.chaincode.dto.Medication;
import com.medicaldatasharing.form.AddDrugForm;
import com.medicaldatasharing.form.AddMedicationForm;
import com.medicaldatasharing.form.SearchMedicationForm;
import com.medicaldatasharing.model.User;
import com.medicaldatasharing.repository.AdminRepository;
import com.medicaldatasharing.repository.DoctorRepository;
import com.medicaldatasharing.repository.MedicalInstitutionRepository;
import com.medicaldatasharing.repository.PatientRepository;
import com.medicaldatasharing.response.MedicationResponse;
import com.medicaldatasharing.security.service.UserDetailsServiceImpl;
import com.owlike.genson.Genson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ManufacturerService {
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

    public String getListMedicationByManufacturerId(SearchMedicationForm searchMedicationForm) throws Exception {
        User user = userDetailsService.getLoggedUser();
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

    public String addMedication(AddMedicationForm addMedicationForm) throws Exception {
        User user = userDetailsService.getLoggedUser();
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
        User user = userDetailsService.getLoggedUser();
        try {
            List<Drug> drugList = hyperledgerService.addDrug(user, addDrugForm);
            return new Genson().serialize(drugList);
        }
        catch (Exception e) {
            throw e;
        }
    }
}

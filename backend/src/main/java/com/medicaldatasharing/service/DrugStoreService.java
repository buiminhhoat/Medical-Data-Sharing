package com.medicaldatasharing.service;

import com.medicaldatasharing.chaincode.dto.ViewPrescriptionRequest;
import com.medicaldatasharing.dto.PrescriptionDto;
import com.medicaldatasharing.form.GetPrescriptionForm;
import com.medicaldatasharing.form.SendViewPrescriptionRequestForm;
import com.medicaldatasharing.model.User;
import com.medicaldatasharing.repository.AdminRepository;
import com.medicaldatasharing.repository.DoctorRepository;
import com.medicaldatasharing.repository.MedicalInstitutionRepository;
import com.medicaldatasharing.repository.PatientRepository;
import com.medicaldatasharing.security.service.UserDetailsServiceImpl;
import com.owlike.genson.Genson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DrugStoreService {
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

    public String getPrescriptionByDrugStore(GetPrescriptionForm getPrescriptionForm) throws Exception {
        User user = userDetailsService.getLoggedUser();
        try {
            getPrescriptionForm.setDrugStoreId(user.getId());
            PrescriptionDto prescriptionDto = hyperledgerService.getPrescriptionByDrugStore(user,
                    getPrescriptionForm);
            return new Genson().serialize(prescriptionDto);
        }
        catch (Exception e) {
            throw e;
        }
    }

    public String sendViewPrescriptionRequest(SendViewPrescriptionRequestForm sendViewPrescriptionRequestForm) throws Exception {
        User user = userDetailsService.getLoggedUser();
        try {
            sendViewPrescriptionRequestForm.setSenderId(user.getId());
            ViewPrescriptionRequest viewPrescriptionRequest = hyperledgerService.sendViewPrescriptionRequest(user,
                    sendViewPrescriptionRequestForm);
            return new Genson().serialize(viewPrescriptionRequest);
        }
        catch (Exception e) {
            throw e;
        }
    }
}

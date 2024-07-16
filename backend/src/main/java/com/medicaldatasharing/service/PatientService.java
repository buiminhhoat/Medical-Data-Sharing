package com.medicaldatasharing.service;

import com.medicaldatasharing.chaincode.dto.AppointmentRequest;
import com.medicaldatasharing.chaincode.dto.MedicalRecord;
import com.medicaldatasharing.chaincode.dto.Purchase;
import com.medicaldatasharing.dto.PrescriptionDto;
import com.medicaldatasharing.dto.PurchaseDto;
import com.medicaldatasharing.form.GetPrescriptionForm;
import com.medicaldatasharing.form.SearchMedicalRecordForm;
import com.medicaldatasharing.form.SearchPurchaseForm;
import com.medicaldatasharing.form.SendAppointmentRequestForm;
import com.medicaldatasharing.model.User;
import com.medicaldatasharing.repository.AdminRepository;
import com.medicaldatasharing.repository.DoctorRepository;
import com.medicaldatasharing.repository.MedicalInstitutionRepository;
import com.medicaldatasharing.repository.PatientRepository;
import com.medicaldatasharing.response.MedicalRecordResponse;
import com.medicaldatasharing.response.PurchaseResponse;
import com.medicaldatasharing.security.service.UserDetailsServiceImpl;
import com.owlike.genson.Genson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class PatientService {
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

    public String getListMedicalRecordByPatientId(SearchMedicalRecordForm searchMedicalRecordForm) throws Exception {
        User user = userDetailsService.getLoggedUser();
        try {
            List<MedicalRecord> medicalRecordList = hyperledgerService.getListMedicalRecordByPatientQuery(user,
                    searchMedicalRecordForm);
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

    public String getPrescriptionByPrescriptionId(GetPrescriptionForm getPrescriptionForm) throws Exception {
        User user = userDetailsService.getLoggedUser();
        try {
            getPrescriptionForm.setPatientId(user.getId());
            PrescriptionDto prescriptionDto = hyperledgerService.getPrescriptionByPatient(user,
                    getPrescriptionForm);
            return new Genson().serialize(prescriptionDto);
        }
        catch (Exception e) {
            throw e;
        }
    }

    public String sendAppointmentRequest(SendAppointmentRequestForm appointmentRequestForm) throws Exception {
        User user = userDetailsService.getLoggedUser();
        try {
            appointmentRequestForm.setSenderId(user.getId());
            AppointmentRequest appointmentRequest = hyperledgerService.sendAppointmentRequest(user, appointmentRequestForm);
            return new Genson().serialize(appointmentRequest);
        } catch (Exception e) {
            throw e;
        }
    }

    public String getListPurchaseByPatientId(SearchPurchaseForm searchPurchaseForm) throws Exception {
        User user = userDetailsService.getLoggedUser();
        try {
            searchPurchaseForm.setPatientId(user.getId());
            List<Purchase> purchaseList = hyperledgerService.getListPurchaseByPatientId(user, searchPurchaseForm);
            List<PurchaseResponse> purchaseResponseList = new ArrayList<>();
            for (Purchase purchase: purchaseList) {
                PurchaseResponse purchaseResponse = new PurchaseResponse(purchase);
                purchaseResponse.setPatientName(userDetailsService.getUserByUserId(purchaseResponse.getPatientId()).getFullName());
                purchaseResponse.setDrugStoreName(userDetailsService.getUserByUserId(purchaseResponse.getDrugStoreId()).getFullName());
                purchaseResponseList.add(purchaseResponse);
            }
            return new Genson().serialize(purchaseResponseList);
        } catch (Exception e) {
            throw e;
        }
    }

    public String getPurchaseByPurchaseId(String purchaseId) throws Exception {
        User user = userDetailsService.getLoggedUser();
        try {
            PurchaseDto purchaseDto = hyperledgerService.getPurchaseByPurchaseId(user, purchaseId);
            PurchaseResponse purchaseResponse = new PurchaseResponse(purchaseDto);
            purchaseResponse.setPatientName(userDetailsService.getUserByUserId(purchaseResponse.getPatientId()).getFullName());
            purchaseResponse.setDrugStoreName(userDetailsService.getUserByUserId(purchaseResponse.getDrugStoreId()).getFullName());
            return new Genson().serialize(purchaseResponse);
        } catch (Exception e) {
            throw e;
        }
    }

//    public DefineRequestDto defineRequest(
//            DefineRequestForm defineRequestForm
//    ) throws Exception {
//        User user = userDetailsService.getLoggedUser();
//        Request request = hyperledgerService.defineRequest(user, defineRequestForm);
//
//        DefineRequestDto defineRequestDto = new DefineRequestDto();
//        defineRequestDto.setRequestId(request.getRequestId());
//        defineRequestDto.setRequestStatus(request.getRequestStatus());
//        defineRequestDto.setAccessAvailableFrom(request.getAccessAvailableFrom());
//        defineRequestDto.setAccessAvailableUntil(request.getAccessAvailableUntil());
//        return defineRequestDto;
//    }
}

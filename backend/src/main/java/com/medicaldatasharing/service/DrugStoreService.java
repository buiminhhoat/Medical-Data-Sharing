package com.medicaldatasharing.service;

import com.medicaldatasharing.chaincode.dto.Drug;
import com.medicaldatasharing.chaincode.dto.Purchase;
import com.medicaldatasharing.chaincode.dto.ViewPrescriptionRequest;
import com.medicaldatasharing.dto.PrescriptionDto;
import com.medicaldatasharing.dto.PurchaseDto;
import com.medicaldatasharing.form.GetPrescriptionForm;
import com.medicaldatasharing.form.SearchDrugForm;
import com.medicaldatasharing.form.SearchPurchaseForm;
import com.medicaldatasharing.form.SendViewPrescriptionRequestForm;
import com.medicaldatasharing.model.DrugStore;
import com.medicaldatasharing.model.User;
import com.medicaldatasharing.repository.AdminRepository;
import com.medicaldatasharing.repository.DoctorRepository;
import com.medicaldatasharing.repository.DrugStoreRepository;
import com.medicaldatasharing.repository.MedicalInstitutionRepository;
import com.medicaldatasharing.response.PurchaseResponse;
import com.medicaldatasharing.security.service.UserDetailsServiceImpl;
import com.owlike.genson.Genson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class DrugStoreService {
    @Autowired
    private DrugStoreRepository drugStoreRepository;

    @Autowired
    private UserDetailsServiceImpl userDetailsService;

    @Autowired
    private HyperledgerService hyperledgerService;

    public User getUser(String email) {
        DrugStore drugStore = drugStoreRepository.findDrugStoreByEmail(email);
        if (drugStore != null) {
            return drugStore;
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

    public String getPrescriptionByDrugStore(GetPrescriptionForm getPrescriptionForm) throws Exception {
        User user = getLoggedUser();
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
        User user = getLoggedUser();
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


    public String getListDrugByMedicationIdAndOwnerId(SearchDrugForm searchDrugForm) throws Exception {
        User user = getLoggedUser();
        try {
            searchDrugForm.setOwnerId(user.getId());
            List<Drug> drugList = hyperledgerService.getListDrugByOwnerId(user, searchDrugForm);
            return new Genson().serialize(drugList);
        } catch (Exception e) {
            throw e;
        }
    }

    public String addPurchase(PurchaseDto purchaseDto) throws Exception {
        User user = getLoggedUser();
        try {
            purchaseDto.setDrugStoreId(user.getId());
            Purchase purchase = hyperledgerService.addPurchase(user, purchaseDto);
            return new Genson().serialize(purchase);
        } catch (Exception e) {
            throw e;
        }
    }

    public String getListPurchaseByDrugStoreId(SearchPurchaseForm searchPurchaseForm) throws Exception {
        User user = getLoggedUser();
        try {
            searchPurchaseForm.setDrugStoreId(user.getId());
            List<Purchase> purchaseList = hyperledgerService.getListPurchaseByDrugStoreId(user, searchPurchaseForm);
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
        User user = getLoggedUser();
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
}

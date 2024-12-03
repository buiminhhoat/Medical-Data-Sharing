package com.medicaldatasharing.service;

import com.medicaldatasharing.chaincode.dto.*;
import com.medicaldatasharing.dto.PrescriptionDto;
import com.medicaldatasharing.dto.PurchaseDto;
import com.medicaldatasharing.enumeration.RequestType;
import com.medicaldatasharing.form.*;
import com.medicaldatasharing.model.DrugStore;
import com.medicaldatasharing.model.Patient;
import com.medicaldatasharing.model.User;
import com.medicaldatasharing.repository.DrugStoreRepository;
import com.medicaldatasharing.response.*;
import com.medicaldatasharing.security.service.UserDetailsServiceImpl;
import com.medicaldatasharing.util.Constants;
import com.owlike.genson.Genson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
public class DrugStoreService {
    @Autowired
    private DrugStoreRepository drugStoreRepository;

    @Autowired
    private HyperledgerService hyperledgerService;

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public String getFullNameFromUserService(String id) {
        String url = "http://localhost:9000/api/user/get-full-name/" + id;
        return restTemplate.getForObject(url, String.class);
    }

    public PatientResponse getPatientResponseFromPatientService(String id) {
        String url = "http://localhost:9001/api/patient/permit-all/get-patient-response/" + id;
        String patientResponseStr = restTemplate.getForObject(url, String.class);
        return new Genson().deserialize(patientResponseStr, PatientResponse.class);
    }

    public String getFullName(String id) {
        String org = id.substring(0, id.indexOf("-"));
        if (org.equals("DrugStore")) {
            DrugStore drugStore = drugStoreRepository.findDrugStoreById(id);
            if (drugStore != null) {
                return drugStore.getFullName();
            }
        }

        String fullName = getFullNameFromUserService(id);
        return fullName;
    }

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
                purchaseResponse.setPatientName(getFullName(purchaseResponse.getPatientId()));
                purchaseResponse.setDrugStoreName(getFullName(purchaseResponse.getDrugStoreId()));
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
            purchaseResponse.setPatientName(getFullName(purchaseResponse.getPatientId()));
            purchaseResponse.setDrugStoreName(getFullName(purchaseResponse.getDrugStoreId()));
            return new Genson().serialize(purchaseResponse);
        } catch (Exception e) {
            throw e;
        }
    }

    public String getDrugStoreResponse(String id) {
        DrugStore drugStore = drugStoreRepository.findDrugStoreById(id);
        if (drugStore == null) return null;
        DrugStoreResponse drugStoreResponse = new DrugStoreResponse(drugStore);
        return new Genson().serialize(drugStoreResponse);
    }

    public String getAllUserResponse() {
        List<DrugStore> drugStoreList = drugStoreRepository.findAll();
        List<DrugStoreResponse> drugStoreResponseList = new ArrayList<>();
        for (DrugStore patient: drugStoreList) {
            DrugStoreResponse drugStoreResponse = new DrugStoreResponse(patient);
            drugStoreResponseList.add(drugStoreResponse);
        }
        return new Genson().serialize(drugStoreResponseList);
    }

    public String getAllRequest() throws Exception {
        User user = getLoggedUser();
        try {
            List<Request> requestList = hyperledgerService.getAllRequest(user, user.getId());
            List<RequestResponse> requestResponseList = new ArrayList<>();
            for (Request request: requestList) {
                RequestResponse requestResponse = new RequestResponse(request);
                requestResponse.setSenderName(getFullName(request.getSenderId()));
                requestResponse.setRecipientName(getFullName(request.getRecipientId()));
                requestResponseList.add(requestResponse);
            }
            return new Genson().serialize(requestResponseList);
        }
        catch (Exception e) {
            throw e;
        }
    }

    public String getRequest(String requestId, String requestType) throws Exception {
        User user = getLoggedUser();
        Request request = new Request();
        try {
            if (Objects.equals(requestType, RequestType.APPOINTMENT.toString())) {
                request = hyperledgerService.getAppointmentRequest(user, requestId);
            }
            if (Objects.equals(requestType, RequestType.VIEW_RECORD.toString())) {
                request = hyperledgerService.getViewRequest(user, requestId);
            }
            if (Objects.equals(requestType, RequestType.VIEW_PRESCRIPTION.toString())) {
                request = hyperledgerService.getViewPrescriptionRequest(user, requestId);
            }

            RequestResponse requestResponse = new RequestResponse(request);
            requestResponse.setSenderName(getFullName(request.getSenderId()));
            requestResponse.setRecipientName(getFullName(request.getRecipientId()));

            if (Objects.equals(requestResponse.getRequestType(), RequestType.APPOINTMENT.toString())) {
                requestResponse.setMedicalInstitutionName(getFullName(requestResponse.getMedicalInstitutionId()));
            }

            return new Genson().serialize(requestResponse);
        }
        catch (Exception e) {
            throw e;
        }
    }

    public String defineRequest(DefineRequestForm defineRequestForm) throws Exception {
        User user = getLoggedUser();
        Request request = new Request();
        try {
            if (Objects.equals(defineRequestForm.getRequestType(), RequestType.APPOINTMENT.toString())) {
                DefineAppointmentRequestForm defineAppointmentRequestForm = new DefineAppointmentRequestForm(defineRequestForm);
                request = hyperledgerService.defineAppointmentRequest(user, defineAppointmentRequestForm);
            }
            if (Objects.equals(defineRequestForm.getRequestType(), RequestType.VIEW_RECORD.toString())) {
                DefineViewRequestForm defineViewRequestForm = new DefineViewRequestForm(defineRequestForm);
                request = hyperledgerService.defineViewRequest(user, defineViewRequestForm);
            }
            if (Objects.equals(defineRequestForm.getRequestType(), RequestType.VIEW_PRESCRIPTION.toString())) {
                DefineViewPrescriptionRequestForm viewPrescriptionRequestForm = new DefineViewPrescriptionRequestForm(defineRequestForm);
                request = hyperledgerService.defineViewPrescriptionRequest(user, viewPrescriptionRequestForm);
            }

            RequestResponse requestResponse = new RequestResponse(request);
            requestResponse.setSenderName(getFullName(request.getSenderId()));
            requestResponse.setRecipientName(getFullName(request.getRecipientId()));

            if (Objects.equals(requestResponse.getRequestType(), RequestType.APPOINTMENT.toString())) {
                requestResponse.setMedicalInstitutionName(getFullName(requestResponse.getMedicalInstitutionId()));
            }

            return new Genson().serialize(requestResponse);
        }
        catch (Exception e) {
            throw e;
        }
    }

    public String getUserInfo(String id) throws Exception {
        List<UserResponse> userResponseList = new ArrayList<>();
        User user = getLoggedUser();

        if (!Objects.equals(id, user.getId())) {
            throw new Exception("Lỗi xác thực!!!");
        }

        List<DrugStore> drugStoreList = drugStoreRepository.findAllById(id);
        for (DrugStore drugStore: drugStoreList) {
            DrugStoreResponse userResponse = new DrugStoreResponse(drugStore);
            userResponseList.add(userResponse);
        }

        try {
            if (userResponseList.size() == 1) {
                return new Genson().serialize(userResponseList.get(0));
            }
            else {
                throw new Exception("Không tìm thấy thông tin của user " + id);
            }
        }
        catch (Exception e) {
            throw e;
        }
    }

    public String changePassword(ChangePasswordForm changePasswordForm) throws Exception {
        User user = getLoggedUser();
        try {
            Authentication authentication = null;
            try {
                authentication = authenticationManager.authenticate(
                        new UsernamePasswordAuthenticationToken(
                                user.getEmail(),
                                changePasswordForm.getOldPassword()
                        )
                );
            } catch (AuthenticationException e) {
                throw new Exception("Mật khẩu cũ không đúng");
            }

            user.setPassword(passwordEncoder.encode(changePasswordForm.getPassword()));
            switch (user.getRole()) {
                case Constants.ROLE_DRUG_STORE:
                    drugStoreRepository.save((DrugStore) user);
                    break;
            }
            return "Thành công";
        }
        catch (Exception e) {
            throw e;
        }
    }

    public String updateInformation(UpdateInformationForm updateInformationForm) throws Exception {
        User user = getLoggedUser();
        try {
            user.setFullName(updateInformationForm.getFullName());
            user.setAddress(updateInformationForm.getAddress());

            switch (user.getRole()) {
                case Constants.ROLE_DRUG_STORE:
                    drugStoreRepository.save((DrugStore) user);
                    break;
            }
            return "Thành công";
        }
        catch (Exception e) {
            throw e;
        }
    }

    public String getListDrugByOwnerId() throws Exception {
        User user = getLoggedUser();
        try {
            SearchDrugForm searchDrugForm = new SearchDrugForm();
            searchDrugForm.setOwnerId(user.getId());
            List<Drug> drugList = hyperledgerService.getListDrugByOwnerId(user, searchDrugForm);
            return new Genson().serialize(drugList);
        }
        catch (Exception e) {
            throw e;
        }
    }

    public String getMedication(String medicationId) throws Exception {
        User user = getLoggedUser();
        try {
            Medication medication = hyperledgerService.getMedication(user, medicationId);
            MedicationResponse medicationResponse = new MedicationResponse(medication);
            medicationResponse.setManufacturerName(getFullName(medication.getManufacturerId()));
            return new Genson().serialize(medicationResponse);
        }
        catch (Exception exception) {
            throw exception;
        }
    }
}

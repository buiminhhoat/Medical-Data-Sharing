package com.medicaldatasharing.service;

import com.medicaldatasharing.chaincode.dto.*;
import com.medicaldatasharing.dto.PrescriptionDto;
import com.medicaldatasharing.dto.PurchaseDto;
import com.medicaldatasharing.enumeration.RequestType;
import com.medicaldatasharing.form.*;
import com.medicaldatasharing.model.Patient;
import com.medicaldatasharing.model.User;
import com.medicaldatasharing.repository.PatientRepository;
import com.medicaldatasharing.response.*;
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
public class PatientService {

    @Autowired
    private PatientRepository patientRepository;

    @Autowired
    private HyperledgerService hyperledgerService;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private RestTemplate restTemplate;

    public String getFullNameFromUserService(String id) {
        String url = "http://localhost:9000/api/user/get-full-name/" + id;
        return restTemplate.getForObject(url, String.class);
    }

    public DoctorResponse getDoctorResponseFromDoctorService(String id) {
        String url = "http://localhost:9002/api/doctor/permit-all/get-doctor-response/" + id;
        return restTemplate.getForObject(url, DoctorResponse.class);
    }

    public String getFullName(String id) throws Exception {
        String org = id.substring(0, id.indexOf("-"));
        if (org.equals("Patient")) {
            Patient patient = patientRepository.findPatientById(id);
            if (patient != null) {
                return patient.getFullName();
            }
        }

        try {
            String fullName = getFullNameFromUserService(id);
            return fullName;
        }
        catch (Exception e) {
            throw e;
        }
    }

    public User getUser(String email) {
        Patient patient = patientRepository.findByUsername(email);
        if (patient != null) {
            return patient;
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

    public String getListMedicalRecordByPatientId(SearchMedicalRecordForm searchMedicalRecordForm) throws Exception {
        User user = getLoggedUser();
        try {
            List<MedicalRecord> medicalRecordList = hyperledgerService.getListMedicalRecordByPatientQuery(user,
                    searchMedicalRecordForm);
            List<MedicalRecordResponse> medicalRecordResponseList = new ArrayList<>();
            for (MedicalRecord medicalRecord: medicalRecordList) {
                MedicalRecordResponse medicalRecordResponse = new MedicalRecordResponse(medicalRecord);

                medicalRecordResponse.setPatientName(getFullName(medicalRecord.getPatientId()));
                medicalRecordResponse.setDoctorName(getFullName(medicalRecord.getDoctorId()));
                medicalRecordResponse.setMedicalInstitutionName(getFullName(medicalRecord.getMedicalInstitutionId()));

                medicalRecordResponseList.add(medicalRecordResponse);
            }
            return new Genson().serialize(medicalRecordResponseList);
        }
        catch (Exception e) {
            throw e;
        }
    }

    public String getPrescriptionByPrescriptionId(GetPrescriptionForm getPrescriptionForm) throws Exception {
        User user = getLoggedUser();
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
        User user = getLoggedUser();
        try {
            appointmentRequestForm.setSenderId(user.getId());
            AppointmentRequest appointmentRequest = hyperledgerService.sendAppointmentRequest(user, appointmentRequestForm);
            return new Genson().serialize(appointmentRequest);
        } catch (Exception e) {
            throw e;
        }
    }

    public String getListPurchaseByPatientId(SearchPurchaseForm searchPurchaseForm) throws Exception {
        User user = getLoggedUser();
        try {
            searchPurchaseForm.setPatientId(user.getId());
            List<Purchase> purchaseList = hyperledgerService.getListPurchaseByPatientId(user, searchPurchaseForm);
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

    public String defineMedicalRecord(DefineMedicalRecordForm defineMedicalRecordForm) throws Exception {
        User user = getLoggedUser();
        MedicalRecord defineMedicalRecord = hyperledgerService.defineMedicalRecord(user, defineMedicalRecordForm);
        return new Genson().serialize(defineMedicalRecord);
    }

    public String sharePrescriptionByPatient(SendViewPrescriptionRequestForm sendViewPrescriptionRequestForm)
            throws Exception {
        User user = getLoggedUser();
        try {
            sendViewPrescriptionRequestForm.setRecipientId(user.getId());
            ViewPrescriptionRequest viewPrescriptionRequest = hyperledgerService.sharePrescriptionByPatient(user,
                    sendViewPrescriptionRequestForm);
            return new Genson().serialize(viewPrescriptionRequest);
        }
        catch (Exception e) {
            throw e;
        }
    }

    public String updateDrugReactionByPatient(UpdateDrugReactionForm updateDrugReactionForm)
            throws Exception {
        User user = getLoggedUser();
        try {
            Prescription prescription = hyperledgerService.updateDrugReactionFromPatient(user,
                    updateDrugReactionForm);
            return new Genson().serialize(prescription);
        }
        catch (Exception e) {
            throw e;
        }
    }

    // :)
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
                case Constants.ROLE_PATIENT:
                    patientRepository.save((Patient) user);
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

            if (Objects.equals(user.getRole(), Constants.ROLE_PATIENT)) {
                Patient patient = (Patient) user;
                patient.setGender(updateInformationForm.getGender());
                patient.setDateBirthday(updateInformationForm.getDateBirthday());
            }
            else {
                throw new Exception("User must be patient");
            }
            return "Thành công";
        }
        catch (Exception e) {
            throw e;
        }
    }

    public String getPatientResponse(String id) {
        Patient patient = patientRepository.findPatientById(id);
        if (patient == null) return null;
        PatientResponse patientResponse = new PatientResponse(patient);
        return new Genson().serialize(patientResponse);
    }

    public String getAllPatientResponse() {
        List<Patient> patientList = patientRepository.findAll();
        List<PatientResponse> patientResponseList = new ArrayList<>();
        for (Patient patient: patientList) {
            PatientResponse patientResponse = new PatientResponse(patient);
            patientResponseList.add(patientResponse);
        }
        return new Genson().serialize(patientResponseList);
    }

    public String getAllDoctor() {
        return null;
    }

    public String getUserInfo(String id) throws Exception {
        List<UserResponse> userResponseList = new ArrayList<>();
        User user = getLoggedUser();

        if (!Objects.equals(id, user.getId())) {
            throw new Exception("Lỗi xác thực!!!");
        }

        List<Patient> patientList = new ArrayList<>();
        patientList.add(patientRepository.findPatientById(id));
        for (Patient patient: patientList) {
            PatientResponse userResponse = new PatientResponse(patient);
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
}
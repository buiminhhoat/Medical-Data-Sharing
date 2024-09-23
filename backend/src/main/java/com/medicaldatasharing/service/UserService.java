package com.medicaldatasharing.service;

import com.medicaldatasharing.chaincode.dto.Drug;
import com.medicaldatasharing.chaincode.dto.Request;
import com.medicaldatasharing.enumeration.RequestType;
import com.medicaldatasharing.form.*;
import com.medicaldatasharing.model.*;
import com.medicaldatasharing.repository.*;
import com.medicaldatasharing.response.*;
import com.medicaldatasharing.form.ChangePasswordForm;
import com.medicaldatasharing.security.service.UserDetailsServiceImpl;
import com.medicaldatasharing.util.Constants;
import com.medicaldatasharing.util.StringUtil;
import com.owlike.genson.Genson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.print.Doc;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
public class UserService {
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

    @Autowired
    private InsuranceCompanyRepository insuranceCompanyRepository;

    @Autowired
    private ManufacturerRepository manufacturerRepository;

    @Autowired
    private ResearchCenterRepository researchCenterRepository;

    @Autowired
    private ScientistRepository scientistRepository;

    @Autowired
    private DrugStoreRepository drugStoreRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private AuthenticationManager authenticationManager;

    public String getAllRequest() throws Exception {
        User user = userDetailsService.getLoggedUser();
        try {
            List<Request> requestList = hyperledgerService.getAllRequest(user, user.getId());
            List<RequestResponse> requestResponseList = new ArrayList<>();
            for (Request request: requestList) {
                RequestResponse requestResponse = new RequestResponse(request);
                User sender = userDetailsService.getUserByUserId(request.getSenderId());
                requestResponse.setSenderName(sender.getFullName());
                User recipient = userDetailsService.getUserByUserId(request.getRecipientId());
                requestResponse.setRecipientName(recipient.getFullName());
                requestResponseList.add(requestResponse);
            }
            return new Genson().serialize(requestResponseList);
        }
        catch (Exception e) {
            throw e;
        }
    }

    public String getRequest(String requestId, String requestType) throws Exception {
        User user = userDetailsService.getLoggedUser();
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
            if (Objects.equals(requestType, RequestType.PURCHASE.toString())) {
                request = hyperledgerService.getPurchaseRequest(user, requestId);
            }
            if (Objects.equals(requestType, RequestType.PAYMENT.toString())) {
                request = hyperledgerService.getPaymentRequest(user, requestId);
            }
            if (Objects.equals(requestType, RequestType.CONFIRM_PAYMENT.toString())) {
                request = hyperledgerService.getConfirmPaymentRequest(user, requestId);
            }

            RequestResponse requestResponse = new RequestResponse(request);
            User sender = userDetailsService.getUserByUserId(request.getSenderId());
            requestResponse.setSenderName(sender.getFullName());
            User recipient = userDetailsService.getUserByUserId(request.getRecipientId());
            requestResponse.setRecipientName(recipient.getFullName());

            if (Objects.equals(requestResponse.getRequestType(), RequestType.APPOINTMENT.toString())) {
                User medicalInstitution = userDetailsService.getUserByUserId(requestResponse.getMedicalInstitutionId());
                requestResponse.setMedicalInstitutionName(medicalInstitution.getFullName());
            }

            return new Genson().serialize(requestResponse);
        }
        catch (Exception e) {
            throw e;
        }
    }

    public String defineRequest(DefineRequestForm defineRequestForm) throws Exception {
        User user = userDetailsService.getLoggedUser();
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
            if (Objects.equals(defineRequestForm.getRequestType(), RequestType.PURCHASE.toString())) {
                DefinePurchaseRequestForm definePurchaseRequestForm = new DefinePurchaseRequestForm(defineRequestForm);
                request = hyperledgerService.definePurchaseRequest(user, definePurchaseRequestForm);
            }
            if (Objects.equals(defineRequestForm.getRequestType(), RequestType.PAYMENT.toString())) {
                DefinePaymentRequestForm definePaymentRequestForm = new DefinePaymentRequestForm(defineRequestForm);
                request = hyperledgerService.definePaymentRequest(user, definePaymentRequestForm);
            }
            if (Objects.equals(defineRequestForm.getRequestType(), RequestType.CONFIRM_PAYMENT.toString())) {
                DefineConfirmPaymentRequestForm defineConfirmPaymentRequestForm = new DefineConfirmPaymentRequestForm(defineRequestForm);
                request = hyperledgerService.defineConfirmPaymentRequest(user, defineConfirmPaymentRequestForm);
            }

            RequestResponse requestResponse = new RequestResponse(request);
            User sender = userDetailsService.getUserByUserId(request.getSenderId());
            requestResponse.setSenderName(sender.getFullName());
            User recipient = userDetailsService.getUserByUserId(request.getRecipientId());
            requestResponse.setRecipientName(recipient.getFullName());

            if (Objects.equals(requestResponse.getRequestType(), RequestType.APPOINTMENT.toString())) {
                User medicalInstitution = userDetailsService.getUserByUserId(requestResponse.getMedicalInstitutionId());
                requestResponse.setMedicalInstitutionName(medicalInstitution.getFullName());
            }

            return new Genson().serialize(requestResponse);
        }
        catch (Exception e) {
            throw e;
        }
    }

    public String getAllDoctor() {
        try {
            List<Doctor> doctorList = doctorRepository.findAll();
            List<DoctorResponse> doctorResponseList = new ArrayList<>();
            for (Doctor doctor: doctorList) {
                DoctorResponse doctorResponse = new DoctorResponse(doctor);
                MedicalInstitution medicalInstitution = (MedicalInstitution) userDetailsService.getUserByUserId(doctor.getMedicalInstitutionId());
                doctorResponse.setMedicalInstitutionName(medicalInstitution.getFullName());
                doctorResponseList.add(doctorResponse);
            }
            return new Genson().serialize(doctorResponseList);
        }
        catch (Exception e) {
            throw e;
        }
    }

    public String getListDrugByOwnerId() throws Exception {
        User user = userDetailsService.getLoggedUser();
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

    public String getUserInfo(String id) throws Exception {
        List<UserResponse> userResponseList = new ArrayList<>();
        User user = userDetailsService.getLoggedUser();

        if (!Objects.equals(id, user.getId())) {
            throw new Exception("Lỗi xác thực!!!");
        }

        List<Patient> patientList = patientRepository.findAllById(id);
        for (Patient patient: patientList) {
            PatientResponse userResponse = new PatientResponse(patient);
            userResponseList.add(userResponse);
        }

        List<Doctor> doctorList = doctorRepository.findAllById(id);
        for (Doctor doctor: doctorList) {
            DoctorResponse userResponse = new DoctorResponse(doctor);
            userResponse.setMedicalInstitutionName(userDetailsService.getUserByUserId(userResponse.getMedicalInstitutionId()).getFullName());
            userResponseList.add(userResponse);
        }

        List<DrugStore> drugStoreList = drugStoreRepository.findAllById(id);
        for (DrugStore drugStore: drugStoreList) {
            DrugStoreResponse userResponse = new DrugStoreResponse(drugStore);
            userResponseList.add(userResponse);
        }

        List<Manufacturer> manufacturerList = manufacturerRepository.findAllById(id);
        for (Manufacturer manufacturer: manufacturerList) {
            ManufacturerResponse userResponse = new ManufacturerResponse(manufacturer);
            userResponseList.add(userResponse);
        }

        List<MedicalInstitution> medicalInstitutionList = medicalInstitutionRepository.findAllById(id);
        for (MedicalInstitution medicalInstitution: medicalInstitutionList) {
            MedicalInstitutionResponse userResponse = new MedicalInstitutionResponse(medicalInstitution);
            userResponseList.add(userResponse);
        }

        List<ResearchCenter> researchCenterList = researchCenterRepository.findAllById(id);
        for (ResearchCenter researchCenter: researchCenterList) {
            ResearchCenterResponse userResponse = new ResearchCenterResponse(researchCenter);
            userResponseList.add(userResponse);
        }

        List<Scientist> scientistList = scientistRepository.findAllById(id);
        for (Scientist scientist: scientistList) {
            ScientistResponse userResponse = new ScientistResponse(scientist);
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

    public String getFullName(String id) throws Exception {
        List<UserResponse> userResponseList = new ArrayList<>();
        User user = userDetailsService.getLoggedUser();

        List<Patient> patientList = patientRepository.findAllById(id);
        for (Patient patient: patientList) {
            PatientResponse userResponse = new PatientResponse(patient);
            userResponseList.add(userResponse);
        }

        List<Doctor> doctorList = doctorRepository.findAllById(id);
        for (Doctor doctor: doctorList) {
            DoctorResponse userResponse = new DoctorResponse(doctor);
            userResponse.setMedicalInstitutionName(userDetailsService.getUserByUserId(userResponse.getMedicalInstitutionId()).getFullName());
            userResponseList.add(userResponse);
        }

        List<DrugStore> drugStoreList = drugStoreRepository.findAllById(id);
        for (DrugStore drugStore: drugStoreList) {
            DrugStoreResponse userResponse = new DrugStoreResponse(drugStore);
            userResponseList.add(userResponse);
        }

        List<Manufacturer> manufacturerList = manufacturerRepository.findAllById(id);
        for (Manufacturer manufacturer: manufacturerList) {
            ManufacturerResponse userResponse = new ManufacturerResponse(manufacturer);
            userResponseList.add(userResponse);
        }

        List<MedicalInstitution> medicalInstitutionList = medicalInstitutionRepository.findAllById(id);
        for (MedicalInstitution medicalInstitution: medicalInstitutionList) {
            MedicalInstitutionResponse userResponse = new MedicalInstitutionResponse(medicalInstitution);
            userResponseList.add(userResponse);
        }

        List<ResearchCenter> researchCenterList = researchCenterRepository.findAllById(id);
        for (ResearchCenter researchCenter: researchCenterList) {
            ResearchCenterResponse userResponse = new ResearchCenterResponse(researchCenter);
            userResponseList.add(userResponse);
        }

        List<Scientist> scientistList = scientistRepository.findAllById(id);
        for (Scientist scientist: scientistList) {
            ScientistResponse userResponse = new ScientistResponse(scientist);
            userResponseList.add(userResponse);
        }

        try {
            if (userResponseList.size() == 1) {
                return new Genson().serialize(userResponseList.get(0).getFullName());
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
        User user = userDetailsService.getLoggedUser();
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
                case Constants.ROLE_ADMIN:
                    adminRepository.save((Admin) user);
                    break;
                case Constants.ROLE_MANUFACTURER:
                    manufacturerRepository.save((Manufacturer) user);
                    break;
                case Constants.ROLE_DOCTOR:
                    doctorRepository.save((Doctor) user);
                    break;
                case Constants.ROLE_MEDICAL_INSTITUTION:
                    medicalInstitutionRepository.save((MedicalInstitution) user);
                    break;
                case Constants.ROLE_DRUG_STORE:
                    drugStoreRepository.save((DrugStore) user);
                    break;
                case Constants.ROLE_RESEARCH_CENTER:
                    researchCenterRepository.save((ResearchCenter) user);
                    break;
                case Constants.ROLE_INSURANCE_COMPANY:
                    insuranceCompanyRepository.save((InsuranceCompany) user);
                    break;
                case Constants.ROLE_SCIENTIST:
                    scientistRepository.save((Scientist) user);
                    break;
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
        User user = userDetailsService.getLoggedUser();
        try {
            user.setFullName(updateInformationForm.getFullName());
            user.setAddress(updateInformationForm.getAddress());

            if (Objects.equals(user.getRole(), Constants.ROLE_PATIENT)) {
                Patient patient = (Patient) user;
                patient.setGender(updateInformationForm.getGender());
                patient.setDateBirthday(updateInformationForm.getDateBirthday());
            }

            if (Objects.equals(user.getRole(), Constants.ROLE_DOCTOR)) {
                Doctor doctor = (Doctor) user;
                doctor.setGender(updateInformationForm.getGender());
                doctor.setDateBirthday(updateInformationForm.getDateBirthday());
                doctor.setDepartment(updateInformationForm.getDepartment());
            }

            if (Objects.equals(user.getRole(), Constants.ROLE_SCIENTIST)) {
                Scientist scientist = (Scientist) user;
                scientist.setGender(updateInformationForm.getGender());
                scientist.setDateBirthday(updateInformationForm.getDateBirthday());
            }

            switch (user.getRole()) {
                case Constants.ROLE_ADMIN:
                    adminRepository.save((Admin) user);
                    break;
                case Constants.ROLE_MANUFACTURER:
                    manufacturerRepository.save((Manufacturer) user);
                    break;
                case Constants.ROLE_DOCTOR:
                    doctorRepository.save((Doctor) user);
                    break;
                case Constants.ROLE_MEDICAL_INSTITUTION:
                    medicalInstitutionRepository.save((MedicalInstitution) user);
                    break;
                case Constants.ROLE_DRUG_STORE:
                    drugStoreRepository.save((DrugStore) user);
                    break;
                case Constants.ROLE_RESEARCH_CENTER:
                    researchCenterRepository.save((ResearchCenter) user);
                    break;
                case Constants.ROLE_INSURANCE_COMPANY:
                    insuranceCompanyRepository.save((InsuranceCompany) user);
                    break;
                case Constants.ROLE_SCIENTIST:
                    scientistRepository.save((Scientist) user);
                    break;
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
}

package com.medicaldatasharing.service;

import com.medicaldatasharing.chaincode.dto.*;
import com.medicaldatasharing.dto.DrugReactionDto;
import com.medicaldatasharing.dto.GetListAllAuthorizedPatientForManufacturerDto;
import com.medicaldatasharing.dto.GetListAuthorizedMedicalRecordByManufacturerQueryDto;
import com.medicaldatasharing.dto.PrescriptionDto;
import com.medicaldatasharing.enumeration.RequestType;
import com.medicaldatasharing.form.*;
import com.medicaldatasharing.model.*;
import com.medicaldatasharing.repository.ManufacturerRepository;
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
public class ManufacturerService {
    @Autowired
    private ManufacturerRepository manufacturerRepository;

    @Autowired
    private UserDetailsServiceImpl userDetailsService;

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
        if (org.equals("Manufacturer")) {
            Manufacturer manufacturer = manufacturerRepository.findManufacturerById(id);
            if (manufacturer != null) {
                return manufacturer.getFullName();
            }
        }

        String fullName = getFullNameFromUserService(id);
        return fullName;
    }

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

    public String getManufacturerResponse(String id) {
        Manufacturer manufacturer = manufacturerRepository.findManufacturerById(id);
        if (manufacturer == null) return null;
        ManufacturerResponse manufacturerResponse = new ManufacturerResponse(manufacturer);
        return new Genson().serialize(manufacturerResponse);
    }

    public String getAllUserResponse() {
        List<Manufacturer> manufacturerList = manufacturerRepository.findAll();
        List<ManufacturerResponse> manufacturerResponseList = new ArrayList<>();
        for (Manufacturer manufacturer: manufacturerList) {
            ManufacturerResponse manufacturerResponse = new ManufacturerResponse(manufacturer);
            manufacturerResponseList.add(manufacturerResponse);
        }
        return new Genson().serialize(manufacturerResponseList);
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

    public String getUserInfo(String id) throws Exception {
        List<UserResponse> userResponseList = new ArrayList<>();
        User user = getLoggedUser();

        if (!Objects.equals(id, user.getId())) {
            throw new Exception("Lỗi xác thực!!!");
        }

        List<Manufacturer> manufacturerList = manufacturerRepository.findAllById(id);
        for (Manufacturer manufacturer: manufacturerList) {
            ManufacturerResponse userResponse = new ManufacturerResponse(manufacturer);
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
                case Constants.ROLE_SCIENTIST:
                    manufacturerRepository.save((Manufacturer) user);
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
                case Constants.ROLE_MANUFACTURER:
                    manufacturerRepository.save((Manufacturer) user);
                    break;
            }
            return "Thành công";
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
                PatientResponse patientResponse = getPatientResponseFromPatientService(patientId);
                patientResponseList.add(patientResponse);
            }
            return new Genson().serialize(patientResponseList);
        }
        catch (Exception e) {
            throw e;
        }
    }
}

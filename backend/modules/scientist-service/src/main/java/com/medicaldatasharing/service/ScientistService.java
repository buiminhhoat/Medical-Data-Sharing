package com.medicaldatasharing.service;

import com.medicaldatasharing.chaincode.dto.MedicalRecord;
import com.medicaldatasharing.chaincode.dto.Request;
import com.medicaldatasharing.chaincode.dto.ViewRequest;
import com.medicaldatasharing.dto.GetListAllAuthorizedPatientForScientistDto;
import com.medicaldatasharing.dto.GetListAuthorizedMedicalRecordByScientistQueryDto;
import com.medicaldatasharing.dto.PrescriptionDto;
import com.medicaldatasharing.enumeration.RequestType;
import com.medicaldatasharing.form.*;
import com.medicaldatasharing.model.Doctor;
import com.medicaldatasharing.model.Patient;
import com.medicaldatasharing.model.Scientist;
import com.medicaldatasharing.model.User;
import com.medicaldatasharing.repository.ScientistRepository;
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
public class ScientistService {
    @Autowired
    private ScientistRepository scientistRepository;

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
        if (org.equals("Scientist")) {
            Scientist scientist = scientistRepository.findScientistById(id);
            if (scientist != null) {
                return scientist.getFullName();
            }
        }

        String fullName = getFullNameFromUserService(id);
        return fullName;
    }

    public User getUser(String email) {
        Scientist scientist = scientistRepository.findScientistByEmail(email);
        if (scientist != null){
            return scientist;
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

    public String getListMedicalRecord(GetListAuthorizedMedicalRecordByScientistQueryDto getListAuthorizedMedicalRecordByScientistQueryDto) throws Exception {
        User user = getLoggedUser();
        try {
            getListAuthorizedMedicalRecordByScientistQueryDto.setScientistId(user.getId());
            List<MedicalRecord> medicalRecordList = hyperledgerService.getListAuthorizedMedicalRecordByScientistQuery(user,
                    getListAuthorizedMedicalRecordByScientistQueryDto);
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

    public String getPrescriptionByScientist(GetPrescriptionForm getPrescriptionForm) throws Exception {
        User user = getLoggedUser();
        try {
            getPrescriptionForm.setScientistId(user.getId());
            PrescriptionDto prescriptionDto = hyperledgerService.getPrescriptionByScientist(user,
                    getPrescriptionForm);
            return new Genson().serialize(prescriptionDto);
        }
        catch (Exception e) {
            throw e;
        }
    }

    public String getAllPatientManagedByScientistId() throws Exception {
        List<PatientResponse> patientResponseList = new ArrayList<>();
        User user = getLoggedUser();
        try {
            GetListAllAuthorizedPatientForScientistDto getListAllAuthorizedPatientForScientistDto = new GetListAllAuthorizedPatientForScientistDto();
            getListAllAuthorizedPatientForScientistDto.setScientistId(user.getId());
            List<String> stringList = hyperledgerService.getListAllAuthorizedPatientForScientist(
                    user,
                    getListAllAuthorizedPatientForScientistDto
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
                    scientistRepository.save((Scientist) user);
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

            if (Objects.equals(user.getRole(), Constants.ROLE_SCIENTIST)) {
                Scientist scientist = (Scientist) user;
                scientist.setGender(updateInformationForm.getGender());
                scientist.setDateBirthday(updateInformationForm.getDateBirthday());
            }

            switch (user.getRole()) {
                case Constants.ROLE_SCIENTIST:
                    scientistRepository.save((Scientist) user);
                    break;
            }
            return "Thành công";
        }
        catch (Exception e) {
            throw e;
        }
    }

    public String getScientistResponse(String id) {
        Scientist scientist = scientistRepository.findScientistById(id);
        if (scientist == null) return null;
        ScientistResponse scientistResponse = new ScientistResponse(scientist);
        return new Genson().serialize(scientistResponse);
    }

    public String getAllUserResponse() {
        List<Scientist> scientistList = scientistRepository.findAll();
        List<ScientistResponse> scientistResponseList = new ArrayList<>();
        for (Scientist scientist: scientistList) {
            ScientistResponse scientistResponse = new ScientistResponse(scientist);
            scientistResponseList.add(scientistResponse);
        }
        return new Genson().serialize(scientistResponseList);
    }

    public String getAllScientistByResearchCenterId(String researchCenterId) {
        try {
            List<Scientist> scientistList = scientistRepository.findScientistByResearchCenterId(researchCenterId);
            List<ScientistResponse> scientistResponseList = new ArrayList<>();
            for (Scientist scientist: scientistList) {
                ScientistResponse scientistResponse = new ScientistResponse(scientist);
                scientistResponse.setResearchCenterId(getFullName(scientist.getResearchCenterId()));
                scientistResponseList.add(scientistResponse);
            }
            return new Genson().serialize(scientistResponseList);
        }
        catch (Exception e) {
            throw e;
        }
    }
}

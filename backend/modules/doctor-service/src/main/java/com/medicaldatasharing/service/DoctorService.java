package com.medicaldatasharing.service;

import com.medicaldatasharing.chaincode.dto.*;
import com.medicaldatasharing.dto.GetListAllAuthorizedPatientForDoctorDto;
import com.medicaldatasharing.dto.GetListAuthorizedMedicalRecordByDoctorQueryDto;
import com.medicaldatasharing.dto.PrescriptionDto;
import com.medicaldatasharing.enumeration.RequestType;
import com.medicaldatasharing.form.*;
import com.medicaldatasharing.model.*;
import com.medicaldatasharing.repository.DoctorRepository;
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
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class DoctorService {
    @Autowired
    private DoctorRepository doctorRepository;

    @Autowired
    private HyperledgerService hyperledgerService;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private RestTemplate restTemplate;

    public String getFullNameFromUserService(String id) {
        String url = "http://localhost:9000/api/user/permit-all/get-full-name/" + id;
        return restTemplate.getForObject(url, String.class);
    }

    public PatientResponse getPatientResponseFromPatientService(String id) {
        String url = "http://localhost:9001/api/patient/permit-all/get-patient-response/" + id;
        return restTemplate.getForObject(url, PatientResponse.class);
    }

    public String getFullName(String id) {
        String org = id.substring(0, id.indexOf("-"));
        if (org.equals("Doctor")) {
            Doctor doctor = doctorRepository.findDoctorById(id);
            if (doctor != null) {
                return doctor.getFullName();
            }
        }

        String fullName = getFullNameFromUserService(id);
        return fullName;

    }

    public User getUser(String email) {
        Doctor doctor = doctorRepository.findByUsername(email);
        if (doctor != null) {
            return doctor;
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

    public String getAllMedication() throws Exception {
        User user = getLoggedUser();
        try {
            List<Medication> medicationList = hyperledgerService.getAllMedication(user);
            List<ManufacturerResponse> manufacturerResponseList = new ArrayList<>();
            Map<String, List<Medication>> groupedByManufacturer = medicationList.stream()
                    .collect(Collectors.groupingBy(Medication::getManufacturerId));
            groupedByManufacturer.forEach((manufacturerId, medications) -> {
                ManufacturerResponse manufacturerResponse = new ManufacturerResponse();
                manufacturerResponse.setManufacturerId(manufacturerId);
                manufacturerResponse.setManufacturerName(getFullName(manufacturerId));
                manufacturerResponse.setMedicationList(medications);
                manufacturerResponseList.add(manufacturerResponse);
            });
            return new Genson().serialize(manufacturerResponseList);
        }
        catch (Exception e) {
            throw e;
        }
    }

    public String getListMedicalRecord(GetListAuthorizedMedicalRecordByDoctorQueryDto getListAuthorizedMedicalRecordByDoctorQueryDto) throws Exception {
        User user = getLoggedUser();
        try {
            List<MedicalRecord> medicalRecordList = hyperledgerService.getListAuthorizedMedicalRecordByDoctorQuery(user,
                    getListAuthorizedMedicalRecordByDoctorQueryDto);
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

    public String addMedicalRecord(AddMedicalRecordForm addMedicalRecordForm) throws Exception {
        User user = getLoggedUser();

        MedicalRecord medicalRecord = hyperledgerService.addMedicalRecord(user, addMedicalRecordForm);

        return new Genson().serialize(medicalRecord);
    }


    public String getAllPatientManagedByDoctorId() throws Exception {
        List<PatientResponse> patientResponseList = new ArrayList<>();
        User user = getLoggedUser();
        try {
            GetListAllAuthorizedPatientForDoctorDto getListAllAuthorizedPatientForDoctorDto = new GetListAllAuthorizedPatientForDoctorDto();
            getListAllAuthorizedPatientForDoctorDto.setDoctorId(user.getId());
            List<String> stringList = hyperledgerService.getListAllAuthorizedPatientForDoctor(
                    user,
                    getListAllAuthorizedPatientForDoctorDto
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

    public String defineMedicalRecord(DefineMedicalRecordForm defineMedicalRecordForm) throws Exception {
        User user = getLoggedUser();
        MedicalRecord defineMedicalRecord = hyperledgerService.defineMedicalRecord(user, defineMedicalRecordForm);
        return new Genson().serialize(defineMedicalRecord);
    }

    public String getPrescriptionByDoctor(GetPrescriptionForm getPrescriptionForm) throws Exception {
        User user = getLoggedUser();
        try {
            getPrescriptionForm.setDoctorId(user.getId());
            PrescriptionDto prescriptionDto = hyperledgerService.getPrescriptionByDoctor(user,
                    getPrescriptionForm);
            return new Genson().serialize(prescriptionDto);
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

    // todo
    public String getAllDoctor() {
        try {
            List<Doctor> doctorList = doctorRepository.findAll();
            List<DoctorResponse> doctorResponseList = new ArrayList<>();
            for (Doctor doctor: doctorList) {
                DoctorResponse doctorResponse = new DoctorResponse(doctor);
//                doctorResponse.setMedicalInstitutionName(getFullName(doctor.getMedicalInstitutionId()));
                doctorResponseList.add(doctorResponse);
            }
            return new Genson().serialize(doctorResponseList);
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
                case Constants.ROLE_DOCTOR:
                    doctorRepository.save((Doctor) user);
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

            if (Objects.equals(user.getRole(), Constants.ROLE_DOCTOR)) {
                Doctor doctor = (Doctor) user;
                doctor.setGender(updateInformationForm.getGender());
                doctor.setDateBirthday(updateInformationForm.getDateBirthday());
                doctor.setDepartment(updateInformationForm.getDepartment());
            }

            switch (user.getRole()) {
                case Constants.ROLE_DOCTOR:
                    doctorRepository.save((Doctor) user);
                    break;
            }
            return "Thành công";
        }
        catch (Exception e) {
            throw e;
        }
    }

    public String getUserInfo(String id) throws Exception {
        List<UserResponse> userResponseList = new ArrayList<>();
        List<Doctor> doctorList = doctorRepository.findAllById(id);
        for (Doctor doctor: doctorList) {
            DoctorResponse userResponse = new DoctorResponse(doctor);
            userResponse.setMedicalInstitutionName(getFullName(userResponse.getMedicalInstitutionId()));
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

    public DoctorResponse getDoctorResponse(String id) {
        Doctor doctor = doctorRepository.findDoctorById(id);
        if (doctor == null) return null;
        DoctorResponse doctorResponse = new DoctorResponse(doctor);
        return doctorResponse;
    }
}

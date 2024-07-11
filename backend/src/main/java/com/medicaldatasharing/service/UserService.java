package com.medicaldatasharing.service;

import com.medicaldatasharing.chaincode.dto.Request;
import com.medicaldatasharing.enumeration.RequestType;
import com.medicaldatasharing.model.User;
import com.medicaldatasharing.repository.AdminRepository;
import com.medicaldatasharing.repository.DoctorRepository;
import com.medicaldatasharing.repository.MedicalInstitutionRepository;
import com.medicaldatasharing.repository.PatientRepository;
import com.medicaldatasharing.response.RequestResponse;
import com.medicaldatasharing.security.service.UserDetailsServiceImpl;
import com.owlike.genson.Genson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
}

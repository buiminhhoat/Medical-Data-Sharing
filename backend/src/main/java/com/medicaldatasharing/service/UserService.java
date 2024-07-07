package com.medicaldatasharing.service;

import com.medicaldatasharing.chaincode.dto.AppointmentRequest;
import com.medicaldatasharing.chaincode.dto.Request;
import com.medicaldatasharing.enumeration.RequestType;
import com.medicaldatasharing.model.User;
import com.medicaldatasharing.repository.AdminRepository;
import com.medicaldatasharing.repository.DoctorRepository;
import com.medicaldatasharing.repository.MedicalInstitutionRepository;
import com.medicaldatasharing.repository.PatientRepository;
import com.medicaldatasharing.response.GetRequestResponse;
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
            List<GetRequestResponse> getRequestResponseList = new ArrayList<>();
            for (Request request: requestList) {
                GetRequestResponse getRequestResponse = new GetRequestResponse(request);
                User sender = userDetailsService.getUserByUserId(request.getSenderId());
                getRequestResponse.setSenderName(sender.getFullName());
                User recipient = userDetailsService.getUserByUserId(request.getRecipientId());
                getRequestResponse.setRecipientName(recipient.getFullName());
                getRequestResponseList.add(getRequestResponse);
            }
            return new Genson().serialize(getRequestResponseList);
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
            if (Objects.equals(requestType, RequestType.EDIT_RECORD.toString())) {
                request = hyperledgerService.getEditRequest(user, requestId);
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

            GetRequestResponse getRequestResponse = new GetRequestResponse(request);
            User sender = userDetailsService.getUserByUserId(request.getSenderId());
            getRequestResponse.setSenderName(sender.getFullName());
            User recipient = userDetailsService.getUserByUserId(request.getRecipientId());
            getRequestResponse.setRecipientName(recipient.getFullName());
            return new Genson().serialize(getRequestResponse);
        }
        catch (Exception e) {
            throw e;
        }
    }
}

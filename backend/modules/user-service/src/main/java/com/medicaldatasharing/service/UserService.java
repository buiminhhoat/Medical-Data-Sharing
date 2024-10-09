package com.medicaldatasharing.service;

import com.medicaldatasharing.chaincode.dto.Drug;
import com.medicaldatasharing.chaincode.dto.Request;
import com.medicaldatasharing.enumeration.RequestType;
import com.medicaldatasharing.form.*;
import com.medicaldatasharing.model.*;
import com.medicaldatasharing.repository.*;
import com.medicaldatasharing.response.*;
import com.medicaldatasharing.security.service.UserDetailsServiceImpl;
import com.medicaldatasharing.util.Constants;
import com.owlike.genson.Genson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.*;

@Service
public class UserService {
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private RestTemplate restTemplate;

    public String getUserFromOtherService(String id) {
        String org = id.substring(0, id.indexOf("-"));
        org = org.toLowerCase(Locale.ROOT);
        String port = "";
        switch (org) {
            case "patient":
                port = "9001";
                break;
            case "doctor":
                port = "9002";
                break;
            case "medicalinstitution":
                org = "medical_institution";
                port = "9003";
                break;
            case "scientist":
                port = "9004";
                break;
            case "research_center":
                port = "9005";
                break;
            case "manufacturer":
                port = "9006";
                break;
            case "drugstore":
                port = "9007";
                break;
            case "admin":
                port = "9008";
                break;
        }
        if (port.equals("")) {
            return "Not found";
        }
        String url = "http://localhost:" + port + "/api/" + org + "/permit-all/get-full-name/" + id;
        return restTemplate.getForObject(url, String.class);
    }

    public String getFullName(String id) throws Exception {
        return getUserFromOtherService(id);
    }

    public String getAllDoctor() {
        String url = "http://localhost:9002/api/doctor/permit-all/get-all-doctor/";
        return restTemplate.postForObject(url, null, String.class);
    }
}

@Configuration
class RestTemplateConfig {
    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }
}
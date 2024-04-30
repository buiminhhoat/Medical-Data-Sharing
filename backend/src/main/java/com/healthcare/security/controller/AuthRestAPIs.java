package com.healthcare.security.controller;

import com.healthcare.chaincode.Config;
import com.healthcare.chaincode.client.RegisterUserHyperledger;
import jakarta.security.auth.message.AuthException;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/auth")
public class AuthRestAPIs {
    @GetMapping(value = "/signUp")
    public String signUp() throws AuthException {
        try {
            String userIdentityId = "1";
            RegisterUserHyperledger.enrollOrgAppUsers("official.buiminhhoat@gmail.com", Config.COMMON_USER_ORG, userIdentityId);
        } catch (Exception e) {
            throw new AuthException("Error while sign up in Hyperledger");
        }
        return "Success";
    }
}


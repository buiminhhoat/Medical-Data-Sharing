package com.medicaldatasharing.chaincode.user;

import lombok.Getter;
import org.hyperledger.fabric.sdk.Enrollment;

import java.security.PrivateKey;

@Getter
public class CAEnrollment implements Enrollment {
    private PrivateKey key;
    private String cert;

    public CAEnrollment(PrivateKey pkey, String signedPem) {
        this.key = pkey;
        this.cert = signedPem;
    }
}

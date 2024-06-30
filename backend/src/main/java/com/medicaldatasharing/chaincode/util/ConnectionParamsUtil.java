package com.medicaldatasharing.chaincode.util;

import com.medicaldatasharing.chaincode.Config;

import java.util.HashMap;
import java.util.Map;

public class ConnectionParamsUtil {

    public static Map<String, String> setOrgConfigParams(String org) throws Exception {
        switch (org) {
            case Config.ORG1:
                return new HashMap<String, String>() {{
                    put("pemFile", Config.CA_ORG1_PEM_FILE);
                    put("caOrgUrl", Config.CA_ORG1_URL);
                    put("caOrgAdminIdentityId", Config.CA_ORG1_ADMIN_IDENTITY_ID);
                    put("orgMsp", Config.ORG1_MSP);
                    put("caAdminUsername", Config.CA_ADMIN_USERNAME);
                    put("caAdminPassword", Config.CA_ADMIN_PASSWORD);
                    put("orgAffiliation", Config.ORG1_AFFILIATION);
                    put("networkConfigPath", Config.ORG1_CONNECTION_PROFILE_PATH);
                }};
            case Config.ORG2:
                return new HashMap<String, String>() {{
                    put("pemFile", Config.CA_ORG2_PEM_FILE);
                    put("caOrgUrl", Config.CA_ORG2_URL);
                    put("caOrgAdminIdentityId", Config.CA_ORG2_ADMIN_IDENTITY_ID);
                    put("orgMsp", Config.ORG2_MSP);
                    put("caAdminUsername", Config.CA_ADMIN_USERNAME);
                    put("caAdminPassword", Config.CA_ADMIN_PASSWORD);
                    put("orgAffiliation", Config.ORG2_AFFILIATION);
                    put("networkConfigPath", Config.ORG2_CONNECTION_PROFILE_PATH);
                }};
            case Config.ORG3:
                return new HashMap<String, String>() {{
                    put("pemFile", Config.CA_ORG3_PEM_FILE);
                    put("caOrgUrl", Config.CA_ORG3_URL);
                    put("caOrgAdminIdentityId", Config.CA_ORG3_ADMIN_IDENTITY_ID);
                    put("orgMsp", Config.ORG3_MSP);
                    put("caAdminUsername", Config.CA_ADMIN_USERNAME);
                    put("caAdminPassword", Config.CA_ADMIN_PASSWORD);
                    put("orgAffiliation", Config.ORG3_AFFILIATION);
                    put("networkConfigPath", Config.ORG3_CONNECTION_PROFILE_PATH);
                }};
            case Config.ORG4:
                return new HashMap<String, String>() {{
                    put("pemFile", Config.CA_ORG4_PEM_FILE);
                    put("caOrgUrl", Config.CA_ORG4_URL);
                    put("caOrgAdminIdentityId", Config.CA_ORG4_ADMIN_IDENTITY_ID);
                    put("orgMsp", Config.ORG4_MSP);
                    put("caAdminUsername", Config.CA_ADMIN_USERNAME);
                    put("caAdminPassword", Config.CA_ADMIN_PASSWORD);
                    put("orgAffiliation", Config.ORG4_AFFILIATION);
                    put("networkConfigPath", Config.ORG4_CONNECTION_PROFILE_PATH);
                }};
            case Config.ORG5:
                return new HashMap<String, String>() {{
                    put("pemFile", Config.CA_ORG5_PEM_FILE);
                    put("caOrgUrl", Config.CA_ORG5_URL);
                    put("caOrgAdminIdentityId", Config.CA_ORG5_ADMIN_IDENTITY_ID);
                    put("orgMsp", Config.ORG5_MSP);
                    put("caAdminUsername", Config.CA_ADMIN_USERNAME);
                    put("caAdminPassword", Config.CA_ADMIN_PASSWORD);
                    put("orgAffiliation", Config.ORG5_AFFILIATION);
                    put("networkConfigPath", Config.ORG5_CONNECTION_PROFILE_PATH);
                }};
            case Config.ORG6:
                return new HashMap<String, String>() {{
                    put("pemFile", Config.CA_ORG6_PEM_FILE);
                    put("caOrgUrl", Config.CA_ORG6_URL);
                    put("caOrgAdminIdentityId", Config.CA_ORG6_ADMIN_IDENTITY_ID);
                    put("orgMsp", Config.ORG6_MSP);
                    put("caAdminUsername", Config.CA_ADMIN_USERNAME);
                    put("caAdminPassword", Config.CA_ADMIN_PASSWORD);
                    put("orgAffiliation", Config.ORG6_AFFILIATION);
                    put("networkConfigPath", Config.ORG6_CONNECTION_PROFILE_PATH);
                }};
            default:
                throw new Exception();
        }
    }
}

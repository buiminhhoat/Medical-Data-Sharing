package com.medicaldatasharing.chaincode;

public class Config {

    public static final int ORG_COUNT = 2;

    public static final String ORG1_MSP = "Org1MSP";
    //    public static final String ORG1_AFFILIATION = "org1.department1";
    public static final String ORG1_AFFILIATION = "";
    public static final String ORG1_ADMIN_AFFILIATION = "admin";
    public static final String ORG1 = "org1";

    public static final String ORG2_MSP = "Org2MSP";
    //    public static final String ORG2_AFFILIATION = "org2.department1";
    public static final String ORG2_AFFILIATION = "";
    public static final String ORG2 = "org2";

    public static final String ORG3_MSP = "Org3MSP";
    //    public static final String ORG3_AFFILIATION = "org3.department1";
    public static final String ORG3_AFFILIATION = "";
    public static final String ORG3 = "org3";

    public static final String ORG4_MSP = "Org4MSP";
    //    public static final String ORG4_AFFILIATION = "org4.department1";
    public static final String ORG4_AFFILIATION = "";
    public static final String ORG4 = "org4";

    public static final String ORG5_MSP = "Org5MSP";
    //    public static final String ORG5_AFFILIATION = "org5.department1";
    public static final String ORG5_AFFILIATION = "";
    public static final String ORG5 = "org5";

    public static final String PATIENT_ORG = "org1";
    public static final String DOCTOR_ORG = "org2";
    public static final String MANUFACTURER_ORG = "org3";

    public static final String CHANNEL_NAME = "healthcare";

    public static final String WALLET_DIRECTORY = "wallet";

    public static final String CA_ADMIN_USERNAME = "admin";
    public static final String CA_ADMIN_PASSWORD = "adminpw";

    public static final String CA_ORG1_ADMIN_IDENTITY_ID = "org1CaAdmin";
    public static final String CA_ORG2_ADMIN_IDENTITY_ID = "org2CaAdmin";
    public static final String CA_ORG3_ADMIN_IDENTITY_ID = "org3CaAdmin";
    public static final String CA_ORG4_ADMIN_IDENTITY_ID = "org4CaAdmin";
    public static final String CA_ORG5_ADMIN_IDENTITY_ID = "org5CaAdmin";

    public static final String CA_ORG1_PEM_FILE = "../fabric-samples/test-network/organizations/peerOrganizations/org1.example.com/ca/ca.org1.example.com-cert.pem";
    public static final String CA_ORG1_URL = "https://localhost:7054";

    public static final String CA_ORG2_PEM_FILE = "../fabric-samples/test-network/organizations/peerOrganizations/org2.example.com/ca/ca.org2.example.com-cert.pem";
    public static final String CA_ORG2_URL = "https://localhost:8054";

    public static final String CA_ORG3_PEM_FILE = "../fabric-samples/test-network/organizations/peerOrganizations/org3.example.com/ca/ca.org3.example.com-cert.pem";
    public static final String CA_ORG3_URL = "https://localhost:11054";

    public static final String CA_ORG4_PEM_FILE = "../fabric-samples/test-network/organizations/peerOrganizations/org4.example.com/ca/ca.org4.example.com-cert.pem";
    public static final String CA_ORG4_URL = "https://localhost:13054";

    public static final String CA_ORG5_PEM_FILE = "../fablo-target/fabric-config/crypto-config/peerOrganizations/org5.healthcare.com/ca/ca.org5.healthcare.com-cert.pem";
    public static final String CA_ORG5_URL = "https://localhost:7120";

    public static final String ORG1_ADMIN_USERNAME = "admin";
    public static final String ORG1_ADMIN_PASSWORD = "adminpw";

    public static final String ORG2_ADMIN_USERNAME = "admin";
    public static final String ORG2_ADMIN_PASSWORD = "adminpw";

    public static final String ORG3_ADMIN_USERNAME = "admin";
    public static final String ORG3_ADMIN_PASSWORD = "adminpw";

    public static final String ORG4_ADMIN_USERNAME = "admin";
    public static final String ORG4_ADMIN_PASSWORD = "adminpw";

    public static final String ORG5_ADMIN_USERNAME = "admin";
    public static final String ORG5_ADMIN_PASSWORD = "adminpw";

    public static final String CHAINCODE_NAME = "chaincode";

    public static final String ORG1_CONNECTION_PROFILE_PATH = "../fabric-samples/test-network/organizations/peerOrganizations/org1.example.com/connection-org1.json";
    public static final String ORG2_CONNECTION_PROFILE_PATH = "../fabric-samples/test-network/organizations/peerOrganizations/org2.example.com/connection-org2.json";
    public static final String ORG3_CONNECTION_PROFILE_PATH = "../fabric-samples/test-network/organizations/peerOrganizations/org3.example.com/connection-org3.json";
    public static final String ORG4_CONNECTION_PROFILE_PATH = "../fabric-samples/test-network/organizations/peerOrganizations/org4.example.com/connection-org4.json";
    public static final String ORG5_CONNECTION_PROFILE_PATH = "../fabric-samples/test-network/organizations/peerOrganizations/org5.example.com/connection-org5.json";

}
package com.healthcare.chaincode;

public class Config {

    public static final int ORG_COUNT = 4;

    public static final String ORG1_MSP = "patientMSP";
    public static final String ORG1_AFFILIATION = "patient.department1";
    public static final String ORG1_ADMIN_AFFILIATION = "admin";
    public static final String ORG1 = "patient";

    public static final String ORG2_MSP = "doctorMSP";
    public static final String ORG2_AFFILIATION = "doctor.department1";
    public static final String ORG2 = "doctor";

    public static final String ORG3_MSP = "pharmacyMSP";
    public static final String ORG3_AFFILIATION = "pharmacy.department1";
    public static final String ORG3 = "pharmacy";

    public static final String ORG4_MSP = "laboratoryMSP";
    public static final String ORG4_AFFILIATION = "laboratory.department1";
    public static final String ORG4 = "laboratory";

    public static final String ORG5_MSP = "insuranceMSP";
    public static final String ORG5_AFFILIATION = "insurance.department1";
    public static final String ORG5 = "insurance";

    public static final String PATIENT_ORG = "patient";

    public static final String CHANNEL_NAME = "healthcare-channel";

    public static final String WALLET_DIRECTORY = "wallet";

    public static final String CA_ADMIN_USERNAME = "admin";
    public static final String CA_ADMIN_PASSWORD = "adminpw";

    public static final String CA_ORG1_ADMIN_IDENTITY_ID = "patientCaAdmin";
    public static final String CA_ORG2_ADMIN_IDENTITY_ID = "doctorCaAdmin";
    public static final String CA_ORG3_ADMIN_IDENTITY_ID = "pharmacyCaAdmin";
    public static final String CA_ORG4_ADMIN_IDENTITY_ID = "laboratoryCaAdmin";
    public static final String CA_ORG5_ADMIN_IDENTITY_ID = "insuranceCaAdmin";

    public static final String CA_ORG1_PEM_FILE = "../fablo-target/fabric-config/crypto-config/peerOrganizations/patient.healthcare.com/ca/ca.patient.healthcare.com-cert.pem";
    public static final String CA_ORG1_URL = "https://localhost:7040";

    public static final String CA_ORG2_PEM_FILE = "../fablo-target/fabric-config/crypto-config/peerOrganizations/doctor.healthcare.com/ca/ca.doctor.healthcare.com-cert.pem";
    public static final String CA_ORG2_URL = "https://localhost:7060";

    public static final String CA_ORG3_PEM_FILE = "../fablo-target/fabric-config/crypto-config/peerOrganizations/pharmacy.healthcare.com/ca/ca.pharmacy.healthcare.com-cert.pem";
    public static final String CA_ORG3_URL = "https://localhost:7080";

    public static final String CA_ORG4_PEM_FILE = "../fablo-target/fabric-config/crypto-config/peerOrganizations/laboratory.healthcare.com/ca/ca.laboratory.healthcare.com-cert.pem";
    public static final String CA_ORG4_URL = "https://localhost:7100";

    public static final String CA_ORG5_PEM_FILE = "../fablo-target/fabric-config/crypto-config/peerOrganizations/insurance.healthcare.com/ca/ca.insurance.healthcare.com-cert.pem";
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

    public static final String CHAINCODE_NAME = "chaincodes";

    public static final String ORG1_CONNECTION_PROFILE_PATH = "../fablo-target/fabric-config/connection-profiles/connection-profile-patient.yaml";
    public static final String ORG2_CONNECTION_PROFILE_PATH = "../fablo-target/fabric-config/connection-profiles/connection-profile-doctor.yaml";
    public static final String ORG3_CONNECTION_PROFILE_PATH = "../fablo-target/fabric-config/connection-profiles/connection-profile-pharmacy.yaml";
    public static final String ORG4_CONNECTION_PROFILE_PATH = "../fablo-target/fabric-config/connection-profiles/connection-profile-laboratory.yaml";
    public static final String ORG5_CONNECTION_PROFILE_PATH = "../fablo-target/fabric-config/connection-profiles/connection-profile-insurance.yaml";

}


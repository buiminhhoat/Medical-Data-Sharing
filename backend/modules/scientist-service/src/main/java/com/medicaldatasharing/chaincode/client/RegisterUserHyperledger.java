package com.medicaldatasharing.chaincode.client;

import com.medicaldatasharing.chaincode.Config;
import com.medicaldatasharing.chaincode.user.CAEnrollment;
import com.medicaldatasharing.chaincode.user.HyperledgerUser;
import com.medicaldatasharing.chaincode.util.ConnectionParamsUtil;
import com.medicaldatasharing.chaincode.util.WalletUtil;
import org.hyperledger.fabric.gateway.Identities;
import org.hyperledger.fabric.gateway.Identity;
import org.hyperledger.fabric.gateway.X509Identity;
import org.hyperledger.fabric.sdk.Enrollment;
import org.hyperledger.fabric.sdk.User;
import org.hyperledger.fabric.sdk.security.CryptoSuite;
import org.hyperledger.fabric.sdk.security.CryptoSuiteFactory;
import org.hyperledger.fabric_ca.sdk.Attribute;
import org.hyperledger.fabric_ca.sdk.HFCAClient;
import org.hyperledger.fabric_ca.sdk.RegistrationRequest;

import java.util.Map;
import java.util.Properties;

public class RegisterUserHyperledger {

    public static Identity enrollOrgAppUsers(String appUserWalletIdentityId, String org, String userIdentityId) throws Exception {
        WalletUtil walletUtil = new WalletUtil();
        X509Identity userIdentity = walletUtil.getIdentity(appUserWalletIdentityId);
        if (userIdentity != null) {
            System.out.printf("An identity for the user: %s already exists in the wallet%n", appUserWalletIdentityId);
            return userIdentity;
        }

        Map<String, String> orgConfigParams = ConnectionParamsUtil.setOrgConfigParams(org);
        String pemFile = orgConfigParams.get("pemFile");
        String caOrgUrl = orgConfigParams.get("caOrgUrl");
        Properties props = new Properties();
        props.put("pemFile", pemFile);
        props.put("allowAllHostNames", "true");
        HFCAClient caClient = HFCAClient.createNewInstance(caOrgUrl, props);
        CryptoSuite cryptoSuite = CryptoSuiteFactory.getDefault().getCryptoSuite();
        caClient.setCryptoSuite(cryptoSuite);

        String caOrgAdminIdentityId = orgConfigParams.get("caOrgAdminIdentityId");
        X509Identity adminIdentity = walletUtil.getIdentity(caOrgAdminIdentityId);
        if (adminIdentity == null) {
            System.out.printf("CaAdminIdentity: %s needs to be enrolled and added to the wallet first%n", caOrgAdminIdentityId);
            adminIdentity = (X509Identity) EnrollAdmin.enrollOrgCaAdmin(org);
        }

        String name = Config.CA_ADMIN_USERNAME;
        String affiliation = orgConfigParams.get("orgAffiliation");
        String mspId = orgConfigParams.get("orgMsp");

        Enrollment adminEnrollment = new CAEnrollment(adminIdentity.getPrivateKey(), Identities.toPemString(adminIdentity.getCertificate()));
        User caAdmin = new HyperledgerUser(name, null, null, affiliation, adminEnrollment, mspId);

        RegistrationRequest registrationRequest = new RegistrationRequest(appUserWalletIdentityId);
//		registrationRequest.setAffiliation(affiliation);
        registrationRequest.setEnrollmentID(appUserWalletIdentityId);
        Attribute attribute = new Attribute("userIdentityId", userIdentityId, true);
        registrationRequest.addAttribute(attribute);
        String enrollmentSecret = caClient.register(registrationRequest, caAdmin);
        Enrollment enrollment = caClient.enroll(appUserWalletIdentityId, enrollmentSecret);
        Identity user = Identities.newX509Identity(mspId, enrollment);
        walletUtil.putIdentity(appUserWalletIdentityId, user);
        System.out.printf("Successfully enrolled user %s and imported it into the wallet%n", appUserWalletIdentityId);

        return user;
    }

}

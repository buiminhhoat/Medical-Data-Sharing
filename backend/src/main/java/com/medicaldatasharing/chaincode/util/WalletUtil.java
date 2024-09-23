package com.medicaldatasharing.chaincode.util;

import com.medicaldatasharing.chaincode.Config;
import org.hyperledger.fabric.gateway.Identity;
import org.hyperledger.fabric.gateway.Wallet;
import org.hyperledger.fabric.gateway.Wallets;
import org.hyperledger.fabric.gateway.X509Identity;

import java.io.IOException;
import java.nio.file.Paths;

public class WalletUtil {

    private Wallet wallet;

    public WalletUtil() {
        try {
            wallet = Wallets.newFileSystemWallet(Paths.get(Config.WALLET_DIRECTORY));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Wallet getWallet() {
        return wallet;
    }

    public X509Identity getIdentity(String userIdentityId) throws IOException {
        return (X509Identity) wallet.get(userIdentityId);
    }

    public void putIdentity(String userIdentityId, Identity identity) throws IOException {
        wallet.put(userIdentityId, identity);
    }
}

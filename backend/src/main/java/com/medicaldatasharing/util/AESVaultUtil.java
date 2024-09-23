package com.medicaldatasharing.util;

import javax.annotation.PostConstruct;
import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import java.util.Base64;
import java.util.Map;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.jopenlibs.vault.Vault;

import io.github.jopenlibs.vault.VaultConfig;

import io.github.jopenlibs.vault.VaultException;

import io.github.jopenlibs.vault.SslConfig;

import io.github.jopenlibs.vault.api.Logical;
import io.github.jopenlibs.vault.response.AuthResponse;

import io.github.jopenlibs.vault.response.LogicalResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class AESVaultUtil {

    // Nạp các giá trị từ application.properties
    @Value("${vault.address}")
    private String vaultAddress;

    @Value("${vault.token}")
    private String vaultToken;

    @Value("${vault.secret-path}")
    private String secretPath;

    private static byte[] aesKey;

    // Khởi tạo Vault và lấy secret key từ HashiCorp Vault
    @PostConstruct
    public void initializeVault() throws Exception {
        try {
            VaultConfig config = new VaultConfig()
                    .address(vaultAddress)         // Địa chỉ Vault
                    .token(vaultToken)             // Token của Vault
                    .engineVersion(1)
                    .build();

            Vault vault = Vault.create(config);

            // Lấy secret key từ Vault (khóa AES lưu dưới dạng hex tại secretPath)
            LogicalResponse response = vault.logical().read(secretPath);
            if (response == null || response.getData() == null) {
                throw new Exception("No data returned from Vault");
            }

            // Truy cập đến cấu trúc nested data để lấy 'value'
            String jsonData = response.getData().get("data");
            if (jsonData == null) {
                throw new Exception("Data section is missing in the response");
            }

            // Sử dụng ObjectMapper để chuyển đổi từ chuỗi JSON sang Map
            ObjectMapper objectMapper = new ObjectMapper();
            Map<String, String> data = objectMapper.readValue(jsonData, Map.class);

            // Lấy giá trị 'value' từ Map
            String aesKeyHex = data.get("value");
            if (aesKeyHex == null) {
                throw new Exception("AES key is not found in the data");
            }

            // Chuyển chuỗi hex sang byte array
            aesKey = hexStringToByteArray(aesKeyHex);
            System.out.println("AES Key Initialized Successfully");
        }
        catch (Exception e) {
            System.out.println(e);
        }
    }

    // Hàm mã hóa dữ liệu bằng AES
    public static String encrypt(String plainText) throws Exception {
        if (aesKey == null) {
            throw new IllegalStateException("Vault chưa được khởi tạo. Hãy gọi initializeVault trước.");
        }

        Cipher cipher = Cipher.getInstance("AES");
        SecretKeySpec secretKeySpec = new SecretKeySpec(aesKey, "AES");
        cipher.init(Cipher.ENCRYPT_MODE, secretKeySpec);
        byte[] encrypted = cipher.doFinal(plainText.getBytes());
        return Base64.getEncoder().encodeToString(encrypted);
    }

    // Hàm giải mã dữ liệu bằng AES
    public static String decrypt(String encryptedText) throws Exception {
        if (aesKey == null) {
            throw new IllegalStateException("Vault chưa được khởi tạo. Hãy gọi initializeVault trước.");
        }

        Cipher cipher = Cipher.getInstance("AES");
        SecretKeySpec secretKeySpec = new SecretKeySpec(aesKey, "AES");
        cipher.init(Cipher.DECRYPT_MODE, secretKeySpec);
        byte[] decrypted = cipher.doFinal(Base64.getDecoder().decode(encryptedText));
        return new String(decrypted);
    }

    // Hàm chuyển đổi từ chuỗi hex thành mảng byte[]
    private static byte[] hexStringToByteArray(String hexString) {
        int len = hexString.length();
        byte[] data = new byte[len / 2];
        for (int i = 0; i < len; i += 2) {
            data[i / 2] = (byte) ((Character.digit(hexString.charAt(i), 16) << 4)
                    + Character.digit(hexString.charAt(i + 1), 16));
        }
        return data;
    }
}

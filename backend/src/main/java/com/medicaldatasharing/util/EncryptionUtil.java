package com.medicaldatasharing.util;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.security.SecureRandom;
import java.util.Base64;

@Component
public class EncryptionUtil {
    @Value("${aes.key}")
    private String aesKey;

    private static SecretKey secretKey;

    // Phương thức này được gọi sau khi @Value inject giá trị của khóa AES từ application.properties
    @PostConstruct
    public void init() {
        // Kiểm tra khóa AES có hợp lệ không (32 ký tự cho AES-256)
        if (aesKey == null || aesKey.length() != 32) {
            throw new IllegalArgumentException("Khóa AES không hợp lệ. Phải có độ dài 32 ký tự cho AES-256.");
        }
        // Tạo đối tượng SecretKeySpec từ khóa AES
        secretKey = new SecretKeySpec(aesKey.getBytes(), "AES");
    }

    // Mã hóa dữ liệu
    public static String encrypt(String data) throws Exception {
        byte[] iv = new byte[16]; // Tạo IV ngẫu nhiên
        new SecureRandom().nextBytes(iv);
        IvParameterSpec ivSpec = new IvParameterSpec(iv);

        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        cipher.init(Cipher.ENCRYPT_MODE, secretKey, ivSpec);
        byte[] encrypted = cipher.doFinal(data.getBytes());

        // Trả về IV + dữ liệu mã hóa (Base64)
        return Base64.getEncoder().encodeToString(iv) + ":" + Base64.getEncoder().encodeToString(encrypted);
    }

    // Giải mã dữ liệu
    public static String decrypt(String encryptedData) throws Exception {
        String[] parts = encryptedData.split(":");
        IvParameterSpec ivSpec = new IvParameterSpec(Base64.getDecoder().decode(parts[0]));

        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        cipher.init(Cipher.DECRYPT_MODE, secretKey, ivSpec);
        byte[] decrypted = cipher.doFinal(Base64.getDecoder().decode(parts[1]));

        return new String(decrypted);
    }
}


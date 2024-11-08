package com.cqupt.medical.hypergraph.util;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.Base64;

/**
 * @Author EthanJ
 * @Date 2024/7/24 22:23
 * @Version 1.0
 *
 * 山东部署新增加密解密工具类
 */
public class EncryptionUtil {
    private static final byte[] SALT = {
            (byte) 0x11, (byte) 0x22, (byte) 0x33, (byte) 0x44,
            (byte) 0x55, (byte) 0x66, (byte) 0x77, (byte) 0x88
    };

    public static SecretKey generateKey(String key) throws NoSuchAlgorithmException, InvalidKeySpecException {
        char[] password = key.toCharArray();
        int iterations = 1000;
        int keyLength = 128;

        SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256");
        PBEKeySpec ks = new PBEKeySpec(password, SALT, iterations, keyLength);
        SecretKey secretKey = keyFactory.generateSecret(ks);

        byte[] encodedKey = secretKey.getEncoded();
        SecretKey aesKey = new SecretKeySpec(encodedKey, "AES");

        return aesKey;
    }

    public static String encrypt(SecretKey key, String data) throws Exception {
        Cipher cipher = Cipher.getInstance("AES");
        cipher.init(Cipher.ENCRYPT_MODE, key);
        byte[] encryptedData = cipher.doFinal(data.getBytes("UTF-8"));
        // 使用 URL 安全的 Base64 编码
        return Base64.getUrlEncoder().withoutPadding().encodeToString(encryptedData);
    }

    public static String decrypt(SecretKey key, String encryptedData) throws Exception {
        // 使用 URL 安全的 Base64 解码
        byte[] decodedData = Base64.getUrlDecoder().decode(encryptedData);
        Cipher cipher = Cipher.getInstance("AES");
        cipher.init(Cipher.DECRYPT_MODE, key);
        byte[] decryptedData = cipher.doFinal(decodedData);
        return new String(decryptedData, "UTF-8");
    }

    public static void main(String[] args) throws Exception {

        // 注意：bcf584701ba01f4b4f5a5a87971bcb38038b94dfca1f982cb80f6123a99c786a是密码加密后的信息
        String admin = "admin:d931c87b5f78df3ad71d93aa1901d383411db5c5e7eb027f9b9ccf80f876fd93";
        String usr = "usr:96171cf8126ba816f022b2924844de32cc983d5e720e703689fbec591cae6bc3";
        SecretKey key = generateKey("tsdsyujbwefy_ywejwbejwb_euwyeyuugdw_u4lppiiisih_diwhywjhewew_sdsdserkhckfsydyg_IAPAIDHLIAUSDS_tftSFASASA_6A6DA9898");
        String adminS = encrypt(key, admin);
        String usrS = encrypt(key, usr);
        System.out.println(adminS);
        System.out.println(usrS);
        System.out.println(decrypt(key, adminS));
        System.out.println(decrypt(key, usrS));
    }

}

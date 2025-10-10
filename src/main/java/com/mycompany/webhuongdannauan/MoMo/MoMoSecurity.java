package com.mycompany.webhuongdannauan.MoMo;


import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;

public class MoMoSecurity {

    public static String hmacSHA256(String data, String key) {
        try {
            Mac mac = Mac.getInstance("HmacSHA256");
            mac.init(new SecretKeySpec(key.getBytes(StandardCharsets.UTF_8), "HmacSHA256"));
            byte[] hash = mac.doFinal(data.getBytes(StandardCharsets.UTF_8));
            StringBuilder hexResult = new StringBuilder();
            for (byte b : hash) {
                String hex = Integer.toHexString(0xff & b);
                if (hex.length() == 1) hexResult.append('0');
                hexResult.append(hex);
            }
            return hexResult.toString();
        } catch (Exception e) {
            throw new RuntimeException("HMAC error", e);
        }
    }

    public static boolean verifySignature(String signature, String data, String secretKey) {
        String calculated = hmacSHA256(data, secretKey);
        return signature.equals(calculated);
    }
}
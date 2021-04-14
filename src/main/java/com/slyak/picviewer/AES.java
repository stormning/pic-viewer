package com.slyak.picviewer;

import lombok.SneakyThrows;
import org.apache.commons.lang3.StringUtils;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.util.Base64;

public class AES {

    private static final String AES_CBC_PADDING = "AES/CBC/NoPadding";

    @SneakyThrows
    public static String encrypt(String data, String key, String iv) {
        return Base64.getEncoder().encodeToString(encrypt2Bytes(data, key, iv));
    }

    @SneakyThrows
    public static byte[] encrypt2Bytes(String data, String key, String iv) {
        Cipher cipher = Cipher.getInstance(AES_CBC_PADDING);
        int blockSize = 16;
        byte[] dataBytes = data.getBytes();
        int plaintextLength = dataBytes.length;
        if (plaintextLength % blockSize != 0) {
            plaintextLength = plaintextLength + (blockSize - (plaintextLength % blockSize));
        }

        byte[] plaintext = new byte[plaintextLength];
        //对内容进行\0填充.
        System.arraycopy(dataBytes, 0, plaintext, 0, dataBytes.length);
        SecretKeySpec keyspec = new SecretKeySpec(key.getBytes(), "AES");
        IvParameterSpec ivspec = new IvParameterSpec(iv.getBytes());

        cipher.init(Cipher.ENCRYPT_MODE, keyspec, ivspec);
        return cipher.doFinal(plaintext);
    }

    @SneakyThrows
    public static String decrypt(String encrypted, String key, String iv) {
        SecretKeySpec keyspec = new SecretKeySpec(key.getBytes(), "AES");
        IvParameterSpec ivspec = new IvParameterSpec(iv.getBytes());
        Cipher cipher = Cipher.getInstance(AES_CBC_PADDING);
        cipher.init(Cipher.DECRYPT_MODE, keyspec, ivspec);
        byte[] original = cipher.doFinal(Base64.getDecoder().decode(encrypted));
        return StringUtils.trim(new String(original));
    }


    @SneakyThrows
    public static byte[] decrypt(byte[] encrypted, String key, String iv) {
        SecretKeySpec keyspec = new SecretKeySpec(key.getBytes(), "AES");
        IvParameterSpec ivspec = new IvParameterSpec(iv.getBytes());
        Cipher cipher = Cipher.getInstance(AES_CBC_PADDING);
        cipher.init(Cipher.DECRYPT_MODE, keyspec, ivspec);
        return cipher.doFinal(encrypted);
    }

    public static void main(String[] args) {
        String key = "1234567887654321";
        System.out.println(StringUtils.trim(AES.decrypt(AES.encrypt("aaaaaaaaaaaaa", key, key), key, key)));
    }

}

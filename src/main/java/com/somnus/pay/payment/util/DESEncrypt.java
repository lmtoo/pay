package com.somnus.pay.payment.util;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;
import java.io.IOException;
import java.security.Key;

/**
 * DES  BASE64加密静态类
 */
public class DESEncrypt {
    /**
     * 将二进制转换为16进制
     *
     * @param buf
     * @return
     */
    private static String ByteToBase64(byte buf[]) {
        BASE64Encoder enc = new BASE64Encoder();
        return enc.encode(buf);
    }

    // 将 BASE64 编码的字符串 s 进行解码
    private static byte[] Base64ToByte(String Base64Str) throws IOException {
        BASE64Decoder dec = new BASE64Decoder();
        return dec.decodeBuffer(Base64Str);
    }


    /**
     * DES 数据加密
     *
     * @param keyBytes  加密秘钥 byte
     * @param textBytes 加密数据 byte
     * @return 返回加密 byte
     * @throws Exception 异常处理
     */
    private static byte[] encrypt(byte[] keyBytes,
                                  byte[] textBytes) throws Exception {
        Key k = toKey(keyBytes);
        Cipher cipher = Cipher.getInstance("DES");
        cipher.init(Cipher.ENCRYPT_MODE, k);
        return cipher.doFinal(textBytes);
    }

    /**
     * DES解密数据
     *
     * @param keyBytes  加密秘钥 byte
     * @param textBytes 加密数据 byte
     * @return 返回解密 byte
     * @throws Exception 异常处理
     */
    private static byte[] decrypt(byte[] keyBytes,
                                  byte[] textBytes) throws Exception {
        Key k = toKey(keyBytes);
        Cipher cipher = Cipher.getInstance("DES");
        cipher.init(Cipher.DECRYPT_MODE, k);
        return cipher.doFinal(textBytes);
    }

    /**
     * 获取秘钥类
     *
     * @param key 秘钥
     * @return 秘钥类
     * @throws Exception
     */
    private static Key toKey(byte[] key) throws Exception {
        DESKeySpec dks = new DESKeySpec(key);
        SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");
        SecretKey secretKey = keyFactory.generateSecret(dks);
        return secretKey;
    }


    /**
     * 加密数据
     *
     * @param strText 待加密数据
     * @param key     加密秘钥
     * @return 返回加密字符串
     * @throws Exception
     */
    public static String encryptBase64String(String strText, String key) throws Exception {
        // 先进行DES 加密
        byte[] byteEncrypt = DESEncrypt.encrypt(
                key.getBytes(), strText.getBytes());
        // 将des的byte base64加密后生成字符串
        String strEncrypt = DESEncrypt.ByteToBase64(byteEncrypt);
        return strEncrypt;
    }


    /**
     * 解密数据
     *
     * @param strText 解密字符串
     * @param key     解密秘钥
     * @return 解密完成的字符串
     * @throws Exception 异常
     */
    public static String decryptBase64String(String strText, String key) throws Exception {
        // 先进行base 解密
        byte[] base64ToByte = Base64ToByte(strText);
        // 解密DES 生成字符串
        String strDecrypt = new String(DESEncrypt.decrypt(
                key.getBytes(), base64ToByte), "utf-8");
        return strDecrypt;
    }
}

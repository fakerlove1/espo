package com.stackfarm.esports.utils;

import com.stackfarm.esports.exception.UnhandledException;
import org.apache.shiro.crypto.hash.SimpleHash;
import org.apache.shiro.util.ByteSource;
import org.springframework.http.HttpStatus;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.*;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;
import java.util.Date;

/**
 * @author croton
 * @create 2021/3/31 21:06
 */
public class EncryptDecryptUtils {
    private static final String RSA_ALGORITHMNAME = "RSA";

    private static final String AES_ALGORITHMNAME = "AES";

    private static final String MD5_ALGORITHMNAME = "MD5";

    private static final String CIPHER_MODE = "AES/ECB/PKCS5Padding";

    private static final int PWD_SIZE = 16;

    private static String PRIVATE_KEY;

    private static String PUBLIC_KEY;

    private static byte[] privateKeyBytes;

    private static byte[] publicKeyBytes;

    static {
        KeyPair keyPair = null;
        try {
            final KeyStore keyStore = createKeys();
            publicKeyBytes = getPublicKey(keyStore);
            privateKeyBytes = getPrivateKey(keyStore);
            PUBLIC_KEY = Base64.getEncoder().encodeToString(publicKeyBytes);
            PRIVATE_KEY = Base64.getEncoder().encodeToString(privateKeyBytes);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
    }

    /**
     * 获取MD5盐值加密密码
     */
    public static String encryptMD5(String password, String salt) {
        ByteSource sal = ByteSource.Util.bytes(salt);
        int hashIterations = 1024;
        SimpleHash hash = new SimpleHash(MD5_ALGORITHMNAME, password, sal, hashIterations);
        return hash.toString();
    }

    /**
     * base64编码
     *
     * @param data 需要编码的数据字节
     * @return 编码过的串
     */
    public static String encodeByte2Base64Str(byte[] data) {
        return Base64.getEncoder().encodeToString(data);
    }

    /**
     * 把ASCILL编码为Base64格式的串
     *
     * @param data 基本的ASCILL文本
     * @return Base64的串
     */
    public static String encodeASCILL2Base64Str(String data) {
        return encodeByte2Base64Str(data.getBytes());
    }

    /**
     * base64解码
     *
     * @param data 由base64编码过的串
     * @return 字节形式
     */
    public static byte[] decodeBase64Str2Byte(String data) {
        return Base64.getDecoder().decode(data);
    }

    /**
     * base64编码
     *
     * @param data Base64格式的串
     * @return ASCILL格式的文本
     */
    public static String decodeBase64Str2ASCILL(String data) {
        return new String(decodeBase64Str2Byte(data));
    }

    private static byte[] pwdHandler(String password) {
        byte[] data = null;
        if (password != null) {
            byte[] bytes = password.getBytes(StandardCharsets.UTF_8);
            if (password.length() < PWD_SIZE) {
                System.arraycopy(bytes, 0, data = new byte[PWD_SIZE], 0, bytes.length);
            } else {
                data = bytes;
            }
        }
        return data;
    }

    public static byte[] encrypt(byte[] clearTextBytes, byte[] pwdBytes) throws UnhandledException {
        try {
            SecretKeySpec keySpec = new SecretKeySpec(pwdBytes, AES_ALGORITHMNAME);
            Cipher cipher = Cipher.getInstance(CIPHER_MODE);
            cipher.init(Cipher.ENCRYPT_MODE, keySpec);
            return cipher.doFinal(clearTextBytes);
        } catch (Exception e) {
            throw new UnhandledException(HttpStatus.INTERNAL_SERVER_ERROR.value(),
                    e.getMessage(),
                    BaseUtils.getRunLocation(Thread.currentThread().getStackTrace()[1]),
                    new Date());
        }
    }

    public static byte[] decrypt(byte[] cipherTextBytes, byte[] pwdBytes) throws UnhandledException {
        try {
            SecretKeySpec keySpec = new SecretKeySpec(pwdBytes, AES_ALGORITHMNAME);
            Cipher cipher = Cipher.getInstance(CIPHER_MODE);
            cipher.init(Cipher.DECRYPT_MODE, keySpec);
            return cipher.doFinal(cipherTextBytes);
        } catch (Exception e) {
            throw new UnhandledException(HttpStatus.INTERNAL_SERVER_ERROR.value(),
                    e.getMessage(),
                    BaseUtils.getRunLocation(Thread.currentThread().getStackTrace()[1]),
                    new Date());
        }
    }

    public static String encryptAESASCILL2Base64Str(String clearText, String password) throws UnhandledException {
        byte[] cipherTextBytes = encrypt(clearText.getBytes(StandardCharsets.UTF_8), pwdHandler(password));
        return encodeByte2Base64Str(cipherTextBytes);
    }

    public static String decryptAESBase64Str2ASCILL(String cipherText, String password) throws UnhandledException {
        try {
            byte[] cipherTextBytes = decodeBase64Str2Byte(cipherText);
            byte[] clearTextBytes;
            clearTextBytes = decrypt(cipherTextBytes, pwdHandler(password));
            return new String(clearTextBytes, StandardCharsets.UTF_8);
        } catch (UnhandledException e) {
            throw new UnhandledException(HttpStatus.INTERNAL_SERVER_ERROR.value(),
                    e.getMessage(),
                    BaseUtils.getRunLocation(Thread.currentThread().getStackTrace()[1]),
                    new Date());
        }
    }

    public static String encryptAESASCILL2Hex(String clearText, String password) throws UnhandledException {
        try {
            byte[] cipherTextBytes = encrypt(clearText.getBytes(StandardCharsets.UTF_8), pwdHandler(password));
            assert cipherTextBytes != null;
            return byte2hex(cipherTextBytes);
        } catch (Exception e) {
            throw new UnhandledException(HttpStatus.INTERNAL_SERVER_ERROR.value(),
                    e.getMessage(),
                    BaseUtils.getRunLocation(Thread.currentThread().getStackTrace()[1]),
                    new Date());
        }
    }

    public static String decryptAESHex2ASCILL(String cipherText, String password) throws UnhandledException {
        try {
            byte[] cipherTextBytes = hex2byte(cipherText);
            byte[] clearTextBytes = decrypt(cipherTextBytes, pwdHandler(password));
            return new String(clearTextBytes, StandardCharsets.UTF_8);
        } catch (Exception e) {
            throw new UnhandledException(HttpStatus.INTERNAL_SERVER_ERROR.value(),
                    e.getMessage(),
                    BaseUtils.getRunLocation(Thread.currentThread().getStackTrace()[1]),
                    new Date());
        }
    }

    public static String byte2hex(byte[] bytes) {
        StringBuilder sb = new StringBuilder(bytes.length * 2);
        String tmp;
        for (byte aByte : bytes) {
            tmp = (Integer.toHexString(aByte & 0XFF));
            if (tmp.length() == 1) {
                sb.append("0");
            }
            sb.append(tmp);
        }
        return sb.toString().toUpperCase();
    }

    private static byte[] hex2byte(String str) {
        if (str == null || str.length() < 2) {
            return new byte[0];
        }
        str = str.toLowerCase();
        int l = str.length() / 2;
        byte[] result = new byte[l];
        for (int i = 0; i < l; ++i) {
            String tmp = str.substring(2 * i, 2 * i + 2);
            result[i] = (byte) (Integer.parseInt(tmp, 16) & 0xFF);
        }
        return result;
    }

    /**
     * AES加密方式有四种
     * 1. Bytes->Bytes
     * 2. Bytes->Base64Str
     * 3. ASCILL->Bytes
     * 4. ASCILL->Base64Str
     */
    private void note1() {
    }

    /**
     * AES对应的解密方式也有四种
     * 1. Bytes->Bytes
     * 2. Bytes->ASCILL
     * 3. Base64Str->Bytes
     * 4. Base64Str->ASCILL
     */
    private void note2() {
    }

    /**
     * 密钥长度，DSA算法的默认密钥长度是1024
     * 密钥长度必须是64的倍数，在512到65536位之间
     */
    private static final int KEY_SIZE = 1024;

    /**
     * 生成密钥对
     */
    public static KeyStore createKeys() throws NoSuchAlgorithmException {
        // KeyPairGenerator用于生成公钥和私钥对。密钥对生成器是使用 getInstance 工厂方法
        KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance(RSA_ALGORITHMNAME);
        keyPairGenerator.initialize(KEY_SIZE);
        KeyPair keyPair = keyPairGenerator.generateKeyPair();
        RSAPrivateKey privateKey = (RSAPrivateKey) keyPair.getPrivate();
        RSAPublicKey publicKey = (RSAPublicKey) keyPair.getPublic();
        return new KeyStore(publicKey, privateKey);
    }

    /**
     * 获取私钥
     */
    private static byte[] getPrivateKey(KeyStore keyStore) {
        return ((RSAPrivateKey) keyStore.privateKey).getEncoded();
    }

    /**
     * 获取公钥
     */
    private static byte[] getPublicKey(KeyStore keyStore) {
        return ((RSAPublicKey) keyStore.publicKey).getEncoded();
    }

    public static byte[] encryptRSAByPublicKey(byte[] data) throws Exception {
        return encryptRSAByPublicKey(data, publicKeyBytes);
    }

    /**
     * 获得String形式的公钥
     *
     * @return String
     */
    public static String getPublicKeyHash() {
        return PUBLIC_KEY;
    }

    /**
     * 获得String形式的私钥
     *
     * @return String
     */
    public static String getPrivateKeyHash() {
        return PRIVATE_KEY;
    }

    /**
     * RSA加密方式，默认密钥情况下有四种，全部一共有8种
     * 1. Bytes->Bytes
     * 2. Bytes->Base64Str
     * 3. ASCILL->Bytes
     * 4. ASCILL->Base64Str
     */
    private void note3() {
    }

    /**
     * RSA公钥加密1
     */
    public static byte[] encryptRSAByPublicKey(byte[] data, byte[] key) throws Exception {
        // 实例化密钥工厂
        KeyFactory keyFactory = KeyFactory.getInstance(RSA_ALGORITHMNAME);
        // 初始化公钥,根据给定的编码密钥创建一个新的 X509EncodedKeySpec。
        X509EncodedKeySpec x509EncodedKeySpec = new X509EncodedKeySpec(key);
        PublicKey publicKey = keyFactory.generatePublic(x509EncodedKeySpec);
        // 数据加密
        Cipher cipher = Cipher.getInstance(keyFactory.getAlgorithm());
        cipher.init(Cipher.ENCRYPT_MODE, publicKey);
        return cipher.doFinal(data);
    }

    /**
     * RSA公钥加密2
     */
    public static String encryptRSAByPublicKeyByte2Base64Str(byte[] data) throws Exception {
        return encodeByte2Base64Str(encryptRSAByPublicKey(data, publicKeyBytes));
    }

    /**
     * RSA公钥加密3
     */
    public static byte[] encryptRSAByPublicKeyASCILL2Byte(String data) throws Exception {
        return encryptRSAByPublicKey(data.getBytes());
    }

    /**
     * RSA公钥加密4
     */
    public static String encryptRSAByPublicKeyASCILL2Base64Str(String data) throws Exception {
        return encodeByte2Base64Str(encryptRSAByPublicKeyASCILL2Byte(data));
    }

    public static byte[] decryptRSAByPrivateKeyByte2Byte(byte[] data) throws UnhandledException {
        return decryptRSAByPrivateKey(data, privateKeyBytes);
    }

    /**
     * RSA解密方法四种
     * 1. Bytes->Bytes
     * 2. Bytes->ASCILL
     * 3. Base64Str->Bytes
     * 4. Base64Str->ASCILL
     */
    private void noye4() {
    }

    /**
     * RSA私钥解密1
     */
    public static byte[] decryptRSAByPrivateKey(byte[] data, byte[] key) throws UnhandledException {
        try {
            // 取得私钥
            PKCS8EncodedKeySpec pkcs8KeySpec = new PKCS8EncodedKeySpec(key);
            KeyFactory keyFactory = KeyFactory.getInstance(RSA_ALGORITHMNAME);
            // 生成私钥
            PrivateKey privateKey = keyFactory.generatePrivate(pkcs8KeySpec);
            // 数据解密
            Cipher cipher = Cipher.getInstance(keyFactory.getAlgorithm());
            cipher.init(Cipher.DECRYPT_MODE, privateKey);
            return cipher.doFinal(data);
        } catch (Exception e) {
            throw new UnhandledException(HttpStatus.BAD_REQUEST.value(),
                    "RSA解密失败: " + e.getMessage(),
                    BaseUtils.getRunLocation(Thread.currentThread().getStackTrace()[1]),
                    new Date());
        }
    }

    /**
     * RSA私钥解2
     */
    public static String decryptRSAByPrivateKeyByte2ASCILL(byte[] data) throws UnhandledException {
        return new String(decryptRSAByPrivateKey(data, privateKeyBytes));
    }

    /**
     * RSA私钥解密3
     */
    public static byte[] decryptRSAByPrivateKeyBase64Str2Byte(String data) throws UnhandledException {
        return decryptRSAByPrivateKeyByte2Byte(decodeBase64Str2Byte(data));
    }

    /**
     * RSA私钥解密4
     */
    public static String decryptRSAByPrivateKeyBase64Str2ASCILL(String data) throws UnhandledException {
        return decryptRSAByPrivateKeyByte2ASCILL(decodeBase64Str2Byte(data));
    }

    /**
     * 公钥解密，一般不用
     */
    private static byte[] decryptByPublicKey(byte[] data, byte[] key) throws Exception {
        // 实例化密钥工厂
        KeyFactory keyFactory = KeyFactory.getInstance(RSA_ALGORITHMNAME);
        // 初始化公钥
        // 密钥材料转换
        X509EncodedKeySpec x509KeySpec = new X509EncodedKeySpec(key);
        // 产生公钥
        PublicKey pubKey = keyFactory.generatePublic(x509KeySpec);
        // 数据解密
        Cipher cipher = Cipher.getInstance(keyFactory.getAlgorithm());
        cipher.init(Cipher.DECRYPT_MODE, pubKey);
        return cipher.doFinal(data);
    }

    /**
     * 私钥加密，一般不用
     */
    private static byte[] encryptByPrivateKey(byte[] data, byte[] key) throws Exception {
        // 取得私钥
        PKCS8EncodedKeySpec pkcs8KeySpec = new PKCS8EncodedKeySpec(key);
        KeyFactory keyFactory = KeyFactory.getInstance(RSA_ALGORITHMNAME);
        // 生成私钥
        PrivateKey privateKey = keyFactory.generatePrivate(pkcs8KeySpec);
        // 数据加密
        Cipher cipher = Cipher.getInstance(keyFactory.getAlgorithm());
        cipher.init(Cipher.ENCRYPT_MODE, privateKey);
        return cipher.doFinal(data);
    }


    private static class KeyStore {

        private Object publicKey;

        private Object privateKey;

        public KeyStore(Object publicKey, Object privateKey) {
            this.publicKey = publicKey;
            this.privateKey = privateKey;
        }
    }
}

package com.blog.cxf.server.security;

/**
 * @description:
 * @author: youcong
 * @time: 2020/10/31 16:55
 */

import com.google.common.collect.Maps;
import org.apache.commons.codec.CharEncoding;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.io.IOUtils;
import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

import javax.crypto.Cipher;
import java.io.*;
import java.security.*;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Enumeration;
import java.util.Map;

/**
 * <p>
 * 数字签名/加密解密工具包
 * </p>
 *
 * @author youcong
 * @version 1.0
 * @date 2020-10-31
 */
@SuppressWarnings("unused")
public class SecretKey {


    public static final String KEY_STORE = "JKS";
    public static final String X509 = "X.509";
    private static final int CACHE_SIZE = 2048;
    private static final int MAX_ENCRYPT_BLOCK = 117;
    private static final int MAX_DECRYPT_BLOCK = 128;

    private static final String SHA1WithRSA = "SHA1WithRSA";
    private static final String RSA = "RSA";
    private static final String ECB = "ECB";

    public static final Map<String, Object> signData = Maps.newConcurrentMap();
    public static final String CRRECEPAY_SIGN_KEYSTORE = "TEST_SIGN_KEYSTORE";
    public static final String CRRECEPAY_SIGN_X509CERTIFICATE = "TEST_SIGN_X509CERTIFICATE";


    public static final String keyPass = "test@2020";
    public static final String storePass = "test@2020";
    public static final String alias = "test";
    public static final String jksFile = "D:/jks/testKey.jks";
    public static final String pubFile = "D:/jks/test.cer";


    public static void initX509Certificate(String cerFilePath) throws Exception {

        InputStream inputStream = null;
        try {
            CertificateFactory certificateFactory = CertificateFactory.getInstance("X.509");
            inputStream = new FileInputStream(cerFilePath);
            X509Certificate x509Certificate = (X509Certificate) certificateFactory.generateCertificate(inputStream);
            signData.put(CRRECEPAY_SIGN_X509CERTIFICATE, x509Certificate);
        } finally {
            IOUtils.closeQuietly(inputStream);
        }
    }

    /**
     * 获取私钥信息
     *
     * @param jksFilePath
     * @param keyAlias
     * @param keyPass
     * @param storePass
     * @return
     * @throws Exception
     */
    public static PrivateKey getPrivateKey(String jksFilePath, String keyAlias, String keyPass, String storePass) throws Exception {

        File jksFile = new File(jksFilePath);
        InputStream in = new FileInputStream(jksFile);
        KeyStore keyStore = KeyStore.getInstance("JKS");
        keyStore.load(in, storePass.toCharArray());
        PrivateKey privateKey = (PrivateKey) keyStore.getKey(keyAlias, keyPass.toCharArray());
        if (in != null) {
            IOUtils.closeQuietly(in);
        }
        return privateKey;
    }

    /**
     * 获取公钥信息
     *
     * @param cerFilePath
     * @return
     * @throws KeyStoreException
     * @throws IOException
     * @throws CertificateException
     * @throws NoSuchAlgorithmException
     */
    public static PublicKey getPublicKey(String cerFilePath) throws KeyStoreException, IOException, CertificateException, NoSuchAlgorithmException {
        PublicKey publicKey = null;
        try {

            X509Certificate x509Certificate = (X509Certificate) signData.get(CRRECEPAY_SIGN_X509CERTIFICATE);
            if (x509Certificate == null) {
                initX509Certificate(cerFilePath);
                x509Certificate = (X509Certificate) signData.get(CRRECEPAY_SIGN_X509CERTIFICATE);
            }
            publicKey = x509Certificate.getPublicKey();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return publicKey;
    }

    /**
     * 加密
     *
     * @param requestStr
     * @return
     * @throws Exception
     */
    public static byte[] encryptContentBytes(String requestStr) throws Exception {

        try {
            PublicKey publicKey = getPublicKey(pubFile);
            String pubKey = Base64.encodeBase64String(publicKey.getEncoded());
            byte[] content = encryptByPublicKey(requestStr.getBytes(), pubKey);
            return content;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 公钥加密
     *
     * @param data
     * @param publicKey
     * @return
     * @throws Exception
     */
    public static byte[] encryptByPublicKey(byte[] data, String publicKey) throws Exception {

        byte[] keyBytes = org.apache.commons.codec.binary.Base64.decodeBase64(publicKey);
        X509EncodedKeySpec x509KeySpec = new X509EncodedKeySpec(keyBytes);
        KeyFactory keyFactory = KeyFactory.getInstance(RSA);
        Key publicK = keyFactory.generatePublic(x509KeySpec);
        // 对数据加密
        Cipher cipher = Cipher.getInstance(keyFactory.getAlgorithm());
        cipher.init(Cipher.ENCRYPT_MODE, publicK);
        int inputLen = data.length;
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        int offSet = 0;
        byte[] cache;
        int i = 0;
        // 对数据分段加密
        while (inputLen - offSet > 0) {
            if (inputLen - offSet > MAX_ENCRYPT_BLOCK) {
                cache = cipher.doFinal(data, offSet, MAX_ENCRYPT_BLOCK);
            } else {
                cache = cipher.doFinal(data, offSet, inputLen - offSet);
            }
            out.write(cache, 0, cache.length);
            i++;
            offSet = i * MAX_ENCRYPT_BLOCK;
        }
        byte[] encryptedData = out.toByteArray();
        out.close();
        return encryptedData;
    }

    /**
     * 私钥解密
     *
     * @param encryptedData
     * @param privateKey
     * @return
     * @throws Exception
     */
    public static byte[] decryptByPrivateKey(byte[] encryptedData, String privateKey) throws Exception {

        byte[] keyBytes = Base64.decodeBase64(privateKey);
        PKCS8EncodedKeySpec pkcs8KeySpec = new PKCS8EncodedKeySpec(keyBytes);
        KeyFactory keyFactory = KeyFactory.getInstance(RSA);
        Key privateK = keyFactory.generatePrivate(pkcs8KeySpec);
        Cipher cipher = Cipher.getInstance(keyFactory.getAlgorithm());
        cipher.init(Cipher.DECRYPT_MODE, privateK);
        int inputLen = encryptedData.length;
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        int offSet = 0;
        byte[] cache;
        int i = 0;
        // 对数据分段解密
        while (inputLen - offSet > 0) {
            if (inputLen - offSet > MAX_DECRYPT_BLOCK) {
                cache = cipher.doFinal(encryptedData, offSet, MAX_DECRYPT_BLOCK);
            } else {
                cache = cipher.doFinal(encryptedData, offSet, inputLen - offSet);
            }
            out.write(cache, 0, cache.length);
            i++;
            offSet = i * MAX_DECRYPT_BLOCK;
        }
        byte[] decryptedData = out.toByteArray();
        out.close();
        return decryptedData;
    }

    /**
     * 解密
     *
     * @param responseDataBytes
     * @return
     * @throws Exception
     */
    public static String decryptContentBytes(byte[] responseDataBytes)
            throws Exception {

        try {
            PrivateKey privateKey = getPrivateKey(jksFile, alias, keyPass, storePass);
            String priKey = Base64.encodeBase64String(privateKey.getEncoded());
            byte[] decryptContentBytes = decryptByPrivateKey(responseDataBytes, priKey);
            return new String(decryptContentBytes, CharEncoding.UTF_8);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 加签
     *
     * @param signData
     * @return
     * @throws Exception
     */
    public static String sign(String signData) throws Exception {

        InputStream in = new FileInputStream(new File(jksFile));
        KeyStore keyStore = KeyStore.getInstance("JKS");
        keyStore.load(in, storePass.toCharArray());

        // 获取jks证书别名
        Enumeration en = keyStore.aliases();
        String pName = null;
        while (en.hasMoreElements()) {
            String n = (String) en.nextElement();
            if (keyStore.isKeyEntry(n)) {
                pName = n;
            }
        }
        PrivateKey key = getPrivateKey(jksFile, pName, keyPass, storePass);
        Signature signature = Signature.getInstance(SHA1WithRSA);
        signature.initSign(key);
        signature.update(signData.getBytes("UTF-8"));
        byte[] signedData = signature.sign();
        String signDate = new BASE64Encoder().encode(signedData);
        signDate = signDate.replaceAll("\r\n", "").replaceAll("\n", "");
        return signDate;
    }

    /**
     * 验签
     *
     * @return
     * @throws Exception
     */
    public static boolean verifySign(String originData, String returnSignData) throws Exception {

        PublicKey publicKey = getPublicKey(pubFile);
        Signature sign3 = Signature.getInstance(SHA1WithRSA);
        sign3.initVerify(publicKey);
        sign3.update(originData.getBytes("UTF-8"));
        boolean isVerifySign = sign3.verify(new BASE64Decoder().decodeBuffer(returnSignData));
        return isVerifySign;
    }

    public static void main(String[] args) throws Exception {

        String originData = "test_webservice_api_2020";
        System.out.println("========> 加密开始");
        byte[] enData = encryptContentBytes(originData);
        System.out.println("加密数据::" + enData);
        String signData = sign(originData);
        System.out.println("========> 加签 signData:" + signData);
        String deData = decryptContentBytes(enData);
        System.out.println("========> 解密 deData:" + deData);
        boolean verifySign = verifySign(originData, signData);
        System.out.println("========> 解密 verifySign:" + verifySign);
    }
}
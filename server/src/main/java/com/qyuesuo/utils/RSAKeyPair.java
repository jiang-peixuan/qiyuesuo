package com.qyuesuo.utils;

import sun.misc.BASE64Decoder;

import javax.crypto.Cipher;
import java.security.*;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Arrays;
import java.util.Base64;

public class RSAKeyPair {

    static String publicKey_str = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQDZMuTjx4sGNUyip8eswKxigTnv3i/Yoq4wK2/7yHg5f+gMAmoYyK1bWUxhOqfqMn583ksvM77hyKamZc+iVJxBgJB1ia4cs7RwlZVrXmF3g78/8ApOaSfUjYxMGQNU9xS2rMuxyyQT9Ms+OTe+TtQFlYUIxWYJ6KovrP+IaqhmJQIDAQAB";
    static  String privateKey_str="clientknows";
//    PublicKey publicKey;
//   public   static PrivateKey privateKey =getPrivateKey(privateKey_str);
   public   static PublicKey publicKey =  getPublicKeyStr2Pub(publicKey_str);



    //公钥加密
    public static byte [] encrypt(byte[] message) throws Exception{
        Cipher cipher =Cipher.getInstance("RSA");
        cipher.init(Cipher.ENCRYPT_MODE,RSAKeyPair.publicKey);
        return cipher.doFinal(message);
    }

    //签名
//    public  static  byte[] sign(byte[]message) throws Exception {
//            Signature signature = Signature.getInstance("SHA1withRSA");
//            signature.initSign(RSAKeyPair.privateKey);
//            signature.update(message);
//            return signature.sign();
//    }

    //对签名进行校验
    public  static boolean verify(byte[] message,byte[] sign)throws  Exception{
        Signature signature = Signature.getInstance("SHA1withRSA");
        signature.initVerify(publicKey);
        signature.update(message);
        return signature.verify(sign);
    }
    

    //将String 类型的PublicKey转出化成 PublicKey类型
    public static PublicKey getPublicKeyStr2Pub(String key)  {
        PublicKey publicKey = null;
        try {
            byte[] keyBytes;
            keyBytes = (new BASE64Decoder()).decodeBuffer(key);
            X509EncodedKeySpec keySpec = new X509EncodedKeySpec(keyBytes);
            KeyFactory keyFactory = KeyFactory.getInstance("RSA");
            publicKey = keyFactory.generatePublic(keySpec);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return publicKey;
    }

    //将String类型的PrivateKey转出化成 PrivateKey类型
    public  PrivateKey getPrivateKey(String key)  {
        byte[] keyBytes;
        PrivateKey privateKey = null;
        try {
            keyBytes = (new BASE64Decoder()).decodeBuffer(key);
            PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(keyBytes);
            KeyFactory keyFactory = KeyFactory.getInstance("RSA");
            privateKey = keyFactory.generatePrivate(keySpec);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return privateKey;
    }




    //生成公私钥
//    public RSAKeyPair() throws NoSuchAlgorithmException {
//        KeyPairGenerator kpgen = KeyPairGenerator.getInstance("RSA");
//        kpgen.initialize(1024);
//        KeyPair keyPair = kpgen.generateKeyPair();
//        this.privateKey = keyPair.getPrivate();
//        this.publicKey = keyPair.getPublic();
//    }

    //从已经保存文件中读取公私钥
//    public RSAKeyPair(byte[] pk, byte[]sk) throws NoSuchAlgorithmException {
//        try {
//            KeyFactory kf = KeyFactory.getInstance("RSA");
//            X509EncodedKeySpec pkSpec = new X509EncodedKeySpec(pk);
//            PKCS8EncodedKeySpec skSpec = new PKCS8EncodedKeySpec(sk);
//            this.privateKey = kf.generatePrivate(pkSpec);
//            this.publicKey= kf.generatePublic(skSpec);
//        } catch (InvalidKeySpecException e) {
//            e.printStackTrace();
//        }
//    }




    
    //将私钥导出为字节
//    public  byte[] getPrivateKey(){
//        return  this.privateKey.getEncoded();
//    }
    //将私钥导出为字节
    public  byte[] getPublickey(){
        return  this.publicKey.getEncoded();
    }

//    public static void main(String[] args)  throws  Exception{
//        String message ="面试笔试经验技巧篇";
//        byte[] message_byte = message.getBytes();
//        RSAKeyPair rsaKeyPair = new RSAKeyPair();
//        System.out.println("rsaKeyPair.privateKey = " + rsaKeyPair.privateKey);
//        System.out.println("rsaKeyPair.publicKey = " + rsaKeyPair.publicKey);
//
//        //加密
//        byte[] encrypt = rsaKeyPair.encrypt(message_byte);
//        System.out.println("encrypted = " + Base64.getEncoder().encodeToString(encrypt) );
//        //解密
//        byte[] decrypt = rsaKeyPair.decrypt(encrypt);
//        System.out.println("decrypted="+new String(decrypt,"utf-8"));
//
//        //保存私钥，公钥
//        byte[] privateKey = rsaKeyPair.getPrivateKey();
//        byte[] publickey = rsaKeyPair.getPublickey();
//        System.out.println("Base64.getEncoder().encodeToString(publickey) = " + Base64.getEncoder().encodeToString(publickey));
//
//        System.out.println("Base64.getEncoder().encodeToString(privateKey) = " + Base64.getEncoder().encodeToString(privateKey));;
//
//
//        System.out.println("..............................................");
//        String mes = "猜猜我是谁?";
//        byte[] sign = sign(mes.getBytes());
//        System.out.println(sign.toString());
//        System.out.println("Arrays.toString(sign) = " + Arrays.toString(sign));
//
//        //验证签名
//        boolean verify = verify(mes.getBytes(), sign);
//        System.out.println("verify = " + verify);
//    }

    
}

package com.chujian.wapp.navigator.utils;

import java.security.Key;
import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;

public class RSAKeyUtil {

  public static RSAPublicKey getPublicKey(String publicKeyStr) throws Exception {
    byte[] bytes = Base64.getDecoder().decode(publicKeyStr);
    X509EncodedKeySpec keySpec = new X509EncodedKeySpec(bytes);
    KeyFactory keyFactory = KeyFactory.getInstance("RSA");
    PublicKey publicKey = keyFactory.generatePublic(keySpec);
    if (!(publicKey instanceof RSAPublicKey)) {
      throw new IllegalArgumentException("publicKey must be an RSA key");
    }
    return (RSAPublicKey) publicKey;
  }

  public static RSAPrivateKey getPrivateKey(String privateKeyStr) throws Exception {
    byte[] bytes = Base64.getDecoder().decode(privateKeyStr);
    PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(bytes);
    KeyFactory keyFactory = KeyFactory.getInstance("RSA");
    PrivateKey privateKey = keyFactory.generatePrivate(keySpec);
    if (!(privateKey instanceof RSAPrivateKey)) {
      throw new IllegalArgumentException("privateKey must be an RSA key");
    }
    return (RSAPrivateKey) privateKey;
  }

  public static String toKeyString(Key key) {
    return key == null ? null : Base64.getEncoder().encodeToString(key.getEncoded());
  }
}

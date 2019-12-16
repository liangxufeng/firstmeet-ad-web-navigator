package com.chujian.wapp.navigator.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.security.Key;
import java.security.KeyStore;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.cert.Certificate;
import java.util.Base64;


public class JKSUtil {

  public static PublicKey getPublicKey(String keyStoreFilePath,
      String password, String alias) throws Exception {
    KeyStore keyStore = KeyStore.getInstance("JKS");
    InputStream inputStream = getInputStream(keyStoreFilePath);

    keyStore.load(inputStream, password.toCharArray());
    Certificate certificate = keyStore.getCertificate(alias);
    PublicKey publicKey = certificate.getPublicKey();
    return publicKey;
  }

  public static PrivateKey getPrivateKey(String keyStoreFilePath,
      String password,
      String alias) throws Exception {
    KeyStore keyStore = KeyStore.getInstance("JKS");
    InputStream inputStream = getInputStream(keyStoreFilePath);

    keyStore.load(inputStream, password.toCharArray());
    PrivateKey privateKey = (PrivateKey) keyStore.getKey(alias, password.toCharArray());
    return privateKey;
  }

  private static InputStream getInputStream(String keyStoreFilePath)
      throws IOException {
    InputStream inputStream = null;
    if (keyStoreFilePath.startsWith("http://") || keyStoreFilePath.startsWith("https://")) {
      URL url = new URL(keyStoreFilePath);
      inputStream = url.openStream();
    } else {
      File file = new File(keyStoreFilePath);
      if (file.exists()) {
        inputStream = new FileInputStream(file);
      } else {
        String[] prefixs = {"classpath:", "./"};
        for (int i = 0; i < prefixs.length; i++) {
          String prefix = prefixs[i];
          if (keyStoreFilePath.startsWith(prefix)) {
            keyStoreFilePath = keyStoreFilePath.substring(prefix.length());
          }
        }
        keyStoreFilePath =
            keyStoreFilePath.startsWith("/") ? keyStoreFilePath : "/" + keyStoreFilePath;
        inputStream = JKSUtil.class.getResourceAsStream(keyStoreFilePath);
      }
    }
    return inputStream;
  }

  public static String toKeyString(Key key) {
    return key == null ? null : Base64.getEncoder().encodeToString(key.getEncoded());
  }

}

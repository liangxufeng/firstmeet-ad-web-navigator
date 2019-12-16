package com.chujian.wapp.navigator.utils;

import org.springframework.util.Base64Utils;
import org.springframework.util.DigestUtils;

public class MD5Utils {

  public static String md5DigestAsBase64(String str) throws Exception {
    byte[] bytes = DigestUtils.md5Digest(str.getBytes("UTF-8"));
    return Base64Utils.encodeToString(bytes);
  }

  public static void main(String[] args) {
    try {
      System.out.print(md5DigestAsBase64("123456"));
    } catch (Exception e) {
      e.printStackTrace();
    }
  }
}

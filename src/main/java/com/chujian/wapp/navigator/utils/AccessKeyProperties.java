package com.chujian.wapp.navigator.utils;

import lombok.Builder;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties("chujian.sso.token.key")
@Data
@Builder
public class AccessKeyProperties {

  private String jksFilepath;

  private String password;

  private String alias;

  private String publicKeyString;

}

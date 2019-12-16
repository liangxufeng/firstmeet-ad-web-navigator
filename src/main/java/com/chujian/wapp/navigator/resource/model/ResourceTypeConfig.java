package com.chujian.wapp.navigator.resource.model;

import java.util.ArrayList;
import java.util.List;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "resource")
public class ResourceTypeConfig {

  public static final String RS_TYPE_SYSTEM = "system";
  public static final String RS_TYPE_BUTTON = "button";
  public static final String RS_TYPE_MENU = "menu";
  public static final String RS_TYPE_LINK = "link";

  private List<TypeInfo> types = new ArrayList<>();

  public List<TypeInfo> getTypes() {
    return types;
  }

  public void setTypes(List<TypeInfo> types) {
    this.types = types;
  }
}

package com.chujian.wapp.navigator.sso.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.io.Serializable;
import java.util.List;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@JsonIgnoreProperties(ignoreUnknown = true)
public class Menu implements Serializable {

  @JsonProperty("id")
  private String id;

  @JsonProperty("parentId")
  private String parentId;

  @JsonProperty("resourceId")
  private String resourceId;

  @JsonProperty("resourceName")
  private String resourceName;

  @JsonProperty("resourceUrl")
  private String resourceUrl;

  @JsonProperty("resourceOrderNum")
  private String resourceOrderNum;

  @JsonProperty("childList")
  private List<Menu> childMenuList;
}

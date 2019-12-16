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
public class AccessResource implements Serializable {

  @JsonProperty("id")
  private String id;

  @JsonProperty("parentId")
  private String parentId;

  @JsonProperty("resourceId")
  private String resourceId;

  @JsonProperty("type")
  private String type;

  @JsonProperty("name")
  private String name;

  @JsonProperty("url")
  private String url;

  @JsonProperty("orderNum")
  private Long orderNum;

  @JsonProperty("childList")
  private List<AccessResource> childList;

}

package com.chujian.wapp.navigator.sso.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.io.Serializable;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@JsonIgnoreProperties(ignoreUnknown = true)
public class AccessDept implements Serializable {

  @JsonProperty("deptId")
  private String deptId;

  @JsonProperty("name")
  private String name;
}

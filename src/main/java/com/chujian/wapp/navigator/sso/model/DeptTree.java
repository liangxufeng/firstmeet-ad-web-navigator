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
public class DeptTree implements Serializable {

  @JsonProperty("id")
  private String id;

  @JsonProperty("parentId")
  private String parentId;

  @JsonProperty("deptId")
  private String deptId;

  @JsonProperty("deptName")
  private String deptName;

  @JsonProperty("deptOrderNum")
  private String deptOrderNum;

  @JsonProperty("childList")
  private List<DeptTree> childDeptList;
}

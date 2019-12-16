package com.chujian.wapp.navigator.user.model;

import java.io.Serializable;
import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class UserDeptDTO implements Serializable {

  private String deptId;

  private String deptName;

  private String deptParentId;

  private Boolean checked;
}

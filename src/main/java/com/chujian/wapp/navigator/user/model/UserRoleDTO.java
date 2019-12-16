package com.chujian.wapp.navigator.user.model;

import java.io.Serializable;
import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class UserRoleDTO implements Serializable {

  private String roleId;

  private String roleName;

  private String roleRemark;
}

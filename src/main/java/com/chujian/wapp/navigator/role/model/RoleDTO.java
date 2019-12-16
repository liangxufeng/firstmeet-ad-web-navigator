package com.chujian.wapp.navigator.role.model;


import java.io.Serializable;
import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class RoleDTO implements Serializable {

  private Long id;
  private String roleId;
  private String roleName;
  private String roleRemark;
  private String createdAt;
  private String updatedAt;
}

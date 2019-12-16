package com.chujian.wapp.navigator.user.model;

import java.io.Serializable;
import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class UserDTO implements Serializable {

  private Long id;
  private String userId;
  private String userName;
  private String userDept;
  private String userDeptId;
  private String userDeptName;
  private String userPassword;
  private String createdAt;
  private String updatedAt;
}

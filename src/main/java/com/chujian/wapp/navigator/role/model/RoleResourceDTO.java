package com.chujian.wapp.navigator.role.model;

import java.io.Serializable;
import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class RoleResourceDTO implements Serializable {

  private Long id;
  private String resourceId;
  private String resourceName;
  private Long parentId;
  private String iconSkin;
  private Boolean checked;
}

package com.chujian.wapp.navigator.role.model;

import java.io.Serializable;
import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class RoleGameDTO implements Serializable {

  private String gameId;
  private String gameName;
}

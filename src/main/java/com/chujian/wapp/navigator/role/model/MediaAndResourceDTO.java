package com.chujian.wapp.navigator.role.model;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class MediaAndResourceDTO {

  private int id;
  private String name;
  private int pid;
  private Boolean checked;

}

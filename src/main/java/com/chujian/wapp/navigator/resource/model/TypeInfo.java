package com.chujian.wapp.navigator.resource.model;

import java.io.Serializable;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class TypeInfo implements Serializable {

  private String type;

  private String name;
}

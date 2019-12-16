package com.chujian.wapp.navigator.resource.model;

import java.io.Serializable;
import java.util.Date;
import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class ResourceDTO implements Serializable {

  private Long id;

  private Long parentId;

  private String resourceId;

  private String resourceName;

  private String resourceType;

  private String resourceUrl;

  private Long resourceOrderNum;

  private String resourceParentName;

  private String iconSkin;

  private Date createdAt;

  private Date updatedAt;

}

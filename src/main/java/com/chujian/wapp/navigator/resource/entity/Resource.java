package com.chujian.wapp.navigator.resource.entity;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import lombok.Builder;
import lombok.Data;

@Builder
@Data
@Entity
@Table(name = "sys_resource")
public class Resource implements Serializable {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "id")
  private Long id;

  @Column(name = "resource_id")
  private String resourceId;

  @Column(name = "resource_name")
  private String resourceName;

  @Column(name = "resource_type")
  private String resourceType;

  @Column(name = "resource_url")
  private String resourceUrl;

  @Column(name = "resource_order_num")
  private Long resourceOrderNum;

  @Column(name = "parent_id")
  private Long parentId;

  @Column(name = "created_at", insertable = false, updatable = false)
  private Date createdAt;

  @Column(name = "updated_at", insertable = false, updatable = false)
  private Date updatedAt;


}

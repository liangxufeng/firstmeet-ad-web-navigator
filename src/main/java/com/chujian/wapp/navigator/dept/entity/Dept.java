package com.chujian.wapp.navigator.dept.entity;

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
@Table(name = "sys_dept")
public class Dept implements Serializable {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "id")
  private Long id;

  @Column(name = "dept_id")
  private String deptId;

  @Column(name = "dept_name")
  private String deptName;

  @Column(name = "dept_order_num")
  private int deptOrderNum;

  @Column(name = "dept_parent_id")
  private String deptParentId;

  @Column(name = "created_at", insertable = false, updatable = false)
  private Date createdAt;

  @Column(name = "updated_at", insertable = false, updatable = false)
  private Date updatedAt;
}

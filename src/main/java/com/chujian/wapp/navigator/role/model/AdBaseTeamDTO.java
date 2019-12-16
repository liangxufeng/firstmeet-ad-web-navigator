package com.chujian.wapp.navigator.role.model;

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
@Table(name = "ad_base_team")
public class AdBaseTeamDTO implements Serializable {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "id")
  private String id;

  @Column(name = "tid")
  private String tid;

  @Column(name = "name")
  private String name;

  @Column(name = "pid")
  private String pid;

  @Column(name = "status")
  private Integer status;

  @Column(name = "insdt", insertable = false, updatable = false)
  private Date insdt;

  @Column(name = "upddt", insertable = false, updatable = false)
  private Date upddt;

  @Column(name = "opuser")
  private String opuser;

}

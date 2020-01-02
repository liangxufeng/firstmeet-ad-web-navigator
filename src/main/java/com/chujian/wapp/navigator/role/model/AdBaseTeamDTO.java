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

  @Column(name = "tcode")
  private String tcode;

  @Column(name = "name")
  private String name;

  @Column(name = "pid")
  private String pid;

  @Column(name = "status")
  private Integer status;

  @Column(name = "ctime")
  private Date ctime;

  @Column(name = "utime")
  private Date utime;

  @Column(name = "updator")
  private String updator;

  @Column(name = "updator_id")
  private String updatorId;

  @Column(name = "creator")
  private String creator;

  @Column(name = "creator_id")
  private String creatorId;

}

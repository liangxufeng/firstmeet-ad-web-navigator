package com.chujian.wapp.navigator.role.entity;

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
@Table(name = "ad_base_product")
public class AdBaseProduct implements Serializable {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "id")
  private Long id;

  @Column(name = "code")
  private String code;

  @Column(name = "gcode")
  private String gcode;

  @Column(name = "name")
  private String name;

  @Column(name = "intro")
  private String intro;

  @Column(name = "game_id")
  private String gameId;

  @Column(name = "iadurl")
  private String iadurl;

  @Column(name = "gaurl")
  private String gaurl;

  @Column(name = "appurl")
  private String appurl;

  @Column(name = "status")
  private Integer status;

//  @Column(name = "insdt", insertable = false, updatable = false)
//  private Date insdt;
  @Column(name = "ctime", insertable = false, updatable = false)
  private Date ctime;

//  @Column(name = "upddt", insertable = false, updatable = false)
//  private Date upddt;
  @Column(name = "utime", insertable = false, updatable = false)
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

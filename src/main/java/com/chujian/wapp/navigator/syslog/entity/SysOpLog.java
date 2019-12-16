package com.chujian.wapp.navigator.syslog.entity;

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
import org.springframework.data.domain.Persistable;

@Builder
@Data
@Entity
@Table(name = "sys_op_log")
public class SysOpLog implements Serializable, Persistable<Long> {

  public static final String OP_RESULT_OK = "OK";
  public static final String OP_RESULT_FAIL = "FAIL";

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "id")
  private Long id;

  @Column(name = "op_user")
  private String opUser;

  @Column(name = "op_module")
  private String opModule;

  @Column(name = "op_type")
  private String opType;

  @Column(name = "op_ip")
  private String opIp;

  @Column(name = "op_result")
  private String opResult;

  @Column(name = "op_desc")
  private String opDesc;

  @Column(name = "created_at", insertable = false, updatable = false)
  private Date createdAt;

  @Column(name = "updated_at", insertable = false, updatable = false)
  private Date updatedAt;

  @Override
  public boolean isNew() {
    return true;
  }
}

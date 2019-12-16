package com.chujian.wapp.navigator.role.model;

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
@Table(name = "ad_base_media")
public class MediaDto {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private String id;
    private String type;
    private String code;
    private String name;
    private String intro;
    private Integer status;
    // 操作用户
    private String opuser;
    @Column(name = "opuser_id")
    private String opuserId;
    @Column(name = "is_del")
    private Integer isDel;
    // 创建用户
    private String creator;
    @Column(name = "creator_id")
    private String creatorId;
    // 创建时间
    private Date ctime;
    // 更新时间
    private Date utime;

}

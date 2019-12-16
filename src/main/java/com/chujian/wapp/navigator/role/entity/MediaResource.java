package com.chujian.wapp.navigator.role.entity;

import java.util.Date;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import lombok.Builder;
import lombok.Data;

@Builder
@Data
@Entity
@Table(name = "ad_base_resource")
public class MediaResource {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    // 资源名称
    @Column(name = "resource_name")
    private String resourceName;

//    @Column(name = "media_id")
//    private Integer mediaId;

    @ManyToOne(cascade = {CascadeType.MERGE, CascadeType.REFRESH})
    @JoinColumn(name = "media_id")
    private Media media;

    @Column(name = "resource_type")
    private String resourceType;

    private String intro;

    private Integer status;

    @Column(name = "is_del")
    private Integer isDel;

    private String creator;

    @Column(name = "creator_id")
    private String creatorId;

    private String updator;
    @Column(name = "updator_id")
    private String updatorId;
    private Date ctime;
    private Date utime;
}

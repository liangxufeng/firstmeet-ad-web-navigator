package com.chujian.wapp.navigator.game.entity;

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
@Table(name = "ad_base_game")
public class Game {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;
    private String code;
    private String name;
    private String icon;
    private String intro;
    @Column(name = "is_del")
    private Integer isDel;
    // 操作用户
    private String updator;
    @Column(name = "updator_id")
    private String updatorId;
    // 创建用户
    private String creator;
    @Column(name = "creator_id")
    private String creatorId;
    // 创建时间
    private Date ctime;
    // 更新时间
    private Date utime;

}

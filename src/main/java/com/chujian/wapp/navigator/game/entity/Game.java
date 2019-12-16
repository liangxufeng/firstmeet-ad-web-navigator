package com.chujian.wapp.navigator.game.entity;


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
@Table(name = "sys_game")
public class Game implements Serializable, Comparable {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "id")
  private Long id;

  @Column(name = "game_id")
  private String gameId;

  @Column(name = "game_name")
  private String gameName;

  @Column(name = "created_at", insertable = false, updatable = false)
  private Date createdAt;

  @Column(name = "updated_at", insertable = false, updatable = false)
  private Date updatedAt;

  @Override
  public int compareTo(Object o) {
    if (o instanceof Game) {
      Game game = (Game) o;
      return this.gameId.compareTo(game.getGameId());//换姓名升序排序
    }
    return 0;
  }
}

package com.chujian.wapp.navigator.game.model;


import java.io.Serializable;
import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class GameDTO implements Serializable {

  private Integer id;
  private String gameId;
  private String gameName;
  private String createdAt;
  private String updatedAt;
}

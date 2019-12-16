package com.chujian.wapp.navigator.game.service;

import com.alibaba.fastjson.JSONObject;
import com.chujian.wapp.navigator.game.entity.Game;
import com.chujian.wapp.navigator.game.model.GameDTO;
import com.chujian.wapp.navigator.game.respository.GameRepository;
import com.chujian.wapp.navigator.role.respository.RoleGameRepository;
import com.chujian.wapp.navigator.utils.DateUtils;
import java.util.ArrayList;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class GameService {

  @Autowired
  private GameRepository gameRepository;

  @Autowired
  private RoleGameRepository roleGameRepository;

  /**
   * 游戏分页
   *
   * @param page
   * @param pageSize
   * @param gameName
   * @return
   */
  public JSONObject findGameList(int page, int pageSize, String gameName) {
    JSONObject resultObj = new JSONObject();

    Pageable pageable = PageRequest.of(page, pageSize, Direction.DESC, "createdAt");
    Page<Game> pageResult = null;
    if (StringUtils.isBlank(gameName)) {
      pageResult = gameRepository.findAll(pageable);
    } else {
      pageResult = gameRepository.findByGameNameLike("%" + gameName + "%", pageable);
    }
    long totalElements = pageResult.getTotalElements();
    int totalPages = pageResult.getTotalPages();
    int number = pageResult.getNumber();
    List<GameDTO> gameList = new ArrayList<>();
    for (Game game : pageResult) {
      String newCreatedAt = convertDate(game.getCreatedAt().toString());
      String newUpdatedAt = convertDate(game.getUpdatedAt().toString());
      gameList.add(
          GameDTO.builder().id(game.getId()).gameId(game.getGameId()).gameName(game.getGameName())
              .createdAt(newCreatedAt).updatedAt(newUpdatedAt).build());
    }
    resultObj.put("total_pages", totalPages);
    resultObj.put("current_page", number);
    resultObj.put("page_size", pageSize);
    resultObj.put("total_elements", totalElements);
    resultObj.put("game", gameList);
    return resultObj;
  }


  /**
   * 保存
   *
   * @param gameName
   */
  public void save(String gameId, String gameName) throws Exception {
    Game game = Game.builder().gameId(gameId).gameName(gameName).build();
    gameRepository.save(game);
  }

  /**
   * 根据gameid查询角色
   *
   * @param gameId
   * @return
   */
  public Game findByGameId(String gameId) {
    return gameRepository.findByGameId(gameId);
  }


  /**
   * 修改角色
   *
   * @param gameId
   * @param gameName
   * @throws Exception
   */
  public void updata(String gameId, String gameName) throws Exception {
    Game game = gameRepository.findByGameId(gameId);
    game.setGameName(gameName);
    game.setCreatedAt(game.getCreatedAt());
    gameRepository.saveAndFlush(game);
  }

  /**
   * 根据gameId查询角色
   *
   * @param gameId
   * @return
   */
  public Game findGameByGameId(String gameId) {
    return gameRepository.findByGameId(gameId);
  }

  /**
   * 删除角色
   *
   * @param gameIds
   */
  public void deleteGame(String[] gameIds) {
    for (int i = 0; i < gameIds.length; i++) {
      //删除游戏的角色信息
      if (roleGameRepository.findByGameId(gameIds[i]) != null) {
        roleGameRepository.deleteByGameId(gameIds[i]);
      }
      //删除游戏的信息
      gameRepository.deleteByGameId(gameIds[i]);
    }
  }

  /**
   * 时间转换方法
   *
   * @param dateStr
   * @return
   */
  private String convertDate(String dateStr) {
    String originalDateTimeFormat = "yyyy-MM-dd'T'HH:mm:ss.SSSZ";
    String convertedDateTimeFormat = "yyyy-MM-dd HH:mm:ss";
    return DateUtils.convertDate(dateStr, originalDateTimeFormat, convertedDateTimeFormat);
  }

  public List<Game> findAll() {
    return gameRepository.findAll();
  }
}
package com.chujian.wapp.navigator.game.service;

import com.alibaba.fastjson.JSONObject;
import com.chujian.wapp.navigator.game.entity.Game;
import com.chujian.wapp.navigator.game.entity.GamePageDTO;
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

    Pageable pageable = PageRequest.of(page, pageSize, Direction.DESC, "utime");
    Page<Game> pageResult = null;
    if (StringUtils.isBlank(gameName)) {
      pageResult = gameRepository.findAll(pageable);
    } else {
      pageResult = gameRepository.findByNameLike("%" + gameName + "%", pageable);
    }
    long totalElements = pageResult.getTotalElements();
    int totalPages = pageResult.getTotalPages();
    int number = pageResult.getNumber();
    List<Game> gameList = pageResult.getContent();
    if (gameList == null || gameList.isEmpty()) {
      resultObj.put("game", gameList);
    } else {
      List<GamePageDTO> gameDtoList = new ArrayList<>();
      for (Game game : gameList) {

        String newCreatedAt = convertDate(game.getCtime().toString());
        String newUpdatedAt = convertDate(game.getUtime().toString());
        GamePageDTO gamePageDTO = null;
        if (game.getIsDel()==1){
           gamePageDTO = GamePageDTO.builder().id(game.getId()).name(game.getName()).code(game.getCode())
              .icon(game.getIcon()).intro(game.getIntro()).isDel("启用")
              .ctime(newCreatedAt).utime(newUpdatedAt).build();
        }else {
           gamePageDTO = GamePageDTO.builder().id(game.getId()).name(game.getName()).code(game.getCode())
              .icon(game.getIcon()).intro(game.getIntro()).isDel("禁用")
              .ctime(newCreatedAt).utime(newUpdatedAt).build();
        }
        gameDtoList.add(gamePageDTO);
      }
      resultObj.put("game", gameDtoList);
    }

    resultObj.put("total_pages", totalPages);
    resultObj.put("current_page", number);
    resultObj.put("page_size", pageSize);
    resultObj.put("total_elements", totalElements);

    return resultObj;
  }


  /**
   * 保存
   *
   * @param gameName
   */
  public void save(String gameId, String gameName) throws Exception {
    Game game = Game.builder().id(Integer.valueOf(gameId)).name(gameName).build();
    gameRepository.save(game);
  }

  /**
   * 根据gameid查询角色
   *
   * @param gameId
   * @return
   */
  public Game findByGameId(String gameId) {
    return gameRepository.findById(Integer.parseInt(gameId));
  }


  /**
   * 修改角色
   *
   * @param gameId
   * @param gameName
   * @throws Exception
   */
  public void updata(String gameId, String gameName) throws Exception {
    Game game = gameRepository.findById(Integer.parseInt(gameId));
    game.setName(gameName);
    game.setCreator(game.getCreator());
    gameRepository.saveAndFlush(game);
  }

  /**
   * 根据gameId查询角色
   *
   * @param gameId
   * @return
   */
  public Game findGameByGameId(String gameId) {
    return gameRepository.findById(Integer.parseInt(gameId));
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
      gameRepository.deleteById(Integer.parseInt(gameIds[i]));
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
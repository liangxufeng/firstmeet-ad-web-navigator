package com.chujian.wapp.navigator.game.controller;

import com.alibaba.fastjson.JSONObject;
import com.chujian.wapp.navigator.game.entity.Game;
import com.chujian.wapp.navigator.game.service.GameService;
import com.chujian.wapp.navigator.syslog.entity.SysOpLog;
import com.chujian.wapp.navigator.syslog.service.SysOpLogService;
import javax.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
public class GameRestController {

  @Autowired
  private GameService gameService;

  @Autowired
  private SysOpLogService opLogService;

  /**
   * 游戏分页展示
   */
  @GetMapping(value = "/game", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
  public ResponseEntity gamePage(
      @RequestParam(value = "game_name", required = false) String gameName,
      @RequestParam("page") int page,
      @RequestParam("page_size") int pageSize) {
    JSONObject jsonObject = new JSONObject();
    try {
      jsonObject = gameService.findGameList((page - 1), pageSize, gameName);
    } catch (Exception e) {
      jsonObject.put("status", "fail");
      log.error("get gamePage Failed:", e);
    }
    return ResponseEntity.ok(jsonObject);
  }

  /**
   * 增加或者修改游戏
   */
  @PostMapping(value = "save_game", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
  public ResponseEntity saveGame(@RequestParam("game_id") String gameId,
      @RequestParam("id") Long id,
      @RequestParam("game_name") String gameName,
      HttpServletRequest request) {
    JSONObject jsonObject = new JSONObject();
    try {
      if (id == null) {
        //根据gameId有无值判断执行新增与否
        Game gamebyGameId = gameService.findByGameId(gameId);
        if (gamebyGameId != null) {
          jsonObject.put("status", "addIdFail");
        } else {//执行添加
          gameService.save(gameId, gameName);
          opLogService.saveOpLog(request, "game", "add", "game add", SysOpLog.OP_RESULT_OK);
          jsonObject.put("status", "ok");
        }
      } else {//执行修改
        gameService.updata(gameId, gameName);
        opLogService.saveOpLog(request, "game", "edit", "game edit", SysOpLog.OP_RESULT_OK);
        jsonObject.put("status", "ok");
      }
    } catch (Exception e) {
      log.error("Save game failed: ", e);
      jsonObject.put("status", "fail");
    }
    return ResponseEntity.ok(jsonObject);
  }

  /**
   * 修改游戏时回显
   */
  @GetMapping(value = "/edit_game", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
  public ResponseEntity editGame(@RequestParam(value = "game_id", required = false) String gameId) {
    Game game = null;
    try {
      game = gameService.findGameByGameId(gameId);
    } catch (Exception ex) {
      log.error("Edit game failed:", ex);
    }
    return ResponseEntity.ok(game);
  }

  /**
   * 删除游戏
   */
  @PostMapping(value = "/delete_game", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
  public ResponseEntity deleteGame(
      @RequestParam(value = "game_ids[]", required = false) String[] gameIds,
      HttpServletRequest request) {
    JSONObject jsonObject = new JSONObject();
    try {
      gameService.deleteGame(gameIds);
      opLogService.saveOpLog(request, "game", "del", "game del", SysOpLog.OP_RESULT_OK);
      jsonObject.put("status", "ok");
    } catch (Exception e) {
      jsonObject.put("status", "fail");
      log.error("Delete game failed:", e);
    }
    return ResponseEntity.ok(jsonObject);
  }

}

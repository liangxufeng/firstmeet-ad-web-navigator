package com.chujian.wapp.navigator.game.controller;

import com.chujian.wapp.navigator.sso.model.ACL;
import com.chujian.wapp.navigator.common.acl.AclResource;
import javax.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@Slf4j
public class GameWebController {

  @AclResource(resourceId = ACL.MENU_GAME)
  @RequestMapping(value = "/game_manage")
  public String systeManage(HttpServletRequest request) {
    return "game/game_query";
  }

}

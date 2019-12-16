package com.chujian.wapp.navigator.sso.model;


import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "resources")
@Data
public class ACL {

  //系统
  public static final String SYS_SYS = "sys";

  //菜单
  public static final String MENU_USER = "sys_user_view";
  public static final String MENU_ROLE = "sys_role_view";
  public static final String MENU_RESOURCE = "sys_resource_view";
  public static final String MENU_GAME = "sys_game_view";
  public static final String MENU_DEPT = "sys_dept_view";

  //按钮
  public static final String RS_USER_ADD= "user_add";
  public static final String RS_USER_EDIT = "user_edit";
  public static final String RS_USER_DEL = "user_del";
  public static final String RS_USER_ROLE = "user_role";

  public static final String RS_ROLE_ADD = "role_add";
  public static final String RS_ROLE_EDIT = "role_edit";
  public static final String RS_ROLE_DEL = "role_del";
  public static final String RS_ROLE_GAME = "role_game";
  public static final String RS_ROLE_RESOURCE = "role_resource";

  public static final String RS_GAME_ADD = "game_add";
  public static final String RS_GAME_EDIT = "game_edit";
  public static final String RS_GAME_DEL = "game_del";

  public static final String RS_RESOURCE_COPY = "resource_copy";
  public static final String RS_RESOURCE_ADD = "resource_add";
  public static final String RS_RESOURCE_EDIT = "resource_edit";

  public static final String RS_DEPT_EDIT = "dept_edit";
  public static final String RS_DEPT_ADD = "dept_add";


}

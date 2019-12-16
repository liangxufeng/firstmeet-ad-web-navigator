package com.chujian.wapp.navigator.role.contorller;

import com.alibaba.fastjson.JSONObject;
import com.chujian.wapp.navigator.game.service.GameService;
import com.chujian.wapp.navigator.role.entity.Role;
import com.chujian.wapp.navigator.role.model.MediaAndResourceDTO;
import com.chujian.wapp.navigator.role.model.RoleResourceDTO;
import com.chujian.wapp.navigator.role.service.RoleService;
import com.chujian.wapp.navigator.syslog.entity.SysOpLog;
import com.chujian.wapp.navigator.syslog.service.SysOpLogService;
import java.util.List;
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
public class RoleRestController {

  @Autowired
  private RoleService roleService;

  @Autowired
  private GameService gameService;

  @Autowired
  private SysOpLogService opLogService;

  /**
   * 角色分页
   */
  @GetMapping(value = "/role", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
  public ResponseEntity rolePage(
      @RequestParam(value = "role_name", required = false) String roleName,
      @RequestParam("page") int page,
      @RequestParam("page_size") int pageSize) {
    JSONObject jsonObject = new JSONObject();
    try {
      jsonObject = roleService.findRoleList((page - 1), pageSize, roleName);
    } catch (Exception e) {
      jsonObject.put("status", "fail");
      log.error("Get rolePage failed:" + e);
    }
    return ResponseEntity.ok(jsonObject);
  }

  /**
   * 增加或者修改角色
   */
  @PostMapping(value = "save_role", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
  public ResponseEntity saveRole(@RequestParam("role_id") String roleId,
      @RequestParam("role_name") String roleName,
      @RequestParam("role_remark") String roleRemark,
      HttpServletRequest request) {
    JSONObject jsonObject = new JSONObject();
    try {
      //根据roleId有无值判断执行新增还是修改操作
      Role rolebyRoleId = roleService.findByRoleId(roleId);
      if (rolebyRoleId == null) {//执行添加
        roleService.save(roleName, roleRemark);
        opLogService.saveOpLog(request, "role", "add", "role add", SysOpLog.OP_RESULT_OK);
        jsonObject.put("status", "ok");
      } else {//执行修改
        Role role = roleService.findByRoleId(roleId);
        roleService.updata(role.getRoleId(), roleName, roleRemark);
        opLogService.saveOpLog(request, "role", "edit", "role edit", SysOpLog.OP_RESULT_OK);
        jsonObject.put("status", "ok");
      }
    } catch (Exception e) {
      jsonObject.put("status", "fail");
      log.error("Save role failed: " + e);
    }
    return ResponseEntity.ok(jsonObject);
  }

  /**
   * 修改角色时回显
   */
  @GetMapping(value = "/edit_role", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
  public ResponseEntity editRole(@RequestParam(value = "role_id", required = false) String roleId) {
    Role role = null;
    try {
      role = roleService.findRoleByRoleId(roleId);
    } catch (Exception ex) {
      log.error("Edit role failed:", ex);
    }
    return ResponseEntity.ok(role);
  }

  /**
   * 删除角色
   */
  @PostMapping(value = "/delete_role", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
  public ResponseEntity deleteRole(
      @RequestParam(value = "role_ids[]", required = false) String[] roleIds,
      HttpServletRequest request) {
    JSONObject jsonObject = new JSONObject();
    try {
      for (int i = 0; i < roleIds.length; i++) {
        if ("admin".equals(roleIds[i])) {
          jsonObject.put("status", "no_delete");
          return ResponseEntity.ok(jsonObject);
        }
      }
      roleService.deleteRole(roleIds);
      opLogService.saveOpLog(request, "role", "del", "role del", SysOpLog.OP_RESULT_OK);
      jsonObject.put("status", "ok");
    } catch (Exception e) {
      jsonObject.put("status", "fail");
      log.error("Delete role failed:", e);
    }
    return ResponseEntity.ok(jsonObject);
  }

  /**
   * 回显树形角色权限
   */
  @GetMapping(value = "/showRoleResourceTree", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
  public List<RoleResourceDTO> showRoleResourceTree(@RequestParam("role_id") String roleId) {
    List<RoleResourceDTO> list = null;
    try {
      list = roleService.resourceWithRoleId(roleId);
    } catch (Exception e) {
      log.error("Get roleResourceTree failed :" + e);
    }
    return list;
  }

  /**
   * 修改角色权限
   */
  @PostMapping(value = "change_roleResource", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
  public ResponseEntity changeRoleResource(HttpServletRequest request,
      @RequestParam(value = "resource_ids[]", required = false) Long[] resourceIdArray,
      @RequestParam("role_id") String roleId) {
    JSONObject jsonObject = new JSONObject();
    try {
      roleService.changeRoleResource(resourceIdArray, roleId);
      opLogService
          .saveOpLog(request, "role", "role_resource edit", "role resource", SysOpLog.OP_RESULT_OK);
      jsonObject.put("status", "ok");
    } catch (Exception e) {
      jsonObject.put("status", "fail");
      log.error("Change roleResource failed:", e);
    }
    return ResponseEntity.ok(jsonObject);
  }

  /**
   * 角色游戏列表
   */
  @GetMapping(value = "/roleGames", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
  public ResponseEntity roleGames(
      @RequestParam(value = "game_name", required = false) String gameName,
      @RequestParam("page") int page,
      @RequestParam("page_size") int pageSize) {
    JSONObject jsonObject = new JSONObject();
    try {
      jsonObject = gameService.findGameList((page - 1), pageSize, gameName);
    } catch (Exception e) {
      jsonObject.put("status", "fail");
      log.error("Get roleGames failed:" + e);
    }
    return ResponseEntity.ok(jsonObject);
  }

  /**
   * 修改角色游戏权限
   */
  @PostMapping(value = "/change_roleGame", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
  public ResponseEntity changeRoleGame(
      HttpServletRequest request,
      @RequestParam(value = "game_ids[]", required = false) String[] gameIds,
      @RequestParam("role_id") String roleId
  ) {
    JSONObject jsonObject = new JSONObject();
    try {
      jsonObject = new JSONObject();
      roleService.changeRoleGame(gameIds, roleId);
      opLogService.saveOpLog(request, "role", "role_game edit", "role game", SysOpLog.OP_RESULT_OK);
      jsonObject.put("status", "ok");
    } catch (Exception e) {
      jsonObject.put("status", "fail");
      log.error("Change roleGame failed:" + e);
    }
    return ResponseEntity.ok(jsonObject);
  }

  /**
   * 回显树形角色数据权限
   */
  @GetMapping(value = "/showRoleDataTree", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
  public List<MediaAndResourceDTO> showRoleDataTree(@RequestParam("role_id") String roleId) {
    List<MediaAndResourceDTO> list = null;
    try {
      list = roleService.showRoleDataTree(roleId);
    } catch (Exception e) {
      log.error("Get roleDataTree failed :" + e);
    }
    return list;
  }

  /**
   * 修改角色数据权限
   */
  @PostMapping(value = "/change_role_data", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
  public ResponseEntity changeRoleData(
      HttpServletRequest request,
      @RequestParam(value = "team_ids[]", required = false) String[] teamIds,
      @RequestParam(value = "product_ids[]", required = false) String[] productIds,
      @RequestParam(value = "media_ids[]", required = false) String[] mediaIds,
      @RequestParam(value = "mediaresource_ids[]", required = false) String[] mediaresourceIds,
      @RequestParam("role_id") String roleId
  ) {
    JSONObject jsonObject = new JSONObject();
    try {
      jsonObject = new JSONObject();
      roleService.changeRoleData(teamIds, productIds, mediaIds, mediaresourceIds, roleId);
      opLogService.saveOpLog(request, "role", "role_data edit", "role data", SysOpLog.OP_RESULT_OK);
      jsonObject.put("status", "ok");
    } catch (Exception e) {
      jsonObject.put("status", "fail");
      log.error("Change roleData failed:" + e);
    }
    return ResponseEntity.ok(jsonObject);
  }

  /**
   *
   */
  @GetMapping(value = "/getMediaResource", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
  public ResponseEntity getMediaResource(
      HttpServletRequest request,
      @RequestParam(value = "media_id", required = false) String mediaId,
      @RequestParam("role_id") String roleId
  ) {
    JSONObject jsonObject = new JSONObject();
    try {
      jsonObject = new JSONObject();
      roleService.getMediaResource(roleId,mediaId);
      opLogService.saveOpLog(request, "role", "media_resource get", "role", SysOpLog.OP_RESULT_OK);
      jsonObject.put("status", "ok");
    } catch (Exception e) {
      jsonObject.put("status", "fail");
      log.error("Change roleData failed:" + e);
    }
    return ResponseEntity.ok(jsonObject);
  }
}

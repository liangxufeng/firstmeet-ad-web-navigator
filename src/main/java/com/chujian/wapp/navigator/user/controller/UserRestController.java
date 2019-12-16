package com.chujian.wapp.navigator.user.controller;

import com.alibaba.fastjson.JSONObject;
import com.chujian.wapp.navigator.dept.entity.Dept;
import com.chujian.wapp.navigator.role.service.RoleService;
import com.chujian.wapp.navigator.syslog.entity.SysOpLog;
import com.chujian.wapp.navigator.syslog.service.SysOpLogService;
import com.chujian.wapp.navigator.user.entity.User;
import com.chujian.wapp.navigator.user.model.UserDTO;
import com.chujian.wapp.navigator.user.service.UserRequestService;
import com.chujian.wapp.navigator.user.service.UserService;
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
public class UserRestController {

  @Autowired
  private UserService userService;

  @Autowired
  private RoleService roleService;

  @Autowired
  private UserRequestService userRequestService;

  @Autowired
  private SysOpLogService opLogService;

  /**
   * 用户分页
   */
  @GetMapping(value = "/user", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
  public ResponseEntity userPage(
      @RequestParam(value = "user_name", required = false) String userName,
      @RequestParam("page") int page,
      @RequestParam("page_size") int pageSize
  ) {
    JSONObject jsonObject = new JSONObject();
    try {
      jsonObject = userService.findUserList((page - 1), pageSize, userName);
    } catch (Exception e) {
      log.error("Get userPage failed:" + e);
      jsonObject.put("status", "fail");
    }
    return ResponseEntity.ok(jsonObject);
  }

  /**
   * 增加或者修改用户
   */
  @PostMapping(value = "save_user", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
  public ResponseEntity saveUser(@RequestParam("user_id") String userId,
      @RequestParam("user_name") String userName,
      @RequestParam("user_secret") String userPassword,
      @RequestParam("user_dept") String userDept,
      @RequestParam("id") String id,
      HttpServletRequest request) {
    JSONObject jsonObject = new JSONObject();
    //根据Id有无判断执行新增还是修改操作
    try {
      if ("".equals(id)) {//执行添加
        User userbyUserId = userService.findByUserId(userId);
        if (userbyUserId != null) {
          jsonObject.put("status", "addUserIdFail");
        } else {
          userService.save(userId, userName, userPassword, userDept);
          opLogService.saveOpLog(request, "user", "add", "user add", SysOpLog.OP_RESULT_OK);
          jsonObject.put("status", "ok");
        }
      } else {//执行修改
        userService.updata(userId, userName, userPassword, userDept);
        opLogService.saveOpLog(request, "user", "edit", "user edit", SysOpLog.OP_RESULT_OK);
        jsonObject.put("status", "ok");
      }
    } catch (Exception e) {
      jsonObject.put("status", "fail");
      log.error("Save user failed: ", e);
    }
    return ResponseEntity.ok(jsonObject);
  }

  /**
   * 修改用户时回显
   */
  @GetMapping(value = "/edit_user", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
  public ResponseEntity editUser(HttpServletRequest request,
      @RequestParam(value = "user_id", required = false) String userId) {
    UserDTO user = null;
    try {
      user = userService.findUserByUserId(userId);
    } catch (Exception ex) {
      log.error("Edit user failed:", ex);
    }
    return ResponseEntity.ok(user);
  }

  /**
   * 删除用户
   */
  @PostMapping(value = "/delete_user", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
  public ResponseEntity deleteUser(
      @RequestParam(value = "user_ids[]", required = false) String[] userIds,
      HttpServletRequest request) {
    JSONObject jsonObject = new JSONObject();
    try {
      for (int i = 0; i < userIds.length; i++) {
        if ("admin".equals(userIds[i])) {
          jsonObject.put("status", "no_delete");
          return ResponseEntity.ok(jsonObject);
        }
      }
      userService.deleteUser(userIds);
      opLogService.saveOpLog(request, "user", "del", "user del", SysOpLog.OP_RESULT_OK);
      jsonObject.put("status", "ok");
    } catch (Exception e) {
      jsonObject.put("status", "fail");
      log.error("Delete user failed:", e);
    }
    return ResponseEntity.ok(jsonObject);
  }


  /**
   * 用户角色列表
   */
  @GetMapping(value = "/userRoles", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
  public ResponseEntity userRoles(
      @RequestParam(value = "role_name", required = false) String roleName,
      @RequestParam("page") int page,
      @RequestParam("page_size") int pageSize) {
    JSONObject jsonObject = new JSONObject();
    try {
      jsonObject = roleService.findRoleList((page - 1), pageSize, roleName);
    } catch (Exception e) {
      jsonObject.put("status", "fail");
      log.error("Get userRoles failed:" + e);
    }
    return ResponseEntity.ok(jsonObject);
  }

  /**
   * 修改用户角色
   */
  @PostMapping(value = "/change_userRole", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
  public ResponseEntity changeUserRole(
      HttpServletRequest request,
      @RequestParam(value = "role_ids[]", required = false) String[] roleIds,
      @RequestParam("user_id") String userId
  ) {
    JSONObject jsonObject = new JSONObject();
    try {
      jsonObject = new JSONObject();
      userService.changeUserRole(roleIds, userId);
      opLogService.saveOpLog(request, "user", "user_role deit", "user role", SysOpLog.OP_RESULT_OK);
      jsonObject.put("status", "ok");
    } catch (Exception e) {
      jsonObject.put("status", "fail");
      log.error("Change userRole failed:" + e);
    }
    return ResponseEntity.ok(jsonObject);
  }

  @GetMapping(value = "/user_dept", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
  public List<Dept> userDept() {
    return userService.getDeptList();
  }
}

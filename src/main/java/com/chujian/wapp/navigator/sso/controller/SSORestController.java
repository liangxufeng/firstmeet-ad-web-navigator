package com.chujian.wapp.navigator.sso.controller;

import com.alibaba.fastjson.JSONObject;
import com.chujian.wapp.navigator.common.Constants;
import com.chujian.wapp.navigator.dept.entity.Dept;
import com.chujian.wapp.navigator.dept.service.DeptService;
import com.chujian.wapp.navigator.resource.model.ResourceTypeConfig;
import com.chujian.wapp.navigator.sso.model.AccessResource;
import com.chujian.wapp.navigator.sso.model.AccessToken;
import com.chujian.wapp.navigator.sso.model.DeptTree;
import com.chujian.wapp.navigator.sso.model.Menu;
import com.chujian.wapp.navigator.sso.service.AccessTokenService;
import com.chujian.wapp.navigator.syslog.entity.SysOpLog;
import com.chujian.wapp.navigator.syslog.service.SysOpLogService;
import com.chujian.wapp.navigator.user.entity.User;
import com.chujian.wapp.navigator.user.service.UserService;
import com.chujian.wapp.navigator.utils.MD5Utils;
import com.chujian.wapp.navigator.utils.SessionUtils;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
@RequestMapping("sso")
public class SSORestController {

  private static ObjectMapper objectMapper = new ObjectMapper();

  @Value("${sso.admin}")
  private String admin;


  @Autowired
  private AccessTokenService accessTokenService;

  @Autowired
  private UserService userService;

  @Autowired
  private DeptService deptService;

  @Resource
  private SysOpLogService opLogService;

  private static DeptTree findChildren(DeptTree deptTree, List<DeptTree> allDeptTreeList) {
    for (DeptTree dt : allDeptTreeList) {
      if (deptTree.getDeptId().equals(dt.getParentId())) {
        if (deptTree.getChildDeptList() == null) {
          deptTree.setChildDeptList(new ArrayList<>());
        }
        deptTree.getChildDeptList().add(findChildren(dt, allDeptTreeList));
      }
    }
    return deptTree;
  }

  @PostMapping(value = "/login", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
  public ResponseEntity login(HttpServletRequest request, HttpServletResponse response,
      @RequestParam("account") String account,
      @RequestParam("password") String password) {
    JSONObject jsonObject = new JSONObject();
    try {
      User user = userService.findByUserId(account);
      if (user != null) {
        String userPassword = user.getUserPassword();
        String md5Password = MD5Utils.md5DigestAsBase64(password);
        if (userPassword.equals(md5Password)) {

          //判断用户是否是admin
          if (admin.equals(user.getUserId())) {
            AccessToken accessToken = accessTokenService.createAdminAccessToken(account, account);
            String accessTokenStr = accessTokenService.generateAccessTokenStr(accessToken);
            SessionUtils.saveAccessToken(request, accessToken, accessTokenStr);
            jsonObject.put("status", "ok");
            opLogService.saveOpLog(request, "sso", "login", "admin login", SysOpLog.OP_RESULT_OK);
            return ResponseEntity.ok(jsonObject);
          }

          AccessToken accessToken = accessTokenService.createAccessToken(user);
          String accessTokenStr = accessTokenService.generateAccessTokenStr(accessToken);
          SessionUtils.saveAccessToken(request, accessToken, accessTokenStr);
          jsonObject.put("status", "ok");
          opLogService.saveOpLog(request, "sso", "login", "user login", SysOpLog.OP_RESULT_OK);
        } else {
          //密码错误
          jsonObject.put("status", "pwd_wrong");
        }
      } else {
        //账号不存在
        jsonObject.put("status", "user_wrong");
      }
    } catch (Exception ex) {
      jsonObject.put("status", "server_wrong");
      log.error("Login failed: ", ex);
    }
    return ResponseEntity.ok(jsonObject);
  }

  @RequestMapping(value = "token", method = {RequestMethod.GET, RequestMethod.POST})
  public ResponseEntity ssoToken(HttpServletRequest request,
      @RequestParam(Constants.REQUEST_PARAM_CODE) String authCode) {
    String accessTokenStr = accessTokenService.getAccessTokenString(authCode);
    if (StringUtils.isBlank(accessTokenStr)) {
      return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }
    try {
      AccessToken accessToken = accessTokenService.getAccessToken(authCode);
      if (!accessTokenService.isValid(accessToken)) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
      }
    } catch (Exception ex) {
      log.error("Request tokenApi failed: ", ex);
      return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }
    accessTokenService.deleteAccessToken(authCode);
    JSONObject tokeObject = new JSONObject();
    tokeObject.put("access_token", accessTokenStr);
    return ResponseEntity.ok(tokeObject);
  }

  @RequestMapping(value = "check_token", method = {RequestMethod.GET, RequestMethod.POST})
  public ResponseEntity checkToken(HttpServletRequest request,
      @RequestParam(Constants.REQUEST_PARAM_TOKEN) String token) {
    JSONObject resultObj = new JSONObject();
    if (StringUtils.isBlank(token)) {
      resultObj.put("valid", false);
      return ResponseEntity.ok(resultObj);
    }
    try {
      AccessToken accessToken = accessTokenService.decodeToken(token);
      if (accessTokenService.isValid(accessToken)) {
        String tokenStr = objectMapper.writeValueAsString(accessToken);
        resultObj = objectMapper.readValue(tokenStr, JSONObject.class);
        resultObj.put("valid", true);
        resultObj.put("access_token", accessToken);
      } else {
        resultObj.put("valid", false);
      }
    } catch (Exception ex) {
      resultObj.put("valid", false);
      log.error("Request check_tokenApi failed: ", ex);
    }
    return ResponseEntity.ok(resultObj);
  }

  @RequestMapping(value = "/menus", method = {RequestMethod.GET, RequestMethod.POST})
  @ResponseBody
  public ResponseEntity getMenu(HttpServletRequest request,
      @RequestParam(Constants.REQUEST_PARAM_TOKEN) String tokenStr,
      @RequestParam(Constants.REQUEST_PARAM_SYSTEM_RESOURCE_ID) String sysResourceId) {

    //验证tokenStr的正确性
    if (StringUtils.isBlank(tokenStr) || StringUtils.isBlank(sysResourceId)) {
      return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
    }

    AccessToken accessToken = null;
    try {
      accessToken = accessTokenService.decodeToken(tokenStr);
    } catch (Exception e) {
      return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
    }

    if (accessToken == null || !accessTokenService.isValid(accessToken)) {
      return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }

    List<AccessResource> resourceList = accessToken.getResourceList();
    if (resourceList == null || resourceList.isEmpty()) {
      return ResponseEntity.status(HttpStatus.OK).body(Collections.emptyList());
    }

    //遍历系统
    List<AccessResource> resourceMenuList = new ArrayList<>();
    AccessResource sysAccessResource = null;
    for (AccessResource accessResource : resourceList) {

      if (accessResource.getResourceId().equals(sysResourceId) && ResourceTypeConfig.RS_TYPE_SYSTEM
          .equals(accessResource.getType())) {
        sysAccessResource = accessResource;
        break;
      }

      List<AccessResource> childList = accessResource.getChildList();
      if (childList==null){
        break;
      }
      for (AccessResource childAccessResource : childList) {
        if (childAccessResource.getResourceId().equals(sysResourceId)
            && ResourceTypeConfig.RS_TYPE_SYSTEM
            .equals(childAccessResource.getType())) {
          sysAccessResource = childAccessResource;
          break;
        }

        if (sysAccessResource != null) {
          break;
        }
      }
    }

    if (sysAccessResource == null) {
      return ResponseEntity.status(HttpStatus.OK).body(Collections.emptyList());
    }

    //遍历菜单
    List<AccessResource> childList = sysAccessResource.getChildList();
    if (childList != null && !childList.isEmpty()) {
      for (AccessResource accessResource : childList) {
        if (accessResource.getType().equals(ResourceTypeConfig.RS_TYPE_MENU)) {
          resourceMenuList.add(accessResource);
        }
      }
    }

    List<Menu> menuList = new ArrayList<>();
    if (resourceMenuList.isEmpty()) {
      return ResponseEntity.status(HttpStatus.OK).body(resourceMenuList);
    }

    for (AccessResource accessResource : resourceMenuList) {
      List<AccessResource> childResourceList = accessResource.getChildList();
      if (childResourceList == null || childResourceList.isEmpty()) {
        continue;
      }

      List<Menu> childMenuList = new ArrayList<>();
      for (AccessResource childResource : childResourceList) {
        Menu menu = Menu.builder().id(childResource.getId())
            .parentId(childResource.getParentId())
            .resourceId(childResource.getResourceId())
            .resourceName(childResource.getName())
            .resourceUrl(childResource.getUrl())
            .resourceOrderNum(childResource.getResourceId())
            .build();
        childMenuList.add(menu);
      }

      Menu menu = Menu.builder().id(accessResource.getId())
          .parentId(accessResource.getParentId())
          .resourceId(accessResource.getResourceId())
          .resourceName(accessResource.getName())
          .resourceUrl(accessResource.getUrl())
          .resourceOrderNum(accessResource.getOrderNum().toString())
          .childMenuList(childMenuList).build();
      menuList.add(menu);
    }
    JSONObject jsonObject = new JSONObject();
    jsonObject.put("menuList", menuList);
    return ResponseEntity.ok(jsonObject);
  }

  @RequestMapping(value = "/depts", method = {RequestMethod.GET, RequestMethod.POST})
  @ResponseBody
  public ResponseEntity depts(HttpServletRequest request,
      @RequestParam(Constants.REQUEST_PARAM_TOKEN) String tokenStr,
      @RequestParam(name = "dept_id", required = false) String deptId) throws Exception {

    if (StringUtils.isBlank(tokenStr)) {
      return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
    }

    AccessToken accessToken = null;
    try {
      accessToken = accessTokenService.decodeToken(tokenStr);
    } catch (Exception e) {
      return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
    }

    if (accessToken == null || !accessTokenService.isValid(accessToken)) {
      return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }

    //deptId为null  返回所有部门的树形数据
    if (StringUtils.isBlank(deptId)) {
      List<DeptTree> deptTreeList = new ArrayList<>();
      List<DeptTree> allDeptTreeList = new ArrayList<>();
      List<Dept> allDeptList = deptService.findAll();
      if (allDeptList == null || allDeptList.isEmpty()) {
        return ResponseEntity.status(HttpStatus.OK).body(Collections.emptyList());
      }
      for (Dept dept : allDeptList) {
        DeptTree deptTree = DeptTree.builder().id(dept.getId().toString())
            .parentId(dept.getDeptParentId().toString()).deptId(dept.getDeptId())
            .deptName(dept.getDeptName()).deptOrderNum(dept.getDeptName())
            .build();
        allDeptTreeList.add(deptTree);
      }
      //构建树形数据
      for (DeptTree deptTree : allDeptTreeList) {
        if (deptService.findDeptByDeptId(deptTree.getParentId()) == null) {
          deptTreeList.add(findChildren(deptTree, allDeptTreeList));
        }
      }
      return ResponseEntity.ok(deptTreeList);
    }

    //deptId不为null  返回当前部门的树形数据
    List<DeptTree> deptTreeList = new ArrayList<>();
    List<DeptTree> allDeptTreeList = new ArrayList<>();
    //查询出当前部门之下的所有部门
    Dept rootDept = deptService.findDeptByDeptId(deptId);
    if (rootDept == null) {
      return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
    }
    List<Dept> allDeptList = deptService.findSubDeptList(deptId);
    allDeptList.add(rootDept);
    for (Dept dept : allDeptList) {
      DeptTree deptTree = DeptTree.builder().id(dept.getId().toString())
          .parentId(dept.getDeptParentId()).deptId(dept.getDeptId())
          .deptName(dept.getDeptName()).deptOrderNum(String.valueOf(dept.getDeptOrderNum()))
          .build();
      allDeptTreeList.add(deptTree);
    }
    //构建树形数据
    for (DeptTree deptTree : allDeptTreeList) {
      if (deptTree.getDeptId().equals(deptId)) {
        deptTreeList.add(findChildren(deptTree, allDeptTreeList));
      }
    }
    return ResponseEntity.ok(deptTreeList);
  }

  @PostMapping(value = "/password_change")
  @ResponseBody
  public ResponseEntity changePassword(HttpServletRequest request,
      @RequestBody JSONObject passwordObj) {
    JSONObject resultObject = new JSONObject();
    resultObject.put("result", "fail");
    AccessToken accessToken = SessionUtils.getAccessToken(request);
    if (accessTokenService.isValid(accessToken) && StringUtils
        .isNotBlank(accessToken.getUserName())) {
      try {
        String oldPassword = passwordObj.getString("old_password");
        String newPassword = passwordObj.getString("new_password");
        int status = userService.modifyPassword(accessToken.getUserId(), oldPassword, newPassword);
        if (status == 0) {
          resultObject.put("result", "ok");
          accessTokenService.increaseUserTokenVersion(accessToken.getUserId());
          SessionUtils.removeAccessToken(request);
          request.getSession().invalidate();
        } else if (status == 2) {
          resultObject.put("result", "wrong_old");
        }
      } catch (Exception ex) {
        log.error("Change password failed:", ex);
      }
    }
    return ResponseEntity.ok(resultObject);
  }

  @RequestMapping(value = "/logout", method = {RequestMethod.GET, RequestMethod.POST})
  @ResponseBody
  public ResponseEntity logout(HttpServletRequest request,
      @RequestParam(Constants.REQUEST_PARAM_TOKEN) String tokenStr) throws Exception {
    if (StringUtils.isBlank(tokenStr)) {
      return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
    }

    AccessToken accessToken = null;
    try {
      accessToken = accessTokenService.decodeToken(tokenStr);
    } catch (Exception e) {
      return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
    }

    if (accessToken == null || !accessTokenService.isValid(accessToken)) {
      return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }
    opLogService.saveOpLog(request, "user", "logout", "user logout", SysOpLog.OP_RESULT_OK);
    SessionUtils.removeAccessToken(request);
    request.getSession().invalidate();
    return ResponseEntity.noContent().build();
  }
}
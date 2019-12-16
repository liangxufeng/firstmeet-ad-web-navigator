package com.chujian.wapp.navigator.user.controller;

import com.chujian.wapp.navigator.sso.model.ACL;
import com.chujian.wapp.navigator.common.acl.AclResource;
import com.chujian.wapp.navigator.dept.entity.Dept;
import com.chujian.wapp.navigator.dept.respository.DeptRepository;
import com.chujian.wapp.navigator.user.service.UserRequestService;
import com.chujian.wapp.navigator.user.service.UserService;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@Slf4j
public class UserWebController {

  @Autowired
  private UserRequestService userRequestService;
  @Autowired
  private DeptRepository deptRepository;
  @Autowired
  private UserService userService;

  @AclResource(resourceId = ACL.MENU_USER)
  @RequestMapping(value = "/user_manage")
  public String userManage(HttpServletRequest request) {
    //将部门列表放入session以实现部门下拉菜单
    List<Dept> deptList = deptRepository.findAllByOrderByDeptOrderNumAsc();
    request.setAttribute("deptList", deptList);
    String userId = userRequestService.putUserIdToRequest(request);
    request.setAttribute("userId", userId);
    return "user/user_query";
  }

  @RequestMapping(value = "/user_role")
  public String userRoleManage(HttpServletRequest request, String userId) {
    userRequestService.putUserToRequest(request, userId);
    return "user/user_role";
  }

}

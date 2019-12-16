package com.chujian.wapp.navigator.user.service;

import com.chujian.wapp.navigator.common.Constants;
import com.chujian.wapp.navigator.role.entity.Role;
import com.chujian.wapp.navigator.role.respository.RoleRepository;
import com.chujian.wapp.navigator.sso.model.AccessToken;
import com.chujian.wapp.navigator.user.entity.User;
import com.chujian.wapp.navigator.user.respository.UserRepository;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserRequestService {

  @Autowired
  private UserRepository userRepository;

  @Autowired
  private RoleRepository roleRepository;

  public void putUserToRequest(HttpServletRequest request, String userId) {

    User user = userRepository.findByUserId(userId);
    request.setAttribute("user", user);

    //查询当前用户所拥有的角色,做数据回显
    List<Role> userRoleList = roleRepository.findRolesByUserId(userId);
    String userRoleStr = userRoleList.toString();
    request.setAttribute("userRoleStr", userRoleStr);

    //查询所有角色信息
    List<Role> roleList = roleRepository.findAll();
    request.setAttribute("roleList", roleList);
  }

  public String putUserIdToRequest(HttpServletRequest request) {
    AccessToken accessToken = (AccessToken) request.getSession()
        .getAttribute(Constants.SESSION_KEY_ACCESS_TOKEN);
    if (accessToken == null) {
      return null;
    }
    String userId = accessToken.getUserId();
    return userId;
  }
}

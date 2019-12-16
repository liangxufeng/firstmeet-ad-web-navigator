package com.chujian.wapp.navigator.user.service;

import com.alibaba.fastjson.JSONObject;
import com.chujian.wapp.navigator.dept.entity.Dept;
import com.chujian.wapp.navigator.dept.respository.DeptRepository;
import com.chujian.wapp.navigator.game.service.GameService;
import com.chujian.wapp.navigator.resource.respository.ResourceRepository;
import com.chujian.wapp.navigator.role.entity.Role;
import com.chujian.wapp.navigator.role.respository.RoleRepository;
import com.chujian.wapp.navigator.role.service.RoleService;
import com.chujian.wapp.navigator.user.entity.User;
import com.chujian.wapp.navigator.user.entity.UserDept;
import com.chujian.wapp.navigator.user.entity.UserRole;
import com.chujian.wapp.navigator.user.model.UserDTO;
import com.chujian.wapp.navigator.user.respository.UserDeptRepository;
import com.chujian.wapp.navigator.user.respository.UserRepository;
import com.chujian.wapp.navigator.user.respository.UserRoleRepository;
import com.chujian.wapp.navigator.utils.DateUtils;
import com.chujian.wapp.navigator.utils.MD5Utils;
import java.util.ArrayList;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class UserService {

  @Autowired
  private UserRepository userRepository;
  @Autowired
  private RoleRepository roleRepository;
  @Autowired
  private UserRoleRepository userRoleRepository;
  @Autowired
  private DeptRepository deptRepository;
  @Autowired
  private UserDeptRepository userDeptRepository;
  @Autowired
  private ResourceRepository resourceRepository;
  @Autowired
  private GameService gameService;
  @Autowired
  private RoleService roleService;
  @Value("${sso.admin}")
  private String admin;


  /**
   * 用户分页
   */
  public JSONObject findUserList(int page, int pageSize, String userName) {
    JSONObject resultObj = new JSONObject();
    Pageable pageable = PageRequest.of(page, pageSize, Direction.DESC, "createdAt");
    Page<User> pageResult = null;
    if (StringUtils.isBlank(userName)) {
      pageResult = userRepository.findAll(pageable);
    } else {
      pageResult = userRepository.findByUserNameLike("%" + userName + "%", pageable);
    }
    long totalElements = pageResult.getTotalElements();
    int totalPages = pageResult.getTotalPages();
    int number = pageResult.getNumber();
    List<UserDTO> userList = new ArrayList<>();
    for (User user : pageResult) {
      String newCreatedAt = convertDate(user.getCreatedAt().toString());
      String newUpdatedAt = convertDate(user.getUpdatedAt().toString());
      UserDept userDept = userDeptRepository.findByUserId(user.getUserId());
      Dept dept = deptRepository.findByDeptId(userDept.getDeptId());
      Dept parentDept = deptRepository.findByDeptId(dept.getDeptParentId());

      if (parentDept != null) {
        //看前端需要什么就构建什么字段
        UserDTO userDTO = UserDTO.builder().
            id(user.getId()).
            userId(user.getUserId()).
            userName(user.getUserName()).
            userDept(userDept.getDeptId()).
            userDeptName(parentDept.getDeptName() + "-" + dept.getDeptName()).
            createdAt(newCreatedAt).
            updatedAt(newUpdatedAt)
            .build();
        userList.add(userDTO);
      } else {
        //看前端需要什么就构建什么字段
        UserDTO userDTO = UserDTO.builder().
            id(user.getId()).
            userId(user.getUserId()).
            userName(user.getUserName()).
            userDept(userDept.getDeptId()).
            createdAt(newCreatedAt).
            userDeptName(dept.getDeptName()).
            updatedAt(newUpdatedAt).build();
        userList.add(userDTO);
      }
    }
    resultObj.put("total_pages", totalPages);
    resultObj.put("current_page", number);
    resultObj.put("page_size", pageSize);
    resultObj.put("total_elements", totalElements);
    resultObj.put("user", userList);
    return resultObj;
  }

  /**
   * 保存
   */
  public void save(String userId, String userName, String userPassword, String userDept)
      throws Exception {
    //使用MD5将密码加密
    String md5Password = MD5Utils.md5DigestAsBase64(userPassword);
    User user = User.builder().userId(userId).userName(userName).userPassword(md5Password).build();
    userRepository.save(user);
    //保存中间表数据
    UserDept userDeptEntity = UserDept.builder().deptId(userDept).userId(userId).build();
    userDeptRepository.save(userDeptEntity);
  }

  /**
   * 根据userid查询用户
   */
  public User findByUserId(String userId) {
    return userRepository.findByUserId(userId);
  }


  /**
   * 修改用户
   */
  public void updata(String userId, String userName, String userPassword, String userDept)
      throws Exception {
    User oldUser = userRepository.findByUserId(userId);
    String oldMd5Password = oldUser.getUserPassword();
    if (userPassword.equals(oldMd5Password)) {
      userRepository.updateUser(userId, userName, userPassword);
    } else {
      String newMd5Password = MD5Utils.md5DigestAsBase64(userPassword);
      userRepository.updateUser(userId, userName, newMd5Password);
    }
    //更新中间表数据
    userDeptRepository.updateUserDept(userId, userDept);
  }


  /**
   * 根据userId查询user
   */
  public UserDTO findUserByUserId(String userId) {
    User user = userRepository.findByUserId(userId);
    UserDept userDept = userDeptRepository.findByUserId(userId);
    Dept dept = deptRepository.findByDeptId(userDept.getDeptId());
    String deptName = "";
    Dept parentDept = deptRepository.findByDeptId(dept.getDeptParentId());
    if (parentDept != null) {
      deptName = parentDept.getDeptName() + "-" + dept.getDeptName();
    } else {
      deptName = dept.getDeptName();
    }
    UserDTO userDTO = UserDTO.builder().id(user.getId()).userId(user.getUserId())
        .userName(user.getUserName())
        .userPassword(user.getUserPassword()).userDept(deptName).userDeptId(userDept.getDeptId())
        .build();
    return userDTO;
  }


  /**
   * 删除user
   */
  public void deleteUser(String[] userIds) {
    for (int i = 0; i < userIds.length; i++) {
      //删除用户的部门信息
      if (userDeptRepository.findByUserId(userIds[i]) != null) {
        userDeptRepository.deleteByUserId(userIds[i]);
      }
      //删除用户角色信息
      if (userRoleRepository.findByUserId(userIds[i]) != null) {
        userRoleRepository.deleteByUserId(userIds[i]);
      }
      // 删除用户信息
      userRepository.deleteByUserId(userIds[i]);
    }
  }

  /**
   * 时间转换方法
   */
  private String convertDate(String dateStr) {
    String originalDateTimeFormat = "yyyy-MM-dd'T'HH:mm:ss.SSSZ";
    String convertedDateTimeFormat = "yyyy-MM-dd HH:mm:ss";
    return DateUtils.convertDate(dateStr, originalDateTimeFormat, convertedDateTimeFormat);
  }

  public void changeUserRole(String[] roleIds, String userId) {
    List<Role> rolesByUserId = roleRepository.findRolesByUserId(userId);
    if (rolesByUserId.size() != 0) {
      //先删除用户之前所拥有的角色
      userRoleRepository.deleteByUserId(userId);
    }
    //给用户绑定新的角色信息
    //遍历角色数组,为用户添加角色
    if (roleIds != null) {
      for (String roleId : roleIds) {
        UserRole userRole = UserRole.builder().userId(userId).roleId(roleId).build();
        userRoleRepository.save(userRole);
      }
    }
  }

  public List<Dept> getDeptList() {
    return deptRepository.findAllByOrderByDeptOrderNumAsc();
  }

  public int modifyPassword(String userId, String oldPassword, String newPassword)
      throws Exception {
    User user = userRepository.findByUserId(userId);
    String md5OldPassword = MD5Utils.md5DigestAsBase64(oldPassword);
    String md5NewPassword = MD5Utils.md5DigestAsBase64(newPassword);
    if (user.getUserPassword().equals(md5OldPassword)) {
      user.setUserPassword(md5NewPassword);
      userRepository.saveAndFlush(user);
      return 0;
    } else {
      return 2;
    }
  }

}

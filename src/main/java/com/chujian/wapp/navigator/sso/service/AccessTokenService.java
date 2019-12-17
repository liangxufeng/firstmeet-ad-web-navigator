package com.chujian.wapp.navigator.sso.service;

import com.alibaba.fastjson.JSONObject;
import com.chujian.wapp.navigator.common.Constants;
import com.chujian.wapp.navigator.dept.entity.Dept;
import com.chujian.wapp.navigator.dept.respository.DeptRepository;
import com.chujian.wapp.navigator.game.entity.Game;
import com.chujian.wapp.navigator.game.respository.GameRepository;
import com.chujian.wapp.navigator.resource.respository.ResourceRepository;
import com.chujian.wapp.navigator.role.entity.Role;
import com.chujian.wapp.navigator.role.entity.RoleGame;
import com.chujian.wapp.navigator.role.entity.RoleResource;
import com.chujian.wapp.navigator.role.respository.RoleGameRepository;
import com.chujian.wapp.navigator.role.respository.RoleRepository;
import com.chujian.wapp.navigator.role.respository.RoleResourceRepository;
import com.chujian.wapp.navigator.sso.model.AccessDept;
import com.chujian.wapp.navigator.sso.model.AccessGame;
import com.chujian.wapp.navigator.sso.model.AccessResource;
import com.chujian.wapp.navigator.sso.model.AccessRole;
import com.chujian.wapp.navigator.sso.model.AccessToken;
import com.chujian.wapp.navigator.user.entity.User;
import com.chujian.wapp.navigator.user.entity.UserDept;
import com.chujian.wapp.navigator.user.entity.UserRole;
import com.chujian.wapp.navigator.user.respository.UserDeptRepository;
import com.chujian.wapp.navigator.user.respository.UserRoleRepository;
import com.chujian.wapp.navigator.utils.AccessKeyProperties;
import com.chujian.wapp.navigator.utils.JKSUtil;
import com.chujian.wapp.navigator.utils.JWTUtil;
import com.chujian.wapp.navigator.utils.SessionUtils;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;
import java.util.UUID;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.Base64Utils;


@Service
@Slf4j
public class AccessTokenService {

  private static ObjectMapper objectMapper = new ObjectMapper();

  @Value("${chujian.sso.token.expire-seconds}")
  private long tokenExpired;

  @Value("${sso.admin}")
  private String admin;


  @Resource
  private AccessKeyProperties accessKeyProperties;

  @Autowired
  private RedissonClient redissonClient;

  @Autowired
  private RoleGameRepository roleGameRepository;

  @Autowired
  private RoleResourceRepository roleResourceRepository;

  @Autowired
  private UserRoleRepository userRoleRepository;

  @Autowired
  private RoleRepository roleRepository;

  @Autowired
  private GameRepository gameRepository;

  @Autowired
  private ResourceRepository resourceRepository;

  @Autowired
  private UserDeptRepository userDeptRepository;

  @Autowired
  private DeptRepository deptRepository;

  public static JSONObject getTokenInfo(String tokenStr) throws Exception {
    String payload = tokenStr.split("\\.")[1];
    return JSONObject.parseObject(new String(Base64Utils.decodeFromString(payload), "UTF-8"));
  }

  public AccessToken createAccessToken(User user) throws Exception {
    return createAccessToken(user.getUserId(), user.getUserName());
  }

  public AccessToken createAccessToken(String userId, String userName) throws Exception {
    long issueAt = System.currentTimeMillis();
    long expired = issueAt + tokenExpired * 1000;
    long version = getUserTokenVersion(userId);

    List<AccessRole> roleList = new ArrayList<>();
    List<AccessResource> resourceList = new ArrayList<>();
    List<AccessGame> gameList = new ArrayList<>();

    /*======组装token=========*/
    // 将  用户 角色 游戏  资源 放入token
    UserDept userDept = userDeptRepository.findByUserId(userId);
    Dept dept = deptRepository.findByDeptId(userDept.getDeptId());

    AccessDept accessDept = AccessDept.builder().deptId(dept == null ? "" : dept.getDeptId())
        .name(dept == null ? "" : dept.getDeptName())
        .build();

    Map<Integer, List<RoleResource>> roleResourceMap = new TreeMap<>();
    int count = 0;
    List<UserRole> userRoleList = userRoleRepository.findByUserId(userId);
    for (UserRole userRole : userRoleList) {
      Role role = roleRepository.findByRoleId(userRole.getRoleId());
      if (role == null) {
        continue;
      }

      AccessRole accessRole = AccessRole.builder().id(role.getRoleId()).name(role.getRoleName())
          .build();
      roleList.add(accessRole);

      List<RoleResource> roleResourceList = roleResourceRepository
          .findByRoleId(role.getRoleId());
      roleResourceMap.put(count++, roleResourceList);

      List<RoleGame> roleGameList = roleGameRepository.findByRoleId(userRole.getRoleId());
      for (RoleGame roleGame : roleGameList) {
        Game game = gameRepository.findById(Integer.valueOf(roleGame.getGameId()));
        if (game == null) {
          continue;
        }
        AccessGame accessGame = AccessGame.builder().id(game.getId().toString())
            .name(game.getName()).build();
        gameList.add(accessGame);
      }
    }

    List<RoleResource> resultRoleResourceList = null;
    List<RoleResource> tempList = new ArrayList<>();
    for (Integer num : roleResourceMap.keySet()) {
      List<RoleResource> newList = roleResourceMap.get(num);
      if (resultRoleResourceList == null) {
        resultRoleResourceList = newList;
        continue;
      }

      if (resultRoleResourceList.size() >= newList.size()) {
        for (RoleResource roleResource : resultRoleResourceList) {
          for (RoleResource resource : newList) {
            if (roleResource.getResourceId().equals(resource.getResourceId())) {
              tempList.add(roleResource);
            }
          }
        }
      } else {
        for (RoleResource roleResource : newList) {
          for (RoleResource resource : resultRoleResourceList) {
            if (roleResource.getResourceId().equals(resource.getResourceId())) {
              tempList.add(roleResource);
            }
          }
        }
      }
      resultRoleResourceList.removeAll(tempList);
      resultRoleResourceList.addAll(newList);
    }

    List<AccessResource> allResource = new ArrayList<>();
    if (resultRoleResourceList != null) {
      for (RoleResource roleResource : resultRoleResourceList) {
        com.chujian.wapp.navigator.resource.entity.Resource resource = resourceRepository
            .findByIdKey(roleResource.getResourceId());
        AccessResource accessChildResource = AccessResource.builder()
            .id(resource.getId().toString())
            .parentId(resource.getParentId().toString())
            .resourceId(resource.getResourceId())
            .name(resource.getResourceName())
            .type(resource.getResourceType())
            .url(resource.getResourceUrl())
            .orderNum(resource.getResourceOrderNum())
            .build();
        allResource.add(accessChildResource);
      }
    }

    for (AccessResource accessResource : allResource) {
      if ("0".equals(accessResource.getParentId())) {
        resourceList.add(findChildren(accessResource, allResource));
      }
    }

    resourceList.sort(Comparator.comparingLong(AccessResource::getOrderNum));
    resourceList = sortResource(resourceList);

    AccessToken accessToken = AccessToken.builder().userId(userId).userName(userName).iat(issueAt)
        .exp(expired).version(version).roleList(roleList).gameList(gameList)
        .resourceList(resourceList).accessDept(accessDept)
        .build();
    return accessToken;
  }

  private AccessResource findChildren(AccessResource accessResource,
      List<AccessResource> allResource) {
    for (AccessResource it : allResource) {
      if (accessResource.getId().equals(it.getParentId())) {
        if (accessResource.getChildList() == null) {
          accessResource.setChildList(new ArrayList<>());
        }
        accessResource.getChildList().add(findChildren(it, allResource));
      }
    }
    return accessResource;
  }

  public String getPublicKey() throws Exception {
    String publicKey = JKSUtil.toKeyString(JKSUtil
        .getPublicKey(accessKeyProperties.getJksFilepath(), accessKeyProperties.getPassword(),
            accessKeyProperties.getAlias()));
    log.info("Public key: {}", publicKey);
    return publicKey;
  }

  public String generateAccessTokenStr(AccessToken accessToken) throws Exception {
    String originalStr = objectMapper.writeValueAsString(accessToken);
    log.info("Original String: {}", originalStr);
    String encodedStr = JWTUtil.encode(originalStr, accessKeyProperties);
    log.info("Access Token: {}", encodedStr);
    return encodedStr;
  }

  public String getAccessTokenString(String authCode) {
    if (StringUtils.isBlank(authCode)) {
      return null;
    }
    return (String) redissonClient.getMap(Constants.REDIS_KEY_AUTH_CODES).get(authCode);
  }

  public AccessToken getAccessToken(String authCode) {
    String accessTokenStr = getAccessTokenString(authCode);
    if (StringUtils.isBlank(accessTokenStr)) {
      return null;
    }
    try {
      return decodeToken(accessTokenStr);
    } catch (Exception ex) {
      return null;
    }
  }

  public boolean isValid(AccessToken accessToken) {
    if (accessToken == null || accessToken.getExp() <= System.currentTimeMillis()) {
      return false;
    }
    String userId = accessToken.getUserId();
    long currTokenVersion = getUserTokenVersion(userId);
    return currTokenVersion == accessToken.getVersion();
  }

  public String getUserName(HttpServletRequest request) {
    AccessToken accessToken = SessionUtils.getAccessToken(request);
    return accessToken == null ? "" : accessToken.getUserName();
  }

  public AccessToken decodeToken(String tokenStr) throws Exception {
    return JWTUtil.decodeToken(tokenStr, accessKeyProperties.getPublicKeyString());
  }

  public void deleteAccessToken(String authCode) {
    if (StringUtils.isBlank(authCode)) {
      return;
    }
    redissonClient.getMap(Constants.REDIS_KEY_AUTH_CODES).remove(authCode);
  }

  public String generateAuthCode(HttpServletRequest request) {
    String accessToken = SessionUtils.getAccessTokenStr(request);
    String authCode = UUID.randomUUID().toString().replace("-", "");
    redissonClient.getMap(Constants.REDIS_KEY_AUTH_CODES).put(authCode, accessToken);
    return authCode;
  }

  public void deleteExpiredAccessTokens() {
    List<String> authCodeList = new ArrayList<>();
    for (Entry<Object, Object> entry : redissonClient.getMap(Constants.REDIS_KEY_AUTH_CODES)
        .entrySet()) {
      String authCode = (String) entry.getKey();
      String accessTokenStr = (String) entry.getValue();

      try {
        AccessToken accessToken = decodeToken(accessTokenStr);
        if (!isValid(accessToken)) {
          authCodeList.add(authCode);
        }
      } catch (Exception ex) {
        //ignore
        authCodeList.add(authCode);
      }
    }

    authCodeList.forEach(this::deleteAccessToken);
  }

  private long getUserTokenVersion(String userId) {
    long defaultVersion = 1L;
    Object tokenVersion = redissonClient.getMap(Constants.REDIS_KEY_USER_TOKEN_VERSION).get(userId);
    if (tokenVersion == null) {
      return defaultVersion;
    }
    try {
      return Long.parseLong(tokenVersion.toString());
    } catch (Exception ex) {
      return defaultVersion;
    }
  }

  public void increaseUserTokenVersion(String userId) {
    long currVersion = getUserTokenVersion(userId) + 1;
    redissonClient.getMap(Constants.REDIS_KEY_USER_TOKEN_VERSION).put(userId, currVersion);
  }


  public AccessToken createAdminAccessToken(String userId, String userName) throws Exception {
    long issueAt = System.currentTimeMillis();
    long expired = issueAt + tokenExpired * 1000;
    long version = getUserTokenVersion(userId);

    List<AccessRole> roleList = new ArrayList<>();
    List<AccessResource> resourceList = new ArrayList<>();
    List<AccessGame> gameList = new ArrayList<>();

    /*======组装token=========*/
    // 将  用户 角色 游戏  资源 放入token
    UserDept userDept = userDeptRepository.findByUserId(userId);
    Dept dept = deptRepository.findByDeptId(userDept.getDeptId());
    AccessDept accessDept = AccessDept.builder().deptId(dept.getDeptId()).name(dept.getDeptName())
        .build();

    roleList.add(AccessRole.builder().id(admin).name(admin).build());

    List<Game> allGameList = gameRepository.findAll();
    for (Game game : allGameList) {
      AccessGame accessGame = AccessGame.builder().id(game.getId().toString())
          .name(game.getName()).build();
      gameList.add(accessGame);
    }

    List<AccessResource> allResource = new ArrayList<>();
    List<com.chujian.wapp.navigator.resource.entity.Resource> allResourceList = resourceRepository
        .findAll();
    for (com.chujian.wapp.navigator.resource.entity.Resource resource : allResourceList) {
      AccessResource accessChildResource = AccessResource.builder()
          .id(resource.getId().toString())
          .parentId(resource.getParentId().toString())
          .resourceId(resource.getResourceId())
          .name(resource.getResourceName())
          .type(resource.getResourceType())
          .url(resource.getResourceUrl())
          .orderNum(resource.getResourceOrderNum())
          .build();
      allResource.add(accessChildResource);
    }

    for (AccessResource accessResource : allResource) {
      if ("0".equals(accessResource.getParentId())) {
        resourceList.add(findChildren(accessResource, allResource));
      }
    }

    resourceList.sort(Comparator.comparingLong(AccessResource::getOrderNum));
    resourceList = sortResource(resourceList);

    AccessToken accessToken = AccessToken.builder().userId(userId).userName(userName).iat(issueAt)
        .exp(expired).version(version).roleList(roleList).gameList(gameList)
        .resourceList(resourceList).accessDept(accessDept)
        .build();
    return accessToken;
  }

  private List<AccessResource> sortResource(List<AccessResource> resourceList) {
    if (resourceList.isEmpty()) {
      return null;
    }
    for (AccessResource accessResource : resourceList) {
      if (accessResource.getChildList() != null) {
        accessResource.getChildList().sort(Comparator.comparingLong(AccessResource::getOrderNum));
        sortResource(accessResource.getChildList());
      }
    }
    return resourceList;
  }
}

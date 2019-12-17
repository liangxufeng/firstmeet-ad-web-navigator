package com.chujian.wapp.navigator.role.service;

import com.alibaba.fastjson.JSONObject;
import com.chujian.wapp.navigator.game.entity.Game;
import com.chujian.wapp.navigator.game.respository.GameRepository;
import com.chujian.wapp.navigator.resource.entity.Resource;
import com.chujian.wapp.navigator.resource.model.ResourceDTO;
import com.chujian.wapp.navigator.resource.respository.ResourceRepository;
import com.chujian.wapp.navigator.resource.service.ResourceService;
import com.chujian.wapp.navigator.role.entity.Media;
import com.chujian.wapp.navigator.role.entity.MediaResource;
import com.chujian.wapp.navigator.role.entity.Role;
import com.chujian.wapp.navigator.role.entity.RoleGame;
import com.chujian.wapp.navigator.role.entity.RoleMedia;
import com.chujian.wapp.navigator.role.entity.RoleMediaResource;
import com.chujian.wapp.navigator.role.entity.RoleProduct;
import com.chujian.wapp.navigator.role.entity.RoleResource;
import com.chujian.wapp.navigator.role.entity.RoleTeam;
import com.chujian.wapp.navigator.role.model.MediaAndResourceDTO;
import com.chujian.wapp.navigator.role.model.RoleDTO;
import com.chujian.wapp.navigator.role.model.RoleResourceDTO;
import com.chujian.wapp.navigator.role.respository.MediaRepository;
import com.chujian.wapp.navigator.role.respository.MediaResourceRepository;
import com.chujian.wapp.navigator.role.respository.RoleGameRepository;
import com.chujian.wapp.navigator.role.respository.RoleMediaRepository;
import com.chujian.wapp.navigator.role.respository.RoleMediaResourceRepository;
import com.chujian.wapp.navigator.role.respository.RoleProductRepository;
import com.chujian.wapp.navigator.role.respository.RoleRepository;
import com.chujian.wapp.navigator.role.respository.RoleResourceRepository;
import com.chujian.wapp.navigator.role.respository.RoleTeamRepository;
import com.chujian.wapp.navigator.user.respository.UserRoleRepository;
import com.chujian.wapp.navigator.utils.DateUtils;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import javax.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class RoleService {

  @Autowired
  private RoleRepository roleRepository;

  @Autowired
  private ResourceRepository resourceRepository;

  @Autowired
  private RoleResourceRepository roleResourceRepository;

  @Autowired
  private GameRepository gameRepository;

  @Autowired
  private RoleGameRepository roleGameRepository;

  @Autowired
  private UserRoleRepository userRoleRepository;

  @Autowired
  private ResourceService resourceService;

  @Autowired
  private RoleTeamRepository roleTeamRepository;

  @Autowired
  private RoleMediaRepository roleMediaRepository;

  @Autowired
  private RoleMediaResourceRepository roleMediaResourceRepository;

  @Autowired
  private RoleProductRepository roleProductRepository;

  @Autowired
  private MediaResourceRepository mediaResourceRepository;

  @Autowired
  private MediaRepository mediaRepository;


  /**
   * 角色分页
   *
   * @param page
   * @param pageSize
   * @param roleName
   * @return
   */
  public JSONObject findRoleList(int page, int pageSize, String roleName) {
    JSONObject resultObj = new JSONObject();
    Pageable pageable = PageRequest.of(page, pageSize, Direction.DESC, "createdAt");
    Page<Role> pageResult = null;
    if (StringUtils.isBlank(roleName)) {
      pageResult = roleRepository.findAll(pageable);
    } else {
      pageResult = roleRepository.findByRoleNameLike("%" + roleName + "%", pageable);
    }
    long totalElements = pageResult.getTotalElements();
    int totalPages = pageResult.getTotalPages();
    int number = pageResult.getNumber();
    List<RoleDTO> roleList = new ArrayList<>();
    for (Role role : pageResult) {
      String newCreatedAt = convertDate(role.getCreatedAt().toString());
      String newUpdatedAt = convertDate(role.getUpdatedAt().toString());
      roleList.add(
          RoleDTO.builder().
              roleId(role.getRoleId()).
              roleName(role.getRoleName()).
              roleRemark(role.getRoleRemark()).
              createdAt(newCreatedAt).
              updatedAt(newUpdatedAt).build()
      );
    }
    resultObj.put("total_pages", totalPages);
    resultObj.put("current_page", number);
    resultObj.put("page_size", pageSize);
    resultObj.put("total_elements", totalElements);
    resultObj.put("role", roleList);

    return resultObj;
  }


  /**
   * 保存
   *
   * @param roleName
   * @param roleRemark
   */
  public void save(String roleName, String roleRemark) throws Exception {
    Role role = Role.builder().roleId(UUID.randomUUID().toString().replace("-", ""))
        .roleName(roleName).roleRemark(roleRemark).build();
    roleRepository.save(role);
  }

  /**
   * 根据roleid查询角色
   *
   * @param roleId
   * @return
   */
  public Role findByRoleId(String roleId) {
    return roleRepository.findByRoleId(roleId);
  }


  /**
   * 修改角色
   *
   * @param roleId
   * @param roleName
   * @param roleRemark
   * @throws Exception
   */
  public void updata(String roleId, String roleName, String roleRemark) throws Exception {
    roleRepository.updateRole(roleId, roleName, roleRemark);
  }

  /**
   * 根据roleId查询角色
   *
   * @param roleId
   * @return
   */
  public Role findRoleByRoleId(String roleId) {
    return roleRepository.findByRoleId(roleId);
  }

  /**
   * 删除角色
   *
   * @param roleIds
   */
  public void deleteRole(String[] roleIds) {
    for (int i = 0; i < roleIds.length; i++) {
      //删除角色的用户信息
      if (userRoleRepository.findByRoleId(roleIds[i]) != null) {
        userRoleRepository.deleteByRoleId(roleIds[i]);
      }
      //删除角色的资源信息
      if (roleResourceRepository.findByRoleId(roleIds[i]) != null) {
        roleResourceRepository.deleteByRoleId(roleIds[i]);
      }
      //删除角色的游戏信息
      if (roleGameRepository.findByRoleId(roleIds[i]) != null) {
        roleGameRepository.deleteByRoleId(roleIds[i]);
      }
      //删除角色的信息
      roleRepository.deleteByRoleId(roleIds[i]);
    }
  }

  /**
   * 时间转换方法
   *
   * @param dateStr
   * @return
   */
  private String convertDate(String dateStr) {
    String originalDateTimeFormat = "yyyy-MM-dd'T'HH:mm:ss.SSSZ";
    String convertedDateTimeFormat = "yyyy-MM-dd HH:mm:ss";
    return DateUtils.convertDate(dateStr, originalDateTimeFormat, convertedDateTimeFormat);
  }

  public List<RoleResourceDTO> resourceWithRoleId(String roleId) {
    //查询全部资源列表
    List<ResourceDTO> newList = resourceService.findAll();
    List<Resource> resourceList = resourceRepository.findAll();
    //查询角色所拥有的资源列表
    List<RoleResource> roleResourceList = roleResourceRepository.findByRoleId(roleId);
    //判断&&构造数据
    List<RoleResourceDTO> dtoList = new ArrayList<>();
    if (newList.size() > 0) {
      for (ResourceDTO resource : newList) {
        if (roleResourceList.size() > 0) {
          for (RoleResource resourceIn : roleResourceList) {
            if (resourceIn.getResourceId().equals(resource.getId())) {
              RoleResourceDTO roleResourceDTO = RoleResourceDTO.builder().
                  id(resource.getId()).
                  resourceId(resource.getResourceId()).
                  resourceName(resource.getResourceName()).
                  parentId(resource.getParentId()).
                  iconSkin(resource.getIconSkin()).
                  checked(true).build();
              dtoList.add(roleResourceDTO);
            }
          }
          RoleResourceDTO roleResourceDTO = RoleResourceDTO.builder().
              id(resource.getId()).
              resourceId(resource.getResourceId()).
              resourceName(resource.getResourceName()).
              parentId(resource.getParentId()).
              iconSkin(resource.getIconSkin()).
              checked(false).build();
          if (dtoList.size() > 0) {
            if (!dtoList.get(dtoList.size() - 1).getId().equals(roleResourceDTO.getId())) {
              dtoList.add(roleResourceDTO);
            }
          } else {
            dtoList.add(roleResourceDTO);
          }
        } else {
          RoleResourceDTO roleResourceDTO = RoleResourceDTO.builder().
              id(resource.getId()).
              resourceId(resource.getResourceId()).
              resourceName(resource.getResourceName()).
              parentId(resource.getParentId()).
              iconSkin(resource.getIconSkin()).
              checked(false).build();
          dtoList.add(roleResourceDTO);
        }
      }
      return dtoList;
    }
    return null;
  }

  public void changeRoleResource(Long[] resourceIdArray, String roleId) {
    if (resourceIdArray == null) {
      //删除之前的权限
      List<RoleResource> roleResourceList = roleResourceRepository.findByRoleId(roleId);
      if (roleResourceList.size() > 0) {
        for (RoleResource roleResource : roleResourceList) {
          roleResourceRepository.deleteByRoleId(roleResource.getRoleId());
        }
      }
      return;
    }
    //删除之前的权限
    List<RoleResource> roleResourceList = roleResourceRepository.findByRoleId(roleId);
    if (roleResourceList.size() > 0) {
      for (RoleResource roleResource : roleResourceList) {
        roleResourceRepository.deleteByRoleId(roleResource.getRoleId());
      }
    }
    //添加新修改的权限
    for (int i = 0; i < resourceIdArray.length; i++) {
      RoleResource roleResource = RoleResource.builder().roleId(roleId)
          .resourceId(resourceIdArray[i]).build();
      roleResourceRepository.save(roleResource);
    }
  }

  public void changeRoleGame(String[] gameIds, String roleId) {
    List<Game> gamesByRoleId = gameRepository.findGamesByRoleId(roleId);
    if (gamesByRoleId.size() != 0) {
      //先删除角色之前所拥有的游戏
      roleGameRepository.deleteByRoleId(roleId);
    }
    //给角色绑定新的角色信息
    //遍历游戏数组,为角色添加游戏
    if (gameIds != null) {
      for (String gameId : gameIds) {
        RoleGame roleGame = RoleGame.builder().gameId(gameId).roleId(roleId).build();
        roleGameRepository.save(roleGame);
      }
    }
  }

  @Transactional
  public void changeRoleData(String[] teamIds, String[] productIds, String[] mediaIds,
      String[] mediaresourceIds,
      String roleId) {
    //查询角色之前拥有的团队列表并删除
    List<RoleTeam> roleTeamList = roleTeamRepository.findByRoleId(roleId);
    if (!roleTeamList.isEmpty()) {
      roleTeamRepository.deleteByRoleId(roleId);
    }
    if (teamIds != null) {
      for (String teamId : teamIds) {
        if (StringUtils.isBlank(teamId)) {
          continue;
        }
        roleTeamRepository
            .save(RoleTeam.builder().teamId(Integer.parseInt(teamId)).roleId(roleId).build());
      }
    }
    //查询角色之前拥有的产品列表并删除
    List<RoleProduct> roleProductList = roleProductRepository.findByRoleId(roleId);
    if (!roleProductList.isEmpty()) {
      roleProductRepository.deleteByRoleId(roleId);
    }
    if (productIds != null) {
      for (String productId : productIds) {
        if (StringUtils.isBlank(productId)) {
          continue;
        }
        roleProductRepository
            .save(RoleProduct.builder().productId(Integer.parseInt(productId)).roleId(roleId)
                .build());
      }
    }
    //查询角色之前拥有的媒体列表并删除
    List<RoleMedia> roleMediaList = roleMediaRepository.findByRoleId(roleId);
    if (!roleMediaList.isEmpty()) {
      roleMediaRepository.deleteByRoleId(roleId);
    }
    if (mediaIds != null) {
      for (String mediaId : mediaIds) {
        if (StringUtils.isBlank(mediaId)) {
          continue;
        }
        roleMediaRepository
            .save(RoleMedia.builder().mediaId(Integer.parseInt(mediaId)).roleId(roleId).build());
      }
    }
    //查询角色之前拥有的资源列表并删除
    List<RoleMediaResource> roleMediaResourcesList = roleMediaResourceRepository
        .findByRoleId(roleId);
    if (!roleMediaResourcesList.isEmpty()) {
      roleMediaResourceRepository.deleteByRoleId(roleId);
    }
    if (mediaresourceIds != null) {
      for (String mediaresourceId : mediaresourceIds) {
        if (StringUtils.isBlank(mediaresourceId)) {
          continue;
        }
        roleMediaResourceRepository
            .save(RoleMediaResource.builder().mediaResourceId(Integer.parseInt(mediaresourceId))
                .roleId(roleId).build());
      }
    }
  }

  public void getMediaResource(String roleId, String mediaId) {
    List<MediaResource> mediaResourceList = mediaResourceRepository.findByIsDel(1);
    List<MediaResource> mediaResourceDtoList = new ArrayList<>();
    if (mediaResourceList == null || mediaResourceList.isEmpty()) {
      return;
    }
    for (MediaResource mediaResource : mediaResourceList) {
      if (mediaResource.getMedia().getId().equals(Integer.valueOf(mediaId))) {
        mediaResourceDtoList.add(mediaResource);
      }
    }

    List<RoleMediaResource> roleMediaResourceList = roleMediaResourceRepository
        .findByRoleId(roleId);
    List<Integer> roleMediaResourceDtoList = new ArrayList<>();
    if (roleMediaResourceList == null || roleMediaResourceList.isEmpty()) {
      return;
    }
    if (mediaResourceDtoList.isEmpty()) {
      return;
    }
    for (MediaResource mediaResource : mediaResourceDtoList) {
      for (RoleMediaResource roleMediaResource : roleMediaResourceList) {
        if (roleMediaResource.getMediaResourceId() == mediaResource.getId()) {
          roleMediaResourceDtoList.add(mediaResource.getId());
        }
      }
    }
    //mediaResourceDtoList为当前媒体下的资源
    //roleMediaResourceDtoList为当前媒体下用户所拥有的资源
  }

  public List<MediaAndResourceDTO> showRoleDataTree(String roleId) {
    List<MediaAndResourceDTO> list = new ArrayList<>();
    //查出所有的媒体
    List<Media> mediaList = mediaRepository.findByIsDel(1);
    //查出所有资源
    List<MediaResource> mediaResourceList = mediaResourceRepository.findByIsDel(1);
    //查出角色所拥有的媒体
    List<RoleMedia> roleMediaList = roleMediaRepository.findByRoleId(roleId);
    //查出所有的用户拥有的媒体资源
    List<RoleMediaResource> roleMediaResourceList = roleMediaResourceRepository
        .findByRoleId(roleId);

    if (mediaList == null || mediaList.isEmpty()) {
      return list;
    }
    for (Media media : mediaList) {
      if (roleMediaList == null || roleMediaList.isEmpty()) {//角色所拥有的媒体为空
        if (mediaResourceList == null || mediaResourceList.isEmpty()) {//资源为空
          list.add(MediaAndResourceDTO.builder().id(media.getId()).pid(0).name(media.getName())
              .checked(false).build());
        } else {//资源不为空
          list.add(MediaAndResourceDTO.builder().id(media.getId()).pid(0).name(media.getName())
              .checked(false).build());
          //找出当前媒体下的资源
          for (MediaResource mediaResource : mediaResourceList) {
            if (media.getId().equals(mediaResource.getMedia().getId())) {
              list.add(MediaAndResourceDTO.builder().id(mediaResource.getId()).pid(media.getId())
                  .name(mediaResource.getResourceName()).checked(false).build());
            }
          }
        }
      } else {//角色所拥有的媒体不为空
        if (mediaResourceList == null || mediaResourceList.isEmpty()) {//资源为空
          for (RoleMedia roleMedia : roleMediaList) {
            if (media.getId() == roleMedia.getMediaId()) {
              list.add(MediaAndResourceDTO.builder().id(media.getId()).pid(0).name(media.getName())
                  .checked(true).build());
            } else {
              list.add(MediaAndResourceDTO.builder().id(media.getId()).pid(0).name(media.getName())
                  .checked(false).build());
            }
          }
        } else {//资源不为空
          for (RoleMedia roleMedia : roleMediaList) {
            if (media.getId() == roleMedia.getMediaId()) {
              list.add(MediaAndResourceDTO.builder().id(media.getId()).pid(0).name(media.getName())
                  .checked(true).build());
            } else {
              list.add(MediaAndResourceDTO.builder().id(media.getId()).pid(0).name(media.getName())
                  .checked(false).build());
            }
          }
          if (roleMediaResourceList == null || roleMediaResourceList.isEmpty()) {//角色拥有的资源为空
            for (MediaResource mediaResource : mediaResourceList) {
              if (media.getId().equals(mediaResource.getMedia().getId())) {
                list.add(MediaAndResourceDTO.builder().id(mediaResource.getId()).pid(media.getId())
                    .name(mediaResource.getResourceName()).checked(false).build());
              }
            }
          } else {//角色拥有的资源不为空
            for (MediaResource mediaResource : mediaResourceList) {
              for (RoleMediaResource roleMediaResource : roleMediaResourceList) {
                if (mediaResource.getId() == roleMediaResource.getMediaResourceId() && mediaResource.getMedia().getId().equals(media.getId())){
                  list.add(MediaAndResourceDTO.builder().id(mediaResource.getId()).pid(mediaResource.getMedia().getId())
                      .name(mediaResource.getResourceName()).checked(true).build());
                }else if (mediaResource.getId() != roleMediaResource.getMediaResourceId() && mediaResource.getMedia().getId().equals(media.getId())){
                  list.add(MediaAndResourceDTO.builder().id(mediaResource.getId()).pid(mediaResource.getMedia().getId())
                      .name(mediaResource.getResourceName()).checked(false).build());
                } else {
                  continue;
                }
              }
            }
          }
        }
      }
    }
    return list;
  }
}

package com.chujian.wapp.navigator.role.service;


import com.chujian.wapp.navigator.game.entity.Game;
import com.chujian.wapp.navigator.game.model.GameDTO;
import com.chujian.wapp.navigator.game.respository.GameRepository;
import com.chujian.wapp.navigator.role.entity.AdBaseProduct;
import com.chujian.wapp.navigator.role.entity.AdBaseTeam;
import com.chujian.wapp.navigator.role.entity.Media;
import com.chujian.wapp.navigator.role.entity.MediaResource;
import com.chujian.wapp.navigator.role.entity.Role;
import com.chujian.wapp.navigator.role.entity.RoleGame;
import com.chujian.wapp.navigator.role.entity.RoleMedia;
import com.chujian.wapp.navigator.role.entity.RoleMediaResource;
import com.chujian.wapp.navigator.role.entity.RoleProduct;
import com.chujian.wapp.navigator.role.entity.RoleTeam;
import com.chujian.wapp.navigator.role.model.AdBaseProductDTO;
import com.chujian.wapp.navigator.role.model.AdBaseTeamDTO;
import com.chujian.wapp.navigator.role.model.MediaDto;
import com.chujian.wapp.navigator.role.model.MediaResourceDTO;
import com.chujian.wapp.navigator.role.respository.AdProductRepository;
import com.chujian.wapp.navigator.role.respository.MediaRepository;
import com.chujian.wapp.navigator.role.respository.MediaResourceRepository;
import com.chujian.wapp.navigator.role.respository.RoleGameRepository;
import com.chujian.wapp.navigator.role.respository.RoleMediaRepository;
import com.chujian.wapp.navigator.role.respository.RoleMediaResourceRepository;
import com.chujian.wapp.navigator.role.respository.RoleProductRepository;
import com.chujian.wapp.navigator.role.respository.RoleRepository;
import com.chujian.wapp.navigator.role.respository.RoleTeamRepository;
import com.chujian.wapp.navigator.role.respository.TeamRepository;
import java.util.ArrayList;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RoleRequestService {

  @Autowired
  private RoleRepository roleRepository;
  @Autowired
  private GameRepository gameRepository;
  @Autowired
  private RoleGameRepository roleGameRepository;

  @Autowired
  private MediaRepository mediaRepository;
  @Autowired
  private MediaResourceRepository mediaResourceRepository;
  @Autowired
  private AdProductRepository adProductRepository;
  @Autowired
  private TeamRepository teamRepository;

  @Autowired
  private RoleTeamRepository roleTeamRepository;
  @Autowired
  private RoleMediaRepository roleMediaRepository;
  @Autowired
  private RoleMediaResourceRepository roleMediaResourceRepository;
  @Autowired
  private RoleProductRepository roleProductRepository;

  public void putRoleToRequest(HttpServletRequest request, String roleId) {

    Role role = roleRepository.findByRoleId(roleId);
    request.setAttribute("role", role);

    //查询当前角色所拥有的游戏,做数据回显
//    List<Game> roleGameList = gameRepository.findGamesByRoleId(roleId);
    List<RoleGame> roleGameList = roleGameRepository.findByRoleId(roleId);
    List<String> roleGameDtoList = new ArrayList<>();
    if (roleGameList == null || roleGameList.isEmpty()){
      request.setAttribute("roleGameStr", roleGameDtoList.toString());
    }else {
      for (RoleGame roleGame : roleGameList) {
        roleGameDtoList.add(roleGame.getGameId());
      }
      String roleGameStr = roleGameDtoList.toString();
      request.setAttribute("roleGameStr", roleGameStr);
    }

    //查询所有游戏信息
    List<Game> gameList = gameRepository.findByIsDel(1);
    if (gameList == null || gameList.isEmpty()){

      request.setAttribute("gameList", gameList);
      return;
    }
    List<GameDTO> gameDtoList = new ArrayList<>();
    for (Game game : gameList) {
      gameDtoList.add(GameDTO.builder().gameId(game.getId().toString()).gameName(game.getName()).build());
    }
    request.setAttribute("gameList", gameDtoList);

  }


  public void putRoleDataToRequest(HttpServletRequest request, String roleId) {
    Role role = roleRepository.findByRoleId(roleId);
    request.setAttribute("role", role);

    //查询出所有的团队
    List<AdBaseTeam> teamList = teamRepository.findByStatus(1);
    List<AdBaseTeamDTO> teamDtoList = new ArrayList<>();
    if (teamList == null || teamList.isEmpty()) {
      request.setAttribute("teamList", teamDtoList);
    } else {
      for (AdBaseTeam adBaseTeam : teamList) {
        teamDtoList.add(AdBaseTeamDTO.builder().id(adBaseTeam.getId().toString())
            .name(adBaseTeam.getName()).build());
      }
      request.setAttribute("teamList", teamDtoList);
    }
    //查询角色所拥有的团队
    List<RoleTeam> roleTeamList = roleTeamRepository.findByRoleId(roleId);
    List<Integer> roleTeamDtoList = new ArrayList<>();
    if (roleTeamList != null) {
      for (RoleTeam roleTeam : roleTeamList) {
        roleTeamDtoList.add(roleTeam.getTeamId());
      }
    }
    String roleTeamStr = roleTeamDtoList.toString();
    request.setAttribute("roleTeamStr", roleTeamStr);

    //查询出所有的产品
    List<AdBaseProduct> productList = adProductRepository.findByStatus(1);
    List<AdBaseProductDTO> productDtoList = new ArrayList<>();
    if (productList == null || productList.isEmpty()) {
      request.setAttribute("productList", productDtoList);
    } else {
      for (AdBaseProduct adBaseProduct : productList) {
        productDtoList.add(
            AdBaseProductDTO.builder().id(adBaseProduct.getId().toString())
                .name(adBaseProduct.getName()).build());
      }
      request.setAttribute("productList", productDtoList);
    }
    //查询角色所拥有的产品
    List<RoleProduct> roleProductList = roleProductRepository.findByRoleId(roleId);
    List<Integer> roleProductDtoList = new ArrayList<>();
    if (roleProductList!=null){
      for (RoleProduct roleProduct : roleProductList) {
        roleProductDtoList.add(roleProduct.getProductId());
      }
    }
    String roleProductStr = roleProductDtoList.toString();
    request.setAttribute("roleProductStr", roleProductStr);

    //查出所有的媒体
    List<Media> mediaList = mediaRepository.findByIsDel(1);
    List<MediaDto> mediaDtoList = new ArrayList<>();
    if (mediaList == null || mediaList.isEmpty()) {
      request.setAttribute("mediaList", mediaDtoList);
    } else {
      for (Media media : mediaList) {
        mediaDtoList.add(
            MediaDto.builder().id(media.getId().toString())
                .name(media.getName()).build());
      }
      request.setAttribute("mediaList", mediaDtoList);
    }
    //查出角色所拥有的媒体
    List<RoleMedia> roleMediaList = roleMediaRepository.findByRoleId(roleId);
    List<Integer> roleMediaDtoList = new ArrayList<>();
    if (roleProductList!=null){
      for (RoleMedia roleMedia : roleMediaList) {
        roleMediaDtoList.add(roleMedia.getMediaId());
      }
    }
    String roleMediaStr = roleMediaDtoList.toString();
    request.setAttribute("roleMediaStr", roleMediaStr);

    //查询出所有的媒体资源
    List<MediaResource> mediaResourceList = mediaResourceRepository.findByIsDel(1);
    List<MediaResourceDTO> mediaResourceDtoList = new ArrayList<>();
    if (mediaResourceList == null || mediaResourceList.isEmpty()) {
      request.setAttribute("mediaResourceList", productDtoList);
    } else {
      for (MediaResource mediaResource : mediaResourceList) {
        mediaResourceDtoList.add(
            MediaResourceDTO.builder().id(mediaResource.getId().toString())
                .resourceName(mediaResource.getResourceName()).build());
      }
      request.setAttribute("mediaResourceList", mediaResourceDtoList);
    }
    //查询角色所拥有的媒体资源
    List<RoleMediaResource> roleMediaResourceList = roleMediaResourceRepository
        .findByRoleId(roleId);
    List<Integer> roleMediaResourceDtoList = new ArrayList<>();
    if (roleMediaResourceList!=null){
      for (RoleMediaResource roleMediaResource : roleMediaResourceList) {
        roleMediaResourceDtoList.add(roleMediaResource.getMediaResourceId());
      }
    }
    String roleMediaResourceStr = roleMediaResourceDtoList.toString();
    request.setAttribute("roleMediaResourceStr", roleMediaResourceStr);


  }
}

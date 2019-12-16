package com.chujian.wapp.navigator.resource.controller;

import com.alibaba.fastjson.JSONObject;
import com.chujian.wapp.navigator.resource.entity.Resource;
import com.chujian.wapp.navigator.resource.model.ResourceDTO;
import com.chujian.wapp.navigator.resource.model.ResourceTypeConfig;
import com.chujian.wapp.navigator.resource.service.ResourceService;
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
public class ResourceRestController {

  @Autowired
  private ResourceService resourceService;

  @Autowired
  private SysOpLogService opLogService;

  //展示资源菜单树
  @GetMapping(value = "/resource")
  public List<ResourceDTO> resourceTree() {
    List<ResourceDTO> list = null;
    try {
      list = resourceService.findAll();
    } catch (Exception e) {
      log.error("show resourceTree failed :" + e);
    }
    return list;
  }

  @PostMapping(value = "save_resource", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
  public ResponseEntity saveResource(
      @RequestParam("id") Long id,
      @RequestParam("resource_name") String resourceName,
      @RequestParam("resource_id") String resourceId,
      @RequestParam("resource_url") String resourceUrl,
      @RequestParam("resource_parent_id") Long parentId,
      @RequestParam("resource_type") String resourceType,
      @RequestParam("resource_orderNum") Long resourceOrderNum,
      HttpServletRequest request) {
    JSONObject jsonObject = new JSONObject();

    try {
      if (id == null) {//新增
        List<Resource> resourceList = resourceService.findAllWithParentId(parentId);//所在系统的全部资源集合
        List<Resource> rootList = resourceService
            .findResourceByType(ResourceTypeConfig.RS_TYPE_SYSTEM);//系统集合
        if (ResourceTypeConfig.RS_TYPE_SYSTEM.equals(resourceType)) {//新增系统: 确保系统集合中不重复
          if (!checkSystemId(rootList, resourceId) && !checkResourceId(resourceList,
              resourceId)) {//如果不重复,
            //执行添加
            Long idKey = resourceService
                .addResource(id, resourceId, resourceName, resourceUrl, resourceType, parentId,
                    resourceOrderNum);
            opLogService
                .saveOpLog(request, "resource", "add", "resource add", SysOpLog.OP_RESULT_OK);
            jsonObject.put("status", "ok");
            jsonObject.put("id", idKey);
          } else {//如果重复
            jsonObject.put("status", "addIdFail");
          }
        } else {//新增 链接  按钮  菜单
          if (!checkResourceId(resourceList, resourceId)) {//如果不重复,
            //执行添加
            Long idKey = resourceService
                .addResource(id, resourceId, resourceName, resourceUrl, resourceType, parentId,
                    resourceOrderNum);
            opLogService
                .saveOpLog(request, "resource", "add", "resource add", SysOpLog.OP_RESULT_OK);
            jsonObject.put("status", "ok");
            jsonObject.put("id", idKey);
          } else {//如果重复
            jsonObject.put("status", "addRIdFail");
          }
        }
      } else {//修改
        //执行修改操作
        resourceService.updateResource(id, resourceId, resourceName, resourceUrl, resourceType,
            parentId, resourceOrderNum);
        opLogService
            .saveOpLog(request, "resource", "edit", "resource edit", SysOpLog.OP_RESULT_OK);
        jsonObject.put("status", "ok");
      }
    } catch (Exception e) {
      jsonObject.put("status", "addFail");
      log.error("Save resource failed: ", e);
    }
    return ResponseEntity.ok(jsonObject);
  }

  private boolean checkResourceId(List<Resource> resourceList, String resourceId) {
    if (resourceList.isEmpty()) {
      return false;
    }
    for (Resource resource : resourceList) {
      if (resource.getResourceId().equals(resourceId)) {
        return true;
      }
    }
    return false;
  }

  private boolean checkSystemId(List<Resource> rootList, String resourceId) {
    if (rootList == null || rootList.isEmpty()) {
      return false;
    }
    for (Resource resource : rootList) {
      if (resource.getResourceId().equals(resourceId)) {
        return true;
      }
    }
    return false;
  }


  /**
   * 修改资源菜单时回显
   */
  @GetMapping(value = "/edit_resource", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
  public ResponseEntity editResource(@RequestParam(value = "id") Long id,
      HttpServletRequest request) {
    Resource resource = null;
    try {
      resource = resourceService.findById(id);
      request.setAttribute("typeName", resource.getResourceType());
    } catch (Exception ex) {
      log.error("Edit resource failed:", ex);
    }
    return ResponseEntity.ok(resource);
  }

  /**
   * 删除资源菜单
   */
  @PostMapping(value = "/delete_resource")
  public ResponseEntity deleteResource(HttpServletRequest request,
      @RequestParam(value = "id") Long id) {
    JSONObject jsonObject = new JSONObject();
    try {
      resourceService.deleteById(id);
      opLogService.saveOpLog(request, "resource", "del", "resource del", SysOpLog.OP_RESULT_OK);
      jsonObject.put("status", "ok");
    } catch (Exception e) {
      jsonObject.put("status", "fail");
      log.error("Delete resource failed:", e);
    }
    return ResponseEntity.ok(jsonObject);
  }


  @PostMapping(value = "/copy_resource")
  public ResponseEntity copyResource(HttpServletRequest request,
      @RequestParam(value = "copyId") Long copyId,
      @RequestParam(value = "targetId") Long targetId,
      @RequestParam(value = "changeResourceId") String resourceId,
      @RequestParam(value = "changeResourceUrl", required = false) String resourceUrl,
      @RequestParam(value = "changeResourceName", required = false) String resourceName
  ) {
    JSONObject jsonObject = new JSONObject();
    try {
      if (!resourceService.findById(copyId).getResourceType()
          .equals(ResourceTypeConfig.RS_TYPE_SYSTEM)) {
        jsonObject.put("status", "typeFail");
        return ResponseEntity.ok(jsonObject);
      }
      if (targetId != 0 && resourceService.findById(targetId).getParentId() != 0) {
        jsonObject.put("status", "targetTypeFail");
        return ResponseEntity.ok(jsonObject);
      }
      List<Resource> systemList = resourceService
          .findResourceByType(ResourceTypeConfig.RS_TYPE_SYSTEM);
      if (!systemList.isEmpty()) {
        for (Resource resource : systemList) {
          if (resource.getResourceId().equals(resourceId)) {
            jsonObject.put("status", "idFail");
            return ResponseEntity.ok(jsonObject);
          }
        }
      }
      resourceService.copyResource(copyId, targetId, resourceId, resourceUrl, resourceName);
      opLogService.saveOpLog(request, "resource", "copy", "resource copy", SysOpLog.OP_RESULT_OK);
      jsonObject.put("status", "ok");
    } catch (Exception e) {
      jsonObject.put("status", "fail");
      log.error("Copy resource failed:", e);
    }
    return ResponseEntity.ok(jsonObject);
  }
}

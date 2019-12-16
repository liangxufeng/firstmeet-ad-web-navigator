package com.chujian.wapp.navigator.dept.controller;

import com.alibaba.fastjson.JSONObject;
import com.chujian.wapp.navigator.dept.entity.Dept;
import com.chujian.wapp.navigator.dept.service.DeptService;
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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
public class DeptRestController {

  @Autowired
  private DeptService deptService;

  @Autowired
  private SysOpLogService opLogService;

  //展示部门菜单树
  @GetMapping(value = "/dept")
  public List<Dept> deptTree() {
    List<Dept> list = null;
    try {
      list = deptService.findAll();
    } catch (Exception e) {
      log.error("show deptTree Failed :" + e);
    }
    return list;
  }

  /**
   * 添加或修改部门
   */
  @PostMapping(value = "save_dept", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
  public ResponseEntity saveDept(
      @RequestParam("id") Long id,
      @RequestParam("dept_name") String deptName,
      @RequestParam("dept_id") String deptId,
      @RequestParam("dept_parent_id") String deptParentId,
      @RequestParam("dept_orderNum") String deptOrderNum,
      HttpServletRequest request) {
    JSONObject jsonObject = new JSONObject();
    try {
      //根据id判断执行修改还是添加
      if (id == null) {//执行添加
        //判断deptId的唯一性
        Dept deptByDeptId = deptService.findDeptByDeptId(deptId);
        if (deptByDeptId != null) {
          jsonObject.put("status", "addIdFail");
        } else {
          //执行添加
          deptService.addDept(id, deptId, deptName, deptParentId, deptOrderNum);
          opLogService.saveOpLog(request, "dept", "add", "dept add", SysOpLog.OP_RESULT_OK);
          jsonObject.put("status", "ok");
        }
      } else {
        //执行修改操作
        deptService.updateDept(id, deptId, deptName, deptParentId, deptOrderNum);
        opLogService.saveOpLog(request, "dept", "edit", "dept edit", SysOpLog.OP_RESULT_OK);
        jsonObject.put("status", "ok");
      }
    } catch (Exception e) {
      jsonObject.put("status", "fail");
      log.error("Save resource failed:", e);
    }
    return ResponseEntity.ok(jsonObject);
  }

  /**
   * 修改部门菜单时回显
   */
  @RequestMapping(value = "/edit_dept")
  public ResponseEntity editDept(@RequestParam(value = "dept_id", required = false) String deptId,
      HttpServletRequest request) {
    Dept dept = null;
    try {
      dept = deptService.findDeptByDeptId(deptId);
    } catch (Exception ex) {
      log.error("Edit dept failed:", ex);
    }
    return ResponseEntity.ok(dept);
  }

  /**
   * 删除部门菜单
   */
  @PostMapping(value = "/delete_dept")
  public ResponseEntity deleteDept(HttpServletRequest request,
      @RequestParam(value = "dept_id", required = false) String deptId) {
    JSONObject jsonObject = new JSONObject();
    try {
      deptService.deleteDeptByDeptId(deptId);
      opLogService.saveOpLog(request, "dept", "del", "dept del", SysOpLog.OP_RESULT_OK);
      jsonObject.put("status", "ok");
    } catch (Exception e) {
      jsonObject.put("status", "fail");
      log.error("Delete dept failed:", e);
    }
    return ResponseEntity.ok(jsonObject);
  }
}

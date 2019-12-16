package com.chujian.wapp.navigator.dept.service;

import com.chujian.wapp.navigator.dept.entity.Dept;
import com.chujian.wapp.navigator.dept.respository.DeptRepository;
import com.chujian.wapp.navigator.user.respository.UserDeptRepository;
import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DeptService {

  @Autowired
  private DeptRepository deptRepository;

  @Autowired
  private UserDeptRepository userDeptRepository;

  public void deleteDeptByDeptId(String deptId) {
    //删除用户部门信息
    if (userDeptRepository.findByDeptId(deptId) != null) {
      userDeptRepository.deleteByDeptId(deptId);
    }
    //查出deptid对应的子系统
    //进行递归删除
    List<Dept> list = deptRepository.findSubListByDeptId(deptId);
    if (list.size() > 0) {
      for (Dept dept : list) {
        if (deptRepository.findSubListByDeptId(dept.getDeptId()) != null) {
          if (userDeptRepository.findByDeptId(deptId) != null) {
            userDeptRepository.deleteByDeptId(deptId);
          }
          deleteDeptByDeptId(dept.getDeptId());
        } else {
          if (userDeptRepository.findByDeptId(deptId) != null) {
            userDeptRepository.deleteByDeptId(deptId);
          }
          deptRepository.deleteByDeptId(dept.getDeptId());
        }
      }
    }
    deptRepository.deleteByDeptId(deptId);
  }

  public Dept findDeptByDeptId(String deptId) {
    Dept dept = deptRepository.findByDeptId(deptId);
    return dept;
  }

  public void addDept(Long id, String deptId, String deptName, String deptParentId,
      String deptOrderNum) {
    if ("".equals(deptParentId)) {
      deptParentId = "-1";
    }

    int num = Integer.parseInt(deptOrderNum);
    Dept dept = Dept.builder().
        deptId(deptId).
        deptName(deptName).
        deptOrderNum(num).
        deptParentId(deptParentId).build();
    deptRepository.save(dept);
  }

  public void updateDept(Long id, String deptId, String deptName, String deptParentId,
      String deptOrderNum) {

    int num = Integer.parseInt(deptOrderNum);
    Dept dept = Dept.builder().
        id(id).
        deptId(deptId).
        deptName(deptName).
        deptOrderNum(num).
        deptParentId(deptParentId).build();
    deptRepository.saveAndFlush(dept);
  }

  public List<Dept> findAll() {
    return deptRepository.findAllByOrderByDeptOrderNumAsc();
  }


  public Dept findById(Long id) {
    return deptRepository.getOne(id);
  }

  public List<Dept> findSubDeptList(String deptId) {
    List<Dept> list = new ArrayList<>();
    List<Dept> subDeptList = findChild(deptId, list);
    return subDeptList;
  }

  private List<Dept> findChild(String deptId, List<Dept> list) {
    List<Dept> subList = deptRepository.findSubListByDeptId(deptId);
    if (subList == null || subList.isEmpty()) {
      return list;
    }
    for (Dept dept : subList) {
      list.add(dept);
      if (deptRepository.findSubListByDeptId(dept.getDeptId()) != null) {
        findChild(dept.getDeptId(), list);
      }
    }
    return list;
  }
}
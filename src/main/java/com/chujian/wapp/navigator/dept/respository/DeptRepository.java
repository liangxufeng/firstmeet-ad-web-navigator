package com.chujian.wapp.navigator.dept.respository;

import com.chujian.wapp.navigator.dept.entity.Dept;
import java.util.List;
import javax.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface DeptRepository extends JpaRepository<Dept, Long> {

  @Query(value = "select r from Dept r where r.deptParentId = :deptId")
  List<Dept> findSubListByDeptId(@Param("deptId") String deptId);

  @Modifying
  @Transactional
  @Query(value = "delete from Dept r where r.deptId = :deptId")
  void deleteByDeptId(@Param("deptId") String deptId);

  Dept findByDeptId(String deptId);

  List<Dept> findAllByOrderByDeptOrderNumAsc();

  @Query(value = "select d from Dept d where d.deptParentId = '0'")
  Dept findAdminDept();
}

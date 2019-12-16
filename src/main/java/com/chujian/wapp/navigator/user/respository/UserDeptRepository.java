package com.chujian.wapp.navigator.user.respository;

import com.chujian.wapp.navigator.user.entity.UserDept;
import java.util.List;
import javax.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface UserDeptRepository extends JpaRepository<UserDept, Long> {

  @Transactional
  @Modifying
  @Query(value = "update UserDept  ud set ud.deptId = :userDept where ud.userId = :userId")
  void updateUserDept(@Param("userId") String userId, @Param("userDept") String userDept);

  UserDept findByUserId(String userId);

  @Transactional
  @Modifying
  @Query(value = "delete from UserDept ud where ud.userId = :userId")
  void deleteByUserId(@Param("userId") String userId);

  List<UserDept> findByDeptId(String deptId);

  @Transactional
  @Modifying
  void deleteByDeptId(String deptId);
}

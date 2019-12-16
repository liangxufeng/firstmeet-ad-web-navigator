package com.chujian.wapp.navigator.user.respository;

import com.chujian.wapp.navigator.user.entity.UserRole;
import java.util.List;
import javax.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRoleRepository extends JpaRepository<UserRole, Long> {

  @Transactional
  @Modifying
  @Query(value = "delete from UserRole ur where ur.userId = :userId")
  void deleteByUserId(@Param(("userId")) String userId);

  List<UserRole> findByUserId(String userId);

  List<UserRole> findByRoleId(String roleId);

  @Transactional
  @Modifying
  void deleteByRoleId(String roleId);
}

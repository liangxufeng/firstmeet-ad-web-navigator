package com.chujian.wapp.navigator.role.respository;

import com.chujian.wapp.navigator.role.entity.Role;
import java.util.List;
import javax.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {

  @Transactional
  @Modifying
  @Query(value = "update Role u set u.roleName = :roleName,u.roleRemark = :roleRemark  where u.roleId = :roleId")
  void updateRole(@Param("roleId") String roleId, @Param("roleName") String roleName,
      @Param("roleRemark") String roleRemark);

  Role findByRoleId(String roleId);

  @Transactional
  @Modifying
  void deleteByRoleId(String roleId);

  @Query(value = "select r.* from sys_role r , sys_user_role u where r.role_id=u.role_id and u.user_id = :userId ", nativeQuery = true)
  List<Role> findRolesByUserId(@Param("userId") String userId);

  Page<Role> findByRoleNameLike(String s, Pageable pageable);
}

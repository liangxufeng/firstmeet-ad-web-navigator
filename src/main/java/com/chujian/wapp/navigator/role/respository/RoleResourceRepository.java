package com.chujian.wapp.navigator.role.respository;

import com.chujian.wapp.navigator.role.entity.RoleResource;
import java.util.List;
import javax.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Repository;

@Repository
public interface RoleResourceRepository extends JpaRepository<RoleResource, Long> {

  @Modifying
  @Transactional
  void deleteByResourceId(Long resourceId);

  List<RoleResource> findByRoleId(String roleId);

  @Modifying
  @Transactional
  void deleteByRoleId(String roleId);

  List<RoleResource> findByResourceId(Long resourceId);
}

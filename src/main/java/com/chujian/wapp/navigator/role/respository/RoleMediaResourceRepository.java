package com.chujian.wapp.navigator.role.respository;

import com.chujian.wapp.navigator.role.entity.RoleMediaResource;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RoleMediaResourceRepository extends JpaRepository<RoleMediaResource, Long> {

  List<RoleMediaResource> findByRoleId(String roleId);

  void deleteByRoleId(String roleId);
}

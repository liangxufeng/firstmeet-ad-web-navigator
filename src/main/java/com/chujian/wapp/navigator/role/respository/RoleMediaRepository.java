package com.chujian.wapp.navigator.role.respository;

import com.chujian.wapp.navigator.role.entity.RoleMedia;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RoleMediaRepository extends JpaRepository<RoleMedia, Long> {


  List<RoleMedia> findByRoleId(String roleId);

  void deleteByRoleId(String roleId);
}

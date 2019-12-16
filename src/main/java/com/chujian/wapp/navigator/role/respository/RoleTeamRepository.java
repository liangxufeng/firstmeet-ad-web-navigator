package com.chujian.wapp.navigator.role.respository;

import com.chujian.wapp.navigator.role.entity.RoleTeam;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RoleTeamRepository extends JpaRepository<RoleTeam, Long> {

  List<RoleTeam> findByRoleId(String roleId);

  void deleteByRoleId(String roleId);
}

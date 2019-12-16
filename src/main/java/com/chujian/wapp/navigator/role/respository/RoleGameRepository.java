package com.chujian.wapp.navigator.role.respository;

import com.chujian.wapp.navigator.role.entity.RoleGame;
import java.util.List;
import javax.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Repository;

@Repository
public interface RoleGameRepository extends JpaRepository<RoleGame, Long> {

  @Modifying
  @Transactional
  void deleteByRoleId(String roleId);

  @Modifying
  @Transactional
  void deleteByGameId(String gameId);

  List<RoleGame> findByRoleId(String roleId);

  List<RoleGame> findByGameId(String gameId);
}

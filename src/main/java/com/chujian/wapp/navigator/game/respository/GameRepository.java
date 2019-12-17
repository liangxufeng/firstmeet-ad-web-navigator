package com.chujian.wapp.navigator.game.respository;

import com.chujian.wapp.navigator.game.entity.Game;
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
public interface GameRepository extends JpaRepository<Game, Long> {

  Game findById(Integer gameId);

  @Transactional
  @Modifying
  void deleteById(Integer gameId);

  @Query(value = "SELECT r.* from sys_game r , sys_role_game u where r.game_id=u.game_id and u.role_id = :roleId ", nativeQuery = true)
  List<Game> findGamesByRoleId(@Param("roleId") String roleId);

  Page<Game> findByNameLike(String s, Pageable pageable);

  List<Game> findByIsDel(int i);

}

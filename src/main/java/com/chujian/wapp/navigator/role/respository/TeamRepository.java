package com.chujian.wapp.navigator.role.respository;

import com.chujian.wapp.navigator.role.entity.AdBaseTeam;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TeamRepository extends JpaRepository<AdBaseTeam, Long> {

  List<AdBaseTeam> findByStatus(int status);
}

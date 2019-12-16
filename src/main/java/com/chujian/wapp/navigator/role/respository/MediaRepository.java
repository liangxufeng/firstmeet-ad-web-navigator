package com.chujian.wapp.navigator.role.respository;

import com.chujian.wapp.navigator.role.entity.Media;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MediaRepository extends JpaRepository<Media, Integer> {

  List<Media> findByIsDel(Integer isDel);
}

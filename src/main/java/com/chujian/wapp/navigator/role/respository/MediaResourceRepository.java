package com.chujian.wapp.navigator.role.respository;

import com.chujian.wapp.navigator.role.entity.MediaResource;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MediaResourceRepository extends JpaRepository<MediaResource, Integer> {

  List<MediaResource> findByIsDel(int isDel);

}

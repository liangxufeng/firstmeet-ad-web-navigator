package com.chujian.wapp.navigator.resource.respository;

import com.chujian.wapp.navigator.resource.entity.Resource;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ResourceRepository extends JpaRepository<Resource, Long> {

  @Query(value = "select r from Resource r where r.parentId = :id")
  List<Resource> findSubListByResourceId(@Param("id") Long id);

  Resource findByResourceId(String resourceId);

  Resource findByResourceName(String resourceName);

  List<Resource> findAllByOrderByResourceOrderNumAsc();

  @Query(value = "select r from Resource r where r.id = :id")
  Resource findByIdKey(@Param("id") Long id);

  @Query(value = "select r from Resource r where r.parentId = :parentId")
  List<Resource> findByParentId(@Param("parentId") Long parentId);

  @Query(value = "select r from Resource r where r.resourceType = :system")
  List<Resource> findResourceByType(@Param("system") String system);
}

package com.chujian.wapp.navigator.role.respository;

import com.chujian.wapp.navigator.role.entity.RoleProduct;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RoleProductRepository extends JpaRepository<RoleProduct, Long> {

  List<RoleProduct> findByRoleId(String roleId);

  void deleteByRoleId(String roleId);
}

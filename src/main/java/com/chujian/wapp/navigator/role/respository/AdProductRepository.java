package com.chujian.wapp.navigator.role.respository;

import com.chujian.wapp.navigator.role.entity.AdBaseProduct;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AdProductRepository extends JpaRepository<AdBaseProduct, Long> {

  List<AdBaseProduct> findByStatus(int status);
}

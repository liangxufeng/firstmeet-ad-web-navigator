package com.chujian.wapp.navigator.user.respository;

import com.chujian.wapp.navigator.user.entity.User;
import javax.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

  User findByUserId(String userId);

  @Transactional
  @Modifying
  @Query(value = "update User u set u.userName = :userName,u.userPassword = :md5Password where u.userId = :userId")
  void updateUser(@Param("userId") String userId, @Param("userName") String userName,
      @Param("md5Password") String md5Password);

  @Transactional
  @Modifying
  void deleteByUserId(String userId);

  Page<User> findByUserNameLike(String s, Pageable pageable);
}

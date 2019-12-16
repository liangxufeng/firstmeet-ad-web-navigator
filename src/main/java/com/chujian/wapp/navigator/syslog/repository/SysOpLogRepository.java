package com.chujian.wapp.navigator.syslog.repository;

import com.chujian.wapp.navigator.syslog.entity.SysOpLog;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SysOpLogRepository extends JpaRepository<SysOpLog, Long> {

  List<SysOpLog> findByOpUser(String opUser);

  List<SysOpLog> findByOpModule(String opModule);

  List<SysOpLog> findByOpType(String opType);
}

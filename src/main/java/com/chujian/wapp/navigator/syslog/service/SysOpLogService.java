package com.chujian.wapp.navigator.syslog.service;

import com.chujian.wapp.navigator.sso.service.AccessTokenService;
import com.chujian.wapp.navigator.syslog.entity.SysOpLog;
import com.chujian.wapp.navigator.syslog.repository.SysOpLogRepository;
import com.chujian.wapp.navigator.utils.RequestUtils;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class SysOpLogService {

  @Resource
  private SysOpLogRepository sysOpLogRepository;

  @Resource
  private AccessTokenService accessTokenService;

  public void saveOpLog(HttpServletRequest request, String opModule, String opType, String opDesc,
      String opResult) {
    saveOpLog(SysOpLog.builder().opUser(accessTokenService.getUserName(request)).opIp(
        RequestUtils.getRequestIp(request)).opModule(opModule).opType(opType).opDesc(opDesc)
        .opResult(opResult)
        .build());
  }

  public void saveOpLog(SysOpLog opLog) {
    try {
      sysOpLogRepository.save(opLog);
    } catch (Exception ex) {
      log.error("Save the operate log failed:", ex);
    }
  }
}

package com.chujian.wapp.navigator.sso.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.SchedulingConfigurer;
import org.springframework.scheduling.config.ScheduledTaskRegistrar;
import org.springframework.stereotype.Component;

@Component
@EnableScheduling
@Slf4j
@RefreshScope
public class AccessTokenScheduler implements SchedulingConfigurer {

  @Autowired
  private AccessTokenService accessTokenService;

  @Override
  public void configureTasks(ScheduledTaskRegistrar taskRegistrar) {

    taskRegistrar.addFixedRateTask(() -> {

      accessTokenService.deleteExpiredAccessTokens();

    }, 60 * 60 * 1000);
  }
}

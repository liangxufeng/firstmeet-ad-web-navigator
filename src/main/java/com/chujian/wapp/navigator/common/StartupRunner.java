package com.chujian.wapp.navigator.common;

import com.chujian.wapp.navigator.sso.service.AccessTokenService;
import javax.annotation.Resource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;

@Component
public class StartupRunner implements CommandLineRunner {

  @Autowired
  private WebApplicationContext applicationContext;

  @Resource
  private AccessTokenService accessTokenService;

  @Value("${ups.wapp.service.name}")
  private String serviceName;

  @Override
  public void run(String... args) throws Exception {
    applicationContext.getServletContext().setAttribute("serviceName", serviceName);
    applicationContext.getServletContext().setAttribute("accessTokenService", accessTokenService);
  }
}

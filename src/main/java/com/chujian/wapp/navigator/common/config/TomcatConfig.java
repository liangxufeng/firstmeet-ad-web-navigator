package com.chujian.wapp.navigator.common.config;

import javax.servlet.ServletException;
import lombok.extern.slf4j.Slf4j;
import org.apache.catalina.startup.Tomcat;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.web.embedded.tomcat.TomcatServletWebServerFactory;
import org.springframework.boot.web.embedded.tomcat.TomcatWebServer;
import org.springframework.boot.web.servlet.server.ServletWebServerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@Slf4j
public class TomcatConfig {

  @Value("${server.tomcat.manager.enable}")
  private boolean isEnableManager;

  @Value("${server.tomcat.manager.context-path}")
  private String managerContextPath;

  @Bean
  @ConditionalOnProperty(name = "server.tomcat.manager.enable", havingValue = "true")
  public ServletWebServerFactory servletContainer() {
    TomcatServletWebServerFactory tomcatServerFactory = new TomcatServletWebServerFactory() {

      @Override
      protected TomcatWebServer getTomcatWebServer(Tomcat tomcat) {
        if (isEnableManager && StringUtils.isNotBlank(managerContextPath)) {
          tomcat.addUser("admin", "chujian");
          tomcat.addRole("admin", "manager-gui");
          try {
            tomcat.addWebapp("/manager", managerContextPath);
          } catch (ServletException ex) {
            log.error("Configure the tomcat manager failed:", ex);
          }
        }
        return super.getTomcatWebServer(tomcat);
      }

    };

    return tomcatServerFactory;
  }

}

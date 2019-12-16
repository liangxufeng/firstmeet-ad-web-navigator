package com.chujian.wapp.navigator.common.acl;

import com.chujian.wapp.navigator.sso.service.AccessControlService;
import javax.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;


@Slf4j
@Aspect
@Component
@Lazy(false)
public class AclAspect {

  @Autowired
  private AccessControlService accessControlService;

  @Pointcut("@annotation(aclResource)")
  private void doAclMenu(AclResource aclResource) {
  }

  @Around("doAclMenu(aclResource)")
  public Object doBefore(ProceedingJoinPoint pjp, AclResource aclResource) throws Throwable {
    ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder
        .getRequestAttributes();
    HttpServletRequest request = attributes.getRequest();
    String resourceId = aclResource.resourceId();
    if (StringUtils.isBlank(resourceId)) {
      return "error";
    }
    if (accessControlService.accessMenu(request,resourceId)) {
      //执行原方法
      log.info("访问资源:{}",resourceId);
      return pjp.proceed();
    }
    return "error";
  }
}

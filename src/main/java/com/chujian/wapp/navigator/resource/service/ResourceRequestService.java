package com.chujian.wapp.navigator.resource.service;

import com.chujian.wapp.navigator.resource.model.ResourceTypeConfig;
import com.chujian.wapp.navigator.resource.model.TypeInfo;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
@Slf4j
public class ResourceRequestService {

  @Autowired
  private ResourceTypeConfig resourceTypeConfig;

  public void putResourceTypeToRequest(HttpServletRequest request) {
    List<TypeInfo> typeList = resourceTypeConfig.getTypes();
    request.setAttribute("typeList", typeList);
  }
}

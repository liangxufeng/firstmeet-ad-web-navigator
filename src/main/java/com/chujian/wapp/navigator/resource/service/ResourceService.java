package com.chujian.wapp.navigator.resource.service;

import com.chujian.wapp.navigator.resource.entity.Resource;
import com.chujian.wapp.navigator.resource.model.ResourceDTO;
import com.chujian.wapp.navigator.resource.model.ResourceTypeConfig;
import com.chujian.wapp.navigator.resource.respository.ResourceRepository;
import com.chujian.wapp.navigator.role.respository.RoleResourceRepository;
import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ResourceService {

  @Autowired
  RoleResourceRepository roleResourceRepository;

  @Autowired
  private ResourceRepository resourceRepository;


  public Resource findResourceByResourceId(String resourceId) {
    Resource resource = resourceRepository.findByResourceId(resourceId);
    return resource;
  }

  public Resource findResourceByResourceName(String resourceName) {
    return resourceRepository.findByResourceName(resourceName);
  }

  public Long addResource(Long id, String resourceId, String resourceName, String resourceUrl,
      String resourceType, Long parentId, Long resourceOrderNum) {

    Resource resource = Resource.builder().
        resourceId(resourceId).
        resourceName(resourceName).
        resourceUrl(resourceUrl).
        resourceType(resourceType).
        resourceOrderNum(resourceOrderNum).
        parentId(parentId).build();
    resourceRepository.save(resource);
    return resource.getId();
  }

  public void updateResource(Long id, String resourceId, String resourceName, String resourceUrl,
      String resourceType, Long parentId, Long resourceOrderNum) {

    Resource resource = Resource.builder().
        id(id).
        resourceId(resourceId).
        resourceName(resourceName).
        resourceUrl(resourceUrl).
        resourceType(resourceType).
        resourceOrderNum(resourceOrderNum).
        parentId(parentId).build();
    resourceRepository.saveAndFlush(resource);
  }

  public List<ResourceDTO> findAll() {
    List<Resource> list = resourceRepository
        .findAllByOrderByResourceOrderNumAsc();
    List<ResourceDTO> resultList = new ArrayList<>();
    if (list.isEmpty()) {
      return resultList;
    }
    for (Resource resource : list) {
      if (ResourceTypeConfig.RS_TYPE_SYSTEM.equals(resource.getResourceType())) {
        resultList.add(ResourceDTO.builder().id(resource.getId()).parentId(resource.getParentId())
            .resourceId(resource.getResourceId())
            .resourceName(resource.getResourceName()).resourceType(resource.getResourceType())
            .iconSkin(ResourceTypeConfig.RS_TYPE_SYSTEM)
            .resourceUrl(resource.getResourceUrl()).resourceOrderNum(resource.getResourceOrderNum())
            .build());
      }
      if (ResourceTypeConfig.RS_TYPE_MENU.equals(resource.getResourceType())) {
        resultList.add(ResourceDTO.builder().id(resource.getId()).parentId(resource.getParentId())
            .resourceId(resource.getResourceId())
            .resourceName(resource.getResourceName()).resourceType(resource.getResourceType())
            .iconSkin(ResourceTypeConfig.RS_TYPE_MENU)
            .resourceUrl(resource.getResourceUrl()).resourceOrderNum(resource.getResourceOrderNum())
            .build());
      }
      if (ResourceTypeConfig.RS_TYPE_LINK.equals(resource.getResourceType())) {
        resultList.add(ResourceDTO.builder().id(resource.getId()).parentId(resource.getParentId())
            .resourceId(resource.getResourceId())
            .resourceName(resource.getResourceName()).resourceType(resource.getResourceType())
            .iconSkin(ResourceTypeConfig.RS_TYPE_LINK)
            .resourceUrl(resource.getResourceUrl()).resourceOrderNum(resource.getResourceOrderNum())
            .build());
      }
      if (ResourceTypeConfig.RS_TYPE_BUTTON.equals(resource.getResourceType())) {
        resultList.add(ResourceDTO.builder().id(resource.getId()).parentId(resource.getParentId())
            .resourceId(resource.getResourceId())
            .resourceName(resource.getResourceName()).resourceType(resource.getResourceType())
            .iconSkin(ResourceTypeConfig.RS_TYPE_BUTTON)
            .resourceUrl(resource.getResourceUrl()).resourceOrderNum(resource.getResourceOrderNum())
            .build());
      }
    }
    return resultList;
  }

  public Resource findById(Long id) {
    Resource resource = resourceRepository.findByIdKey(id);
    return resource;
  }

  public void deleteById(Long id) {
    //删除资源的角色信息
    if (roleResourceRepository.findByResourceId(id) != null) {
      roleResourceRepository.deleteByResourceId(id);
    }
    //查出resourceid对应的子系统
    //进行递归删除
    List<Resource> list = resourceRepository.findSubListByResourceId(id);
    if (list.size() > 0) {//删除子资源及其对应的角色的信息
      for (Resource resource : list) {
        if (resourceRepository.findSubListByResourceId(resource.getId()) != null) {
          if (roleResourceRepository.findByResourceId(resource.getId()) != null) {
            roleResourceRepository.deleteByResourceId(resource.getId());
          }
          deleteById(resource.getId());
        } else {
          if (roleResourceRepository.findByResourceId(resource.getId()) != null) {
            roleResourceRepository.deleteByResourceId(resource.getId());
          }
          resourceRepository.deleteById(resource.getId());
        }
      }
    }
    resourceRepository.deleteById(id);
  }

  public List<Resource> findAllWithParentId(Long parentId) {
    List<Resource> allResourceList = new ArrayList<>();
    if (parentId == 0) {
      return allResourceList;
    }
    Resource rootResource = findRootResource(parentId);
    Long rootId = rootResource.getId();
    allResourceList.add(rootResource);
    List<Resource> childList = resourceRepository.findByParentId(rootId);
    allResourceList = findChildResource(allResourceList, childList);
    return allResourceList;
  }

  private Resource findRootResource(Long parentId) {
    Resource resource = resourceRepository.findByIdKey(parentId);
    if (resourceRepository.findByIdKey(resource.getParentId()) != null) {
      resource = findRootResource(resource.getParentId());
    }
    return resource;
  }

  private List<Resource> findChildResource(List<Resource> allResourceList,
      List<Resource> childList) {
    if (childList != null) {
      for (Resource resource : childList) {
        allResourceList.add(resource);
        List<Resource> list = resourceRepository.findByParentId(resource.getId());
        findChildResource(allResourceList, list);
      }
    }
    return allResourceList;
  }

  public List<Resource> findResourceByType(String system) {
    return resourceRepository.findResourceByType(system);
  }

  public void copyResource(Long copyId, Long targetId, String resourceId, String resourceUrl,
      String resourceName) {
    //一级复制
    Resource newResource = Resource.builder()
        .parentId(targetId).resourceId(resourceId).resourceName(resourceName)
        .resourceUrl(resourceUrl)
        .resourceType("system").resourceOrderNum(Long.valueOf(0))
        .build();
    resourceRepository.save(newResource);
    Resource firstResource = resourceRepository.findByResourceId(resourceId);
    //查出copyId对应的子级资源
    List<Resource> secResourceList = resourceRepository.findByParentId(copyId);
    //二级复制
    if (secResourceList == null) {
      return;
    }
    for (Resource resource : secResourceList) {
      Resource newSecResource = Resource.builder()
          .parentId(firstResource.getId()).resourceId(resource.getResourceId())
          .resourceName(resource.getResourceName())
          .resourceUrl(resource.getResourceUrl())
          .resourceType(resource.getResourceType())
          .resourceOrderNum(resource.getResourceOrderNum())
          .build();
      resourceRepository.save(newSecResource);
      //三级复制
      List<Resource> threeResourceList = resourceRepository.findByParentId(resource.getId());
      if (threeResourceList == null) {
        return;
      }
      for (Resource threeResource : threeResourceList) {
        Resource newThreeResource = Resource.builder()
            .parentId(newSecResource.getId()).resourceId(threeResource.getResourceId())
            .resourceName(threeResource.getResourceName())
            .resourceUrl(threeResource.getResourceUrl())
            .resourceType(threeResource.getResourceType())
            .resourceOrderNum(threeResource.getResourceOrderNum())
            .build();
        resourceRepository.save(newThreeResource);
        //四级复制
        List<Resource> fourResourceList = resourceRepository.findByParentId(threeResource.getId());
        if (fourResourceList == null) {
          return;
        }
        for (Resource fourResource : fourResourceList) {
          Resource newFourResource = Resource.builder()
              .parentId(newThreeResource.getId()).resourceId(fourResource.getResourceId())
              .resourceName(fourResource.getResourceName())
              .resourceUrl(fourResource.getResourceUrl())
              .resourceType(fourResource.getResourceType())
              .resourceOrderNum(fourResource.getResourceOrderNum())
              .build();
          resourceRepository.save(newFourResource);
          //五级复制
          List<Resource> fiveResourceList = resourceRepository.findByParentId(fourResource.getId());
          if (fiveResourceList == null) {
            return;
          }
          for (Resource fiveResource : fiveResourceList) {
            Resource newFiveResource = Resource.builder()
                .parentId(newFourResource.getId()).resourceId(fiveResource.getResourceId())
                .resourceName(fiveResource.getResourceName())
                .resourceUrl(fiveResource.getResourceUrl())
                .resourceType(fiveResource.getResourceType())
                .resourceOrderNum(fiveResource.getResourceOrderNum())
                .build();
            resourceRepository.save(newFiveResource);
            //六级复制
            List<Resource> sixResourceList = resourceRepository
                .findByParentId(fiveResource.getId());
            if (sixResourceList == null) {
              return;
            }
            for (Resource sixResource : sixResourceList) {
              Resource newSixResource = Resource.builder()
                  .parentId(newFiveResource.getId()).resourceId(sixResource.getResourceId())
                  .resourceName(sixResource.getResourceName())
                  .resourceUrl(sixResource.getResourceUrl())
                  .resourceType(sixResource.getResourceType())
                  .resourceOrderNum(sixResource.getResourceOrderNum())
                  .build();
              resourceRepository.save(newSixResource);
              //七级复制
              List<Resource> sevenResourceList = resourceRepository
                  .findByParentId(sixResource.getId());
              if (sevenResourceList == null) {
                return;
              }
              for (Resource sevenResource : sevenResourceList) {
                Resource newSevenResource = Resource.builder()
                    .parentId(newSixResource.getId()).resourceId(sevenResource.getResourceId())
                    .resourceName(sevenResource.getResourceName())
                    .resourceUrl(sevenResource.getResourceUrl())
                    .resourceType(sevenResource.getResourceType())
                    .resourceOrderNum(sevenResource.getResourceOrderNum())
                    .build();
                resourceRepository.save(newSevenResource);
                //八级复制
                List<Resource> eightResourceList = resourceRepository
                    .findByParentId(sevenResource.getId());
                if (eightResourceList == null) {
                  return;
                }
                for (Resource eightResource : sevenResourceList) {
                  Resource newEightResource = Resource.builder()
                      .parentId(newSevenResource.getId()).resourceId(eightResource.getResourceId())
                      .resourceName(eightResource.getResourceName())
                      .resourceUrl(eightResource.getResourceUrl())
                      .resourceType(eightResource.getResourceType())
                      .resourceOrderNum(eightResource.getResourceOrderNum())
                      .build();
                  resourceRepository.save(newEightResource);
                }
              }
            }
          }
        }
      }
    }
  }
}

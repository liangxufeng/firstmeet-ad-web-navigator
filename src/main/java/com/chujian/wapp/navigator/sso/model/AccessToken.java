package com.chujian.wapp.navigator.sso.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.io.Serializable;
import java.util.List;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@JsonIgnoreProperties(ignoreUnknown = true)
public class AccessToken implements Serializable {

  @JsonProperty("user_id")
  private String userId;

  @JsonProperty("user_name")
  private String userName;

  @JsonProperty("iat")
  private long iat;

  @JsonProperty("exp")
  private long exp;

  @JsonProperty("version")
  private long version;

  @JsonProperty("roles")
  private List<AccessRole> roleList;

  @JsonProperty("resources")
  private List<AccessResource> resourceList;

  @JsonProperty("games")
  private List<AccessGame> gameList;

  @JsonProperty("medias")
  private List<Integer> mediaList;

  @JsonProperty("mediaResource")
  private List<Integer> mediaResourceList;

  @JsonProperty("product")
  private List<Integer> productList;

  @JsonProperty("dept")
  private AccessDept accessDept;
}

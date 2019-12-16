package com.chujian.wapp.navigator.utils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.util.Collections;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

@Slf4j
public class JSONUtils {

  private static final ObjectMapper mapper = new ObjectMapper();

  public static <T> T fromJsonToObject(String json, Class<T> clz) throws IOException {
    return mapper.readValue(json, clz);
  }

  public static JSONObject toJSONObject(String jsonData) {
    if (StringUtils.isBlank(jsonData)) {
      return new JSONObject();
    }
    return JSONObject.parseObject(jsonData);
  }

  public static JSONArray toJSONArray(String jsonData) {
    if (StringUtils.isBlank(jsonData)) {
      return new JSONArray();
    }
    return JSONArray.parseArray(jsonData);
  }

  public static String toPrettyJSONString(Object jsonObject) {
    if (jsonObject == null) {
      return "{}";
    }
    return JSONObject.toJSONString(jsonObject, true);
  }

  public static String toJSONString(Object jsonObject) {
    if (jsonObject == null) {
      return "";
    }
    try {
      return JSON.toJSONString(jsonObject, SerializerFeature.WRITE_MAP_NULL_FEATURES);
    } catch (Exception ex) {
      return "";
    }
  }

  public static void sort(List<JSONObject> list, String sortField) {
    Collections.sort(list, (o1, o2) ->
        sort(o1, o2, sortField)
    );
  }

  public static void sort(List<JSONObject> list, String sortField1, String sortFiled2,
      String sortFiled3) {
    Collections.sort(list, (o1, o2) -> {

      int ret = sort(o1, o2, sortField1);
      if (ret == 0) {
        ret = sort(o1, o2, sortFiled2);
        if (ret == 0) {
          return sort(o1, o2, sortFiled3);
        } else {
          return ret;
        }
      } else {
        return ret;
      }
    });
  }

  public static void sort(List<JSONObject> list, String sortField1, String sortFiled2) {
    Collections.sort(list, (o1, o2) -> {

      int ret = sort(o1, o2, sortField1);

      if (ret == 0) {
        return sort(o1, o2, sortFiled2);
      } else {
        return ret;
      }
    });
  }

  private static int sort(JSONObject o1, JSONObject o2, String sortField) {
    String str1 = o1 == null ? "" : o1.getString(sortField);
    str1 = str1 == null ? "" : str1;
    String str2 = o2 == null ? "" : o2.getString(sortField);
    str2 = str2 == null ? "" : str2;
    return str1.compareTo(str2);
  }

}

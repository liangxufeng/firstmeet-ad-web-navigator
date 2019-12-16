package com.chujian.wapp.navigator.utils;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import javax.servlet.http.HttpServletRequest;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.servlet.support.RequestContextUtils;

public class MessageUtils {

  public static String getMessage(HttpServletRequest request, String code) {
    return getMessage(request, code, "");
  }

  public static String getMessage(HttpServletRequest request, String code, Object... args) {
    return RequestContextUtils.findWebApplicationContext(request).getMessage(code, args,
        RequestContextUtils.getLocale(request));
  }

  public static String getLocaleMessage(HttpServletRequest request, String code, Locale locale) {
    return getLocaleMessage(request, code, locale, "");
  }

  public static String getLocaleMessage(HttpServletRequest request, String code, Locale locale,
      Object... args) {
    return RequestContextUtils.findWebApplicationContext(request).getMessage(code, args, locale);
  }

  public static List<JSONObject> getChannelList(List<String> channelIdList,
      HttpServletRequest request, boolean isUpper) {
    List<JSONObject> list = new ArrayList<>();

    if (channelIdList == null || channelIdList.isEmpty()) {
      return list;
    }
    channelIdList.forEach(channel -> {

      JSONObject channelObj = new JSONObject();
      channelObj.put("channel_id", isUpper ? channel.toUpperCase() : channel);
      channelObj.put("channel_name", getChannelName(channel, request));
      list.add(channelObj);
    });

    return list;
  }

  public static List<JSONObject> getOsList(List<String> osIdList,
      HttpServletRequest request, boolean isUpper) {
    List<JSONObject> list = new ArrayList<>();

    if (osIdList == null || osIdList.isEmpty()) {
      return list;
    }
    osIdList.forEach(os -> {

      JSONObject osObj = new JSONObject();
      osObj.put("os_id", isUpper ? os.toUpperCase() : os);
      osObj.put("os_name", getOsName(os, request));
      list.add(osObj);
    });
    return list;
  }

  public static void fillInChannelName(List<JSONObject> jsonObjectList, String channelKey,
      HttpServletRequest request) {
    if (jsonObjectList != null && !jsonObjectList.isEmpty()) {
      jsonObjectList.forEach(jsonObj -> {
        String channelId = jsonObj.getString(channelKey);
        if (StringUtils.isBlank(channelId)) {
          return;
        }
        jsonObj.put("channel_name", getChannelName(channelId, request));
      });
    }
  }

  public static JSONArray getChannelNameArray(JSONArray channelIdArr, HttpServletRequest request) {
    JSONArray channelNameArr = new JSONArray();
    if (channelIdArr != null) {
      channelIdArr.forEach(channelIdObj -> {
        String channelId = channelIdObj.toString();
        channelNameArr.add(getChannelName(channelId, request));
      });
    }
    return channelNameArr;
  }

  public static String getChannelName(String channelId, HttpServletRequest request) {
    String code = "channel_" + channelId.toLowerCase();
    String channelName = MessageUtils.getMessage(request, code);
    if (StringUtils.isBlank(channelName) || channelName.equals(code)) {
      channelName = channelId;
    }
    return channelName;
  }

  public static void fillInOsName(List<JSONObject> jsonObjectList, String osKey,
      HttpServletRequest request) {
    if (jsonObjectList != null && !jsonObjectList.isEmpty()) {
      jsonObjectList.forEach(jsonObj -> {
        String osId = jsonObj.getString(osKey);
        if (StringUtils.isBlank(osId)) {
          return;
        }
        jsonObj.put("os_name", getOsName(osId, request));
      });
    }
  }

  public static JSONArray getOsNameArray(JSONArray osIdArr, HttpServletRequest request) {
    JSONArray osNameArr = new JSONArray();
    if (osIdArr != null) {
      osIdArr.forEach(osIdObj -> {
        String oslId = osIdObj.toString();
        osNameArr.add(MessageUtils.getOsName(oslId, request));
      });
    }
    return osNameArr;
  }

  public static String getOsName(String osId, HttpServletRequest request) {
    String code = "os_" + osId.toLowerCase();
    String osName = MessageUtils.getMessage(request, code);
    if (StringUtils.isBlank(osName) || osName.equals(code)) {
      osName = osId;
    }
    return osName;
  }
}

package com.chujian.wapp.navigator.common.mvc;

import com.chujian.wapp.navigator.common.Constants;
import java.util.Locale;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.lang.Nullable;
import org.springframework.web.servlet.LocaleResolver;

@Slf4j
public class ApplicationLocaleResolver implements LocaleResolver {

  @Override
  public Locale resolveLocale(HttpServletRequest request) {
    String i18nLanguage = request.getParameter(Constants.PARAM_I18N_LANGUAGE);
    Locale locale = Locale.getDefault();
    try {
      if (StringUtils.isNotBlank(i18nLanguage)) {
        String[] language = i18nLanguage.split("_");
        if (language.length == 2) {
          locale = new Locale(language[0], language[1]);
        } else {
          return locale;
        }

        HttpSession session = request.getSession();
        session.setAttribute(Constants.SESSION_KEY_I18N_LANGUAGE, locale);
      } else {
        HttpSession session = request.getSession();
        Locale localeInSession = (Locale) session
            .getAttribute(Constants.SESSION_KEY_I18N_LANGUAGE);
        if (localeInSession != null) {
          locale = localeInSession;
        }
      }
    } catch (Exception ex) {
      log.error("Get locale failed:", ex);
    }
    return locale;
  }

  @Override
  public void setLocale(HttpServletRequest request, @Nullable HttpServletResponse response,
      @Nullable Locale locale) {

  }
}

package com.example.demo.common.resolver;

import com.example.demo.common.annotation.CursorDefault;
import com.example.demo.common.dto.Cursor;
import org.springframework.core.MethodParameter;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

public class CursorArgumentResolver implements HandlerMethodArgumentResolver {

  @Override
  public boolean supportsParameter(MethodParameter parameter) {
    return parameter.getParameterType().equals(Cursor.class)
        && parameter.hasParameterAnnotation(CursorDefault.class);
  }

  @Override
  public Object resolveArgument(
      MethodParameter parameter,
      ModelAndViewContainer mavContainer,
      NativeWebRequest webRequest,
      WebDataBinderFactory binderFactory) {
    CursorDefault cursorDefault = parameter.getParameterAnnotation(CursorDefault.class);

    String cursorParam = webRequest.getParameter("cursor");
    String sizeParam = webRequest.getParameter("size");

    Long cursor = cursorParam != null ? Long.parseLong(cursorParam) : cursorDefault.cursor();
    Integer size = sizeParam != null ? Integer.parseInt(sizeParam) : cursorDefault.size();

    return new Cursor(cursor, size);
  }
}

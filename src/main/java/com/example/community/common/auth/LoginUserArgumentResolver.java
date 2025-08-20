package com.example.community.common.auth;

import com.example.community.common.SessionConst;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.core.MethodParameter;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.*;
import org.springframework.web.server.ResponseStatusException;

@Component
public class LoginUserArgumentResolver implements HandlerMethodArgumentResolver {
    @Override
    public boolean supportsParameter(MethodParameter parameter){
        return parameter.hasParameterAnnotation(LoginUser.class)
                && (parameter.getParameterType().equals(Long.class))
                || parameter.getParameterType().equals(long.class);
    }
    @Override
    public Object resolveArgument(MethodParameter p, ModelAndViewContainer m,
                                  NativeWebRequest w, WebDataBinderFactory b){
        HttpServletRequest req = w.getNativeRequest(HttpServletRequest.class);
        HttpSession session = (req != null)? req.getSession(false) : null;

        Object id = (session != null)
                ? session.getAttribute(SessionConst.LOGIN_USER_ID) : null;

        if (id == null) throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "로그인이 필요합니다.");
        return (Long)id;
    }
}

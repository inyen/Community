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
/*
세션에서 로그인 사용자 ID를 꺼내 파라미터에 주입
 */
@Component
public class LoginUserArgumentResolver implements HandlerMethodArgumentResolver {
    //지원 파라미터
    @Override
    public boolean supportsParameter(MethodParameter parameter){
        return parameter.hasParameterAnnotation(LoginUser.class)
                && (parameter.getParameterType().equals(Long.class))
                || parameter.getParameterType().equals(long.class);
    }

    //실제 주입 파라미터
    @Override
    public Object resolveArgument(MethodParameter p, ModelAndViewContainer m,
                                  NativeWebRequest w, WebDataBinderFactory b)
    {
        HttpServletRequest req = w.getNativeRequest(HttpServletRequest.class);
        // false -> 기존 세션이 없으면 새로 만들지 않음
        HttpSession session = (req != null)? req.getSession(false) : null;

        Object id = (session != null)
                ? session.getAttribute(SessionConst.LOGIN_USER_ID) : null;

        if (id == null)
            //로그인하지 않은 경우 401 반환
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "로그인이 필요합니다.");
        return (Long)id;
    }
}
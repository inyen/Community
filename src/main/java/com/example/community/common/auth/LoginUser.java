package com.example.community.common.auth;

import java.lang.annotation.*;
/*
컨트롤러 파라미터에 현재 로그인 사용자 주입
*/
@Target(ElementType.PARAMETER)  //파라미터에만 사용
@Retention(RetentionPolicy.RUNTIME)     //런타임까지 유지
@Documented
public @interface LoginUser {
}
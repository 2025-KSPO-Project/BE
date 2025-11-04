package com.kspo.carefit.base.security.util;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

@Component
public class CookieUtil {

    // 쿠키를 생성하는 메소드
    public Cookie createCookie(String key, String value) {

        Cookie cookie = new Cookie(key, value);
        cookie.setMaxAge(60*60*60);
        //cookie.setSecure(true);
        cookie.setPath("/");
        cookie.setHttpOnly(true);

        return cookie;
    }

    // 쿠키에서 Authorization 을 가져오는 메소드
    public String parseAuthorization(Cookie[] cookies){

        String authorization = null;

        for(Cookie cookie : cookies){

            if(cookie.getName().equals("Authorization")){

                authorization = cookie.getValue();
                return authorization;
            }
        }

        return null;
    }

    // 쿠키에서 refresh 토큰을 추출하는 메소드
    public String findRefreshFromCookie(HttpServletRequest request){

        String refresh = null;
        Cookie[] cookies = request.getCookies();
        if(cookies != null){
            for( Cookie cookie : cookies){
                if(cookie.getName().equals("refresh")){
                    return cookie.getValue();
                }
            }
        }

        return refresh;
    }

    // 쿠키를 비우는 메소드
    public void zeroCookie(HttpServletResponse response){

        Cookie cookie = new Cookie("refresh",null);
        cookie.setMaxAge(0);
        cookie.setPath("/");

        response.addCookie(cookie);
        response.setStatus(HttpStatus.OK.value());

    }

}

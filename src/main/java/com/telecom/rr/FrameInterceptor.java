package com.telecom.rr;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

/**
 * 拦截器类
 * @author
 */
public class FrameInterceptor extends HandlerInterceptorAdapter {

    public FrameInterceptor() {

    }

    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        if (!(handler == null || handler instanceof HandlerMethod)) {
            return true;
        }
        response.setCharacterEncoding("UTF-8");
        return true;
    }

    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex)
            throws Exception {

    }

}

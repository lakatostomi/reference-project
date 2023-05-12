package com.example.caloriecalculator.interceptor;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Enumeration;
import java.util.Iterator;

@Slf4j
public class LoggerInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        log.info("Request from IP={} to URI={}", getClientIp(request), request.getRequestURI());
        logRequestHeaders(request);
        getParameters(request);
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        logResponseHeaders(response);
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {

    }

    private void logRequestHeaders(HttpServletRequest request) {
        log.info("Request header:");
        Iterator<String> itr = request.getHeaderNames().asIterator();
        while (itr.hasNext()) {
                String key = itr.next();
                if (request.getHeader(key) != null || !request.getHeader(key).isEmpty()) {
                    log.debug(key + " " + request.getHeader(key));
                }
        }
    }

    private void logResponseHeaders(HttpServletResponse response) {
        log.info("Response header:");
        Iterator<String> itr = response.getHeaderNames().iterator();
        while (itr.hasNext()) {
            String key = itr.next();
            if (response.getHeader(key) != null || !response.getHeader(key).isEmpty()) {
                log.debug(key + " " + response.getHeader(key));
            }
        }
    }

    private void getParameters(HttpServletRequest request) {
        StringBuffer output = new StringBuffer();
        Enumeration<String> parameterNames = request.getParameterNames();
        if (parameterNames != null) {
            output.append("params:");
            while (parameterNames.hasMoreElements()) {
                if (!output.equals("params:")) {
                    output.append("&");
                }
                String param = parameterNames.nextElement();
                if (param.equals("password")) {
                    output.append("*****");
                } else {
                    output.append(request.getParameter(param));
                }
            }
        }
        log.debug(output.toString());
    }

    private String getClientIp(HttpServletRequest request) {
        String ip = request.getHeader("X-FORWARDED-FOR");
        if (ip == null || ip.isEmpty()) {
            return request.getRemoteAddr();
        } else {
            return ip;
        }
    }
}

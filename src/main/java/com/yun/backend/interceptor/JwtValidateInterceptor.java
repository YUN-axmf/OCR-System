package com.yun.backend.interceptor;

import com.alibaba.fastjson.JSON;
import com.yun.backend.util.JwtUtil;
import com.yun.backend.util.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Component
@Slf4j
public class JwtValidateInterceptor implements HandlerInterceptor {
    @Resource
    private JwtUtil jwtUtil;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String token = request.getHeader("X-Token");
        log.debug(request.getRequestURI() + "需要验证：" + token);
        if (token != null) {
            try {
                jwtUtil.parseToken(token);
                log.debug(request.getRequestURI() + "验证成功");
                return true;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        log.debug(request.getRequestURI() + "验证失败");
        // 拦截
        response.setContentType("application/json;charset=utf-8");
        Result<Object> result = Result.unauthorized();
        response.getWriter().write(JSON.toJSONString(result));
        return false;
    }
}

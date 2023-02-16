package com.roy.reggie_takeout.filter;

import com.alibaba.fastjson.JSON;
import com.roy.reggie_takeout.common.BaseContext;
import com.roy.reggie_takeout.common.R;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.AntPathMatcher;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @description: 检查用户是否已经完成登录
 * @author: Ruofan Li
 * @date: 2023/2/7
 */
@WebFilter(filterName = "loginCheckFilter", urlPatterns = "/*") //urlPatterns拦截的路径
@Slf4j
public class LoginCheckFilter implements Filter {
    //路径匹配器，支持通配符
    public static final AntPathMatcher PATH_MATCHER = new AntPathMatcher();
    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest)servletRequest;
        HttpServletResponse response = (HttpServletResponse)servletResponse;

        //1. 获取本次请求的uri
        String requestURI = request.getRequestURI();
        log.info("拦截到请求: {}", requestURI);
        
        String[] urls = new String[]{
                "/employee/login",
                "/employee/logout",
                "/backend/**",
                "/front/**",
                "/common/**",
                "/user/sendMsg",
                "/user/login",
        };

        //2. 判断本次请求是否需要处理
        boolean check = check(urls, requestURI);

        //3. 如果不需要处理则放行
        if(check){
            log.info("本次请求{}不需要处理", requestURI);
            filterChain.doFilter(request, response); //放行
            return;
        }

        //4-1. 判断员工登录状态，如果已登录，则直接放行
        if(request.getSession().getAttribute("employee") != null){
            log.info("员工已登录，员工id为{}", request.getSession().getAttribute("employee"));

            Long empId = (Long) request.getSession().getAttribute("employee");
            BaseContext.setCurrentId(empId);

            filterChain.doFilter(request, response); //放行
            return;
        }
        //4-2. 判断移动端登录状态，如果已登录，则直接放行
        if(request.getSession().getAttribute("user") != null){
            log.info("用户已登录，用户id为{}", request.getSession().getAttribute("user"));

            Long userId = (Long) request.getSession().getAttribute("user");
            BaseContext.setCurrentId(userId);

            filterChain.doFilter(request, response); //放行
            return;
        }

        log.info("用户未登录");
        //5. 如果未登录则返回未登录结果，通过输出流的方式向客户端页面相应数据
        response.getWriter().write(JSON.toJSONString(R.error("NOTLOGIN")));
        return;
    }

    /**
     * @description: 路径匹配，检查本次请求是否需要放行
     * @param requestURI
     * @return boolean
     * @author: Ruofan Li
     * @date: 2023/2/7
     */
    public boolean check(String[] urls, String requestURI){
        for(String url : urls){
            boolean match = PATH_MATCHER.match(url, requestURI);
            if(match){
                return true;
            }
        }
        return false;
    }
}

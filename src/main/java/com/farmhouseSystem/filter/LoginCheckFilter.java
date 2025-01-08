package com.farmhouseSystem.filter;


import com.alibaba.fastjson.JSON;
import com.farmhouseSystem.common.BaseContext;
import com.farmhouseSystem.common.R;

import lombok.extern.slf4j.Slf4j;
import org.springframework.util.AntPathMatcher;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebFilter(filterName = "loginCheckFilter",urlPatterns = "/*")
@Slf4j
public class LoginCheckFilter implements Filter {
    //路径匹配器,支持通配符
    public static final AntPathMatcher PATTERN_MATCHER = new AntPathMatcher();
    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;
        Object employee = request.getSession().getAttribute("employee");
        Object admin = request.getSession().getAttribute("admin");
        Object employeeall = request.getSession().getAttribute("employeeall");
        if (employee != null){
            System.out.println("employee ID："+employee);
        }
        if (admin != null){
            System.out.println("admin ID："+employee);
        }
        if (employeeall != null){
            System.out.println("employeeall ID："+employee);
        }

        String requestURI = request.getRequestURI();
        log.info("拦截到请求：{}",requestURI);
        String[] urls = new String[]{
                "/employee/login",
                "/employee/logout",
                "/employee/**",
                "/employee-all/login",
                "/employee-all/logout",
                "/admin/login",
                "/admin/logout",
                "/backend/**",
                "/front/**",
                "/user/sendMsg",
                "/user/login",
                "/doc.html",
                "/webjars/**",
                "/swagger-resources",
                "/v2/api-docs",
                "/employee-all/**",
                "/attractions/**",
                "/attractions/ids",
                "/branchstore/**",
                "/route/**",
                "/orders/**",
        };
        String[] RESTRICTED_URLS = new String[]{
                "/attractions/page",
                "/attractions/list",
                "/common/download",
                "/branchstore/page",
                "/branchstore/list",
                "/orders/**",
                "/route/page",
                "/route/list",
                "/user/sendMsg",
                "/user/login",
                "/doc.html",
                "/webjars/**",
                "/swagger-resources",
                "/v2/api-docs"
        };

        //判断当前路径是否包含在白名单中
        boolean check = check(urls,requestURI);
        if (check){
            log.info("本次请求{}不需要处理",requestURI);
            filterChain.doFilter(request,response);

            return;
        }
        //4-1判断登录状态是否登录
//        ServletContext sc = request.getServletContext();
        String sessionId = request.getHeader("token");
        System.out.println("sessionId:"+sessionId);
        Cookie[] cookies = request.getCookies();
        if (cookies != null){
            System.out.println("cookie:"+request.getCookies());

        }
//        Object obj = sc.getAttribute(sessionId);
//        HttpSession session = (HttpSession) obj;
//        System.out.println("session:"+session.getId());
//        sessionId == session.getId() &&
//        String sessionId = request.getHeader("token");
//        if (sessionId != null){
//            request.setAttribute("token",sessionId);
//        }
//            String token = "";
            // 如果是 OPTIONS 请求，我们就让他通过，不管他
//            if (request.getMethod().equals("OPTIONS")) {
//                response.setStatus(HttpServletResponse.SC_OK);
//                // 如果不是，我们就把token拿到，用来做判断
//            }else {
//                token = request.getHeader("token");
//            }
//        // 从请求头中获取 sessionId
//        String sessionId = request.getHeader("token");
////        String token = request.getHeader("token");
//        System.out.println("token:"+token);
//        System.out.println(sessionId);
//        if ("1" != null) {
//            // 使用 sessionId 获取 HttpSession 对象
//            HttpSession session = request.getSession(false);
//            if (session != null && sessionId.equals(session.getId())) {
//
//                // 会话存在且 sessionId 匹配
//                log.info("会话已存在，sessionId: {}", sessionId);
//                filterChain.doFilter(request, response);
//                return;
//            }
//        }


        //4-2判断登录状态是否登录
        if (request.getSession().getAttribute("employee")!=null){
            log.info("分店操作员已登录，用户id为：{}",request.getSession().getAttribute("employee"));
            response.getWriter().write(JSON.toJSONString(R.error("EMPLOYEE_NO_POWER")));
            return;
        }
        if (request.getSession().getAttribute("employeeall")!=null){
            log.info("总店操作员已登录，用户id为：{}",request.getSession().getAttribute("employeeall"));
            response.getWriter().write(JSON.toJSONString(R.error("EMPLOYEE_NO_POWER")));
            return;
        }
        if ("1" != null){
            log.info("管理员已登录，用户id为：{}",request.getSession().getAttribute("admin"));
            Long adminId = (Long) request.getSession().getAttribute("admin");
            BaseContext.setCurrentId(adminId);
            filterChain.doFilter(request,response);
            return;
        }
        //4-3判断登录状态是否登录
        if (request.getSession().getAttribute("user")!=null){
            log.info("用户已登录，用户id为：{}",request.getSession().getAttribute("user"));
            Long userId = (Long) request.getSession().getAttribute("user");
            // 检查用户是否有权限访问请求的路径
            if (isRestricted(RESTRICTED_URLS,requestURI)) {
                log.info("访问成功：{}", requestURI);
                BaseContext.setCurrentId(userId);
                filterChain.doFilter(request,response);
                return;
            }
            response.getWriter().write(JSON.toJSONString(R.error("USER_NO_POWER")));
            log.info("用户无权限访问");
            return;
        }
        log.info("用户未登录");
        //在conllector控制层中加了@RestController注解会自动将方法的返回值转为json响应回去但是在过滤器中需要手动转换将对象转为json格式再响应回去
        response.getWriter().write(JSON.toJSONString(R.error("NOTLOGIN")));

        log.info("拦截到请求：{}",request.getRequestURI());
    }
    public boolean check(String[] urls,String requestURI){
        for (String url : urls) {
            boolean match = PATTERN_MATCHER.match(url,requestURI);
            if (match){
                return true;
            }
        }
        return false;
    }
    public boolean isRestricted(String[] RESTRICTED_URLS,String requestURI) {
        for (String restrictedUrl : RESTRICTED_URLS) {
            boolean match = PATTERN_MATCHER.match(restrictedUrl, requestURI);
            if (match) {
                return true;
            }
        }
        return false;
    }
}

package com.taotao.portal.interceptor;

import com.taotao.common.utils.CookieUtils;
import com.taotao.pojo.TbUser;
import com.taotao.portal.service.impl.UserServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @ Create by ostreamBaba on 18-12-4
 * @ 描述
 */
public class LoginInterceptor implements HandlerInterceptor{

    @Autowired
    private UserServiceImpl userService;

    @Override
    public boolean preHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o) throws Exception {
        //判断用户是否登录
        //从cookie中取cookie
        String token = CookieUtils.getCookieValue(httpServletRequest, "TT_TOKEN");
        //根据token取用户信息 调用sso服务
        TbUser user = userService.getUserByToken(token);
        //取不到 登录页面 把用户请求的url作为参数传递给登录页面
        if(user == null){
            System.out.println(httpServletRequest.getRequestURI());
            httpServletResponse.sendRedirect(userService.getSSO_BASE_URL() + userService.getSSO_PAGE_LOGIN()
            + "?redirect=" + httpServletRequest.getRequestURI());
            return false;
        }
        //取到 放行
        return true;
        //返回值决定handler是否执行 true执行 false 不执行
    }

    @Override
    public void postHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, ModelAndView modelAndView) throws Exception {

    }

    @Override
    public void afterCompletion(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, Exception e) throws Exception {
        //取异常
    }
}

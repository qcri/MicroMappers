package org.qcri.micromappers.interceptor;

import org.qcri.micromappers.entity.Account;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by jlucas on 12/20/16.
 */
public class AdminInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response,
                             Object handler) throws Exception {
        Object obj = request.getSession().getAttribute("current_user");
        Object obj2 = request.getSession().getAttribute("account");
        String xrequest = request.getHeader("x-requested-with");

        if (obj == null || !(obj2 instanceof Account)) {
            if("XMLHttpRequest".equalsIgnoreCase(xrequest)) {
                response.setHeader("sessionstatus", "timeout");
            }else {
                response.sendRedirect(request.getContextPath() + "signin");
            }

            return false;
        }
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
                           ModelAndView modelAndView) throws Exception {

    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response,
                                Object handler, Exception ex) throws Exception {

    }
}

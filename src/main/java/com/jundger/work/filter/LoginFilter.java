package com.jundger.work.filter;

import org.apache.log4j.Logger;
import org.springframework.util.StringUtils;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

/**
 * Title: UmbrellaStorage
 * Date: Create in 2018/4/16 0:55
 * Description:
 *
 * @author Jundger
 * @version 1.0
 */
public class LoginFilter implements Filter {
	private Logger log = Logger.getLogger(LoginFilter.class);

	public void destroy() {
		log.info("destroy LoginFilter ...");
	}

	public void doFilter(ServletRequest req, ServletResponse resp, FilterChain chain) throws IOException, ServletException {
		HttpServletRequest request = (HttpServletRequest) req;
		HttpServletResponse response = (HttpServletResponse) resp;
//		HttpSession session = request.getSession();
//		//判断Session中是否有登录用户信息
//		String toke = (String) session.getAttribute("LOGIN_TOKE");
//		if(!StringUtils.isEmpty(toke)){
//			chain.doFilter(req, resp);
//		}else{
//			//若没有则，跳转到登录页面
//			response.sendRedirect(request.getContextPath() + "/user/toLogin");
//		}

		log.info("doFilter LoginFilter ...");
		// TODO 由OAuth2.0机制获取用户openid

		// TODO 登录


		response.sendRedirect(request.getContextPath() + "/index.jsp");
	}

	public void init(FilterConfig arg0) throws ServletException {
		log.info("init LoginFilter ...");
	}
}

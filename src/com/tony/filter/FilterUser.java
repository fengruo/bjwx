package com.tony.filter;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public class FilterUser implements Filter {

	public void destroy() {
		
	}

	public void doFilter(ServletRequest request, ServletResponse response,
			FilterChain arg2) throws IOException, ServletException {
		HttpServletRequest rq = (HttpServletRequest) request;
		HttpSession session = rq.getSession();

		String requesturi = rq.getRequestURI();
		if (requesturi.endsWith("css") || requesturi.endsWith("js")
				|| requesturi.endsWith("jpg") || requesturi.endsWith("gif")
				|| requesturi.endsWith("login.tony")
				|| session.getAttribute("cmsuserid") != null) {
			arg2.doFilter(request, response);
		} else if (session.getAttribute("cmsuserid") == null
				|| "".equals(session.getAttribute("cmsuserid"))) {
			HttpServletResponse rs = (HttpServletResponse) response;
			rs.sendRedirect("/login.jsp");
		}
	}

	public void init(FilterConfig arg0) throws ServletException {
		// TODO Auto-generated method stub

	}

}

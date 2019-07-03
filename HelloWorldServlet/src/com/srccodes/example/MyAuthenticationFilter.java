package com.srccodes.example;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.xml.crypto.dsig.spec.XPathType.Filter;

public class MyAuthenticationFilter extends Filter{
	
	private ServletContext context;

	public void init(FilterConfig fConfig) throws ServletException {
		this.context = fConfig.getServletContext();
		this.context.log("AuthenticationFilter initialized");
	}

	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {

HttpServletRequest req = (HttpServletRequest) request;
		
		
		String ipAddress = req.getRemoteAddr(); 
		
		// pass the request along the filter chain 
		chain.doFilter(request, response);

	}

	public void destroy() {
		// close any resources here
	}


}

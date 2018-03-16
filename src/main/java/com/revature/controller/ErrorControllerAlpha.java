package com.revature.controller;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;

public class ErrorControllerAlpha implements ErrorController {
	
	private static Logger logger = Logger.getLogger(ErrorControllerAlpha.class);

	@Override
	public String showError(HttpServletRequest request) {
		String uri = request.getRequestURI();
		if (uri.endsWith("/404.do")) {
			return "404.html";
		} else if (uri.endsWith("/403.do")) {
			return "403.html";
		} else {
			return "oops.html";
		}
	}

}

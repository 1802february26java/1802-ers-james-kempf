package com.revature.controller;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;

public class ErrorControllerAlpha implements ErrorController {
	
	private static Logger logger = Logger.getLogger(ErrorControllerAlpha.class);

	@Override
	public String showError(HttpServletRequest request) {
		String uri = request.getRequestURI();
		if (uri.endsWith("/404.do")) {
			logger.trace("Shoing 404");
			return "404.html";
		} else if (uri.endsWith("/403.do")) {
			logger.trace("Shoing 403");
			return "403.html";
		} else {
			logger.trace("Shoing oops");
			return "oops.html";
		}
	}

}

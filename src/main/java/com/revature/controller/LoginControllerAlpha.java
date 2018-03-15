package com.revature.controller;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;

import com.revature.ajax.ClientMessage;
import com.revature.model.Employee;
import com.revature.service.EmployeeServiceAlpha;

public class LoginControllerAlpha implements LoginController {

	private static Logger logger = Logger.getLogger(EmployeeServiceAlpha.class);
	
	@Override
	public Object login(HttpServletRequest request) {
		logger.trace("Logging in");
		if (request.getMethod().equals("GET")) {
			return "login.html";
		}
		Employee employee = new Employee();
		employee.setUsername(request.getParameter("username"));
		employee.setPassword(request.getParameter("password"));
		employee = EmployeeServiceAlpha.getInstance().authenticate(employee);
		logger.trace(employee.toString());
		if (employee == null) {
			return new ClientMessage("Authentication Failed");
		}
 		return "home.html";
	}

	@Override
	public String logout(HttpServletRequest request) {
		return "login.html";
	}

}

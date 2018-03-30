package com.revature.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

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
		if (employee == null) {
			return new ClientMessage("Authentication Failed");
		} else {
			request.getSession().setAttribute("employee", employee);
			return employee;
		}
	}

	@Override
	public String logout(HttpServletRequest request) {
		HttpSession session = request.getSession(false);
		session.invalidate();
		session = null;
		return "login.html";
	}

}

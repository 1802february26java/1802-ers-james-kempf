package com.revature.controller;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;

import com.revature.ajax.ClientMessage;
import com.revature.model.Employee;
import com.revature.model.EmployeeRole;
import com.revature.service.EmployeeService;
import com.revature.service.EmployeeServiceAlpha;

public class EmployeeInformationControllerAlpha implements EmployeeInformationController {
	
	private EmployeeService employeeService = EmployeeServiceAlpha.getInstance();
	private static Logger logger = Logger.getLogger(EmployeeInformationControllerAlpha.class);

	@Override
	public Object registerEmployee(HttpServletRequest request) {
		Employee loggedEmployee = (Employee) request.getSession().getAttribute("employee");
		if (loggedEmployee != null) {
	 		logger.trace("registering employee");
			if (request.getMethod() == "GET") {
				logger.trace("registering employee - GET");
				return "/register.html";
			} else {
				if (loggedEmployee.getEmployeeRole().getId() == 2) {
					Employee newEmployee = new Employee(
							request.getParameter("firstname"),
							request.getParameter("lastname"),
							request.getParameter("username"),
							request.getParameter("password"),
							request.getParameter("email"),
							new EmployeeRole(Integer.parseInt(request.getParameter("employeeRoleID")))
							);
					if (!employeeService.isUsernameTaken(newEmployee)) {
						if (employeeService.createEmployee(newEmployee)) {
							return new ClientMessage("Registration successful");
						}
					} else {
						return new ClientMessage("Username is already taken");
					}
				}
				return new ClientMessage("Registration failed");
			}
		}
		return "/login.html";
	}

	@Override
	public Object updateEmployee(HttpServletRequest request) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object viewEmployeeInformation(HttpServletRequest request) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object viewAllEmployees(HttpServletRequest request) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object usernameExists(HttpServletRequest request) {
		// TODO Auto-generated method stub
		return null;
	}

}

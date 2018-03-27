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
		logger.trace("registering employee");
		Employee loggedEmployee = (Employee) request.getSession().getAttribute("employee");
		if (loggedEmployee == null) {
			return "login.html";
		} else if (loggedEmployee.getEmployeeRole().getId() != 2) {
			return "403.html";
		} else if (request.getMethod() == "GET") {
			return "register.html";
		} else {
			Employee employee = new Employee(
					request.getParameter("firstname"),
					request.getParameter("lastname"),
					request.getParameter("username"),
					request.getParameter("password"),
					request.getParameter("email"),
					new EmployeeRole(Integer.parseInt(request.getParameter("employeeRoleID")))
					);
			if (!employeeService.isUsernameTaken(employee)) {
				if (employeeService.createEmployee(employee)) {
					return new ClientMessage("Registration successful");
				}
			} else {
				return new ClientMessage("Username is already taken");
			}
		}
		return new ClientMessage("Registration failed");
	}

	@Override
	public Object updateEmployee(HttpServletRequest request) {
 		logger.trace("Updating employee");
		Employee loggedEmployee = (Employee) request.getSession().getAttribute("employee");
		if (loggedEmployee == null) {
			return "login.html";
		} else if (request.getMethod() == "GET") {
			return "update-employee.html";
		} else {
			logger.trace(loggedEmployee.toString());
			String firstName = request.getParameter("firstName");
			String lastName = request.getParameter("lastName");
			String username = request.getParameter("username");
			String password = request.getParameter("password");
			String email = request.getParameter("email");
			if (firstName == "") {
				firstName = loggedEmployee.getFirstName();
			}
			if (lastName == "") {
				lastName = loggedEmployee.getLastName();
			}
			if (username == "") {
				username = loggedEmployee.getUsername();
			}
			if (password == "") {
				password = null;
			}
			if (email == "") {
				email = loggedEmployee.getEmail();
			}
			Employee employee = new Employee(
					loggedEmployee.getId(),
					firstName,
					lastName,
					username,
					password,
					email,
					loggedEmployee.getEmployeeRole()
					);
			if (employeeService.updateEmployeeInformation(employee)) {
				return new ClientMessage("Update successful");
			}
		}
		return new ClientMessage("Update failed");
	}

	@Override
	public Object viewEmployeeInformation(HttpServletRequest request) {
		logger.trace("View employee information");
		Employee loggedEmployee = (Employee) request.getSession().getAttribute("employee");
		if (loggedEmployee == null) {
			return "login.html";
		} else {
			return employeeService.getEmployeeInformation(loggedEmployee);
		}
	}

	@Override
	public Object viewAllEmployees(HttpServletRequest request) {
		logger.trace("View all employee information");
		Employee loggedEmployee = (Employee) request.getSession().getAttribute("employee");
		if (loggedEmployee == null) {
			return "login.html";
		} else if (loggedEmployee.getEmployeeRole().getId() == 2) {
			return employeeService.getAllEmployeesInformation();
		} else {
			return "403.html";
		}
	}

	@Override
	public Object usernameExists(HttpServletRequest request) {
		logger.trace("Checking if username is taken");
		boolean taken = employeeService.isUsernameTaken(new Employee(request.getParameter("username")));
		if (taken) {
			return new ClientMessage("Username already exists");
		} else {
			return new ClientMessage("Username available");
		}
	}

}

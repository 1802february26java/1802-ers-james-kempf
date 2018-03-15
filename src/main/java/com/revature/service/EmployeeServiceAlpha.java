package com.revature.service;

import java.time.LocalDateTime;
import java.util.Set;

import org.apache.log4j.Logger;

import com.revature.model.Employee;
import com.revature.model.EmployeeRole;
import com.revature.model.EmployeeToken;
import com.revature.repository.EmployeeRepository;
import com.revature.repository.EmployeeRepositoryJdbc;

public class EmployeeServiceAlpha implements EmployeeService {
	
	private static EmployeeService service = new EmployeeServiceAlpha();
	private static Logger logger = Logger.getLogger(EmployeeServiceAlpha.class);
	private EmployeeRepository repository = EmployeeRepositoryJdbc.getInstance();
	
	private EmployeeServiceAlpha() {}
	
	public static EmployeeService getInstance() {
		return service;
	}

	@Override
	public Employee authenticate(Employee employee) {
		return null;
	}

	@Override
	public Employee getEmployeeInformation(Employee employee) {
		logger.trace("Getting Employee Information");
		Employee employeeInfo = repository.select(employee.getId());
		return employeeInfo;
	}

	@Override
	public Set<Employee> getAllEmployeesInformation() {
		logger.trace("Getting all employee information");
		Set<Employee> employees = repository.selectAll();
		return employees;
	}

	@Override
	public boolean createEmployee(Employee employee) {
		logger.trace("Creating employee");
		boolean inserted = repository.insert(employee);
		return inserted;
	}

	@Override
	public boolean updateEmployeeInformation(Employee employee) {
		logger.trace("Updating employee");
		boolean updated = repository.update(employee);
		return updated;
	}

	@Override
	public boolean updatePassword(Employee employee) {
		logger.trace("Updating password");
		boolean updated = repository.update(employee);
		return updated;
	}

	@Override
	public boolean isUsernameTaken(Employee employee) {
		logger.trace("Checking if username taken");
		boolean taken = (repository.select(employee.getUsername()) != null);
		return taken;
	}

	@Override
	public boolean createPasswordToken(Employee employee) {
		logger.trace("Creating password token");
		LocalDateTime now = LocalDateTime.now();
		String token = Integer.toString((employee.toString() + now.toString()).hashCode());
		EmployeeToken employeeToken = new EmployeeToken(0, token, LocalDateTime.now(), employee);
		boolean created = repository.insertEmployeeToken(employeeToken);
		return created;
	}

	@Override
	public boolean deletePasswordToken(EmployeeToken employeeToken) {
		logger.trace("Deleteing password token");
		boolean deleted = repository.deleteEmployeeToken(employeeToken);
		return deleted;
	}

	@Override
	public boolean isTokenExpired(EmployeeToken employeeToken) {
		logger.trace("Cheking if token expired");
		boolean expired = (repository.selectEmployeeToken(employeeToken) == null);
		return expired;
	}
}

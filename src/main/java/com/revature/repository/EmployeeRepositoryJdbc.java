package com.revature.repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Set;

import org.apache.log4j.Logger;

import com.revature.model.Employee;
import com.revature.model.EmployeeRole;
import com.revature.model.EmployeeToken;

public class EmployeeRepositoryJdbc implements EmployeeRepository {

	private static EmployeeRepositoryJdbc repository = new EmployeeRepositoryJdbc();
	private static Logger logger = Logger.getLogger(EmployeeRepositoryJdbc.class);
	
	private EmployeeRepositoryJdbc() {}
	
	public static EmployeeRepositoryJdbc getInstance() {
		return repository;
	}
	
	@Override
	public boolean insert(Employee employee) {
		logger.trace("Inserting new employee");
		try (Connection connection = ConnectionUtil.getConnection()) {
			int parameterIndex = 0;
			String sql = "INSERT INTO USER_T VALUES(?,?,?,?,?,?,?)";
			
			PreparedStatement statement = connection.prepareStatement(sql);
			statement.setInt(++parameterIndex, employee.getId());
			statement.setString(++parameterIndex, employee.getFirstName());
			statement.setString(++parameterIndex, employee.getLastName());
			statement.setString(++parameterIndex, employee.getUsername());
			statement.setString(++parameterIndex, employee.getPassword());
			statement.setString(++parameterIndex, employee.getEmail());
			statement.setInt(++parameterIndex, employee.getEmployeeRole().getId());

			return (statement.executeUpdate() > 0);
		} catch (SQLException e) {
			logger.error("Exception thrown while inserting new employee", e);
		}
		return false;
	}

	@Override
	public boolean update(Employee employee) {
		logger.trace("Updating employee");
		try (Connection connection = ConnectionUtil.getConnection()) {
			int parameterIndex = 0;
			String sql = "UPDATE USER_T SET "
					+ "U_FIRSTNAME = ?, "
					+ "U_LASTNAME = ?, "
					+ "U_USERNAME = ?, "
					+ "U_PASSWORD = ?, "
					+ "U_EMAIL = ?, "
					+ "UR_ID = ? "
					+ "WHERE U_ID = ?";
			logger.trace(sql);
			PreparedStatement statement = connection.prepareStatement(sql);
			statement.setString(++parameterIndex, employee.getFirstName());
			statement.setString(++parameterIndex, employee.getLastName());
			statement.setString(++parameterIndex, employee.getUsername());
			statement.setString(++parameterIndex, employee.getPassword());
			statement.setString(++parameterIndex, employee.getEmail());
			statement.setInt(++parameterIndex, employee.getEmployeeRole().getId());
			statement.setInt(++parameterIndex, employee.getId());

			return (statement.executeUpdate() > 0);
		} catch (SQLException e) {
			logger.error("Exception thrown while updating new employee", e);
		}
		return false;
	}

	@Override
	public Employee select(int employeeId) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Employee select(String username) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Set<Employee> selectAll() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getPasswordHash(Employee employee) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean insertEmployeeToken(EmployeeToken employeeToken) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean deleteEmployeeToken(EmployeeToken employeeToken) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public EmployeeToken selectEmployeeToken(EmployeeToken employeeToken) {
		// TODO Auto-generated method stub
		return null;
	}
	
	public static void main(String[] args) {
		EmployeeRole er = new EmployeeRole(1,"Temp");
		Employee e = new Employee(100,"James","Kempf","jamesk4321","password1","example@gmail.com",er);
		EmployeeRepositoryJdbc repository = EmployeeRepositoryJdbc.getInstance();
		repository.insert(e);
		e.setEmail("NewExample@gmail.com");
		repository.update(e);
		
	}
}

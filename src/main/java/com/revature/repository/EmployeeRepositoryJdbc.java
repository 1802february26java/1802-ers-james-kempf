package com.revature.repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.HashSet;
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
			logger.error("Exception thrown while updating employee", e);
		}
		return false;
	}

	@Override
	public Employee select(int employeeId) {
		logger.trace("Selecting employee");
		try (Connection connection = ConnectionUtil.getConnection()) {
			int parameterIndex = 0;
			String sql = "SELECT * FROM USER_T INNER JOIN USER_ROLE "
					+ "ON USER_T.UR_ID = USER_ROLE.UR_ID "
					+ "WHERE U_ID = ?";
			
			PreparedStatement statement = connection.prepareStatement(sql);
			statement.setInt(++parameterIndex, employeeId);

			ResultSet result = statement.executeQuery();
			
			if (result.next()) {
				return new Employee(
						result.getInt("U_ID"),
						result.getString("U_FIRSTNAME"),
						result.getString("U_LASTNAME"),
						result.getString("U_USERNAME"),
						result.getString("U_PASSWORD"),
						result.getString("U_EMAIL"),
						new EmployeeRole(
								result.getInt("UR_ID"),
								result.getString("UR_TYPE")
								)
						);
			}
		} catch (SQLException e) {
			logger.error("Exception thrown while selecting employee", e);
		}
		return null;
	}

	@Override
	public Employee select(String username) {
		logger.trace("Selecting employee");
		try (Connection connection = ConnectionUtil.getConnection()) {
			int parameterIndex = 0;
			String sql = "SELECT * FROM USER_T INNER JOIN USER_ROLE "
					+ "ON USER_T.UR_ID = USER_ROLE.UR_ID "
					+ "WHERE U_USERNAME = ?";
			
			PreparedStatement statement = connection.prepareStatement(sql);
			statement.setString(++parameterIndex, username);

			ResultSet result = statement.executeQuery();
			
			if (result.next()) {
				return new Employee(
						result.getInt("U_ID"),
						result.getString("U_FIRSTNAME"),
						result.getString("U_LASTNAME"),
						result.getString("U_USERNAME"),
						result.getString("U_PASSWORD"),
						result.getString("U_EMAIL"),
						new EmployeeRole(
								result.getInt("UR_ID"),
								result.getString("UR_TYPE")
								)
						);
			}
		} catch (SQLException e) {
			logger.error("Exception thrown while selecting employee", e);
		}
		return null;
	}

	@Override
	public Set<Employee> selectAll() {
		logger.trace("Selecting all employees");
		try (Connection connection = ConnectionUtil.getConnection()) {
			String sql = "SELECT * FROM USER_T INNER JOIN USER_ROLE "
					+ "ON USER_T.UR_ID = USER_ROLE.UR_ID";
			
			PreparedStatement statement = connection.prepareStatement(sql);

			ResultSet result = statement.executeQuery();
			Set<Employee> employees = new HashSet<>();
			
			while (result.next()) {
				employees.add(new Employee(
						result.getInt("U_ID"),
						result.getString("U_FIRSTNAME"),
						result.getString("U_LASTNAME"),
						result.getString("U_USERNAME"),
						result.getString("U_PASSWORD"),
						result.getString("U_EMAIL"),
						new EmployeeRole(
								result.getInt("UR_ID"),
								result.getString("UR_TYPE")
								)
						));
			}
			return employees;
		} catch (SQLException e) {
			logger.error("Exception thrown while selecting all employees", e);
		}
		return null;
	}

	@Override
	public String getPasswordHash(Employee employee) {
		logger.trace("Getting password hash");
		try(Connection connection = ConnectionUtil.getConnection()) {
			int statementIndex = 0;
			String command = "SELECT GET_HASH(?) AS HASH FROM DUAL";
			PreparedStatement statement = connection.prepareStatement(command);
			statement.setString(++statementIndex, employee.getPassword());
			ResultSet result = statement.executeQuery();

			if(result.next()) {
				return result.getString("HASH");
			}
		} catch (SQLException e) {
			logger.warn("Exception getting customer hash", e);
		} 
		return new String();
	}

	@Override
	public boolean insertEmployeeToken(EmployeeToken employeeToken) {
		logger.trace("Inserting new employee token");
		try (Connection connection = ConnectionUtil.getConnection()) {
			int parameterIndex = 0;
			String sql = "INSERT INTO PASSWORD_RECOVERY VALUES(?,?,?,?)";
			
			PreparedStatement statement = connection.prepareStatement(sql);
			statement.setInt(++parameterIndex, employeeToken.getId());
			statement.setString(++parameterIndex, employeeToken.getToken());
			statement.setTimestamp(++parameterIndex, Timestamp.valueOf(employeeToken.getCreationDate()));
			statement.setInt(++parameterIndex, employeeToken.getRequester().getId());

			return (statement.executeUpdate() > 0);
		} catch (SQLException e) {
			logger.error("Exception thrown while inserting new employee token", e);
		}
		return false;
	}

	@Override
	public boolean deleteEmployeeToken(EmployeeToken employeeToken) {
		logger.trace("Deleting employee token");
		try (Connection connection = ConnectionUtil.getConnection()) {
			int parameterIndex = 0;
			String sql = "DELETE FROM PASSWORD_RECOVERY WHERE PR_ID = ?";
			
			PreparedStatement statement = connection.prepareStatement(sql);
			statement.setInt(++parameterIndex, employeeToken.getId());

			return (statement.executeUpdate() > 0);
		} catch (SQLException e) {
			logger.error("Exception thrown while deleting employee token", e);
		}
		return false;
	}

	@Override
	public EmployeeToken selectEmployeeToken(EmployeeToken employeeToken) {
		logger.trace("Selecting employee token");
		try (Connection connection = ConnectionUtil.getConnection()) {
			int parameterIndex = 0;
			String sql = "SELECT * FROM PASSWORD_RECOVERY INNER JOIN USER_T "
					+ "ON PASSWORD_RECOVERY.U_ID = USER_T.U_ID "
					+ "INNER JOIN USER_ROLE "
					+ "ON USER_T.UR_ID = USER_ROLE.UR_ID "
					+ "WHERE PR_ID = ?";
			
			PreparedStatement statement = connection.prepareStatement(sql);
			statement.setInt(++parameterIndex, employeeToken.getId());

			ResultSet result = statement.executeQuery();
			
			if (result.next()) {
				return new EmployeeToken(
						result.getInt("PR_ID"),
						result.getString("PR_TOKEN"),
						result.getTimestamp("PR_TIME").toLocalDateTime(),
						new Employee(
								result.getInt("U_ID"),
								result.getString("U_FIRSTNAME"),
								result.getString("U_LASTNAME"),
								result.getString("U_USERNAME"),
								result.getString("U_PASSWORD"),
								result.getString("U_EMAIL"),
								new EmployeeRole(
										result.getInt("UR_ID"),
										result.getString("UR_TYPE")
										)
								)
						);
			}
		} catch (SQLException e) {
			logger.error("Exception thrown while selecting employee token", e);
		}
		return null;
	}
	
	public static void main(String[] args) {
		EmployeeRole er = new EmployeeRole(1, "EMPLOYEE");
		Employee e = new Employee(100,"James","Kempf","jamesk4321","p4ssw0rd","example@gmail.com",er);
		EmployeeRepositoryJdbc repository = EmployeeRepositoryJdbc.getInstance();
		
//		logger.trace(repository.insert(e));
		e.setEmail("NewExample@gmail.com");
		logger.trace(repository.update(e));
		logger.trace(repository.select(100).toString());
		logger.trace(repository.select("jamesk4321").toString());
		logger.trace(repository.selectAll());
		logger.trace(repository.getPasswordHash(e));
		
		EmployeeToken et = new EmployeeToken(100, null, LocalDateTime.now(), e);
		String token = et.getRequester().getUsername() + Timestamp.valueOf(et.getCreationDate());
		et.setToken(Integer.toString(token.hashCode()));
		logger.trace(repository.insertEmployeeToken(et));
		logger.trace(repository.selectEmployeeToken(et));
		logger.trace(repository.deleteEmployeeToken(et));
	}
}

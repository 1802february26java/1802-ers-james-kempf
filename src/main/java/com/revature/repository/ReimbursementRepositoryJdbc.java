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
import com.revature.model.Reimbursement;
import com.revature.model.ReimbursementStatus;
import com.revature.model.ReimbursementType;

public class ReimbursementRepositoryJdbc implements ReimbursementRepository {

	private static ReimbursementRepositoryJdbc repository = new ReimbursementRepositoryJdbc();
	private static Logger logger = Logger.getLogger(ReimbursementRepositoryJdbc.class);

	private ReimbursementRepositoryJdbc() {}

	public static ReimbursementRepositoryJdbc getInstance() {
		return repository;
	}

	@Override
	public boolean insert(Reimbursement reimbursement) {
		logger.trace("Inserting new reimbursement");
		try (Connection connection = ConnectionUtil.getConnection()) {
			int parameterIndex = 0;
			String sql = "INSERT INTO REIMBURSEMENT(R_REQUESTED, "
					+ "R_AMOUNT,"
					+ "R_DESCRIPTION, "
					+ "EMPLOYEE_ID, "
					+ "RS_ID, "
					+ "RT_ID) "
					+ "VALUES(?,?,?,?,?,?)";  

			PreparedStatement statement = connection.prepareStatement(sql);
			statement.setTimestamp(++parameterIndex, Timestamp.valueOf(reimbursement.getRequested()));
			statement.setDouble(++parameterIndex, reimbursement.getAmount());
			statement.setString(++parameterIndex, reimbursement.getDescription());
			statement.setInt(++parameterIndex, reimbursement.getRequester().getId());
			statement.setInt(++parameterIndex, reimbursement.getStatus().getId());
			statement.setInt(++parameterIndex, reimbursement.getType().getId());

			return (statement.executeUpdate() > 0);
		} catch (SQLException e) {
			logger.error("Exception thrown while inserting new reimbursement", e);
		}
		return false;
	}

	@Override
	public boolean update(Reimbursement reimbursement) {
		logger.trace("Updating reimbursement");
		try (Connection connection = ConnectionUtil.getConnection()) {
			int parameterIndex = 0;
			String sql = "UPDATE REIMBURSEMENT "
					+ "SET R_REQUESTED = ?, "
					+ "R_RESOLVED = ?, "
					+ "R_AMOUNT = ?, "
					+ "R_DESCRIPTION = ?, "
					+ "EMPLOYEE_ID = ?, "
					+ "MANAGER_ID = ?, "
					+ "RS_ID = ?, "
					+ "RT_ID = ? "
					+ "WHERE R_ID = ?"; 

			PreparedStatement statement = connection.prepareStatement(sql);
			statement.setTimestamp(++parameterIndex, Timestamp.valueOf(reimbursement.getRequested()));
			if (reimbursement.getResolved() != null) {
				statement.setTimestamp(++parameterIndex, Timestamp.valueOf(reimbursement.getResolved()));
			} else {
				statement.setTimestamp(++parameterIndex, null);
			}
			statement.setDouble(++parameterIndex, reimbursement.getAmount());
			statement.setString(++parameterIndex, reimbursement.getDescription());
			statement.setInt(++parameterIndex, reimbursement.getRequester().getId());
			if (reimbursement.getApprover() != null) {
				statement.setInt(++parameterIndex, reimbursement.getApprover().getId());
			} else {
				statement.setNull(++parameterIndex, java.sql.Types.INTEGER);
			}
			statement.setInt(++parameterIndex, reimbursement.getStatus().getId());
			statement.setInt(++parameterIndex, reimbursement.getType().getId());
			statement.setInt(++parameterIndex, reimbursement.getId());

			return (statement.executeUpdate() > 0);
		} catch (SQLException e) {
			logger.error("Exception thrown while Updating reimbursement", e);
		}
		return false;
	}

	@Override
	public Reimbursement select(int reimbursementId) {
		logger.trace("Selecting reimbursement");
		try (Connection connection = ConnectionUtil.getConnection()) {
			int parameterIndex = 0;
			String sql = "SELECT * FROM REIMBURSEMENT R INNER JOIN REIMBURSEMENT_STATUS RS "
					+ "ON R.RS_ID = RS.RS_ID "
					+ "INNER JOIN REIMBURSEMENT_TYPE RT "
					+ "ON R.RT_ID = RT.RT_ID "
					+ "WHERE R.R_ID = ?";  

			PreparedStatement statement = connection.prepareStatement(sql);
			statement.setInt(++parameterIndex, reimbursementId);

			ResultSet result = statement.executeQuery();

			if (result.next()) {
				Reimbursement reimbursement = new Reimbursement(
						result.getInt("R_ID"),
						result.getTimestamp("R_REQUESTED").toLocalDateTime(),
						null,
						result.getDouble("R_AMOUNT"),
						result.getString("R_DESCRIPTION"),
						EmployeeRepositoryJdbc.getInstance().select(result.getInt("EMPLOYEE_ID")),
						EmployeeRepositoryJdbc.getInstance().select(result.getInt("MANAGER_ID")),
						new ReimbursementStatus(
								result.getInt("RS_ID"),
								result.getString("RS_STATUS")
								),
						new ReimbursementType(
								result.getInt("RT_ID"),
								result.getString("RT_TYPE")
								)
						);
				if (result.getString("R_RESOLVED") != null) {
					reimbursement.setResolved(result.getTimestamp("R_RESOLVED").toLocalDateTime());
				}
				return reimbursement;
			}
		} catch (SQLException e) {
			logger.error("Exception thrown while selecting reimbursement", e);
		}
		return null;
	}

	@Override
	public Set<Reimbursement> selectPending(int employeeId) {
		logger.trace("Selecting pending reimbursement");
		try (Connection connection = ConnectionUtil.getConnection()) {
			int parameterIndex = 0;
			String sql = "SELECT * FROM REIMBURSEMENT R INNER JOIN REIMBURSEMENT_STATUS RS "
					+ "ON R.RS_ID = RS.RS_ID "
					+ "INNER JOIN REIMBURSEMENT_TYPE RT "
					+ "ON R.RT_ID = RT.RT_ID "
					+ "WHERE R.EMPLOYEE_ID = ? AND R.RS_ID = ?";

			PreparedStatement statement = connection.prepareStatement(sql);
			statement.setInt(++parameterIndex, employeeId);
			statement.setInt(++parameterIndex, 1);

			ResultSet result = statement.executeQuery();
			Set<Reimbursement> reimbursements = new HashSet<>();
			
			while (result.next()) {
				reimbursements.add(new Reimbursement(
						result.getInt("R_ID"),
						result.getTimestamp("R_REQUESTED").toLocalDateTime(),
						null,
						result.getDouble("R_AMOUNT"),
						result.getString("R_DESCRIPTION"),
						EmployeeRepositoryJdbc.getInstance().select(result.getInt("EMPLOYEE_ID")),
						EmployeeRepositoryJdbc.getInstance().select(result.getInt("MANAGER_ID")),
						new ReimbursementStatus(
								result.getInt("RS_ID"),
								result.getString("RS_STATUS")
								),
						new ReimbursementType(
								result.getInt("RT_ID"),
								result.getString("RT_TYPE")
								)
						));
			}
			return reimbursements;

		} catch (SQLException e) {
			logger.error("Exception thrown while selecting pending reimbursement", e);
		}
		return null;
	}

	@Override
	public Set<Reimbursement> selectFinalized(int employeeId) {
		logger.trace("Selecting finalized reimbursement");
		try (Connection connection = ConnectionUtil.getConnection()) {
			int parameterIndex = 0;
			String sql = "SELECT * FROM REIMBURSEMENT R INNER JOIN REIMBURSEMENT_STATUS RS "
					+ "ON R.RS_ID = RS.RS_ID "
					+ "INNER JOIN REIMBURSEMENT_TYPE RT "
					+ "ON R.RT_ID = RT.RT_ID "
					+ "WHERE R.EMPLOYEE_ID = ? AND (R.RS_ID = ? OR R.RS_ID = ?)";

			PreparedStatement statement = connection.prepareStatement(sql);
			statement.setInt(++parameterIndex, employeeId);
			statement.setInt(++parameterIndex, 2);
			statement.setInt(++parameterIndex, 3);

			ResultSet result = statement.executeQuery();
			Set<Reimbursement> reimbursements = new HashSet<>();
			
			while (result.next()) {
				reimbursements.add(new Reimbursement(
						result.getInt("R_ID"),
						result.getTimestamp("R_REQUESTED").toLocalDateTime(),
						result.getTimestamp("R_RESOLVED").toLocalDateTime(),
						result.getDouble("R_AMOUNT"),
						result.getString("R_DESCRIPTION"),
						EmployeeRepositoryJdbc.getInstance().select(result.getInt("EMPLOYEE_ID")),
						EmployeeRepositoryJdbc.getInstance().select(result.getInt("MANAGER_ID")),
						new ReimbursementStatus(
								result.getInt("RS_ID"),
								result.getString("RS_STATUS")
								),
						new ReimbursementType(
								result.getInt("RT_ID"),
								result.getString("RT_TYPE")
								)
						));
			}
			return reimbursements;
		} catch (SQLException e) {
			logger.error("Exception thrown while selecting finalized reimbursement", e);
		}
		return null;
	}

	@Override
	public Set<Reimbursement> selectAllPending() {
		logger.trace("Selecting all pending reimbursement");
		try (Connection connection = ConnectionUtil.getConnection()) {
			int parameterIndex = 0;
			String sql = "SELECT * FROM REIMBURSEMENT R INNER JOIN REIMBURSEMENT_STATUS RS "
					+ "ON R.RS_ID = RS.RS_ID "
					+ "INNER JOIN REIMBURSEMENT_TYPE RT "
					+ "ON R.RT_ID = RT.RT_ID "
					+ "WHERE R.RS_ID = ?";

			PreparedStatement statement = connection.prepareStatement(sql);
			statement.setInt(++parameterIndex, 1);

			ResultSet result = statement.executeQuery();
			Set<Reimbursement> reimbursements = new HashSet<>();
			
			while (result.next()) {
				reimbursements.add(new Reimbursement(
						result.getInt("R_ID"),
						result.getTimestamp("R_REQUESTED").toLocalDateTime(),
						null,
						result.getDouble("R_AMOUNT"),
						result.getString("R_DESCRIPTION"),
						EmployeeRepositoryJdbc.getInstance().select(result.getInt("EMPLOYEE_ID")),
						EmployeeRepositoryJdbc.getInstance().select(result.getInt("MANAGER_ID")),
						new ReimbursementStatus(
								result.getInt("RS_ID"),
								result.getString("RS_STATUS")
								),
						new ReimbursementType(
								result.getInt("RT_ID"),
								result.getString("RT_TYPE")
								)
						));
			}
			return reimbursements;
		} catch (SQLException e) {
			logger.error("Exception thrown while selecting pending reimbursement", e);
		}
		return null;
	}

	@Override
	public Set<Reimbursement> selectAllFinalized() {
		logger.trace("Selecting all finalized reimbursement");
		try (Connection connection = ConnectionUtil.getConnection()) {
			int parameterIndex = 0;
			String sql = "SELECT * FROM REIMBURSEMENT R INNER JOIN REIMBURSEMENT_STATUS RS "
					+ "ON R.RS_ID = RS.RS_ID "
					+ "INNER JOIN REIMBURSEMENT_TYPE RT "
					+ "ON R.RT_ID = RT.RT_ID "
					+ "WHERE R.RS_ID = ? OR R.RS_ID = ?";

			PreparedStatement statement = connection.prepareStatement(sql);
			statement.setInt(++parameterIndex, 2);
			statement.setInt(++parameterIndex, 3);

			ResultSet result = statement.executeQuery();
			Set<Reimbursement> reimbursements = new HashSet<>();
			
			while (result.next()) {
				reimbursements.add(new Reimbursement(
						result.getInt("R_ID"),
						result.getTimestamp("R_REQUESTED").toLocalDateTime(),
						result.getTimestamp("R_RESOLVED").toLocalDateTime(),
						result.getDouble("R_AMOUNT"),
						result.getString("R_DESCRIPTION"),
						EmployeeRepositoryJdbc.getInstance().select(result.getInt("EMPLOYEE_ID")),
						EmployeeRepositoryJdbc.getInstance().select(result.getInt("MANAGER_ID")),
						new ReimbursementStatus(
								result.getInt("RS_ID"),
								result.getString("RS_STATUS")
								),
						new ReimbursementType(
								result.getInt("RT_ID"),
								result.getString("RT_TYPE")
								)
						));
			}
			return reimbursements;
		} catch (SQLException e) {
			logger.error("Exception thrown while selecting finalized reimbursement", e);
		}
		return null;
	}

	@Override
	public Set<ReimbursementType> selectTypes() {
		logger.trace("Selecting types");
		try (Connection connection = ConnectionUtil.getConnection()) {
			String sql = "SELECT * FROM REIMBURSEMENT_TYPE";

			PreparedStatement statement = connection.prepareStatement(sql);

			ResultSet result = statement.executeQuery();
			Set<ReimbursementType> types = new HashSet<>();
			
			while (result.next()) {
				types.add(new ReimbursementType(
						result.getInt("RT_ID"),
						result.getString("RT_TYPE")
						));
			}
			return types;
		} catch (SQLException e) {
			logger.error("Exception thrown while selecting types", e);
		}
		return null;
	}

	public static void main(String[] args) {
		ReimbursementRepositoryJdbc repository = ReimbursementRepositoryJdbc.getInstance();
		EmployeeRole er = new EmployeeRole(1, "EMPLOYEE");
		Employee e = new Employee(100,"James","Kempf","jamesk4321","password1","example@gmail.com",er);
		ReimbursementStatus rs = new ReimbursementStatus(1,"PENDING");
		ReimbursementType rt = new ReimbursementType(1,"OTHER");
		Reimbursement r = new Reimbursement(100,LocalDateTime.now(),null,10,"Sample",e,null,rs,rt);
		//		logger.trace(repository.insert(r));
		r.setAmount(100);
		logger.trace(repository.update(r));
		logger.trace(repository.select(100));
		logger.trace(repository.selectPending(100));
		logger.trace(repository.selectFinalized(100));
		logger.trace(repository.selectAllPending());
		logger.trace(repository.selectAllFinalized());
		logger.trace(repository.selectTypes());

	}
}

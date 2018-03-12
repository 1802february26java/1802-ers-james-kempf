package com.revature.repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
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
			String sql = "INSERT INTO REIMBURSEMENT(R_ID, "
					+ "R_REQUESTED, "
					+ "R_AMOUNT,"
					+ "R_DESCRIPTION, "
					+ "EMPLOYEE_ID, "
					+ "RS_ID, "
					+ "RT_ID) "
					+ "VALUES(?,?,?,?,?,?,?)";  
			
			PreparedStatement statement = connection.prepareStatement(sql);
			statement.setInt(++parameterIndex, reimbursement.getId());
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
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public Reimbursement select(int reimbursementId) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Set<Reimbursement> selectPending(int employeeId) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Set<Reimbursement> selectFinalized(int employeeId) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Set<Reimbursement> selectAllPending() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Set<Reimbursement> selectAllFinalized() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Set<ReimbursementType> selectTypes() {
		// TODO Auto-generated method stub
		return null;
	}

	public static void main(String[] args) {
		ReimbursementRepositoryJdbc repository = ReimbursementRepositoryJdbc.getInstance();
		EmployeeRole er = new EmployeeRole(1, "EMPLOYEE");
		Employee e = new Employee(100,"James","Kempf","jamesk4321","password1","example@gmail.com",er);
		ReimbursementStatus rs = new ReimbursementStatus(1,"PENDING");
		ReimbursementType rt = new ReimbursementType(1,"OTHER");
		Reimbursement r = new Reimbursement(100,LocalDateTime.now(),null,10,"Sample",e,null,rs,rt);
		System.out.println(r.toString());
		logger.trace(repository.insert(r));
	}
}

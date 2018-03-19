package com.revature.service;

import java.util.Set;

import org.apache.log4j.Logger;

import com.revature.model.Employee;
import com.revature.model.Reimbursement;
import com.revature.model.ReimbursementType;
import com.revature.repository.ReimbursementRepository;
import com.revature.repository.ReimbursementRepositoryJdbc;

public class ReimbursementServiceAlpha implements ReimbursementService {

	private static ReimbursementService service = new ReimbursementServiceAlpha();
	private static Logger logger = Logger.getLogger(ReimbursementServiceAlpha.class);
	private ReimbursementRepository repository = ReimbursementRepositoryJdbc.getInstance();
	
	private ReimbursementServiceAlpha() {}
	
	public static ReimbursementService getInstance() {
		return service;
	}
	
	@Override
	public boolean submitRequest(Reimbursement reimbursement) {
		logger.trace("Submiting reimbursement");
		boolean inserted = repository.insert(reimbursement);
		return inserted;
	}

	@Override
	public boolean finalizeRequest(Reimbursement reimbursement) {
		logger.trace("Finalizing reimbursement");
		boolean updated = repository.update(reimbursement);
		return updated;
	}

	@Override
	public Reimbursement getSingleRequest(Reimbursement reimbursement) {
		logger.trace("Getting reimbursement");
		Reimbursement selectedReimbursement = repository.select(reimbursement.getId());
		return selectedReimbursement;
	}

	@Override
	public Set<Reimbursement> getUserPendingRequests(Employee employee) {
		logger.trace("Getting user pending requests");
		Set<Reimbursement> pendingReimbursements = repository.selectPending(employee.getId());
		return pendingReimbursements;
	}

	@Override
	public Set<Reimbursement> getUserFinalizedRequests(Employee employee) {
		logger.trace("Getting user finalized requests");
		Set<Reimbursement> finalizedReimbursements = repository.selectFinalized(employee.getId());
		return finalizedReimbursements;
	}

	@Override
	public Set<Reimbursement> getAllPendingRequests() {
		logger.trace("Getting all pending requests");
		Set<Reimbursement> pendingReimbursements = repository.selectAllPending();
		return pendingReimbursements;
	}

	@Override
	public Set<Reimbursement> getAllResolvedRequests() {
		logger.trace("Getting all finalized requests");
		Set<Reimbursement> finalizedReimbursements = repository.selectAllFinalized();
		return finalizedReimbursements;
	}

	@Override
	public Set<ReimbursementType> getReimbursementTypes() {
		logger.trace("Getting reimbursement types");
		Set<ReimbursementType> reimbursementTypes = repository.selectTypes();
		return reimbursementTypes;
	}

}

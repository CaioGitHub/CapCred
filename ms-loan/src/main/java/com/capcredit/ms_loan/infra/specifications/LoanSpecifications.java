package com.capcredit.ms_loan.infra.specifications;

import java.util.UUID;

import org.springframework.data.jpa.domain.Specification;

import com.capcredit.ms_loan.domain.enums.LoanStatus;
import com.capcredit.ms_loan.domain.enums.RequestStatus;
import com.capcredit.ms_loan.infra.entities.LoanEntity;

public class LoanSpecifications {
    
    public static Specification<LoanEntity> build() {
        return Specification.unrestricted();
    }

    public static Specification<LoanEntity> withUserId(UUID userId) {
        return (root, query, criteriaBuilder) -> 
        userId == null ? null : criteriaBuilder.equal(root.get("userId"), userId);
    }

	public static Specification<LoanEntity> withLoanStatus(LoanStatus loanStatus) {
		return (root, query, criteriaBuilder) -> 
		    loanStatus == null ? null : criteriaBuilder.equal(root.get("loanStatus"), loanStatus);
	}

	public static Specification<LoanEntity> withRequestStatus(RequestStatus requestStatus) {
		return (root, query, criteriaBuilder) -> 
		    requestStatus == null ? null : criteriaBuilder.equal(root.get("requestStatus"), requestStatus);
	}
}

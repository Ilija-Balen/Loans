package com.fioneer.homework.repository;

import com.fioneer.homework.Entity.IssueLoanRequest;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface IssueLoanRequestRepository  extends JpaRepository<IssueLoanRequest, Long> {

    boolean existsById(Long id);
    @Query(value = "SELECT * from issue_loan_requests where id = :id", nativeQuery = true)
    IssueLoanRequest findByIdCustom(Long id);

    @Query(value = "SELECT status from issue_loan_requests where id = :id", nativeQuery = true)
    String getIssueLoanRequestStatusById(@Param("id")Long issueLoanRequestId);

    @Modifying
    @Transactional
    @Query(value = "UPDATE issue_loan_requests SET status = :status WHERE id = :id", nativeQuery = true)
    int updateStatus(@Param("id")Long issueLoanRequestId, @Param("status")String status);

    @Query(value = "SELECT COUNT(*) FROM issue_loan_requests WHERE loan_typeid = :id", nativeQuery = true)
    int countAllByLoanTypeID(@Param("id") Long loanTypeId);

    @Query(value = "SELECT * FROM issue_loan_requests WHERE status = :status", nativeQuery = true)
    List<IssueLoanRequest> findAllByStatus(@Param("status") String status);
}

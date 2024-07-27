package com.fioneer.homework.repository;

import com.fioneer.homework.Entity.IssueStepsStatus;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface IssueStepsStatusRepository extends JpaRepository<IssueStepsStatus, Long> {

    @Query(value = "SELECT * from issue_steps_statuses where issue_loan_request_id = :id", nativeQuery = true)
    List<IssueStepsStatus> findAllByIssueLoanRequestId(@Param("id")Long issueLoanRequestId);

    @Query(value = "SELECT id from issue_steps_statuses where issue_loan_request_id = :isl_id and loan_step_id = :id", nativeQuery = true)
    Long getIdByLoanStepIdAndIssueLoanRequestId(@Param("isl_id")Long issueLoanRequestId, @Param("id")Long loanStepId);

    @Query(value = "SELECT status from issue_steps_statuses where id = :id", nativeQuery = true)
    String getStepStatus(@Param("id")Long issueStepStatusId);

    @Modifying
    @Transactional
    @Query(value = "UPDATE issue_steps_statuses SET status = :status WHERE id = :id", nativeQuery = true)
    int updateStatus(@Param("id")Long issueStepStatusId, @Param("status")String status);

    @Modifying
    @Transactional
    @Query(value = "UPDATE issue_steps_statuses SET spent_time = :spentTime WHERE id = :id", nativeQuery = true)
    int updateSpentTime(@Param("id")Long issueStepStatusId, @Param("spentTime")int spentTime);

    @Query(value = "SELECT COUNT(*) FROM issue_steps_statuses WHERE status == 'pending' " +
            "AND issue_loan_request_id = :issueLoanRequestId " +
            "AND loan_step_id < :loanStepId", nativeQuery = true)
    int countByStatusAndIssueLoanRequestIdAndLoanStepId(
            @Param("issueLoanRequestId") Long issueLoanRequestId,
            @Param("loanStepId") Long loanStepId);


    @Query(value = "SELECT COUNT(*) FROM issue_steps_statuses WHERE issue_loan_request_id = :issueLoanRequestId", nativeQuery = true)
    int countAllByIssueLoanRequestId(@Param("issueLoanRequestId") Long issueLoanRequestId);


    @Query(value = "SELECT COUNT(*) FROM issue_steps_statuses WHERE status == 'successful' " +
            "AND issue_loan_request_id = :issueLoanRequestId", nativeQuery = true)
    int countAllSuccessfulByIssueLoanRequestId(@Param("issueLoanRequestId") Long issueLoanRequestId);
}

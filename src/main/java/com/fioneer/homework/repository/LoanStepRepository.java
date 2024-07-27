package com.fioneer.homework.repository;

import com.fioneer.homework.Entity.IssueStepsStatus;
import com.fioneer.homework.Entity.LoanStep;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface LoanStepRepository extends JpaRepository<LoanStep, Long> {
    public boolean existsByNameAndAndOrderNumberAndExpectedDurationInDays(String name, Integer orderNumber, Integer expectedDuraInteger);
    @Query(value = "SELECT * from loan_steps where loan_typeid = :id", nativeQuery = true)
    List<LoanStep> findAllByLoanTypeId(@Param("id")Long loanTypeId);
    boolean existsById(Long id);

}

package com.fioneer.homework.repository;

import com.fioneer.homework.Entity.LoanStep;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface LoanStepRepository extends JpaRepository<LoanStep, Long> {
    boolean existsByNameAndAndOrderNumberAndExpectedDurationInDays(String name, Integer orderNumber, Integer expectedDuraInteger);
}

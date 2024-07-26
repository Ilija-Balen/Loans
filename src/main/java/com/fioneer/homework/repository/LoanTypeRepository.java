package com.fioneer.homework.repository;


import com.fioneer.homework.Entity.LoanType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface LoanTypeRepository extends JpaRepository<LoanType, Long> {
    List<LoanType> findByNameContaining(String name);
    @Query(
            value = "SELECT * FROM loan_types",
            nativeQuery = true)
    public List<LoanType> findAll();
    boolean existsByName(String name);
    LoanType findByName(String name);
}
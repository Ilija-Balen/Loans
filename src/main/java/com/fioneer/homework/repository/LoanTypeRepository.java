package com.fioneer.homework.repository;


import com.fioneer.homework.Entity.LoanStep;
import com.fioneer.homework.Entity.LoanType;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface LoanTypeRepository extends JpaRepository<LoanType, Long> {
    List<LoanType> findByNameContaining(String name);
    @Query(value = "SELECT * FROM loan_types", nativeQuery = true)
    public List<LoanType> findAll();
    public boolean existsByName(String name);
    public boolean existsById(Long id);
    public LoanType findByName(String name);
    public void deleteById(Long id);

    @Modifying
    @Transactional
    @Query(value = "update loan_types set name = :name WHERE id = :id", nativeQuery = true)
    public int updateNameById(@Param("id") Long id, @Param("name") String name);

}
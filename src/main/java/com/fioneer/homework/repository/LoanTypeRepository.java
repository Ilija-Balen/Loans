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
    List<LoanType> findAll();
    boolean existsByName(String name);
    boolean existsById(Long id);
    LoanType findByName(String name);
    void deleteById(Long id);
    @Query(value = "SELECT id FROM loan_types where name = :name", nativeQuery = true)
    Long getIdByName(@Param("name") String name);

    @Modifying
    @Transactional
    @Query(value = "update loan_types set name = :name WHERE id = :id", nativeQuery = true)
    int updateNameById(@Param("id") Long id, @Param("name") String name);

}
package com.fioneer.homework.controller;


import com.fioneer.homework.Entity.LoanType;
import com.fioneer.homework.dto.LoanRequest;
import com.fioneer.homework.dto.LoanResponse;
import com.fioneer.homework.service.LoanTypeServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/loan-types")
public class LoanTypeController {

    @Autowired
    private LoanTypeServiceImpl loanTypeServiceImpl;



    @PostMapping
    public LoanResponse createLoanType(@RequestBody LoanRequest loanRequest) {
        return loanTypeServiceImpl.createLoan(loanRequest);
    }
//
//    @GetMapping
//    public List<LoanType> getAllLoanTypes() {
//        return loanTypeServiceImpl.getAllLoanTypes();
//    }
//
//    @GetMapping("/{id}")
//    public Optional<LoanType> getLoanTypeById(@PathVariable Long id) {
//        return loanTypeServiceImpl.getLoanTypeById(id);
//    }
//
    @GetMapping("/search")
    public LoanResponse searchLoanTypesByName(@RequestParam String name) {
        return loanTypeServiceImpl.searchLoanTypesByName(name);
    }
//
//    @DeleteMapping("/{id}")
//    public void deleteLoanType(@PathVariable Long id) {
//        loanTypeServiceImpl.deleteLoanType(id);
//    }
}

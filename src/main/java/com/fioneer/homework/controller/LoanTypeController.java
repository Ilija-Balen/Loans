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

    @GetMapping("/search")
    public LoanResponse searchLoanTypesByName(@RequestParam String name) {
        return loanTypeServiceImpl.searchLoanTypesByName(name);
    }

    @DeleteMapping("/id/{id}")
    public LoanResponse deleteLoanType(@PathVariable Long id) {
        return loanTypeServiceImpl.deleteLoanById(id);
    }

    @DeleteMapping("/name/{name}")
    public LoanResponse deleteLoanType(@PathVariable String name) {
        return loanTypeServiceImpl.deleteLoanByName(name);
    }

    @PutMapping("/update/{id}")
    public LoanResponse updateLoanType(@PathVariable Long id, @RequestBody LoanRequest loanRequest) {
        return loanTypeServiceImpl.updateLoanNameById(id, loanRequest);
    }
}

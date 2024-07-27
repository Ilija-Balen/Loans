package com.fioneer.homework.service;

import com.fioneer.homework.dto.LoanDetails;
import com.fioneer.homework.dto.LoanRequest;
import com.fioneer.homework.dto.LoanResponse;


public interface LoanTypeService {
    public LoanResponse createLoan(LoanRequest loanRequest);
    public LoanResponse searchLoanTypesByName(String name);
    public LoanResponse deleteLoanByName(String name);
    public LoanResponse deleteLoanById(Long id);
    public LoanResponse updateLoanNameById(Long id, LoanRequest loanRequest);
}

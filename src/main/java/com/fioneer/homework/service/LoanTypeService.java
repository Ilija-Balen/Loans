package com.fioneer.homework.service;

import com.fioneer.homework.dto.LoanDetails;
import com.fioneer.homework.dto.LoanRequest;
import com.fioneer.homework.dto.LoanResponse;


public interface LoanTypeService {
    LoanResponse createLoan(LoanRequest LoanRequest);
    LoanResponse searchLoanTypesByName(String name);
}

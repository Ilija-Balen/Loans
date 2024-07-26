package com.fioneer.homework.dto;


import com.fioneer.homework.Entity.LoanType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class LoanResponse {

    private String responseCode;
    private String responseMessage;
    private LoanType loanType;
    private LoanDetails loanDetails;
}

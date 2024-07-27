package com.fioneer.homework.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class IssueRequest {

    private Long loanTypeId;
    private String firstName;
    private String lastName;
    private String loanAmmount;

    private String newStepStatus;
    private int spentTime;
    private Long loanStepId;
    private Long issueLoanRequestId;
}

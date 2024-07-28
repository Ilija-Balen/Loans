package com.fioneer.homework.dto;

import com.fioneer.homework.Entity.IssueStepsStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class IssueDetails {
    private String firstName;
    private String lastName;
    private String loanAmmount;
    private String issueLoanRequestStatus;
    private int totalExpectedTime;
    private int totalSpentTime;
    private List<IssueStepsStatus> issueStepsStatuses;
}

package com.fioneer.homework.service;

import com.fioneer.homework.dto.IssueRequest;
import com.fioneer.homework.dto.IssueResponse;

public interface IssueLoanRequestService {
    IssueResponse createIssue (IssueRequest issueRequest);
    IssueResponse updateStepStatus (IssueRequest issueRequest);
    IssueResponse infoIssueLoanRequest (Long issueLoanRequestId);
    IssueResponse searchByStatus (String status);
}

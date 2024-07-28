package com.fioneer.homework.controller;


import com.fioneer.homework.dto.IssueRequest;
import com.fioneer.homework.dto.IssueResponse;
import com.fioneer.homework.dto.LoanRequest;
import com.fioneer.homework.dto.LoanResponse;
import com.fioneer.homework.service.IssueLoanRequestServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/issue-loan")
public class IssueLoanController {

    @Autowired
    IssueLoanRequestServiceImpl issueLoanRequestService;

    @PostMapping
    public IssueResponse createLoanType(@RequestBody IssueRequest issueRequest) {
        return issueLoanRequestService.createIssue(issueRequest);
    }

    @PutMapping("/update")
    public IssueResponse updateLoanType(@RequestBody IssueRequest issueRequest) {
        return issueLoanRequestService.updateStepStatus(issueRequest);
    }

    @GetMapping("/search")
    public IssueResponse searchLoanTypesByName(@RequestParam Long id) {
        return issueLoanRequestService.infoIssueLoanRequest(id);
    }

    @GetMapping("/searchByStatus")
    public IssueResponse searchLoanTypesByStatus(@RequestParam String status) {
        return issueLoanRequestService.searchByStatus(status);
    }
}

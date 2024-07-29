package com.fioneer.homework.service;

import com.fioneer.homework.Entity.IssueLoanRequest;
import com.fioneer.homework.Entity.IssueStepsStatus;
import com.fioneer.homework.Entity.LoanStep;
import com.fioneer.homework.Entity.LoanType;
import com.fioneer.homework.Utils.IssueLoanEnum;
import com.fioneer.homework.Utils.IssueUtils;
import com.fioneer.homework.Utils.LoanUtils;
import com.fioneer.homework.Utils.StepEnum;
import com.fioneer.homework.dto.IssueDetails;
import com.fioneer.homework.dto.IssueRequest;
import com.fioneer.homework.dto.IssueResponse;
import com.fioneer.homework.dto.LoanResponse;
import com.fioneer.homework.repository.IssueLoanRequestRepository;
import com.fioneer.homework.repository.IssueStepsStatusRepository;
import com.fioneer.homework.repository.LoanStepRepository;
import com.fioneer.homework.repository.LoanTypeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
public class IssueLoanRequestServiceImpl implements IssueLoanRequestService{

    @Autowired
    private IssueLoanRequestRepository issueLoanRequestRepository;
    @Autowired
    private IssueStepsStatusRepository issueStepsStatusRepository;
    @Autowired
    private LoanStepRepository loanStepRepository;
    @Autowired
    private LoanTypeRepository loanTypeRepository;

   @Transactional
    @Override
    public IssueResponse createIssue(IssueRequest issueRequest) {
        //check if data all data is present
        if(issueRequest.getLoanTypeId() == null || issueRequest.getFirstName() == null
                || issueRequest.getLastName() == null || issueRequest.getLoanAmmount() == null)
            return IssueResponse.builder()
                    .responseCode(IssueUtils.ISSUE_DATA_MISSING_CODE)
                    .responseMessage(IssueUtils.ISSUE_DATA_MISSING_MESSAGE)
                    .build();

        //check if LoanType exists
        if(!loanTypeRepository.existsById(issueRequest.getLoanTypeId())) return IssueResponse.builder()
                .responseCode(LoanUtils.LOAN_NOT_EXISTS_CODE)
                .responseMessage(LoanUtils.LOAN_NOT_EXISTS_MESSAGE)
                .build();

        IssueLoanRequest issueLoanRequest = IssueLoanRequest.builder()
                .loanTypeID(issueRequest.getLoanTypeId())
                .firstname(issueRequest.getFirstName())
                .lastname(issueRequest.getLastName())
                .loanAmmount(issueRequest.getLoanAmmount())
                .status(String.valueOf(IssueLoanEnum.processing))
                .build();

        issueLoanRequest = issueLoanRequestRepository.save(issueLoanRequest);

        List<LoanStep> loanStepss = loanStepRepository.findAllByLoanTypeId(issueLoanRequest.getLoanTypeID());

        List<IssueStepsStatus> issueStepsStatusesList = new ArrayList<>();
        for(LoanStep is : loanStepss) {
            issueStepsStatusesList.add(IssueStepsStatus.builder()
                    .LoanStepId(is.getId())
                    .IssueLoanRequestId(issueLoanRequest.getId())
                    .status(String.valueOf(StepEnum.pending))
                    .expectedDurationInDays(is.getExpectedDurationInDays())
                    .build());
        }
       issueStepsStatusRepository.saveAll(issueStepsStatusesList);

        return IssueResponse.builder()
                .responseCode(IssueUtils.ISSUE_CREATED_CODE)
                .responseMessage(IssueUtils.ISSUE_CREATED_MESSAGE)
                .build();
    }

    @Transactional
    @Override
    public IssueResponse updateStepStatus(IssueRequest issueRequest) {

        //check if loanStep exists
        if(!loanStepRepository.existsById(issueRequest.getLoanStepId())) return IssueResponse.builder()
                .responseCode(LoanUtils.STEP_NOT_EXISTS_CODE)
                .responseMessage(LoanUtils.STEP_NOT_EXISTS_MESSAGE)
                .build();

        String newStepStatus = issueRequest.getNewStepStatus();
        //check if status is successful or failed
        switch (newStepStatus){
            case "successful":
            case "failed":
                break;
            default: return IssueResponse.builder()
                    .responseCode(LoanUtils.STEP_ENUM_NOT_EXISTS_CODE)
                    .responseMessage(LoanUtils.STEP_ENUM_NOT_EXISTS_MESSAGE)
                    .build();
        }

        //check if provided duration is legal
        int spentTime = issueRequest.getSpentTime();
        if( spentTime <= 0)
            return IssueResponse.builder()
                    .responseCode(IssueUtils.ISSUE_SPENT_TIME_NULL_CODE)
                    .responseMessage(IssueUtils.ISSUE_SPENT_TIME_NULL_MESSAGE)
                    .build();

        //check if status of this step is pending
        Long loanStepId = issueRequest.getLoanStepId();
        Long issueLoanRequestId = issueRequest.getIssueLoanRequestId();
        Long issueStepStatusId = issueStepsStatusRepository.getIdByLoanStepIdAndIssueLoanRequestId(
                issueLoanRequestId, loanStepId);
        String status = issueStepsStatusRepository.getStepStatus(issueStepStatusId);
        if(status.equals(String.valueOf(StepEnum.successful)) || status.equals(String.valueOf(StepEnum.failed)))
            return IssueResponse.builder()
                .responseCode(LoanUtils.STEP_STATUS_COMPLETED_CODE)
                .responseMessage(LoanUtils.STEP_STATUS_COMPLETED_MESSAGE)
                .build();

        //check if previous steps have been successfully implemented
        if(issueStepsStatusRepository.countByStatusAndIssueLoanRequestIdAndLoanStepId(
                issueLoanRequestId,loanStepId) > 0)
            return IssueResponse.builder()
                    .responseCode(LoanUtils.PREVIOUS_STEPS_NOT_FINISHED_CODE)
                    .responseMessage(LoanUtils.PREVIOUS_STEPS_NOT_FINISHED_MESSAGE)
                    .build();



        //update this status and spentTime
        issueStepsStatusRepository.updateStatusAndSpentTime(issueStepStatusId, newStepStatus, spentTime);

        //check if loan request status can be updated
        String statusOfIssueLoanRequest = issueLoanRequestRepository.getIssueLoanRequestStatusById(issueLoanRequestId);
        if(!statusOfIssueLoanRequest.equals(String.valueOf(IssueLoanEnum.rejected)) &&
                !statusOfIssueLoanRequest.equals(String.valueOf(IssueLoanEnum.approved))){
            if(issueRequest.getNewStepStatus().equals(String.valueOf(StepEnum.failed))){
                issueLoanRequestRepository.updateStatus(issueLoanRequestId, String.valueOf(IssueLoanEnum.rejected));
            }else{
                if(issueStepsStatusRepository.countAllByIssueLoanRequestId(issueLoanRequestId)
                        == issueStepsStatusRepository.countAllSuccessfulByIssueLoanRequestId(issueLoanRequestId)){
                    issueLoanRequestRepository.updateStatus(issueLoanRequestId, String.valueOf(IssueLoanEnum.approved));
                }
            }
        }

        return IssueResponse.builder()
                .responseCode(LoanUtils.STEP_STATUS_UPDATED_CODE)
                .responseMessage(LoanUtils.STEP_STATUS_UPDATED_MESSAGE)
                .build();
    }

    @Override
    public IssueResponse infoIssueLoanRequest(Long issueLoanRequestId) {

       IssueLoanRequest foundLoanRequest = issueLoanRequestRepository.findByIdCustom(issueLoanRequestId);
        if(foundLoanRequest == null) return IssueResponse.builder()
                .responseCode(IssueUtils.ISSUE_NOT_EXISTS_CODE)
                .responseMessage(IssueUtils.ISSUE_NOT_EXISTS_MESSAGE)
                .issueDetails(null)
                .build();

        List<IssueStepsStatus> issueStepsStatuses= issueStepsStatusRepository.findAllByIssueLoanRequestId(
                issueLoanRequestId);

        int totalExpectedTime = 0;
        int totalSpentTime = 0;
        for(IssueStepsStatus is : issueStepsStatuses){
            totalExpectedTime += is.getExpectedDurationInDays();
            totalSpentTime += is.getSpentTime();
        }


        IssueDetails issueDetails = IssueDetails.builder()
                .firstName(foundLoanRequest.getFirstname())
                .lastName(foundLoanRequest.getLastname())
                .loanAmmount(foundLoanRequest.getLoanAmmount())
                .issueLoanRequestStatus(foundLoanRequest.getStatus())
                .totalExpectedTime(totalExpectedTime)
                .totalSpentTime(totalSpentTime)
                .issueStepsStatuses(issueStepsStatuses)
                .build();

        return IssueResponse.builder()
                .responseCode(IssueUtils.ISSUE_FOUND_CODE)
                .responseMessage(IssueUtils.ISSUE_FOUND_MESSAGE)
                .issueDetails(issueDetails)
                .build();
    }

    @Override
    public IssueResponse searchByStatus(String status) {

        List<IssueLoanRequest> issueLoanRequests = issueLoanRequestRepository.findAllByStatus(status);

        List<IssueDetails> issueDetailsList = new ArrayList<>();
        for(IssueLoanRequest issueLoanRequest : issueLoanRequests){
            issueDetailsList.add(infoIssueLoanRequest(issueLoanRequest.getId()).getIssueDetails());
        }

        if(issueDetailsList.size() <= 0) return IssueResponse.builder()
                .responseCode(IssueUtils.ISSUE_NOT_EXISTS_STATUS_CODE)
                .responseMessage(IssueUtils.ISSUE_NOT_EXISTS_STATUS_MESSAGE + status)
                .build();

        return IssueResponse.builder()
                .responseCode(IssueUtils.ISSUE_FOUND_CODE)
                .responseMessage(IssueUtils.ISSUE_FOUND_MESSAGE)
                .issueDetailsList(issueDetailsList)
                .build();
    }
}

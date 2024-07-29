package com.fioneer.homework.service;


import com.fioneer.homework.Entity.LoanStep;
import com.fioneer.homework.Entity.LoanType;
import com.fioneer.homework.Utils.LoanUtils;
import com.fioneer.homework.dto.LoanDetails;
import com.fioneer.homework.dto.LoanRequest;
import com.fioneer.homework.dto.LoanResponse;
import com.fioneer.homework.repository.IssueLoanRequestRepository;
import com.fioneer.homework.repository.LoanStepRepository;
import com.fioneer.homework.repository.LoanTypeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;


@Service
public class LoanTypeServiceImpl  implements LoanTypeService{

    @Autowired
    private LoanTypeRepository loanTypeRepository;

    @Autowired
    private LoanStepRepository loanStepRepository;
    @Autowired
    private IssueLoanRequestRepository issueLoanRequestRepository;

    @Transactional
    @Override
    public LoanResponse createLoan(LoanRequest loanRequest) {
        //check if data all data is present
        if(loanRequest.getName() == null) return LoanResponse.builder()
                .responseCode(LoanUtils.LOAN_DATA_MISSING_CODE)
                .responseMessage(LoanUtils.LOAN_DATA_MISSING_MESSAGE)
                .loanType(null)
                .build();
        for(LoanStep l : loanRequest.getSteps()) if(l.getName() == null || l.getOrderNumber() <= 0
                || l.getExpectedDurationInDays() <= 0) return LoanResponse.builder()
                .responseCode(LoanUtils.LOAN_DATA_MISSING_CODE)
                .responseMessage(LoanUtils.LOAN_DATA_MISSING_MESSAGE)
                .loanType(null)
                .build();

        //name of loanType must be unique
        if(loanTypeRepository.existsByName(loanRequest.getName())) return LoanResponse.builder()
                .responseCode(LoanUtils.LOAN_EXISTS_CODE)
                .responseMessage(LoanUtils.LOAN_EXISTS_MESSAGE)
                .loanType(null)
                .build();

        //check if there is 0 steps
        if(loanRequest.getSteps() == null) return LoanResponse.builder()
                .responseCode(LoanUtils.STEP_CANT_BE_NULL_CODE)
                .responseMessage(LoanUtils.STEP_CANT_BE_NULL_MESSAGE)
                .loanType(null)
                .build();

        //check if steps are associated with any other loanType
        for(LoanStep l: loanRequest.getSteps()){
            if(loanStepRepository.existsByNameAndAndOrderNumberAndExpectedDurationInDays
                    (l.getName(), l.getOrderNumber(), l.getExpectedDurationInDays()))
                return LoanResponse.builder()
                    .responseCode(LoanUtils.STEP_EXISTS_CODE)
                    .responseMessage(LoanUtils.STEP_EXISTS_MESSAGE)
                    .loanType(null)
                    .build();
        }

        //create loanType
        LoanType loanType = LoanType.builder()
                .name(loanRequest.getName())
                .build();

        //save loanType
        loanTypeRepository.save(loanType);

        List<LoanStep> loanStepList = new ArrayList<>();
        //save all steps required for loanType
        for(LoanStep l: loanRequest.getSteps()){
            loanStepList.add(LoanStep.builder()
                            .name(l.getName())
                            .orderNumber(l.getOrderNumber())
                            .expectedDurationInDays(l.getExpectedDurationInDays())
                            .loanTypeID(loanType.getId())
                            .build());
        }
        loanStepRepository.saveAll(loanStepList);

        return LoanResponse.builder()
                .responseCode(LoanUtils.LOAN_CREATED_CODE)
                .responseMessage(LoanUtils.LOAN_CREATED_MESSAGE)
                .loanType(loanType)
                .build();
    }

    @Override
    public LoanResponse searchLoanTypesByName(String name) {
        LoanType loanType = loanTypeRepository.findByName(name);
        if(loanType == null) return LoanResponse.builder()
                .responseCode(LoanUtils.LOAN_NOT_EXISTS_CODE)
                .responseMessage(LoanUtils.LOAN_NOT_EXISTS_MESSAGE)
                .loanType(null)
                .loanDetails(null)
                .build();

        int totalDuration = 0;
        for(LoanStep l: loanType.getSteps()){
            totalDuration += l.getExpectedDurationInDays();
        }
        LoanDetails loanDetails = LoanDetails.builder()
                .loanTypeName(loanType.getName())
                .totalDuration("Total duration of all steps is: " + totalDuration)
                .loanSteps(loanType.getSteps())
                .build();

        return LoanResponse.builder()
                .responseCode(LoanUtils.LOAN_FOUND_CODE)
                .responseMessage(LoanUtils.LOAN_FOUND_MESSAGE)
                .loanType(null)
                .loanDetails(loanDetails)
                .build();
    }

    @Override
    public LoanResponse deleteLoanByName(String name) {
        LoanType loanType = loanTypeRepository.findByName(name);
        if(loanType == null) return LoanResponse.builder()
                .responseCode(LoanUtils.LOAN_NOT_EXISTS_CODE)
                .responseMessage(LoanUtils.LOAN_NOT_EXISTS_MESSAGE)
                .loanType(null)
                .loanDetails(null)
                .build();

        //check if there is loan request issued for this loan type
        if(issueLoanRequestRepository.countAllByLoanTypeID(loanType.getId()) != 0) return LoanResponse.builder()
                .responseCode(LoanUtils.LOAN_EXISTS_IN_ISSUE_CODE)
                .responseMessage(LoanUtils.LOAN_EXISTS_IN_ISSUE_MESSAGE)
                .loanType(null)
                .loanDetails(null)
                .build();

        loanTypeRepository.deleteById(loanType.getId());

        return LoanResponse.builder()
                .responseCode(LoanUtils.LOAN_DELETED_CODE)
                .responseMessage(LoanUtils.LOAN_DELETED_MESSAGE)
                .loanType(null)
                .loanDetails(null)
                .build();
    }

    @Override
    public LoanResponse deleteLoanById(Long id) {
        if(!loanTypeRepository.existsById(id)) return LoanResponse.builder()
                .responseCode(LoanUtils.LOAN_NOT_EXISTS_CODE)
                .responseMessage(LoanUtils.LOAN_NOT_EXISTS_MESSAGE)
                .loanType(null)
                .loanDetails(null)
                .build();

        //check if there is loan request issued for this loan type
        if(issueLoanRequestRepository.countAllByLoanTypeID(id) != 0) return LoanResponse.builder()
                .responseCode(LoanUtils.LOAN_EXISTS_IN_ISSUE_CODE)
                .responseMessage(LoanUtils.LOAN_EXISTS_IN_ISSUE_MESSAGE)
                .loanType(null)
                .loanDetails(null)
                .build();

        loanTypeRepository.deleteById(id);

        return LoanResponse.builder()
                .responseCode(LoanUtils.LOAN_DELETED_CODE)
                .responseMessage(LoanUtils.LOAN_DELETED_MESSAGE)
                .loanType(null)
                .loanDetails(null)
                .build();
    }

    @Override
    public LoanResponse updateLoanNameById(Long id, LoanRequest loanRequest) {
        if(!loanTypeRepository.existsById(id)) return LoanResponse.builder()
                .responseCode(LoanUtils.LOAN_NOT_EXISTS_CODE)
                .responseMessage(LoanUtils.LOAN_NOT_EXISTS_MESSAGE)
                .loanType(null)
                .loanDetails(null)
                .build();

        //check if there is loan request issued for this loan type
        if(issueLoanRequestRepository.countAllByLoanTypeID(id) != 0) return LoanResponse.builder()
                .responseCode(LoanUtils.LOAN_EXISTS_IN_ISSUE_CODE)
                .responseMessage(LoanUtils.LOAN_EXISTS_IN_ISSUE_MESSAGE)
                .loanType(null)
                .loanDetails(null)
                .build();

        loanTypeRepository.updateNameById(id, loanRequest.getName());

        return LoanResponse.builder()
                .responseCode(LoanUtils.LOAN_UPDATED_CODE)
                .responseMessage(LoanUtils.LOAN_UPDATED_MESSAGE)
                .loanType(null)
                .loanDetails(null)
                .build();
    }

}
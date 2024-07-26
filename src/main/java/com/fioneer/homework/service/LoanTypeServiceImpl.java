package com.fioneer.homework.service;


import com.fioneer.homework.Entity.LoanStep;
import com.fioneer.homework.Entity.LoanType;
import com.fioneer.homework.Utils.LoanUtils;
import com.fioneer.homework.dto.LoanDetails;
import com.fioneer.homework.dto.LoanRequest;
import com.fioneer.homework.dto.LoanResponse;
import com.fioneer.homework.repository.LoanStepRepository;
import com.fioneer.homework.repository.LoanTypeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;



@Service
public class LoanTypeServiceImpl  implements LoanTypeService{

    @Autowired
    private LoanTypeRepository loanTypeRepository;

    @Autowired
    private LoanStepRepository loanStepRepository;

    @Override
    public LoanResponse createLoan(LoanRequest loanRequest) {
        //name of loanType must be unique
        if(loanTypeRepository.existsByName(loanRequest.getName())) return LoanResponse.builder()
                .responseCode(LoanUtils.LOAN_EXISTS_CODE)
                .responseMessage(LoanUtils.LOAN_EXISTS_MESSAGE)
                .loanType(null)
                .build();

        //check if steps are associated with any other loanType
//        for(LoanStep l: loanRequest.getSteps()){
//            if(loanStepRepository.existsByNameAndAndOrderNumberAndExpectedDurationInDays
//                    (l.getName(), l.getOrderNumber(), l.getExpectedDurationInDays()))
//                return LoanResponse.builder()
//                    .responseCode(LoanUtils.STEP_EXISTS_CODE)
//                    .responseMessage(LoanUtils.STEP_EXISTS_MESSAGE)
//                    .loanType(null)
//                    .build();
//        }

        //create loanType
        LoanType loanType = LoanType.builder()
                .name(loanRequest.getName())
                .build();

        //save loanType
        loanTypeRepository.save(loanType);

        //save all steps required for loanType
        for(LoanStep l: loanRequest.getSteps()){
            loanStepRepository.save(LoanStep.builder()
                            .name(l.getName())
                            .orderNumber(l.getOrderNumber())
                            .expectedDurationInDays(l.getExpectedDurationInDays())
                            .loanTypeID(loanType.getId())
                            .build());
        }


        return LoanResponse.builder()
                .responseCode(LoanUtils.LOAN_CREATED_CODE)
                .responseMessage(LoanUtils.LOAN_CREATED_MESSAGE)
                .loanType(loanType)
                .build();
    }

    @Override
    public LoanResponse searchLoanTypesByName(String name) {

        if(!loanTypeRepository.existsByName(name)) return LoanResponse.builder()
                .responseCode(LoanUtils.LOAN_NOT_EXISTS_CODE)
                .responseMessage(LoanUtils.LOAN_NOT_EXISTS_MESSAGE)
                .loanType(null)
                .loanDetails(null)
                .build();

        LoanType loanType = loanTypeRepository.findByName(name);

        int combinedDuration = 0;
        for(LoanStep l: loanType.getSteps()){
            combinedDuration += l.getExpectedDurationInDays();
            System.out.println("ime " + l.getName());
        }
        LoanDetails loanDetails = LoanDetails.builder()
                .durationCombined("Combined duration of all steps is: " + combinedDuration)
                .loanSteps(loanType.getSteps())
                .build();

        return LoanResponse.builder()
                .responseCode(LoanUtils.LOAN_FOUND_CODE)
                .responseMessage(LoanUtils.LOAN_FOUND_MESSAGE)
                .loanType(null)
                .loanDetails(loanDetails)
                .build();
    }


//
//    public LoanType saveLoanType(LoanType loanType) {
//
//
//        return loanTypeRepository.save(loanType);
//    }
//
//    public List<LoanType> getAllLoanTypes() {
//        return loanTypeRepository.findAll();
//    }
//
//    public Optional<LoanType> getLoanTypeById(Long id) {
//        return loanTypeRepository.findById(id);
//    }
//
//    public List<LoanType> searchLoanTypesByName(String name) {
//        return loanTypeRepository.findByNameContaining(name);
//    }
//
//    public void deleteLoanType(Long id) {
//        loanTypeRepository.deleteById(id);
//    }
}
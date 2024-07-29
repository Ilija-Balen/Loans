package com.fioneer.homework.dto;


import com.fioneer.homework.Entity.LoanStep;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class LoanDetails {

    private String loanTypeName;
    private String totalDuration;
    private List<LoanStep> loanSteps;
}

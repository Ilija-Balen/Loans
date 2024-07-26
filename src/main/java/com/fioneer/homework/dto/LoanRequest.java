package com.fioneer.homework.dto;


import com.fioneer.homework.Entity.LoanStep;
import jakarta.persistence.CascadeType;
import jakarta.persistence.OneToMany;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class LoanRequest {

    private String name;
    private List<LoanStep> steps;

}

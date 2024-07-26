package com.fioneer.homework.Entity;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name="LoanSteps")
@Entity
public class LoanStep {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private int orderNumber;
    private int expectedDurationInDays;

    private Long loanTypeID;

}
package com.fioneer.homework.Entity;


import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name="IssueStepsStatuses")
@Entity
public class IssueStepsStatus {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String status;
    private Long IssueLoanRequestId;
    private Long LoanStepId;
    private int spentTime;

    private int expectedDurationInDays;
}

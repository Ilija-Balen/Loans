package com.fioneer.homework.Entity;


import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name="LoanTypes")
@Entity
public class LoanType {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String name;

    @OneToMany(mappedBy = "loanTypeID", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<LoanStep> steps;

    // Getters and Setters
}
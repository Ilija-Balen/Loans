package com.fioneer.homework.Entity;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name="IssueLoanRequests")
@Entity
public class IssueLoanRequest {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String firstname;
    private String lastname;
    private String loanAmmount;
    private String status;

    private Long loanTypeID;

}

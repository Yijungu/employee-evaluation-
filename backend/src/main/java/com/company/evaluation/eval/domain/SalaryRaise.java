package com.company.evaluation.eval.domain;

import com.company.evaluation.employee.Employee;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "salary_raise")
@Getter
@Setter
@NoArgsConstructor
public class SalaryRaise {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "employee_id")
    private Employee employee;

    @Column(name = "raise_percent", nullable = false)
    private Double raisePercent;

    @Column(name = "raised_salary", nullable = false)
    private Long raisedSalary;
}



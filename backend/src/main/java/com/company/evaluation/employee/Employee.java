package com.company.evaluation.employee;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "employee")
@Getter
@Setter
@NoArgsConstructor
public class Employee {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "emp_no", nullable = false, unique = true)
    private String employeeNumber;

    @Column(nullable = false)
    private String name;

    @Column(name = "join_year", nullable = false)
    private Integer joinYear;

    @Column(name = "base_salary", nullable = false)
    private Long baseSalary;

    @Column(nullable = false)
    private String position;

    @Column(name = "work_status", nullable = false)
    private String workStatus;

    @Column(name = "work_region", nullable = false)
    private String workRegion;

    private String email;
    private String phone;
    private String notes;
}




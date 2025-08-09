package com.company.evaluation.eval;

import com.company.evaluation.employee.Employee;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "employee_memo")
@Getter
@Setter
@NoArgsConstructor
public class EmployeeMemo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "employee_id")
    private Employee employee;

    private String projectName;
    private String period;
    @Column(columnDefinition = "TEXT")
    private String detail;
}



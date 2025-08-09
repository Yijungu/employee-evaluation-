package com.company.evaluation.eval;

import com.company.evaluation.employee.Employee;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(
        name = "evaluation",
        uniqueConstraints = {
                @UniqueConstraint(name = "uk_employee_item", columnNames = {"employee_id", "eval_item_id"})
        }
)
@Getter
@Setter
@NoArgsConstructor
public class Evaluation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "employee_id")
    private Employee employee;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "eval_item_id")
    private EvalItem item;

    @Column(nullable = false)
    private Integer score;
}



package com.company.evaluation.eval.repository;

import com.company.evaluation.eval.domain.SalaryRaise;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface SalaryRaiseRepository extends JpaRepository<SalaryRaise, Long> {
    Optional<SalaryRaise> findTopByEmployeeIdOrderByIdDesc(Long employeeId);
}



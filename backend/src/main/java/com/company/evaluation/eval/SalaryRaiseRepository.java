package com.company.evaluation.eval;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface SalaryRaiseRepository extends JpaRepository<SalaryRaise, Long> {
    Optional<SalaryRaise> findTopByEmployeeIdOrderByIdDesc(Long employeeId);
}



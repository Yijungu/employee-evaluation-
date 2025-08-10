package com.company.evaluation.eval;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface EmployeeMemoRepository extends JpaRepository<EmployeeMemo, Long> {
    List<EmployeeMemo> findByEmployeeId(Long employeeId);
}



package com.company.evaluation.eval.repository;

import com.company.evaluation.eval.domain.EmployeeMemo;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface EmployeeMemoRepository extends JpaRepository<EmployeeMemo, Long> {
    List<EmployeeMemo> findByEmployeeId(Long employeeId);
}



package com.company.evaluation.eval;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

interface EvalCategoryRepository extends JpaRepository<EvalCategory, Long> {}
interface EvalItemRepository extends JpaRepository<EvalItem, Long> {}
interface EvaluationRepository extends JpaRepository<Evaluation, Long> {
    List<Evaluation> findByEmployeeId(Long employeeId);
    Evaluation findByEmployeeIdAndItemId(Long employeeId, Long itemId);
}
interface EmployeeMemoRepository extends JpaRepository<EmployeeMemo, Long> {
    List<EmployeeMemo> findByEmployeeId(Long employeeId);
}
interface SalaryRaiseRepository extends JpaRepository<SalaryRaise, Long> {
    Optional<SalaryRaise> findTopByEmployeeIdOrderByIdDesc(Long employeeId);
}


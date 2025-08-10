package com.company.evaluation.eval;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.List;

public interface EvaluationRepository extends JpaRepository<Evaluation, Long> {
    List<Evaluation> findByEmployeeId(Long employeeId);
    Evaluation findByEmployeeIdAndItemId(Long employeeId, Long itemId);

    @Query("select e.employee.id as employeeId, sum(e.score) as totalScore from Evaluation e where e.employee.id in :ids group by e.employee.id")
    List<Object[]> findScoreSumByEmployeeIds(@Param("ids") List<Long> employeeIds);
}



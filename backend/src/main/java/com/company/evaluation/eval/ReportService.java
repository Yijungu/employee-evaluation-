package com.company.evaluation.eval;

import com.company.evaluation.employee.Employee;
import com.company.evaluation.employee.EmployeeRepository;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class ReportService {
    private final EmployeeRepository employeeRepository;
    private final EvaluationRepository evaluationRepository;
    private final SalaryRaiseRepository salaryRaiseRepository;

    public ReportService(EmployeeRepository employeeRepository, EvaluationRepository evaluationRepository, SalaryRaiseRepository salaryRaiseRepository) {
        this.employeeRepository = employeeRepository;
        this.evaluationRepository = evaluationRepository;
        this.salaryRaiseRepository = salaryRaiseRepository;
    }

    public ReportResponse buildReport() {
        List<Employee> employees = employeeRepository.findAll();
        List<Long> ids = employees.stream().map(Employee::getId).toList();

        Map<Long, Integer> scoreSum = new HashMap<>();
        for (Object[] row : evaluationRepository.findScoreSumByEmployeeIds(ids)) {
            Long empId = (Long) row[0];
            Number sum = (Number) row[1];
            scoreSum.put(empId, sum != null ? sum.intValue() : null);
        }

        Map<Long, Long> lastRaised = new HashMap<>();
        for (Long id : ids) {
            Long raised = salaryRaiseRepository.findTopByEmployeeIdOrderByIdDesc(id)
                    .map(SalaryRaise::getRaisedSalary).orElse(null);
            lastRaised.put(id, raised);
        }

        List<ReportEmployeeDto> rows = employees.stream()
                .map(e -> new ReportEmployeeDto(
                        e.getId(),
                        e.getName(),
                        e.getBaseSalary(),
                        lastRaised.get(e.getId()),
                        scoreSum.get(e.getId())
                )).toList();

        long working = employees.stream().filter(e -> "근무".equals(e.getWorkStatus())).count();
        long retired = employees.stream().filter(e -> "퇴직".equals(e.getWorkStatus())).count();
        long totalBase = rows.stream().mapToLong(r -> Optional.ofNullable(r.getBaseSalary()).orElse(0L)).sum();
        long totalRaised = rows.stream().mapToLong(r -> Optional.ofNullable(r.getRaisedSalary()).orElse(r.getBaseSalary())).sum();

        double avgRaise = 0.0;
        long counted = rows.stream().filter(r -> r.getRaisedSalary() != null && r.getBaseSalary() != null && r.getBaseSalary() > 0).count();
        if (counted > 0) {
            double sumPct = rows.stream()
                    .filter(r -> r.getRaisedSalary() != null && r.getBaseSalary() != null && r.getBaseSalary() > 0)
                    .mapToDouble(r -> (r.getRaisedSalary() - r.getBaseSalary()) * 100.0 / r.getBaseSalary())
                    .sum();
            avgRaise = Math.round((sumPct / counted) * 10.0) / 10.0;
        }

        return new ReportResponse(working, retired, avgRaise, totalBase, totalRaised, rows);
    }
}



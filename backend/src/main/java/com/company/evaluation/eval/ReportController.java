package com.company.evaluation.eval;

import com.company.evaluation.common.ApiResponse;
import com.company.evaluation.employee.Employee;
import com.company.evaluation.employee.EmployeeRepository;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/report")
@CrossOrigin
public class ReportController {
    private final EmployeeRepository employeeRepository;
    private final EvaluationRepository evaluationRepository;
    private final SalaryRaiseRepository salaryRaiseRepository;

    public ReportController(EmployeeRepository employeeRepository, EvaluationRepository evaluationRepository, SalaryRaiseRepository salaryRaiseRepository) {
        this.employeeRepository = employeeRepository;
        this.evaluationRepository = evaluationRepository;
        this.salaryRaiseRepository = salaryRaiseRepository;
    }

    @GetMapping
    public ApiResponse<ReportResponse> report() {
        List<Employee> employees = employeeRepository.findAll();

        List<ReportEmployeeDto> rows = employees.stream().map(e -> {
            Integer sumScore = evaluationRepository.findByEmployeeId(e.getId()).stream()
                    .map(Evaluation::getScore).reduce(Integer::sum).orElse(null);
            Long raised = salaryRaiseRepository.findTopByEmployeeIdOrderByIdDesc(e.getId())
                    .map(SalaryRaise::getRaisedSalary).orElse(null);
            return new ReportEmployeeDto(e.getId(), e.getName(), e.getBaseSalary(), raised, sumScore);
        }).toList();

        long working = employees.stream().filter(e -> "근무".equals(e.getWorkStatus())).count();
        long retired = employees.stream().filter(e -> "퇴직".equals(e.getWorkStatus())).count();
        long totalBase = rows.stream().mapToLong(r -> r.getBaseSalary() == null ? 0 : r.getBaseSalary()).sum();
        long totalRaised = rows.stream().mapToLong(r -> r.getRaisedSalary() == null ? r.getBaseSalary() : r.getRaisedSalary()).sum();

        double avgRaise = 0.0;
        long counted = rows.stream().filter(r -> r.getRaisedSalary() != null && r.getBaseSalary() != null && r.getBaseSalary() > 0).count();
        if (counted > 0) {
            double sumPct = rows.stream()
                    .filter(r -> r.getRaisedSalary() != null && r.getBaseSalary() != null && r.getBaseSalary() > 0)
                    .mapToDouble(r -> (r.getRaisedSalary() - r.getBaseSalary()) * 100.0 / r.getBaseSalary())
                    .sum();
            avgRaise = Math.round((sumPct / counted) * 10.0) / 10.0; // one decimal
        }

        ReportResponse resp = new ReportResponse(
                working,
                retired,
                avgRaise,
                totalBase,
                totalRaised,
                rows
        );
        return ApiResponse.ok(resp);
    }
}



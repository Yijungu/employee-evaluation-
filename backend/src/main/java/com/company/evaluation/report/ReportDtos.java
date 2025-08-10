package com.company.evaluation.report;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
class ReportEmployeeDto {
    private Long id;
    private String name;
    private Long baseSalary;
    private Long raisedSalary; // null if none
    private Integer score;     // null if none
}

@Data
@NoArgsConstructor
@AllArgsConstructor
class ReportResponse {
    private long workingCount;
    private long retiredCount;
    private double avgRaisePercent;
    private long totalBasePayroll;
    private long totalRaisedPayroll;
    private List<ReportEmployeeDto> employees;
}




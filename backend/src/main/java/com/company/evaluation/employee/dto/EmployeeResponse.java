package com.company.evaluation.employee.dto;

import lombok.Data;

@Data
public class EmployeeResponse {
    private Long id;
    private String employeeNumber;
    private String name;
    private Integer joinYear;
    private Long baseSalary;
    private String position;
    private String workStatus;
    private String workRegion;
    private String email;
    private String phone;
    private String notes;
}



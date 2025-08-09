package com.company.evaluation.employee;

import jakarta.validation.constraints.*;
import lombok.Data;

@Data
public class EmployeeDto {
    private Long id;

    @NotBlank
    private String employeeNumber;

    @NotBlank
    private String name;

    @NotNull
    @Min(1900)
    private Integer joinYear;

    @NotNull
    @Min(0)
    private Long baseSalary;

    @NotBlank
    private String position;

    @NotBlank
    private String workStatus;

    @NotBlank
    private String workRegion;

    @Email
    private String email;
    private String phone;
    private String notes;
}




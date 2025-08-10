package com.company.evaluation.employee.dto;

import jakarta.validation.constraints.*;
import lombok.Data;

@Data
public class EmployeeRequest {
    private Long id; // 업데이트 시 사용(선택)

    @NotBlank
    private String employeeNumber;

    @NotBlank
    private String name;

    @NotNull
    @Min(1900)
    private Integer joinYear;

    @NotNull
    @PositiveOrZero
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



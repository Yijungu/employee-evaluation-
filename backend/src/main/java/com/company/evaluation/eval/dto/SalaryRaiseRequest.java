package com.company.evaluation.eval.dto;

import jakarta.validation.constraints.*;
import lombok.Data;

@Data
public class SalaryRaiseRequest {
    @NotNull @PositiveOrZero
    private Double raisePercent;

    @NotNull @PositiveOrZero
    private Long raisedSalary;
}



package com.company.evaluation.eval.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class EvaluationUpsertRequest {
    @NotNull
    private Long itemId;
    @NotNull @Min(0)
    private Integer score;
}



package com.company.evaluation.eval.dto;

import jakarta.validation.constraints.*;
import lombok.Data;

@Data
public class EvalItemRequest {
    @NotBlank
    private String name;

    @NotNull @Min(0)
    private Integer maxScore;

    @NotNull
    private Boolean enabled;

    private String description;

    private Long categoryId;
}



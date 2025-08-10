package com.company.evaluation.eval.dto;

import lombok.Data;

@Data
public class EvalItemResponse {
    private Long id;
    private String name;
    private Integer maxScore;
    private Boolean enabled;
    private String description;
    private Long categoryId;
    private String categoryName;
}



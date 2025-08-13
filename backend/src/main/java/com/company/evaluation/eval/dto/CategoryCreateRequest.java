package com.company.evaluation.eval.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class CategoryCreateRequest {
    @NotBlank(message = "카테고리명은 필수입니다")
    private String name;
}



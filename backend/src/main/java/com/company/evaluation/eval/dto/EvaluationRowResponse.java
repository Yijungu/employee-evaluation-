package com.company.evaluation.eval.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EvaluationRowResponse {
    private Long itemId;
    private String itemName;
    private Integer score;
}



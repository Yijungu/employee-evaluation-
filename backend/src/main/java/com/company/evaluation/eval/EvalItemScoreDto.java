package com.company.evaluation.eval;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import com.company.evaluation.eval.domain.Evaluation;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EvalItemScoreDto {
    private Long itemId;
    private String itemName;
    private Integer score;

    public static EvalItemScoreDto from(Evaluation e) {
        return new EvalItemScoreDto(
                e.getItem() != null ? e.getItem().getId() : null,
                e.getItem() != null ? e.getItem().getName() : null,
                e.getScore()
        );
    }
}



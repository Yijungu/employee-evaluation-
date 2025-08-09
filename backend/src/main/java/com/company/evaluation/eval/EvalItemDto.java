package com.company.evaluation.eval;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EvalItemDto {
    private Long id;
    private String name;
    private Integer maxScore;
    private Boolean enabled;
    private String description;
    private Long categoryId;
    private String categoryName;

    public static EvalItemDto from(EvalItem e) {
        Long catId = e.getCategory() != null ? e.getCategory().getId() : null;
        String catName = e.getCategory() != null ? e.getCategory().getName() : null;
        return new EvalItemDto(e.getId(), e.getName(), e.getMaxScore(), e.getEnabled(), e.getDescription(), catId, catName);
    }
}



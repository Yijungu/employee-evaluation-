package com.company.evaluation.eval.mapper;

import com.company.evaluation.eval.EvalItem;
import com.company.evaluation.eval.EvalCategory;
import com.company.evaluation.eval.dto.EvalItemRequest;
import com.company.evaluation.eval.dto.EvalItemResponse;
import org.mapstruct.*;

@Mapper(componentModel = "spring")
public interface EvalItemMapper {
    @Mapping(target = "category", expression = "java(toCategory(req.getCategoryId()))")
    EvalItem toEntity(EvalItemRequest req);

    @Mapping(target = "categoryId", source = "category.id")
    @Mapping(target = "categoryName", source = "category.name")
    EvalItemResponse toResponse(EvalItem entity);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "category", expression = "java(toCategory(req.getCategoryId()))")
    void updateEntity(@MappingTarget EvalItem target, EvalItemRequest req);

    default EvalCategory toCategory(Long id) {
        if (id == null) return null;
        EvalCategory c = new EvalCategory();
        c.setId(id);
        return c;
    }
}



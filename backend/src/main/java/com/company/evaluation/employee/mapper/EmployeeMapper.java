package com.company.evaluation.employee.mapper;

import com.company.evaluation.employee.Employee;
import com.company.evaluation.employee.dto.EmployeeRequest;
import com.company.evaluation.employee.dto.EmployeeResponse;
import org.mapstruct.*;

@Mapper(componentModel = "spring")
public interface EmployeeMapper {

    Employee toEntity(EmployeeRequest req);

    EmployeeResponse toResponse(Employee entity);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateEntity(@MappingTarget Employee target, EmployeeRequest source);
}



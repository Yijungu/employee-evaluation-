package com.company.evaluation.employee;

import com.company.evaluation.employee.dto.EmployeeRequest;
import com.company.evaluation.employee.dto.EmployeeResponse;
import com.company.evaluation.employee.mapper.EmployeeMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

@Service
public class EmployeeService {
    private final EmployeeRepository repository;
    private final EmployeeMapper mapper;

    public EmployeeService(EmployeeRepository repository, EmployeeMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    public Page<EmployeeResponse> search(String name, String status, String region, int page, int size) {
        String n = (name == null || name.isBlank()) ? null : name;
        String s = (status == null || status.isBlank()) ? null : status;
        String r = (region == null || region.isBlank()) ? null : region;
        return repository.search(n, s, r, PageRequest.of(page, size))
                .map(mapper::toResponse);
    }

    public EmployeeResponse save(EmployeeRequest req) {
        Employee entity = (req.getId() != null)
                ? repository.findById(req.getId()).orElse(new Employee())
                : new Employee();
        if (entity.getId() == null) {
            entity = mapper.toEntity(req);
        } else {
            mapper.updateEntity(entity, req);
        }
        return mapper.toResponse(repository.save(entity));
    }
}




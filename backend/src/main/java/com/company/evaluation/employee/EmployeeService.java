package com.company.evaluation.employee;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

@Service
public class EmployeeService {
    private final EmployeeRepository repository;

    public EmployeeService(EmployeeRepository repository) {
        this.repository = repository;
    }

    public Page<Employee> search(String name, String status, String region, int page, int size) {
        String n = (name == null || name.isBlank()) ? null : name;
        String s = (status == null || status.isBlank()) ? null : status;
        String r = (region == null || region.isBlank()) ? null : region;
        return repository.search(n, s, r, PageRequest.of(page, size));
    }

    public Employee save(EmployeeDto dto) {
        Employee entity = dto.getId() != null ? repository.findById(dto.getId()).orElse(new Employee()) : new Employee();
        entity.setEmployeeNumber(dto.getEmployeeNumber());
        entity.setName(dto.getName());
        entity.setJoinYear(dto.getJoinYear());
        entity.setBaseSalary(dto.getBaseSalary());
        entity.setPosition(dto.getPosition());
        entity.setWorkStatus(dto.getWorkStatus());
        entity.setWorkRegion(dto.getWorkRegion());
        entity.setEmail(dto.getEmail());
        entity.setPhone(dto.getPhone());
        entity.setNotes(dto.getNotes());
        return repository.save(entity);
    }
}




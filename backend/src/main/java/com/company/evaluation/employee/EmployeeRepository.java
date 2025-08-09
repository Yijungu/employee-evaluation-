package com.company.evaluation.employee;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface EmployeeRepository extends JpaRepository<Employee, Long> {

    @Query("SELECT e FROM Employee e WHERE (:name IS NULL OR e.name LIKE %:name%) " +
           "AND (:status IS NULL OR e.workStatus = :status) " +
           "AND (:region IS NULL OR e.workRegion = :region)")
    Page<Employee> search(@Param("name") String name,
                          @Param("status") String status,
                          @Param("region") String region,
                          Pageable pageable);
}




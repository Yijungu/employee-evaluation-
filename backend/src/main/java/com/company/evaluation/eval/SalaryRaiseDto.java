package com.company.evaluation.eval;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import com.company.evaluation.eval.domain.SalaryRaise;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SalaryRaiseDto {
    private Double raisePercent;
    private Long raisedSalary;

    public static SalaryRaiseDto from(SalaryRaise r) {
        SalaryRaiseDto dto = new SalaryRaiseDto();
        dto.setRaisePercent(r.getRaisePercent());
        dto.setRaisedSalary(r.getRaisedSalary());
        return dto;
    }
}



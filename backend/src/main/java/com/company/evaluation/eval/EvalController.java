package com.company.evaluation.eval;

import com.company.evaluation.common.ApiResponse;
import com.company.evaluation.eval.dto.EvalItemRequest;
import com.company.evaluation.eval.dto.EvalItemResponse;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
@CrossOrigin
public class EvalController {
    private final EvalService service;

    public EvalController(EvalService service) {
        this.service = service;
    }

    // Categories
    @GetMapping("/categories")
    public ApiResponse<List<EvalCategory>> categories() { return ApiResponse.ok(service.listCategories()); }

    @PostMapping("/categories")
    public ApiResponse<EvalCategory> createCategory(@RequestBody @Valid EvalCategory c) { return ApiResponse.ok(service.createCategory(c)); }

    // Items
    @GetMapping("/items")
    public ApiResponse<List<EvalItemResponse>> items() { return ApiResponse.ok(service.listItems()); }

    @PutMapping("/items/{id}")
    public ApiResponse<EvalItemResponse> updateItem(@PathVariable("id") Long id, @RequestBody @Valid EvalItemRequest i) { return ApiResponse.ok(service.updateItem(id, i)); }

    @PostMapping("/items")
    public ApiResponse<EvalItemResponse> createItem(@RequestBody @Valid EvalItemRequest i) { return ApiResponse.ok(service.createItem(i)); }

    // Evaluations (EAV)
    @PostMapping("/employees/{id}/evaluations")
    public ApiResponse<List<Evaluation>> saveEvaluations(@PathVariable("id") Long employeeId,
                                                         @RequestBody List<Evaluation> evaluations) { return ApiResponse.ok(service.saveEvaluations(employeeId, evaluations)); }

    // Fetch evaluations per employee (for showing saved rows)
    @GetMapping("/employees/{id}/evaluations")
    public ApiResponse<List<com.company.evaluation.eval.dto.EvaluationRowResponse>> getEvaluations(@PathVariable("id") Long employeeId) { return ApiResponse.ok(service.getEvaluations(employeeId)); }

    // Memo
    @PostMapping("/employees/{id}/memos")
    public ApiResponse<EmployeeMemo> addMemo(@PathVariable("id") Long employeeId, @RequestBody @Valid EmployeeMemo memo) { return ApiResponse.ok(service.saveMemo(employeeId, memo)); }

    // Salary raise
    @PostMapping("/employees/{id}/raise")
    public ApiResponse<com.company.evaluation.eval.dto.SalaryRaiseResponse> saveRaise(@PathVariable("id") Long employeeId,
                                              @RequestBody @NotNull @Valid com.company.evaluation.eval.dto.SalaryRaiseRequest raise) { return ApiResponse.ok(service.saveRaise(employeeId, raise)); }

    @GetMapping("/employees/{id}/raise")
    public ApiResponse<com.company.evaluation.eval.dto.SalaryRaiseResponse> getRaise(@PathVariable("id") Long employeeId) { return ApiResponse.ok(service.getRaise(employeeId)); }
}



package com.company.evaluation.eval;

import com.company.evaluation.common.ApiResponse;
import com.company.evaluation.employee.Employee;
import com.company.evaluation.employee.EmployeeRepository;
import jakarta.validation.constraints.NotNull;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
@CrossOrigin
public class EvalController {
    private final EvalCategoryRepository categoryRepo;
    private final EvalItemRepository itemRepo;
    private final EvaluationRepository evaluationRepo;
    private final EmployeeMemoRepository memoRepo;
    private final SalaryRaiseRepository raiseRepo;
    private final EmployeeRepository employeeRepo;

    public EvalController(EvalCategoryRepository categoryRepo, EvalItemRepository itemRepo,
                          EvaluationRepository evaluationRepo, EmployeeMemoRepository memoRepo,
                          SalaryRaiseRepository raiseRepo, EmployeeRepository employeeRepo) {
        this.categoryRepo = categoryRepo;
        this.itemRepo = itemRepo;
        this.evaluationRepo = evaluationRepo;
        this.memoRepo = memoRepo;
        this.raiseRepo = raiseRepo;
        this.employeeRepo = employeeRepo;
    }

    // Categories
    @GetMapping("/categories")
    public ApiResponse<List<EvalCategory>> categories() {
        return ApiResponse.ok(categoryRepo.findAll());
    }

    @PostMapping("/categories")
    public ApiResponse<EvalCategory> createCategory(@RequestBody EvalCategory c) {
        return ApiResponse.ok(categoryRepo.save(c));
    }

    // Items
    @GetMapping("/items")
    public ApiResponse<List<EvalItemDto>> items() {
        return ApiResponse.ok(itemRepo.findAll().stream().map(EvalItemDto::from).toList());
    }

    @PutMapping("/items/{id}")
    public ApiResponse<EvalItemDto> updateItem(@PathVariable("id") Long id, @RequestBody EvalItemDto i) {
        EvalItem exist = itemRepo.findById(id).orElseThrow();
        exist.setName(i.getName());
        exist.setMaxScore(i.getMaxScore());
        exist.setEnabled(Boolean.TRUE.equals(i.getEnabled()));
        if (i.getCategoryId() != null) {
            exist.setCategory(categoryRepo.findById(i.getCategoryId()).orElse(null));
        }
        exist.setDescription(i.getDescription());
        return ApiResponse.ok(EvalItemDto.from(itemRepo.save(exist)));
    }

    @PostMapping("/items")
    public ApiResponse<EvalItemDto> createItem(@RequestBody EvalItemDto i) {
        EvalItem entity = new EvalItem();
        entity.setName(i.getName());
        entity.setMaxScore(i.getMaxScore());
        entity.setEnabled(Boolean.TRUE.equals(i.getEnabled()));
        entity.setDescription(i.getDescription());
        if (i.getCategoryId() != null) {
            entity.setCategory(categoryRepo.findById(i.getCategoryId()).orElse(null));
        }
        EvalItem saved = itemRepo.save(entity);
        return ApiResponse.ok(EvalItemDto.from(saved));
    }

    // Evaluations (EAV)
    @PostMapping("/employees/{id}/evaluations")
    public ApiResponse<List<Evaluation>> saveEvaluations(@PathVariable("id") Long employeeId,
                                                         @RequestBody List<Evaluation> evaluations) {
        Employee emp = employeeRepo.findById(employeeId).orElseThrow();
        for (Evaluation ev : evaluations) {
            ev.setEmployee(emp);
            if (ev.getItem() != null && ev.getItem().getId() != null) {
                EvalItem it = itemRepo.findById(ev.getItem().getId()).orElseThrow();
                ev.setItem(it);
            }
            if (ev.getItem() == null || ev.getItem().getId() == null) {
                continue;
            }
            // 중복 방지: 같은 (employee, item) 존재하면 업데이트, 없으면 생성
            Evaluation existing = evaluationRepo.findByEmployeeIdAndItemId(employeeId, ev.getItem().getId());
            if (existing != null) {
                existing.setScore(ev.getScore());
                evaluationRepo.save(existing);
            } else {
                evaluationRepo.save(ev);
            }
        }
        // 반환: 최신 상태 목록
        List<Evaluation> latest = evaluationRepo.findByEmployeeId(employeeId);
        return ApiResponse.ok(latest);
    }

    // Fetch evaluations per employee (for showing saved rows)
    @GetMapping("/employees/{id}/evaluations")
    public ApiResponse<List<EvalItemScoreDto>> getEvaluations(@PathVariable("id") Long employeeId) {
        List<EvalItemScoreDto> rows = evaluationRepo.findByEmployeeId(employeeId)
                .stream().map(EvalItemScoreDto::from).toList();
        return ApiResponse.ok(rows);
    }

    // Memo
    @PostMapping("/employees/{id}/memos")
    public ApiResponse<EmployeeMemo> addMemo(@PathVariable("id") Long employeeId, @RequestBody EmployeeMemo memo) {
        memo.setEmployee(employeeRepo.findById(employeeId).orElseThrow());
        return ApiResponse.ok(memoRepo.save(memo));
    }

    // Salary raise
    @PostMapping("/employees/{id}/raise")
    public ApiResponse<SalaryRaise> saveRaise(@PathVariable("id") Long employeeId,
                                              @RequestBody @NotNull SalaryRaise raise) {
        raise.setEmployee(employeeRepo.findById(employeeId).orElseThrow());
        return ApiResponse.ok(raiseRepo.save(raise));
    }

    @GetMapping("/employees/{id}/raise")
    public ApiResponse<SalaryRaiseDto> getRaise(@PathVariable("id") Long employeeId) {
        return ApiResponse.ok(
                raiseRepo.findTopByEmployeeIdOrderByIdDesc(employeeId)
                        .map(SalaryRaiseDto::from).orElse(null)
        );
    }
}



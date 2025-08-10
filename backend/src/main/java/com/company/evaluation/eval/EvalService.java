package com.company.evaluation.eval;

import com.company.evaluation.employee.Employee;
import com.company.evaluation.employee.EmployeeRepository;
import com.company.evaluation.eval.dto.EvalItemRequest;
import com.company.evaluation.eval.dto.EvalItemResponse;
import com.company.evaluation.eval.dto.EvaluationRowResponse;
import com.company.evaluation.eval.dto.SalaryRaiseRequest;
import com.company.evaluation.eval.dto.SalaryRaiseResponse;
import com.company.evaluation.eval.mapper.EvalItemMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class EvalService {
    private final EvalCategoryRepository categoryRepo;
    private final EvalItemRepository itemRepo;
    private final EvaluationRepository evaluationRepo;
    private final EmployeeMemoRepository memoRepo;
    private final SalaryRaiseRepository raiseRepo;
    private final EmployeeRepository employeeRepo;
    private final EvalItemMapper itemMapper;

    public EvalService(EvalCategoryRepository categoryRepo, EvalItemRepository itemRepo,
                       EvaluationRepository evaluationRepo, EmployeeMemoRepository memoRepo,
                       SalaryRaiseRepository raiseRepo, EmployeeRepository employeeRepo,
                       EvalItemMapper itemMapper) {
        this.categoryRepo = categoryRepo;
        this.itemRepo = itemRepo;
        this.evaluationRepo = evaluationRepo;
        this.memoRepo = memoRepo;
        this.raiseRepo = raiseRepo;
        this.employeeRepo = employeeRepo;
        this.itemMapper = itemMapper;
    }

    // Categories
    public List<EvalCategory> listCategories() { return categoryRepo.findAll(); }
    public EvalCategory createCategory(EvalCategory c) { return categoryRepo.save(c); }

    // Items
    public List<EvalItemResponse> listItems() { return itemRepo.findAll().stream().map(itemMapper::toResponse).toList(); }
    public EvalItemResponse createItem(EvalItemRequest i) {
        EvalItem entity = itemMapper.toEntity(i);
        return itemMapper.toResponse(itemRepo.save(entity));
    }
    public EvalItemResponse updateItem(Long id, EvalItemRequest i) {
        EvalItem exist = itemRepo.findById(id).orElseThrow();
        itemMapper.updateEntity(exist, i);
        return itemMapper.toResponse(itemRepo.save(exist));
    }

    // Evaluations
    public List<Evaluation> saveEvaluations(Long employeeId, List<Evaluation> evaluations) {
        Employee emp = employeeRepo.findById(employeeId).orElseThrow();
        for (Evaluation ev : evaluations) {
            ev.setEmployee(emp);
            if (ev.getItem() != null && ev.getItem().getId() != null) {
                EvalItem it = itemRepo.findById(ev.getItem().getId()).orElseThrow();
                ev.setItem(it);
            }
            if (ev.getItem() == null || ev.getItem().getId() == null) continue;
            Evaluation existing = evaluationRepo.findByEmployeeIdAndItemId(employeeId, ev.getItem().getId());
            if (existing != null) {
                existing.setScore(ev.getScore());
                evaluationRepo.save(existing);
            } else {
                evaluationRepo.save(ev);
            }
        }
        return evaluationRepo.findByEmployeeId(employeeId);
    }

    public List<EvaluationRowResponse> getEvaluations(Long employeeId) {
        return evaluationRepo.findByEmployeeId(employeeId).stream()
                .map(e -> new EvaluationRowResponse(
                        e.getItem() != null ? e.getItem().getId() : null,
                        e.getItem() != null ? e.getItem().getName() : null,
                        e.getScore()
                )).toList();
    }

    public EmployeeMemo saveMemo(Long employeeId, EmployeeMemo memo) {
        memo.setEmployee(employeeRepo.findById(employeeId).orElseThrow());
        return memoRepo.save(memo);
    }

    public SalaryRaiseResponse saveRaise(Long employeeId, SalaryRaiseRequest req) {
        SalaryRaise raise = new SalaryRaise();
        raise.setEmployee(employeeRepo.findById(employeeId).orElseThrow());
        raise.setRaisePercent(req.getRaisePercent());
        raise.setRaisedSalary(req.getRaisedSalary());
        SalaryRaise saved = raiseRepo.save(raise);
        SalaryRaiseResponse res = new SalaryRaiseResponse();
        res.setRaisePercent(saved.getRaisePercent());
        res.setRaisedSalary(saved.getRaisedSalary());
        return res;
    }

    public SalaryRaiseResponse getRaise(Long employeeId) {
        return raiseRepo.findTopByEmployeeIdOrderByIdDesc(employeeId)
                .map(s -> {
                    SalaryRaiseResponse r = new SalaryRaiseResponse();
                    r.setRaisePercent(s.getRaisePercent());
                    r.setRaisedSalary(s.getRaisedSalary());
                    return r;
                })
                .orElse(null);
    }
}



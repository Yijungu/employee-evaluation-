package com.company.evaluation.eval;

import com.company.evaluation.employee.Employee;
import com.company.evaluation.employee.EmployeeRepository;
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

    public EvalService(EvalCategoryRepository categoryRepo, EvalItemRepository itemRepo,
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
    public List<EvalCategory> listCategories() { return categoryRepo.findAll(); }
    public EvalCategory createCategory(EvalCategory c) { return categoryRepo.save(c); }

    // Items
    public List<EvalItemDto> listItems() { return itemRepo.findAll().stream().map(EvalItemDto::from).toList(); }
    public EvalItemDto createItem(EvalItemDto i) {
        EvalItem entity = new EvalItem();
        entity.setName(i.getName());
        entity.setMaxScore(i.getMaxScore());
        entity.setEnabled(Boolean.TRUE.equals(i.getEnabled()));
        entity.setDescription(i.getDescription());
        if (i.getCategoryId() != null) entity.setCategory(categoryRepo.findById(i.getCategoryId()).orElse(null));
        return EvalItemDto.from(itemRepo.save(entity));
    }
    public EvalItemDto updateItem(Long id, EvalItemDto i) {
        EvalItem exist = itemRepo.findById(id).orElseThrow();
        exist.setName(i.getName());
        exist.setMaxScore(i.getMaxScore());
        exist.setEnabled(Boolean.TRUE.equals(i.getEnabled()));
        exist.setDescription(i.getDescription());
        if (i.getCategoryId() != null) exist.setCategory(categoryRepo.findById(i.getCategoryId()).orElse(null));
        return EvalItemDto.from(itemRepo.save(exist));
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

    public List<EvalItemScoreDto> getEvaluations(Long employeeId) {
        return evaluationRepo.findByEmployeeId(employeeId).stream().map(EvalItemScoreDto::from).toList();
    }

    public EmployeeMemo saveMemo(Long employeeId, EmployeeMemo memo) {
        memo.setEmployee(employeeRepo.findById(employeeId).orElseThrow());
        return memoRepo.save(memo);
    }

    public SalaryRaise saveRaise(Long employeeId, SalaryRaise raise) {
        raise.setEmployee(employeeRepo.findById(employeeId).orElseThrow());
        return raiseRepo.save(raise);
    }

    public SalaryRaiseDto getRaise(Long employeeId) {
        return raiseRepo.findTopByEmployeeIdOrderByIdDesc(employeeId).map(SalaryRaiseDto::from).orElse(null);
    }
}



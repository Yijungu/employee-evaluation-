package com.company.evaluation.employee;

import com.company.evaluation.common.ApiResponse;
import com.company.evaluation.employee.dto.EmployeeRequest;
import com.company.evaluation.employee.dto.EmployeeResponse;
import jakarta.validation.Valid;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/employees")
@CrossOrigin
public class EmployeeController {
    private final EmployeeService service;

    public EmployeeController(EmployeeService service) {
        this.service = service;
    }

    @GetMapping
    public ApiResponse<Page<EmployeeResponse>> search(@RequestParam(name = "name", required = false) String name,
                                              @RequestParam(name = "status", required = false) String status,
                                              @RequestParam(name = "region", required = false) String region,
                                              @RequestParam(name = "page", defaultValue = "0") int page,
                                              @RequestParam(name = "size", defaultValue = "20") int size) {
        return ApiResponse.ok(service.search(name, status, region, page, size));
    }

    @PostMapping
    public ApiResponse<EmployeeResponse> save(@Valid @RequestBody EmployeeRequest dto) {
        return ApiResponse.ok(service.save(dto));
    }

    // Excel 업로드: 첫 행 헤더, 컬럼 순서 예시
    // 사번 | 사원명 | 입사년도 | 급여 | 직급 | 근무상태 | 근무지역 | 이메일 | 전화번호 | 메모
    @PostMapping(value = "/upload", consumes = {"multipart/form-data"})
    public ApiResponse<Integer> upload(@RequestPart("file") MultipartFile file) throws Exception {
        int saved = 0;
        try (InputStream is = file.getInputStream(); Workbook wb = new XSSFWorkbook(is)) {
            Sheet sheet = wb.getSheetAt(0);
            List<EmployeeRequest> rows = new ArrayList<>();
            boolean header = true;
            for (Row row : sheet) {
                if (header) { header = false; continue; }
                if (row == null) continue;
                EmployeeRequest dto = new EmployeeRequest();
                dto.setEmployeeNumber(getString(row, 0));
                dto.setName(getString(row, 1));
                dto.setJoinYear(getInt(row, 2));
                dto.setBaseSalary(getLong(row, 3));
                dto.setPosition(getString(row, 4));
                dto.setWorkStatus(getString(row, 5));
                dto.setWorkRegion(getString(row, 6));
                dto.setEmail(getString(row, 7));
                dto.setPhone(getString(row, 8));
                dto.setNotes(getString(row, 9));
                if (dto.getEmployeeNumber() != null && !dto.getEmployeeNumber().isBlank()) {
                    rows.add(dto);
                }
            }
            for (EmployeeRequest dto : rows) {
                service.save(dto);
                saved++;
            }
        }
        return ApiResponse.ok(saved);
    }

    private static String getString(Row row, int idx) {
        if (row.getCell(idx) == null) return null;
        row.getCell(idx).setCellType(org.apache.poi.ss.usermodel.CellType.STRING);
        String s = row.getCell(idx).getStringCellValue();
        return s != null ? s.trim() : null;
    }
    private static Integer getInt(Row row, int idx) {
        try { return (int) Math.round(row.getCell(idx).getNumericCellValue()); } catch (Exception e) { return null; }
    }
    private static Long getLong(Row row, int idx) {
        try { return (long) Math.round(row.getCell(idx).getNumericCellValue()); } catch (Exception e) { return null; }
    }

    
}




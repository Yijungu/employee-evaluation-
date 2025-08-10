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
    private final EmployeeExcelService excelService;

    public EmployeeController(EmployeeService service, EmployeeExcelService excelService) {
        this.service = service;
        this.excelService = excelService;
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
        return ApiResponse.ok(excelService.importExcel(file));
    }

    
}




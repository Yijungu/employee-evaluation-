package com.company.evaluation.employee;

import com.company.evaluation.employee.dto.EmployeeRequest;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

@Service
public class EmployeeExcelService {
    private final EmployeeService employeeService;

    public EmployeeExcelService(EmployeeService employeeService) {
        this.employeeService = employeeService;
    }

    public int importExcel(MultipartFile file) throws Exception {
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
                employeeService.save(dto);
                saved++;
            }
        }
        return saved;
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



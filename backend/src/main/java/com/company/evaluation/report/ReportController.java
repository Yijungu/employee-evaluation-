package com.company.evaluation.report;

import com.company.evaluation.common.ApiResponse;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/report")
@CrossOrigin
public class ReportController {
    private final ReportService reportService;

    public ReportController(ReportService reportService) {
        this.reportService = reportService;
    }

    @GetMapping
    public ApiResponse<ReportResponse> report() {
        return ApiResponse.ok(reportService.buildReport());
    }
}




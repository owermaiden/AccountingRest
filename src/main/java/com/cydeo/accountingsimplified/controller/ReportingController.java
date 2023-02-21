package com.cydeo.accountingsimplified.controller;

import com.cydeo.accountingsimplified.dto.ResponseWrapper;
import com.cydeo.accountingsimplified.service.ReportingService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/reports")
public class ReportingController {

    private final ReportingService reportingService;

    public ReportingController( ReportingService reportingService) {
        this.reportingService = reportingService;
    }

    @GetMapping("/stockData")
    public ResponseEntity<ResponseWrapper> getStockData() throws Exception {
        var stockData = reportingService.getStockData();
        return ResponseEntity.ok(new ResponseWrapper("StockData successfully retrieved",stockData, HttpStatus.OK));
    }

    @GetMapping("/profitLossData")
    public ResponseEntity<ResponseWrapper> getMonthlyProfitLossData() {
        var profitLossData = reportingService.getMonthlyProfitLossDataMap();
        return ResponseEntity.ok(new ResponseWrapper("ProfitLossData successfully retrieved",profitLossData, HttpStatus.OK));
    }


}
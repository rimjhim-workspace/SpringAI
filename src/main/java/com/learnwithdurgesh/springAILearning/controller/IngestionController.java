package com.learnwithdurgesh.springAILearning.controller;

import com.learnwithdurgesh.springAILearning.service.IngestionService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/rag")
public class IngestionController {
    private IngestionService ingestionService;
    public IngestionController(IngestionService ingestionService){
        this.ingestionService = ingestionService;
    }
//    first will check via sample data and then we will go through pdf
    @PostMapping("/ingest-sample")
    public ResponseEntity<String> ingestSampleData(){
        String companyPolicy= """
            Company Leave & Remote Work Policy 2026:
            1. Employees are entitled to 24 paid leaves annually.
            2. Remote work is permitted up to 2 days per week with manager approval.
            3. The official health insurance provider is CareHealth, covering up to 5 Lakh INR.
            4. Core working hours are from 10:00 AM to 4:00 PM IST.
                """;
        ingestionService.ingestText(companyPolicy,"HR_Policy_2026","hr");
        return ResponseEntity.ok("Sample data ingested successfully.");
    }
    @Value("classpath:/ingestPdf/sample-pdf-a4-size-65kb.pdf")
    private Resource pdfFile;

    @GetMapping("/ingest-pdf")
    public String ingestPdf(){
        ingestionService.ingestPDF(pdfFile);
        return "Pdf data ingested successfully";
    }
}

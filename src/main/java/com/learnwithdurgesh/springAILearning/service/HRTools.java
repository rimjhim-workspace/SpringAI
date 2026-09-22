package com.learnwithdurgesh.springAILearning.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ai.document.Document;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.ai.vectorstore.SearchRequest;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class HRTools {
    private Logger logger = LoggerFactory.getLogger(HRTools.class);
    private final VectorStore vectorStore;
    public HRTools(VectorStore vectorStore) {
        this.vectorStore = vectorStore;
    }

    @Tool(description = "Search and read company HR policies, leave rules, and guidelines from ChromaDB ")
    public String companyPolicy(String query){
        logger.info("tool started ",query);
        List<Document> docs = vectorStore.similaritySearch(SearchRequest.builder().build());
        if(docs.isEmpty()){
            logger.info("no docs found");
        }
        return docs.stream().map(Document::getText).collect(Collectors.joining("\n"));

    }
    //abhi sirf policy read krwayi he now its time to make check where leave balance is available because if there is no work after reading then it is like rag but lets make task
    @Tool(description = "Check how many remaining paid leaves an employee currently has")
    public String checkLeaveBalance(String employeeId){
        logger.info("tool started ",employeeId);
        return "Employee" + employeeId + " has 14 pls.";
    }

//    submit leave req
    @Tool(description = "Submit a formal leave request for an employee for a specific date")
    public String submitLeaveRequest(String employeeId,String date){
        logger.info("tool started ",employeeId);
        return "SUCCESS: Leave request submitted for " + employeeId + " on date " + date + ". Application ID: LV-8821.";
    }
}
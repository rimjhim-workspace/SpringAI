package com.learnwithdurgesh.springAILearning.controller;


import com.learnwithdurgesh.springAILearning.service.RagChatService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("api/v1/rag")
public class RagChatController {
    private final RagChatService ragChatService;

    public RagChatController(RagChatService ragChatService) {
        this.ragChatService = ragChatService;
    }

//    manual rag endpoint
    @GetMapping("/ask-manual")
    public ResponseEntity<Map<String,String>> askManual(@RequestParam("q") String q){
        String answer = ragChatService.manualRag(q);
        return ResponseEntity.ok(Map.of("query" , q,
                "mode","manual rag",
                "answer",answer));
    }
    @GetMapping("/ask")
    public ResponseEntity<Map<String, String>> ask(@RequestParam("q") String q){
        String answer = ragChatService.askAdvisor(q);
        return ResponseEntity.ok(Map.of("query" , q,
                "mode","ask rag",
                "answer",answer));
    }




}

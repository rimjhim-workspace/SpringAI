package com.learnwithdurgesh.springAILearning.controller;

import com.learnwithdurgesh.springAILearning.service.HRAgentService;
import com.learnwithdurgesh.springAILearning.service.ReturnAgentService;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/agent")
public class AgentController {

    private final HRAgentService hrAgentService;
    public AgentController(HRAgentService hrAgentService ) {
        this.hrAgentService=hrAgentService;


    }
    @GetMapping("/goal")
    public ResponseEntity<Map<String,String>> getGoal(@RequestParam("goal") String goal){
        String result = hrAgentService.runAgent(goal);
        return ResponseEntity.ok(Map.of("user-goal", goal , "agent-response",result));
    }
//    @GetMapping("/ask")
//    public ResponseEntity<String> getRefund(@RequestParam("req") String query ,
//                                                         @RequestParam(value = "conversationId", defaultValue = "default_session")String conversationId){
//        String res = returnAgentService.handleRequest(query ,  conversationId);
//        return ResponseEntity.ok(res);
//    }
}

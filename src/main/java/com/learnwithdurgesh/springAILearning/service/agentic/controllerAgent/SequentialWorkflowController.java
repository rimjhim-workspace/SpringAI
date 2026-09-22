package com.learnwithdurgesh.springAILearning.service.agentic.controllerAgent;

import com.learnwithdurgesh.springAILearning.service.agentic.model.SequentialBlogState;
import com.learnwithdurgesh.springAILearning.service.agentic.workflow.SequentialWorkflowService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("api/v1/stage1")
public class SequentialWorkflowController {
    private final SequentialWorkflowService workflowService;
    public SequentialWorkflowController(SequentialWorkflowService workflowService) {
        this.workflowService = workflowService;
    }
    @GetMapping("/seq")
    public ResponseEntity<Map<String , Object>> runSequentialWorkflow(@RequestParam("topic") String topic) {
        SequentialBlogState result = workflowService.runSequentialPipeline(topic);
        return ResponseEntity.ok(Map.of(
                "topic " , result.getTopic(),
                "Step 1: outline ",result.getOutline(),
                "Step 2: draft ",result.getDraft(),
                "Step 3: finalArticle ", result.getFinalArticle()
        ));
    }
}

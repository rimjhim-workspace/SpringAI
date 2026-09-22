package com.learnwithdurgesh.springAILearning.service.agentic.controllerAgent;

import com.learnwithdurgesh.springAILearning.service.agentic.model.FeedbackState;
import com.learnwithdurgesh.springAILearning.service.agentic.model.SequentialBlogState;
import com.learnwithdurgesh.springAILearning.service.agentic.workflow.ConditionalRoutingService;
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
    private final ConditionalRoutingService conditionalRoutingService;
    public SequentialWorkflowController(SequentialWorkflowService workflowService , ConditionalRoutingService conditionalRoutingService) {
        this.workflowService = workflowService;
        this.conditionalRoutingService = conditionalRoutingService;
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
    // conditional router controller
    @GetMapping("/feed")
    public ResponseEntity<Map<String,Object>> runConditionalWorkflow(@RequestParam("comment") String feedback) {

        FeedbackState res = conditionalRoutingService.routingFeedback(feedback);
        return ResponseEntity.ok(Map.of(
                "inputFeedBack" ,feedback,
                "routedPath",res.getSentiment(),
                "response",res.getResponse()
        ));

    }
}

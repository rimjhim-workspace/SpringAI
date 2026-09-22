package com.learnwithdurgesh.springAILearning.service.agentic.controllerAgent;

import com.learnwithdurgesh.springAILearning.service.agentic.model.FeedbackState;
import com.learnwithdurgesh.springAILearning.service.agentic.model.ParallelReviewState;
import com.learnwithdurgesh.springAILearning.service.agentic.model.SequentialBlogState;
import com.learnwithdurgesh.springAILearning.service.agentic.workflow.ConditionalRoutingService;
import com.learnwithdurgesh.springAILearning.service.agentic.workflow.ParallelWorkflowService;
import com.learnwithdurgesh.springAILearning.service.agentic.workflow.SequentialWorkflowService;
import com.learnwithdurgesh.springAILearning.service.agentic.workflow.StreamService;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("api/v1/stage1")
public class SequentialWorkflowController {
    private final SequentialWorkflowService workflowService;
    private final ConditionalRoutingService conditionalRoutingService;
    private final ParallelWorkflowService parallelWorkflowService;
    private final StreamService streamService;

    public SequentialWorkflowController(SequentialWorkflowService workflowService , ConditionalRoutingService conditionalRoutingService,
                                        ParallelWorkflowService parallelWorkflowService,StreamService streamService) {
        this.workflowService = workflowService;
        this.conditionalRoutingService = conditionalRoutingService;
        this.parallelWorkflowService = parallelWorkflowService;
        this.streamService = streamService;
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
    @GetMapping("/parallel")
    public ResponseEntity<Map<String , Object>> testParallel(@RequestParam("code")String code){
        ParallelReviewState result = parallelWorkflowService.runParallelAudit(code);
        return ResponseEntity.ok(Map.of(
                "code" , result.getUserCode(),
                "node A " ,result.getQualityReview(),
                "node B ",result.getSecurityReview(),
                "final Verdict", result.getFinalVerdict()
        ));
    }
//  this is streaming output chatgpt like typing effect word by word result

    @GetMapping(value="/stream",produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<String> streamAnswer(@RequestParam("q") String query){
        return streamService.streamAnswer(query);
    }

}

package com.learnwithdurgesh.springAILearning.service.agentic.workflow;

import com.learnwithdurgesh.springAILearning.service.agentic.model.ParallelReviewState;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.stereotype.Service;

import java.util.concurrent.CompletableFuture;

@Service
public class ParallelWorkflowService {
    private static final Logger logger= LoggerFactory.getLogger(ParallelWorkflowService.class);

    private final ChatClient chatClient;
    public ParallelWorkflowService(ChatClient chatClient) {
        this.chatClient = chatClient;
    }
//    Node A :Code quality reviewer
    public String reviewQuality(String code){
        logger.info("Thread [{}] running Node A (Quality Review)....",Thread.currentThread().getName());

        return chatClient.prompt()
                .system("You are  a Senior Jave Architect. Review code for readability rate it out of 10 NS O(N) COMPLEXITY")
                .user(code)
                .advisors(a->a.param(ChatMemory.CONVERSATION_ID,"user123"))
                .call()
                .content();
    }

//    Node B : Security Auditor
public String reviewSecurity(String code) {
    logger.info("Thread [{}] running Node B (Security Review)...", Thread.currentThread().getName());
    return chatClient.prompt()
            .system("You are a Cybersecurity Expert. Check strictly for SQL injection, hardcoded secrets, or memory leaks in 2 bullet points.")
            .user(code)
            .advisors(a-> a.param(ChatMemory.CONVERSATION_ID,"user123" +
                    ""))
            .call()
            .content();
}

    //    NODE C : Aggregator Node
    public String aggregateVerdict(String quality,String security ) {
        logger.info("Thread [{}] running Node A (Verdict)...", Thread.currentThread().getName());
        return chatClient.prompt()
                .system("Based on the Quality and Security reviews, give a final 1-line verdict: 'APPROVED FOR MERGE' or 'CHANGES REQUESTED' .")
                .user("Quality " + quality + "Security " + security)
                .advisors(a -> a.param(ChatMemory.CONVERSATION_ID, "user123"))
                .call()
                .content();
    }
//    parallel execution
public ParallelReviewState runParallelAudit(String code) {
    ParallelReviewState state = new ParallelReviewState(code);
    long startTime = System.currentTimeMillis();
    // 1. FAN-OUT: Dono nodes ko background threads par EKSATH fire
    CompletableFuture<String> qualityFuture = CompletableFuture.supplyAsync(() -> reviewQuality(state.getUserCode()));
    CompletableFuture<String> securityFuture = CompletableFuture.supplyAsync(() -> reviewSecurity(state.getUserCode()));
    // 2. FAN-IN: Wait hoga jab tak DONO threads apna kaam khatam na kar lein
    CompletableFuture.allOf(qualityFuture, securityFuture).join();
    // State me dono ka data bharo
    state.setQualityReview(qualityFuture.join());
    state.setSecurityReview(securityFuture.join());
    state.setFinalVerdict(aggregateVerdict(state.getQualityReview(), state.getSecurityReview()));
    long totalTime = System.currentTimeMillis() - startTime;
    logger.info("Parallel Workflow completed in total {} ms!", totalTime);
    return state;
}
}

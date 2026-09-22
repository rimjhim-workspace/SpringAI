package com.learnwithdurgesh.springAILearning.service.agentic.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ParallelReviewState {
    private String userCode;
    private String qualityReview;
    private String securityReview;
    private String finalVerdict;
    public ParallelReviewState(String userCode){
        this.userCode=userCode;
    }
}

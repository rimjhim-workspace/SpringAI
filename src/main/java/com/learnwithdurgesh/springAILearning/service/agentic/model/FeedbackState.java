package com.learnwithdurgesh.springAILearning.service.agentic.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class FeedbackState {
    private String customerFeedback;
    private String sentiment;
    private String response;
    public FeedbackState(String customerFeedback){
        this.customerFeedback=customerFeedback;
    }
}

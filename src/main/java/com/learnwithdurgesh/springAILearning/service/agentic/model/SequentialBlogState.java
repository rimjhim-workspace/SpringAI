package com.learnwithdurgesh.springAILearning.service.agentic.model;

import lombok.Getter;
import lombok.Setter;


//State

@Getter
@Setter
public class SequentialBlogState {
    private String topic;
    private String outline;
    private String draft;
    private String finalArticle;

    public SequentialBlogState(String topic) {
        this.topic = topic;
    }

}



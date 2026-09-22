package com.learnwithdurgesh.springAILearning.service;

import reactor.core.publisher.Flux;

public interface ChatService {
    public String chatTemplate(String query,String userId);

    Flux<String> streamChat(String q);
}

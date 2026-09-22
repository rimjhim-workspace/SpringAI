package com.learnwithdurgesh.springAILearning.service.agentic.workflow;

import org.jspecify.annotations.Nullable;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

@Service
public class StreamService {
    private final ChatClient chatClient;
    public StreamService(ChatClient chatClient) {
        this.chatClient = chatClient;
    }
    public @Nullable Flux<String> streamAnswer(String query){
        return chatClient.prompt()
                .user(query)
                .advisors(a-> a.param(ChatMemory.CONVERSATION_ID,"chat123"))
                .stream()
                .content();
    }
}

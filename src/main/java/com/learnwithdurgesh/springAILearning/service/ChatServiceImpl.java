package com.learnwithdurgesh.springAILearning.service;

import jakarta.annotation.Resource;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

@Service
public class ChatServiceImpl implements ChatService {
    private ChatClient chatClient;


    public ChatServiceImpl(ChatClient chatClient) {
        this.chatClient = chatClient;
    }

    @Override
    public  String chatTemplate(String q,String userId){
        return this.chatClient
                .prompt()
                .system("you are helpful assistant answer in 4 words")
                .user(" the answer is "+q)
                .advisors(a->a.param(ChatMemory.CONVERSATION_ID,userId))
                .call()
                .content();
    }

    @Override
    public Flux<String> streamChat(String q) {
        return this.chatClient
                .prompt()
                .system("you are helpful assistant answer in 4 words")
                .user("the guide is  "+q)
                .stream()
                .content();
    }
}

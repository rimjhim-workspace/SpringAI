package com.learnwithdurgesh.springAILearning.service;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.stereotype.Service;

@Service
public class ReturnAgentService {

    private final OrderTools orderTools ;
    private final ChatClient chatClient;
    public ReturnAgentService(OrderTools orderTools , ChatClient chatClient) {
        this.orderTools = orderTools;
        this.chatClient = chatClient;
    }

//    tools ko yaha use krwana he service se
     public String handleRequest(String customerQuery , String customerSessionD ){
        return chatClient.prompt()
                .system("""
                        You are an autonomous return agent.
                        Always verify:
                        1. Fetch order details first.
                        2. Check if the product is within return policy window.
                        3. If eligible, call initiateRefund. If not eligible, explain politely why.
                        """)
                .user(customerQuery)
                .tools(orderTools)
                .advisors(a->a.param("chat_memory_conversation_id","agent1"))
                .call()
                .content();
     }
}

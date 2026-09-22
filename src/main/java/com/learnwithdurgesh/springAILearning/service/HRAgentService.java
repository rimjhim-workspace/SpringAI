package com.learnwithdurgesh.springAILearning.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.stereotype.Service;

@Service
public class HRAgentService {
    private static final Logger logger = LoggerFactory.getLogger(HRAgentService.class);
    private  final ChatClient chatClient;
    private final HRTools hrTools ;
    public HRAgentService(ChatClient chatClient ,  HRTools hrTools) {
        this.chatClient = chatClient;
        this.hrTools = hrTools;
    }
    public String runAgent(String userGoal){
        logger.info("agents are starting to work .");
        String systemPrompt = """
                You are an Autonomous HR Support Agent.
                You have access to tools to:
                1. Search company policies (RAG)
                2. Check employee leave balances
                3. Submit leave requests
                
                Work autonomously: Think step-by-step, call the necessary tools, 
                verify conditions, and return a polite, professional summary of the action taken.
                """;
        return chatClient.prompt()

                .system(systemPrompt)
                .user(userGoal)
                .tools(hrTools)
                .advisors(c -> c.param("chat_memory_conversation_id","agent1"))
                .call()
                .content();
    }
}

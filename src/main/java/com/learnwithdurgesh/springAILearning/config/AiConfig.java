package com.learnwithdurgesh.springAILearning.config;

import com.learnwithdurgesh.springAILearning.advisors.TokenCountAdvisors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.MessageChatMemoryAdvisor;
import org.springframework.ai.chat.client.advisor.SimpleLoggerAdvisor;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.ai.openai.OpenAiChatOptions;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AiConfig {
    private Logger logger = LoggerFactory.getLogger(AiConfig.class);
    @Bean
    public ChatClient chatClient(ChatClient.Builder builder , ChatMemory chatMemory) {
        this.logger.info(chatMemory.getClass().getName());
        MessageChatMemoryAdvisor messageChatMemoryAdvisor= MessageChatMemoryAdvisor.builder(chatMemory).build();
        return builder
                .defaultAdvisors(messageChatMemoryAdvisor,new SimpleLoggerAdvisor())
                .defaultSystem("you are a helpful assistant who give answer in 4 words.")
                .defaultOptions(OpenAiChatOptions.builder().model("openai.gpt-5-mini"))
                .build();


    }
}

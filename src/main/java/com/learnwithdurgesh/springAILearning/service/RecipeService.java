package com.learnwithdurgesh.springAILearning.service;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.stereotype.Service;



@Service
public class RecipeService {
    private final ChatClient chatClient;

    public RecipeService(ChatClient.Builder builder) {
        this.chatClient =builder.build() ;
    }
    public String createRecipe(String query){
         return chatClient
                 .prompt()
                 .user(user-> user.text("""
                         rewrite the recipe of request according to ingredient 
                         and whole process.
                         User Query:
                         {query}
                         """)
                         .param("query",query))
                 .call()
                 .content();
    }
//    Prompt templationg and message are different and stuffing also is a concept and rementver is not just about user message
}

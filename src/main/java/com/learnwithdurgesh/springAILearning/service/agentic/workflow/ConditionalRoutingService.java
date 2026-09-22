package com.learnwithdurgesh.springAILearning.service.agentic.workflow;

import com.learnwithdurgesh.springAILearning.service.agentic.model.FeedbackState;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.stereotype.Service;

@Service
public class ConditionalRoutingService {
    private static final Logger log = LoggerFactory.getLogger(ConditionalRoutingService.class);
    private final ChatClient chatClient;
    public ConditionalRoutingService(ChatClient chatClient) {
        this.chatClient = chatClient;
    }
//    node which will use as a sentiment classifier
    public FeedbackState analyzeSentiment(FeedbackState state){
        log.info("Analyzing the feedback " );
        String sentiment = chatClient.prompt()
                .system("Check the customer sentiment strictly either 'URGENT_COMPLIANT 'or 'POSITIVE'.return only a word.")
                .user(state.getCustomerFeedback())
                .advisors(a->a.param(ChatMemory.CONVERSATION_ID,"user_123"))
                .call()
                .content()
                .trim()
                .toUpperCase();
        state.setSentiment(sentiment.contains("POSITIVE") ? "POSITIVE" : "URGENT_COMPLIANT");
        return state;
    }
//    happy customer node if the feedabck is ++
    public FeedbackState thankYouNode(FeedbackState state){
        String thankyouNote = chatClient.prompt()
                .system("You are customer success Agent . You have to give warm thankyou note in just 3 word .")
                .user(state.getCustomerFeedback())
                .advisors(a->a.param(ChatMemory.CONVERSATION_ID,"user123"))
                .call()
                .content();
        state.setResponse(thankyouNote);
        return state;

    }
    //  NODE 2 : Angry Customer Node (Escalation + Apology)
    public FeedbackState escalationNode(FeedbackState state){
        String apology = chatClient.prompt()
                .system("You are an Escalation Support Manager. Apologize sincerely, assure quick action, and provide a priority ticket reference #ESC-901")
                .user(state.getCustomerFeedback())
                .advisors(a->a.param(ChatMemory.CONVERSATION_ID,"user123"))
                .call()
                .content();
        state.setResponse(apology);
        return state;

    }

//    The routed graph where will connected response with correct customer feednback
    public FeedbackState routingFeedback(String feedback){
        FeedbackState state = new FeedbackState(feedback);
        state = analyzeSentiment(state);
        if(state.getSentiment().equals("POSITIVE")){
            state = thankYouNode(state);
        }else{
            state=escalationNode(state);
        }
        return state;
    }



}

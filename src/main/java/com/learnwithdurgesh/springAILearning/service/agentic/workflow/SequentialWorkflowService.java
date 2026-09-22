package com.learnwithdurgesh.springAILearning.service.agentic.workflow;

import com.learnwithdurgesh.springAILearning.service.agentic.controllerAgent.SequentialWorkflowController;
import com.learnwithdurgesh.springAILearning.service.agentic.model.SequentialBlogState;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.stereotype.Service;

@Service
public class SequentialWorkflowService {
    private static final Logger logger = LoggerFactory.getLogger(SequentialWorkflowController.class);
    private final ChatClient chatClient ;
    public SequentialWorkflowService(ChatClient chatClient) {
        this.chatClient = chatClient;
    }
    //1. outline generator
    public SequentialBlogState generateOutlineState(SequentialBlogState state){
        logger.info("executing generateOutline for topic {} ", state.getTopic());
        String outline = chatClient.prompt()
                .system("create a 3 bullet points in one line outline for the given topic, Return only one line bullets points")
                .user(state.getTopic())
                .advisors(a->a.param(ChatMemory.CONVERSATION_ID,"user1"))
                .call()
                .content();
        state.setOutline(outline);
        return state;
    }
    // 2 . article generator Hamara content writer
    public SequentialBlogState writeDraftNote(SequentialBlogState state){
        logger.info("Executing Node 2 : writing draft from the outline");
        String draft = chatClient.prompt()
                .system("You are the tech blog writer . Write the 1 line article on the provided outline")
                .user(state.getOutline())
                .advisors(a->a.param(ChatMemory.CONVERSATION_ID,"user1"))
                .call()
                .content();
        state.setDraft(draft);
        return state;
    }

    //3.  Built a proof-reader and add seo node
    public SequentialBlogState proofReaderAndSeoNode(SequentialBlogState state){
        logger.info("Executing Node 3 : proof reader and seo from the draft and check the seo");
        String finalOutput= chatClient.prompt()
                .system("Review this draft tone .make it clearly ,make it funny, and append 3 hashtag at the end.")
                .user(state.getDraft())
                .advisors(a->a.param(ChatMemory.CONVERSATION_ID,"user1"))
                .call()
                .content();
        state.setFinalArticle(finalOutput);
        return state;
    }
// pipeline of sequential workflow  node1 --> node2 --> node3
    public SequentialBlogState runSequentialPipeline(String  topic){
        SequentialBlogState state = new SequentialBlogState(topic);
        state = generateOutlineState(state);
        state=writeDraftNote(state);
        state=proofReaderAndSeoNode(state);
        logger.info("successfully completed the pipeline . ");
        return state;
    }

}

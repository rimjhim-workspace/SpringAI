package com.learnwithdurgesh.springAILearning.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.vectorstore.QuestionAnswerAdvisor;
import org.springframework.ai.chat.prompt.PromptTemplate;
import org.springframework.ai.document.Document;
import org.springframework.ai.vectorstore.SearchRequest;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class RagChatService {
    private static final Logger logger= LoggerFactory.getLogger(RagChatService.class);
    private VectorStore vectorStore;
    private ChatClient chatClient;

    public RagChatService(VectorStore vectorStore, ChatClient chatClient) {
        this.vectorStore = vectorStore;
        this.chatClient = chatClient;
    }

// ask manually all the question answer
//    approach 1 :: 1st step : retrieve chroma db ke top chunks nikalo
    public String manualRag(String query){
        logger.info("manualRag",query);
        List<Document> similarDocuments = vectorStore.similaritySearch(
                SearchRequest.builder()
                        .query(query)
                        .topK(2)
                        .build()
        );
        logger.info("similar chunks found count " ,similarDocuments.size());
//         2nd step is to convert chunks to text
        String context = similarDocuments.stream().map(Document::getText).collect(Collectors.joining("\n\n"));
//         chunk ko context banaye uske baad prompt me inject krde
        String prompt = """
              You are a helpful company HR assistant who answer in one line.
              Answer the user's question using ONLY the provided context below.
              If the answer is not present in the context, strictly reply:
              "I am sorry, but I do not have this information in my records.
              
                CONTEXT:
                {context}
                
                USER QUESTION:
                {question}
                """;
        PromptTemplate promptTemplate = new PromptTemplate(prompt);
        String finalPrompt= promptTemplate.render(Map.of(
                "context",context.isEmpty()?"no relevant data provided":context,
                "question",query
        ));
//        give this prompt to llm
        return chatClient.prompt(finalPrompt)
                .advisors(a->a.param("chat_memory_conversation_id","user_1"))
                .call().content();
    }
//    ab advisor se do
    public String askAdvisor(String query){
        logger.info("askAdvisor",query);
        return  chatClient.prompt()
                .user(query)
                .advisors(QuestionAnswerAdvisor.builder(vectorStore).build())
                .advisors(a->a.param("chat_memory_conversation_id","user_1"))
                .call()
                .content();
        }

    }

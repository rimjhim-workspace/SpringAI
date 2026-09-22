package com.learnwithdurgesh.springAILearning.controller;

import com.learnwithdurgesh.springAILearning.service.ChatService;
//import com.learnwithdurgesh.springAILearning.service.ImageService;
import com.learnwithdurgesh.springAILearning.service.RecipeService;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.image.ImageResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;


@RestController
@RequestMapping
public class ChatController {
    private ChatService chatService;

    public ChatController( ChatService chatService) {
        this.chatService = chatService;
    }

    private ChatClient chatClient;

    @GetMapping("/chat")
    public ResponseEntity<String> chat(@RequestParam(value="q", required = true) String q,
                                       @RequestHeader("userId") String userId){
        return ResponseEntity.ok(chatService.chatTemplate(q , userId));
    }
    @GetMapping("/stream")
    public ResponseEntity<Flux<String>> streamChat(
            @RequestParam(value="q",required = true) String q){
        return ResponseEntity.ok(this.chatService.streamChat(q));
    }





//    @GetMapping("ask-ai")
//    public String getResponse(@RequestParam String message){
//        return chatService.getResponse(message);
//    }
//
//
////    }
//    @GetMapping("recipe-creator")
//    public String recipeGenerator(@RequestParam String query){
//        return recipeService.createRecipe(query);
//    }




}

package com.learnwithdurgesh.springAILearning.service;

import org.springframework.ai.tool.annotation.Tool;
import org.springframework.stereotype.Component;

@Component
public class OrderTools {

    @Tool(description="check the return policy rules and time limits for different products.")
    public String checkPolicy(String category){
        return "electronics can be returned in 7 days and clothing within 15 days";
    }
   @Tool(description = "get purchase date , delivery status , and category for an order Id")
    public String getOrderDetails(String orderId){
        return "order #ord-402 : purchased 3 days ago . Category : Electronics. Status: DELIVERED.";
    }

    @Tool(description = "Initiate the refund and generate a return pickup tracking id for the given order id.")
    public String initiateRefund(String orderId , String reason){
        return "Success : Refund initiated for " + orderId + ". Pickup scheduled for tomorrow. ";
    }
}

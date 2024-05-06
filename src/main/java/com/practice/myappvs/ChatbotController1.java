package com.practice.myappvs;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import javax.servlet.http.HttpSession;

@Controller
public class ChatbotController1 {

    @GetMapping("/chatbot")
    public String showChatbotPage(Model model, HttpSession session) {
        // Get the customer name from the session
        String customerName = (String) session.getAttribute("customerName");

        // Add the customer name to the model
        model.addAttribute("customerName", customerName);

        // Add the path to the UPI image
        model.addAttribute("upiImagePath", "img/20230616_221600_0000.png");

        // Return the Thymeleaf template
        return "chatbot";
    }
    
    // Add any necessary logic for chatbot interactions
}

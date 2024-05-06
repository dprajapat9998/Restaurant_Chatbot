package com.practice.myappvs;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;

@Controller
public class HomeController {

    @ModelAttribute("userDetails")
public UserDetails userDetails() {
    return new UserDetails(); // Assuming UserDetails is your form backing object
}


    @GetMapping("/")
    public String home() {
        return "index"; // Update this to match your actual Thymeleaf template file
    }
}
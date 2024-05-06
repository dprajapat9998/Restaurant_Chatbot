package com.practice.myappvs;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;

@Controller
public class FormController {

    @Autowired
    private UserDetailsRepository userDetailsRepository;

    @GetMapping("/form")
    public String showForm(Model model) {
        model.addAttribute("userDetails", new UserDetails());
        return "index";
    }

    @PostMapping("/form")
    public String submitForm(@ModelAttribute("userDetails") @Valid UserDetails userDetails, BindingResult bindingResult, Model model, HttpSession session) {
        if (bindingResult.hasErrors()) {
            return "index";
        }

        userDetailsRepository.save(userDetails);

        // Store the customer's name in the session
        session.setAttribute("customerName", userDetails.getName());

        return "redirect:/chatbot";
    }
}

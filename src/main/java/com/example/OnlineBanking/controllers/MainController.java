package com.example.OnlineBanking.controllers;

import com.example.OnlineBanking.models.User;
import com.example.OnlineBanking.services.CardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class MainController {
    @Autowired
    private CardService cardService;

    @GetMapping("/")
    public String main(@AuthenticationPrincipal User user, Model model){
        model.addAttribute("cards",cardService.getUserCards(user));
        return "profile";
    }

    @GetMapping("/reg")
    public String reg(Model model){
        model.addAttribute("user", new User());
        return "reg";
    }
}

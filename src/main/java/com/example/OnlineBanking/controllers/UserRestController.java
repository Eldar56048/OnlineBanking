package com.example.OnlineBanking.controllers;

import com.example.OnlineBanking.models.*;
import com.example.OnlineBanking.repo.CardRepository;
import com.example.OnlineBanking.repo.HistoryRepository;
import com.example.OnlineBanking.repo.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Collections;

@RestController
public class UserRestController {
    @Autowired
    private HistoryRepository historyRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private CardRepository cardRepository;
    @PostMapping("/reg")
    public void regUser(HttpServletResponse response,User user, Model model) throws IOException {
        user.setRoles(Collections.singleton(Role.USER));
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setEnabled(true);
        userRepository.save(user);
        response.sendRedirect("/login");
    }

    @PostMapping("/user/password/change")
    public void changePassword(HttpServletResponse response,@AuthenticationPrincipal User user, @RequestParam String password, Model model) throws IOException {
        System.out.println(user.getUsername()+" "+password);
        user.setPassword(passwordEncoder.encode(password));
        userRepository.save(user);
        response.sendRedirect("/");
    }

    @PostMapping("/user/card/add")
    public void addUserCard(HttpServletResponse response,@AuthenticationPrincipal User user, Card card, Model model) throws IOException {
        card.setUser(user);
        card.setCash(0);
        cardRepository.save(card);
        response.sendRedirect("/");
    }

    @PostMapping("/user/card/{cardId}/add")
    public void addMoneyToCard(@PathVariable(value = "cardId") long cardId, HttpServletResponse response, @AuthenticationPrincipal User user, Long money) throws IOException {
        Card card = cardRepository.getById(cardId);
        History history = new History(card, Transaction.Income,Type.AddMoney,(float)money,0);
        card.addMoneyToCard((float)money);
        cardRepository.save(card);
        historyRepository.save(history);
        response.sendRedirect("/");
    }


}

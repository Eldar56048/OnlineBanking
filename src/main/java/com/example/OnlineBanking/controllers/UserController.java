package com.example.OnlineBanking.controllers;

import com.example.OnlineBanking.models.*;
import com.example.OnlineBanking.repo.CardRepository;
import com.example.OnlineBanking.repo.HistoryRepository;
import com.example.OnlineBanking.repo.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;

@Controller
@RequestMapping("/user")
public class UserController {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private HistoryRepository historyRepository;
    @Autowired
    private CardRepository cardRepository;

    @GetMapping("/password/change")
    public String getUserPasswordChangePage(@AuthenticationPrincipal User user, Model model){
        model.addAttribute("user",user);
        return "change-password";
    }

    @GetMapping("/card/add")
    public String getAddCardPage(Model model){
        model.addAttribute("card",new Card());
        return "add-card";
    }

    @GetMapping("/card/{cardId}/add")
    public String getCardMoneyAddPage(@PathVariable(value = "cardId") long cardId, Model model){
        model.addAttribute("card",cardRepository.getById(cardId));
        return "card-add-money";
    }

    @GetMapping("/card/{cardId}/history")
    public String getCardHistoryPage(@PathVariable(value = "cardId") long cardId, Model model){
        model.addAttribute("history",historyRepository.getAllByCard(cardRepository.getById(cardId)));
        return "card-history";
    }

    @GetMapping("/transfer")
    public String getTransferPage(@AuthenticationPrincipal User user, Model model){
        model.addAttribute("cards",cardRepository.getAllByUser(user));
        return "transfer";
    }

    @PostMapping("/transfer")
    public String transferMoney(HttpServletResponse response, Model model, @AuthenticationPrincipal User user, @RequestParam float num, @RequestParam long cardId, @RequestParam  String cardNumber){
        Card userCard = cardRepository.getById(cardId);
        History fromCard = null;
        History toCard = null;
        Card card = null;
        System.out.println(cardNumber.substring(0,3));
        if(cardNumber.substring(0,4).equals("5522")){
            card = cardRepository.getByCardNumber(cardNumber);
            if(num<=100000){
                if(!(userCard.getCash()>=num)){
                    model.addAttribute("error","Enough money in the card for transfer you need "+num);
                    return getTransferPage(user,model);
                }
                card.addMoneyToCard(num);
                toCard = new History(card, Transaction.Income, Type.TransferMoneyToThisBankCard,num,0,userCard.getCardNumber());
                fromCard = new History(userCard,Transaction.Outcome,Type.TransferMoneyToThisBankCard,num,0,card.getCardNumber());
                userCard.minusMoney(num);
            }
            else {
                if(!(userCard.getCash()>=(num+num*(float) (0.01)))){
                    model.addAttribute("error","Enough money in the card for transfer you need "+(num+num*(0.01)));
                    return getTransferPage(user,model);
                }
                card.addMoneyToCard(num);
                toCard = new History(card, Transaction.Income, Type.TransferMoneyToThisBankCard,num,(float)0.01,userCard.getCardNumber());
                fromCard = new History(userCard,Transaction.Outcome,Type.TransferMoneyToThisBankCard,num,(float) 0.01,card.getCardNumber());
                userCard.minusMoney((num+num*(float) (0.01)));
            }
        }
        else {
            if(!(userCard.getCash()>=(num+num*(float) (0.01)))){
                model.addAttribute("error","Enough money in the card for transfer you need "+(num+num*(0.01)));
                return getTransferPage(user,model);
            }
            fromCard = new History(userCard,Transaction.Outcome,Type.TransferMoneyToAnotherCardBank,num+num*(float)0.01,(float)0.01,cardNumber);
            userCard.minusMoney((num+num*(float) (0.01)));
        }
        if(userCard!=null){
            cardRepository.save(userCard);
        }
        if(card!=null){
            cardRepository.save(card);
        }
        if(fromCard!=null){
            historyRepository.save(fromCard);
        }
        if(toCard!=null){
            historyRepository.save(toCard);
        }
        return "redirect:/";
    }
    @GetMapping("/transfer/my")
    public String getTransferToUserCardsPage(@AuthenticationPrincipal User user,Model model){
        model.addAttribute("cards",cardRepository.getAllByUser(user));
        return "transfer-user-cards";
    }

    @PostMapping("/transfer/my")
    public String transferMoneyUserCards(@RequestParam float num,@RequestParam long cardId1,@RequestParam long cardId2,Model model,@AuthenticationPrincipal User user){
        if(cardId1==cardId2){
            model.addAttribute("error","Two identical cards");
            return getTransferToUserCardsPage(user,model);
        }
        Card card1 = cardRepository.getById(cardId1);
        Card card2 = cardRepository.getById(cardId2);
        if(!(card1.getCash()>=num)){
            model.addAttribute("error","Enough money in the card for transfer you need "+(num));
            return getTransferToUserCardsPage(user,model);
        }
        card1.minusMoney(num);
        card2.addMoneyToCard(num);
        History card1History = new History(card1,Transaction.Outcome,Type.TransferMoneyToThisUserCard,num,0,card2.getCardNumber());
        History card2History = new History(card2,Transaction.Income,Type.TransferMoneyToThisUserCard,num,0,card1.getCardNumber());
        cardRepository.save(card1);
        cardRepository.save(card2);
        historyRepository.save(card1History);
        historyRepository.save(card2History);
        return "redirect:/";
    }

    @GetMapping("/transfer/services")
    public String getTransferServicesPage(@AuthenticationPrincipal User user,Model model){
        model.addAttribute("cards",cardRepository.getAllByUser(user));
        return "transfer-services";
    }

}

package com.example.OnlineBanking.services;

import com.example.OnlineBanking.models.Card;
import com.example.OnlineBanking.models.User;
import com.example.OnlineBanking.repo.CardRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CardService{
    @Autowired
    private CardRepository cardRepository;

    public Iterable<Card> getUserCards(User user){
        return cardRepository.getAllByUser(user);
    }

}

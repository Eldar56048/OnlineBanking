package com.example.OnlineBanking.repo;

import com.example.OnlineBanking.models.Card;
import com.example.OnlineBanking.models.User;
import org.springframework.data.repository.CrudRepository;

public interface CardRepository extends CrudRepository<Card,Long> {
    Iterable<Card> getAllByUser(User user);
    Boolean existsCardByCardNumber(Card card);
    Card getById(Long id);
    Card getByCardNumber(String cardNumber);
}

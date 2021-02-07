package com.example.OnlineBanking.repo;

import com.example.OnlineBanking.models.Card;
import com.example.OnlineBanking.models.History;
import org.springframework.data.repository.CrudRepository;

public interface HistoryRepository extends CrudRepository<History,Long> {
    Iterable<History> getAllByCard(Card card);
}

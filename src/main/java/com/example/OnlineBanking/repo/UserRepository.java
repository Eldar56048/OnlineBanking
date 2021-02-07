package com.example.OnlineBanking.repo;

import com.example.OnlineBanking.models.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User,Long> {
    User findByUsername(String username);
    User getById(Long id);
}

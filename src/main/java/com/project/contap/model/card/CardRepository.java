package com.project.contap.model.card;
import com.project.contap.model.user.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CardRepository extends JpaRepository<Card, Long> , CustomCardRepository {
    List<Card> findAllByUser(User user);
}

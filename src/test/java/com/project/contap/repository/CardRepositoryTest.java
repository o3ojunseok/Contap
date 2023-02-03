package com.project.contap.repository;

import com.project.contap.model.card.Card;
import com.project.contap.model.card.CardRepository;
import com.project.contap.model.card.QCard;
import com.project.contap.model.card.dto.QCardDto;
import com.project.contap.model.user.User;
import com.project.contap.model.user.UserRepository;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class CardRepositoryTest {
    @Autowired
    private CardRepository cardRepository;

    @Autowired
    private UserRepository userRepository;
    List<QCardDto> firstCard = new ArrayList<>();
    List<QCardDto> secCard = new ArrayList<>();

    @BeforeEach
    void setUp() throws Exception {
        User user1 = User.builder()
                .email("user1@gmail.com")
                .pw("123")
                .userName("user1")
                .field(0).build();
        User user2 = User.builder()
                .email("user2@gmail.com")
                .pw("123")
                .userName("user2")
                .field(1).build();
        firstCard.add(new QCardDto());
        firstCard.add(new QCardDto());
        firstCard.add(new QCardDto());
        secCard.add(new QCardDto());
        secCard.add(new QCardDto());
        secCard.add(new QCardDto());

        firstCard.get(0).setField(0);
        firstCard.get(1).setField(0);
        firstCard.get(2).setField(0);
        secCard.get(0).setField(1);
        secCard.get(1).setField(1);
        secCard.get(2).setField(1);

        Long firid = userRepository.save(user1).getId();
        Long secid = userRepository.save(user2).getId();

        firstCard.get(0).setUserId(firid);
        firstCard.get(1).setUserId(firid);
        firstCard.get(2).setUserId(firid);
        secCard.get(0).setUserId(secid);
        secCard.get(1).setUserId(secid);
        secCard.get(2).setUserId(secid);

        Card card1_1 = Card.builder()
                .user(user1)
                .cardOrder(1L)
                .title("title1_1")
                .content("content1_1")
                .link("link1_1")
                .tagsString("tagsString1_1").build();
        firstCard.get(0).setTitle("title1_1");
        firstCard.get(0).setContent("content1_1");
        firstCard.get(0).setHashTags("tagsString1_1");

        Card card1_2 = Card.builder()
                .user(user1)
                .cardOrder(1L)
                .title("title1_2")
                .content("content1_2")
                .link("link1_2").tagsString("tagsString1_2").build();
        firstCard.get(1).setTitle("title1_2");
        firstCard.get(1).setContent("content1_2");
        firstCard.get(1).setHashTags("tagsString1_2");
        Card card1_3 = Card.builder()
                .user(user1)
                .cardOrder(1L)
                .title("title1_3")
                .content("content1_3")
                .link("link1_3").tagsString("tagsString1_3").build();
        firstCard.get(2).setTitle("title1_3");
        firstCard.get(2).setContent("content1_3");
        firstCard.get(2).setHashTags("tagsString1_3");

        Card card2_1 = Card.builder()
                .user(user2)
                .cardOrder(1L)
                .title("title2_1")
                .content("content2_1")
                .link("link2_1").tagsString("tagsString2_1").build();
        secCard.get(0).setTitle("title2_1");
        secCard.get(0).setContent("content2_1");
        secCard.get(0).setHashTags("tagsString2_1");
        Card card2_2 = Card.builder()
                .user(user2)
                .cardOrder(1L)
                .title("title2_2")
                .content("content2_2")
                .link("link2_2").tagsString("tagsString2_2").build();
        secCard.get(1).setTitle("title2_2");
        secCard.get(1).setContent("content2_2");
        secCard.get(1).setHashTags("tagsString2_2");
        Card card2_3 = Card.builder()
                .user(user2)
                .cardOrder(1L)
                .title("title2_3")
                .content("content2_3")
                .link("link2_3").tagsString("tagsString2_3").build();
        secCard.get(2).setTitle("title2_3");
        secCard.get(2).setContent("content2_3");
        secCard.get(2).setHashTags("tagsString2_3");
        cardRepository.save(card1_1);
        cardRepository.save(card1_2);
        cardRepository.save(card1_3);
        cardRepository.save(card2_1);
        cardRepository.save(card2_2);
        cardRepository.save(card2_3);
    }

    @Test
    @DisplayName("findByUserIdTest")
    void findByCategory01() {
        List<QCardDto> fircardList = cardRepository.findAllByUserId(firstCard.get(0).getUserId());
        List<QCardDto> seccardList = cardRepository.findAllByUserId(secCard.get(0).getUserId());
        for (int i = 0 ; i < 3 ;i++)
        {
            assertEquals(fircardList.get(i).getField(), firstCard.get(i).getField());
            assertEquals(fircardList.get(i).getUserId(), firstCard.get(i).getUserId());
            assertEquals(fircardList.get(i).getHashTags(), firstCard.get(i).getHashTags());
            assertEquals(fircardList.get(i).getContent(), firstCard.get(i).getContent());
            assertEquals(fircardList.get(i).getTitle(), firstCard.get(i).getTitle());

            assertEquals(seccardList.get(i).getField(), secCard.get(i).getField());
            assertEquals(seccardList.get(i).getUserId(), secCard.get(i).getUserId());
            assertEquals(seccardList.get(i).getHashTags(), secCard.get(i).getHashTags());
            assertEquals(seccardList.get(i).getContent(), secCard.get(i).getContent());
            assertEquals(seccardList.get(i).getTitle(), secCard.get(i).getTitle());

        }

    }
}
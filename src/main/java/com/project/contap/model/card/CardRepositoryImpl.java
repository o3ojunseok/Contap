package com.project.contap.model.card;

import com.project.contap.model.card.dto.QCardDto;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;

import javax.persistence.EntityManager;
import java.util.List;

public class CardRepositoryImpl implements CustomCardRepository {
    private final JPAQueryFactory queryFactory;

    public CardRepositoryImpl(EntityManager entityManager) {
        this.queryFactory = new JPAQueryFactory(entityManager);
    }

    @Override
    public List<QCardDto> findAllByUserId(Long userId)
    {
        QCard hu = QCard.card;
        return  queryFactory
                .select(
                        Projections.constructor(QCardDto.class,
                                hu.id,
                                hu.title,
                                hu.content,
                                hu.tagsString,
                                hu.user.id,
                                hu.user.field,
                                hu.link
                        )
                )
                .from(hu)
                .where(hu.user.id.eq(userId))
                .fetch();
    }
}

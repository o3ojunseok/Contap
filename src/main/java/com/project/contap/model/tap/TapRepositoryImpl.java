package com.project.contap.model.tap;

import com.project.contap.model.user.User;
import com.querydsl.jpa.impl.JPAQueryFactory;

import javax.persistence.EntityManager;
import java.util.List;

public class TapRepositoryImpl implements CustomTapRepository{
    private final JPAQueryFactory queryFactory;

    public TapRepositoryImpl(EntityManager entityManager) {
        this.queryFactory = new JPAQueryFactory(entityManager);
    }

    @Override
    public Tap checkReceiveTap(User receiver, User sender)
    {
        QTap qTap = QTap.tap;
        return queryFactory.select(qTap)
                .from(qTap)
                .where(qTap.sendUser.eq(receiver)
                        .and(qTap.receiveUser.eq(sender))
                        .and(qTap.status.eq(0)))
                .fetchOne();
    }
    @Override
    public Boolean checkSendTap(User receiver, User sender)
    {
        QTap qTap = QTap.tap;
        return queryFactory.from(qTap)
                .where(qTap.sendUser.eq(sender)
                        .and(qTap.receiveUser.eq(receiver))
                        .and(qTap.status.eq(0)))
                .fetchFirst() != null;
    }
    @Override
    public List<Tap> getMyTaps(User user)
    {
        QTap qTap = QTap.tap;
        return queryFactory
                .select(qTap)
                .from(qTap)
                .where(qTap.sendUser.eq(user)
                        .or(qTap.receiveUser.eq(user)))
                .fetch();
    }
}

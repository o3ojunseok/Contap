package com.project.contap.model.friend;

import com.project.contap.model.user.User;
import com.querydsl.jpa.impl.JPAQueryFactory;

import javax.persistence.EntityManager;
import java.util.List;

public class FriendRepositoryImpl implements CustomFriendRepository {
    private final JPAQueryFactory queryFactory;

    public FriendRepositoryImpl(EntityManager entityManager) {
        this.queryFactory = new JPAQueryFactory(entityManager);
    }
    public Boolean checkFriend(User me, User you)
    {
        QFriend qFriend = QFriend.friend;
        return  queryFactory.from(qFriend)
                .where(qFriend.me.eq(me)
                        .and(qFriend.you.eq(you)))
                .fetchFirst() != null;
    }
    @Override
    public Friend getFriend(User me, User you)
    {
        QFriend qFriend = QFriend.friend;
        return  queryFactory
                .select(qFriend)
                .from(qFriend)
                .where(qFriend.me.eq(me)
                        .and(qFriend.you.eq(you)))
                .fetchOne();
    }


    @Override
    public List<Friend> getallmyFriend(User user)
    {
        QFriend qFriend = QFriend.friend;
        return  queryFactory
                .select(qFriend)
                .from(qFriend)
                .where(qFriend.me.eq(user)
                        .or(qFriend.you.eq(user)))
                .fetch();
    }
}

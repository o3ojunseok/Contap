package com.project.contap.model.user;

import com.project.contap.common.SearchRequestDto;
import com.project.contap.common.enumlist.UserStatusEnum;
import com.project.contap.common.util.RandomNumberGeneration;
import com.project.contap.model.friend.QFriend;
import com.project.contap.model.tap.QTap;
import com.project.contap.model.user.dto.UserMainDto;
import com.project.contap.model.user.dto.UserTapDto;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;

import javax.persistence.EntityManager;
import java.util.List;

public class UserRepositoryImpl implements CustomUserRepository {
    private final JPAQueryFactory queryFactory;

    public UserRepositoryImpl(EntityManager entityManager) {
        this.queryFactory = new JPAQueryFactory(entityManager);
    }

    @Override
    public List<UserTapDto> findMysendORreceiveTapUserInfo(Long userId, int type, int page)
    {
        // 0 - 내가보낸
        // 1 - 내가받은
        page = 12*page;
        QTap qtap = QTap.tap;
        if (type == 0)
            return queryFactory
                    .select(
                            Projections.constructor(UserTapDto.class,
                                    qtap.receiveUser.id,
                                    qtap.receiveUser.email,
                                    qtap.receiveUser.profile,
                                    qtap.receiveUser.userName,
                                    qtap.receiveUser.hashTagsString,
                                    qtap.id,
                                    qtap.msg,
                                    qtap.receiveUser.field,
                                    qtap.newFriend
                            ))
                    .from(qtap)
                    .where(qtap.sendUser.id.eq(userId))
                    .orderBy(qtap.insertDt.desc())
                    .offset(page).limit(12)
                    .fetch();
        else
            return queryFactory
                    .select(
                            Projections.constructor(UserTapDto.class,
                                    qtap.sendUser.id,
                                    qtap.sendUser.email,
                                    qtap.sendUser.profile,
                                    qtap.sendUser.userName,
                                    qtap.sendUser.hashTagsString,
                                    qtap.id,
                                    qtap.msg,
                                    qtap.sendUser.field,
                                    qtap.newFriend
                            ))
                    .from(qtap)
                    .where(qtap.receiveUser.id.eq(userId))
                    .orderBy(qtap.insertDt.desc())
                    .offset(page).limit(12)
                    .fetch();

    }

    //사실 페이지기능을 고려해서 만들어진 쿼리였는데
    //지금은 페이지가 아니라 다 불러오는거로 바뀌엇으니까. 차후에 수정할필요가있다.
    //하지만 어찌될지몰라서 남겨둠.
    @Override
    public List<UserTapDto> findMyFriendsById(Long userId,List<String> orderList)
    {
        QFriend qfriend = QFriend.friend;
        return queryFactory
                .select(
                        Projections.constructor(UserTapDto.class,
                                qfriend.you.id,
                                qfriend.you.email,
                                qfriend.you.profile,
                                qfriend.you.userName,
                                qfriend.you.hashTagsString,
                                qfriend.you.field,
                                qfriend.roomId,
                                qfriend.newFriend,
                                qfriend.id
                        )).distinct()
                .from(qfriend)
                .where(qfriend.me.id.eq(userId)
                        .and(qfriend.roomId.in(orderList)))
                .fetch();
    }
    @Override
    public List<UserMainDto> findAllByTag(SearchRequestDto tagsandtype, Long userId)
    {
        BooleanBuilder builder = new BooleanBuilder();
        int page = 9*tagsandtype.getPage();
        QUser hu = QUser.user;
        if (tagsandtype.getType() == 0)
            for (String tagna : tagsandtype.getSearchTags()) {
                builder.or(hu.hashTagsString.contains("@"+tagna+"@"));
            }
        else
            for (String tagna : tagsandtype.getSearchTags()) {
                builder.and(hu.hashTagsString.contains("@"+tagna+"@"));
            }

        if(tagsandtype.getField() != 3)
        {
            builder.and(hu.field.eq(tagsandtype.getField()));
        }
        builder.and(hu.userStatus.eq(UserStatusEnum.ACTIVE));
        return queryFactory
                .select(
                        Projections.constructor(UserMainDto.class,
                                hu.id,
                                hu.email,
                                hu.profile,
                                hu.userName,
                                hu.hashTagsString,
                                hu.field
                        )).distinct()
                .from(hu)
                .where(builder
                        .and(hu.hashTagsString.ne("@_@"))
                        .and(hu.cards.isNotEmpty())
                        .and(hu.id.ne(userId)))
                .offset(page).limit(9)
                .fetch();
    }
    @Override
    public List<UserMainDto> getRandomUser(Long usercnt,Long myId)
    {

        Long check1 = (usercnt/9);
        Long check2 = (usercnt%9);

        Long page = check1;

        if(check2.equals(0L) && !check1.equals(0L))
            page = page - 1L;

        page = new Long(RandomNumberGeneration.randomRange(0,page.intValue()));

        QUser hu = QUser.user;
        return queryFactory
                .select(
                        Projections.constructor(UserMainDto.class,
                                hu.id,
                                hu.email,
                                hu.profile,
                                hu.userName,
                                hu.hashTagsString,
                                hu.field
                        )).distinct()
                .from(hu)
                .where(hu.userStatus.eq(UserStatusEnum.ACTIVE)
                        .and(hu.hashTagsString.ne("@_@"))
                        .and(hu.cards.isNotEmpty())
                        .and(hu.id.ne(myId)))
                .offset(page*9).limit(9)
                .fetch();
        //귀찮.. 다음에 페이징처리하자
    }

    @Override
    public Boolean existUserByUserName(String userName)
    {
        QUser qUser = QUser.user;
        return  queryFactory.from(qUser)
                .where(qUser.userName.eq(userName))
                .fetchFirst() != null;
    }
    @Override
    public Boolean existUserByPhoneNumber(String phoneNumber)
    {
        QUser qUser = QUser.user;
        return  queryFactory.from(qUser)
                .where(qUser.phoneNumber.eq(phoneNumber))
                .fetchFirst() != null;
    }

    @Override
    public Long getActiveUsercnt()
    {
        QUser qUser = QUser.user;
        return  queryFactory.select(qUser.id).from(qUser)
                .where(qUser.userStatus.eq(UserStatusEnum.ACTIVE)
                        .and(qUser.hashTagsString.ne("@_@"))
                        .and(qUser.cards.isNotEmpty()))
                .fetchCount();
    }

    @Override
    public Boolean existUserByEmailAndUserStatus(String email) {
        QUser qUser = QUser.user;
        return queryFactory.select(qUser.id).from(qUser)
                .where(qUser.userStatus.eq(UserStatusEnum.ACTIVE)
                        .and(qUser.email.eq(email)))
                .fetchFirst() != null;
    }
}

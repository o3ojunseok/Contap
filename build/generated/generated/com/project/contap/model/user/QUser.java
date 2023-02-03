package com.project.contap.model.user;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QUser is a Querydsl query type for User
 */
@Generated("com.querydsl.codegen.EntitySerializer")
public class QUser extends EntityPathBase<User> {

    private static final long serialVersionUID = -668364002L;

    public static final QUser user = new QUser("user");

    public final com.project.contap.common.util.QTimeStamped _super = new com.project.contap.common.util.QTimeStamped(this);

    public final NumberPath<Integer> authStatus = createNumber("authStatus", Integer.class);

    public final ListPath<com.project.contap.model.card.Card, com.project.contap.model.card.QCard> cards = this.<com.project.contap.model.card.Card, com.project.contap.model.card.QCard>createList("cards", com.project.contap.model.card.Card.class, com.project.contap.model.card.QCard.class, PathInits.DIRECT2);

    public final StringPath email = createString("email");

    public final NumberPath<Integer> field = createNumber("field", Integer.class);

    public final ListPath<com.project.contap.model.friend.Friend, com.project.contap.model.friend.QFriend> friends = this.<com.project.contap.model.friend.Friend, com.project.contap.model.friend.QFriend>createList("friends", com.project.contap.model.friend.Friend.class, com.project.contap.model.friend.QFriend.class, PathInits.DIRECT2);

    public final NumberPath<Long> githubId = createNumber("githubId", Long.class);

    public final StringPath hashTagsString = createString("hashTagsString");

    public final NumberPath<Long> id = createNumber("id", Long.class);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> insertDt = _super.insertDt;

    public final NumberPath<Long> kakaoId = createNumber("kakaoId", Long.class);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> modifiedDt = _super.modifiedDt;

    public final StringPath phoneNumber = createString("phoneNumber");

    public final StringPath profile = createString("profile");

    public final StringPath pw = createString("pw");

    public final StringPath userName = createString("userName");

    public final EnumPath<com.project.contap.common.enumlist.UserStatusEnum> userStatus = createEnum("userStatus", com.project.contap.common.enumlist.UserStatusEnum.class);

    public QUser(String variable) {
        super(User.class, forVariable(variable));
    }

    public QUser(Path<? extends User> path) {
        super(path.getType(), path.getMetadata());
    }

    public QUser(PathMetadata metadata) {
        super(User.class, metadata);
    }

}


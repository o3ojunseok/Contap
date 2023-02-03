package com.project.contap.model.friend;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QFriend is a Querydsl query type for Friend
 */
@Generated("com.querydsl.codegen.EntitySerializer")
public class QFriend extends EntityPathBase<Friend> {

    private static final long serialVersionUID = 1339181822L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QFriend friend = new QFriend("friend");

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final com.project.contap.model.user.QUser me;

    public final NumberPath<Integer> newFriend = createNumber("newFriend", Integer.class);

    public final StringPath roomId = createString("roomId");

    public final com.project.contap.model.user.QUser you;

    public QFriend(String variable) {
        this(Friend.class, forVariable(variable), INITS);
    }

    public QFriend(Path<? extends Friend> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QFriend(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QFriend(PathMetadata metadata, PathInits inits) {
        this(Friend.class, metadata, inits);
    }

    public QFriend(Class<? extends Friend> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.me = inits.isInitialized("me") ? new com.project.contap.model.user.QUser(forProperty("me")) : null;
        this.you = inits.isInitialized("you") ? new com.project.contap.model.user.QUser(forProperty("you")) : null;
    }

}


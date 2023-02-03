package com.project.contap.model.tap;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QTap is a Querydsl query type for Tap
 */
@Generated("com.querydsl.codegen.EntitySerializer")
public class QTap extends EntityPathBase<Tap> {

    private static final long serialVersionUID = -1645633112L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QTap tap = new QTap("tap");

    public final com.project.contap.common.util.QTimeStamped _super = new com.project.contap.common.util.QTimeStamped(this);

    public final NumberPath<Long> id = createNumber("id", Long.class);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> insertDt = _super.insertDt;

    //inherited
    public final DateTimePath<java.time.LocalDateTime> modifiedDt = _super.modifiedDt;

    public final StringPath msg = createString("msg");

    public final NumberPath<Integer> newFriend = createNumber("newFriend", Integer.class);

    public final com.project.contap.model.user.QUser receiveUser;

    public final com.project.contap.model.user.QUser sendUser;

    public final NumberPath<Integer> status = createNumber("status", Integer.class);

    public QTap(String variable) {
        this(Tap.class, forVariable(variable), INITS);
    }

    public QTap(Path<? extends Tap> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QTap(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QTap(PathMetadata metadata, PathInits inits) {
        this(Tap.class, metadata, inits);
    }

    public QTap(Class<? extends Tap> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.receiveUser = inits.isInitialized("receiveUser") ? new com.project.contap.model.user.QUser(forProperty("receiveUser")) : null;
        this.sendUser = inits.isInitialized("sendUser") ? new com.project.contap.model.user.QUser(forProperty("sendUser")) : null;
    }

}


package com.project.contap.model.card;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QCard is a Querydsl query type for Card
 */
@Generated("com.querydsl.codegen.EntitySerializer")
public class QCard extends EntityPathBase<Card> {

    private static final long serialVersionUID = -1253484994L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QCard card = new QCard("card");

    public final com.project.contap.common.util.QTimeStamped _super = new com.project.contap.common.util.QTimeStamped(this);

    public final NumberPath<Long> cardOrder = createNumber("cardOrder", Long.class);

    public final StringPath content = createString("content");

    public final NumberPath<Long> id = createNumber("id", Long.class);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> insertDt = _super.insertDt;

    public final StringPath link = createString("link");

    //inherited
    public final DateTimePath<java.time.LocalDateTime> modifiedDt = _super.modifiedDt;

    public final StringPath tagsString = createString("tagsString");

    public final StringPath title = createString("title");

    public final com.project.contap.model.user.QUser user;

    public QCard(String variable) {
        this(Card.class, forVariable(variable), INITS);
    }

    public QCard(Path<? extends Card> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QCard(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QCard(PathMetadata metadata, PathInits inits) {
        this(Card.class, metadata, inits);
    }

    public QCard(Class<? extends Card> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.user = inits.isInitialized("user") ? new com.project.contap.model.user.QUser(forProperty("user")) : null;
    }

}


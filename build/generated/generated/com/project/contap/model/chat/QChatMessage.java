package com.project.contap.model.chat;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.Generated;
import com.querydsl.core.types.Path;


/**
 * QChatMessage is a Querydsl query type for ChatMessage
 */
@Generated("com.querydsl.codegen.EntitySerializer")
public class QChatMessage extends EntityPathBase<ChatMessage> {

    private static final long serialVersionUID = -1856646423L;

    public static final QChatMessage chatMessage = new QChatMessage("chatMessage");

    public final DateTimePath<java.time.LocalDateTime> createdDt = createDateTime("createdDt", java.time.LocalDateTime.class);

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final StringPath message = createString("message");

    public final StringPath roomId = createString("roomId");

    public final StringPath writer = createString("writer");

    public QChatMessage(String variable) {
        super(ChatMessage.class, forVariable(variable));
    }

    public QChatMessage(Path<? extends ChatMessage> path) {
        super(path.getType(), path.getMetadata());
    }

    public QChatMessage(PathMetadata metadata) {
        super(ChatMessage.class, metadata);
    }

}


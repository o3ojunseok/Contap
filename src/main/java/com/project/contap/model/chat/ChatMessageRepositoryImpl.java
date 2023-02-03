package com.project.contap.model.chat;
import com.querydsl.jpa.impl.JPAQueryFactory;

import javax.persistence.EntityManager;
import java.util.List;

public class ChatMessageRepositoryImpl implements CustomChatMessageRepository {
    private final JPAQueryFactory queryFactory;

    public ChatMessageRepositoryImpl(EntityManager entityManager) {
        this.queryFactory = new JPAQueryFactory(entityManager);
    }
    @Override
    public List<ChatMessage> findMessage(String roomId, Long startId)
    {
        QChatMessage qChatMessage = QChatMessage.chatMessage;
        if(startId <= 0)
            return  queryFactory
                    .select(qChatMessage)
                    .from(qChatMessage)
                    .where(qChatMessage.roomId.eq(roomId))
                    .orderBy(qChatMessage.id.desc())
                    .offset(0).limit(15)
                    .fetch();
        else
            return  queryFactory
                    .select(qChatMessage)
                    .from(qChatMessage)
                    .where(qChatMessage.id.lt(startId).and(qChatMessage.roomId.eq(roomId)))
                    .orderBy(qChatMessage.id.desc())
                    .offset(0).limit(15)
                    .fetch();
    }

    @Override
    public ChatMessage findLastMessage(String roomId)
    {
        QChatMessage qChatMessage = QChatMessage.chatMessage;
        return queryFactory.select(qChatMessage).from(qChatMessage).where(qChatMessage.roomId.eq(roomId)).orderBy(qChatMessage.createdDt.desc()).offset(0).limit(1).fetchOne();
    }
}

package com.project.contap.model.card.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Getter
@Setter
@NoArgsConstructor
public class QCardDto {
    private Long cardId;
    private String title;
    private String content;
    private String hashTags;
    private Long userId;
    private int field;
    private String link;


    public QCardDto(
            Long cardId,
            String title,
            String content,
            String hashTags,
            Long userId,
            int field

    ) {
        this.cardId = cardId;
        this.title = title;
        this.content = content;
        this.hashTags = hashTags;
        this.userId = userId;
        this.field = field;
    }


    public QCardDto(
            Long cardId,
            String title,
            String content,
            String hashTags,
            Long userId,
            int field,
            String link

    ) {
        this.cardId = cardId;
        this.title = title;
        this.content = content;
        this.hashTags = hashTags;
        this.userId = userId;
        this.field = field;
        this.link = link;
    }
}

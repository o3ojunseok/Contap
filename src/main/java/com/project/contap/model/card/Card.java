package com.project.contap.model.card;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.project.contap.common.util.TimeStamped;
import com.project.contap.model.card.dto.BackRequestCardDto;
import com.project.contap.model.user.User;
import lombok.*;

import javax.persistence.*;

@Entity
@NoArgsConstructor
@Getter
@Setter
@AllArgsConstructor
@Builder
public class Card extends TimeStamped {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnore
    private User user;

    @Column
    private Long cardOrder; // (1~10)

    @Column
    private String title;

    @Column
    private String content;

    @Column
    private String link;

    @Column
    private String tagsString;

//    public Card(User user, Long cardOrder, String title, String content){
//        this.user = user;
//        this.cardOrder = cardOrder;
//        this.title = title;
//        this.content = content;
//    }
//
//    public Card(User user, Long cardOrder, String title, String content,List<HashTag> tags) {
//        this.user = user;
//        this.cardOrder = cardOrder;
//        this.title = title;
//        this.content = content;
//        this.tags = tags;
//    }

    public boolean isWritedBy(Long userId) {
        return this.user.getId().equals(userId);
    }

    public void update(BackRequestCardDto backRequestCardDto) {
        this.title = backRequestCardDto.getTitle();
        this.content = backRequestCardDto.getContent();
        this.tagsString = backRequestCardDto.getTagsStr();
        this.link = backRequestCardDto.getLink();
    }
}

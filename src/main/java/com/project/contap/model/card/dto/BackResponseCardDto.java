package com.project.contap.model.card.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Schema(name = "뒷면카드 Response Dto", description = "카드 아이디, 사용자 아이디,카드 제목, 내용 해쉬태그 String, link, 사용자 필드")
public class BackResponseCardDto {
    @Schema(description = "카드 아이디")
    private Long cardId;
    @Schema(description = "사용자 아이디")
    private Long userId;
    @Schema(description = "카드제목")
    private String title;
    @Schema(description = "카드내용")
    private String content;
    @Schema(description = "태그 String")
    private String tagsStr;
    @Schema(description = "카드내 link")
    private String link;
    @Schema(description = "사용자 필드")
    private int field;
}

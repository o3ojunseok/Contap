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
@Schema(name = "뒷면카드 Request Dto", description = "title, content, 해쉬태그 String, link")
public class BackRequestCardDto {
    @Schema(description = "카드제목")
    private String title;
    @Schema(description = "카드내용")
    private String content;
    @Schema(description = "카드해쉬태그 String")
    private String tagsStr;
    @Schema(description = "카드내 linkt")
    private String link;
}

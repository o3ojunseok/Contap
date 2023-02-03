package com.project.contap.model.user.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Schema(name = "앞면카드 Response Dto", description = "프로필 주소, 닉네임, 해쉬태그 String, 사용자 필드")
public class FrontResponseCardDto{
    @Schema(description = "프로필 주소")
    private String profile;
    @Schema(description = "닉네임")
    private String userName;
    @Schema(description = "해쉬태그 String")
    private String hashTagsString;
    @Schema(description = "사용자 필드")
    private int field;
}

package com.project.contap.model.user.dto;

import com.project.contap.model.card.dto.BackResponseCardDto;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
@Schema(name = "사용자 정보 Dto", description = "사용자 아이디, 비밀번호, 닉네임, 프로필사진, 필드, 권한, 해쉬태그(String), 뒷면카드(List)")
public class UserInfoDto {
    @Schema(description = "사용자 아이디")
    private Long userId;
    @Schema(description = "사용자 비밀번호")
    private String password;
    @Schema(description = "닉네임")
    private String userName;
    @Schema(description = "프로필 사진(주소)")
    private String profile;
    @Schema(description = "사용자 필드(0:BE,1:FE,2:Designer)")
    private int field;
    @Schema(description = "사용자 권한")
    private int authStatus;
    @Schema(description = "사용자 전화번호")
    private String phoneNumber;
    @Schema(description = "해쉬태그 String")
    private String hashTagsString;
    @Schema(description = "뒷면카드 List")
    private List<BackResponseCardDto> cardDtoList;
}

package com.project.contap.model.user.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
@Schema(name = "KakaoUser RequestDto",description = "Id,nickname,Email")
public class SnsUserInfoDto {

    @Schema(description = "Id")
    private Long id;
    @Schema(description = "nickname")
    private String nickname;
    @Schema(description = "Email")
    private String email;
    private String profile;

}
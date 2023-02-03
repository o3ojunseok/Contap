package com.project.contap.model.user.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Schema(name = "앞면카드 Request Dto", description = "프로필 주소, 닉네임, 해쉬태그 String, 필드")
public class FrontRequestCardDto {
    @Schema(description = "프로필 주소")
    private MultipartFile profile;
    @Schema(description = "닉네임")
    private String userName;
    @Schema(description = "해쉬태그 String")
    private String hashTagsStr;
    @Schema(description = "사용자 필드")
    private int field;
}

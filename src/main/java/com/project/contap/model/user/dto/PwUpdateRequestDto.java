package com.project.contap.model.user.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Schema(name = "PasswordChange RequestDto",description = "현재 비밀번호,새로운 비밀번호,새로운 비밀번호 확인")
public class PwUpdateRequestDto {

    private String email;

    @Schema(description = "현재 비밀번호")
    private String currentPw;
    @Schema(description = "새로운 비밀번호")
    private String newPw;
    @Schema(description = "새로운 비밀번호 확인")
    private String newPwCheck;
    public void setNewPw(String newPw){
        this.newPw = newPw;
    }
}

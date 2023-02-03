package com.project.contap.model.user.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Schema(name = "SignUp RequestDto",description = "Email,비밀번호,비밀번호 확인,userName")
public class UserLoginDto {
    String email;
    String pw;
}

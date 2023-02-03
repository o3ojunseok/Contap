package com.project.contap.model.user.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
@Schema(name = "Email인증 RequestDto",description = "Email,인증번호")
public class EmailRequestDto {

    @Schema(description = "Email")
    private String email;
    @Schema(description = "인증번호")
    private String certificationNumber;

    public EmailRequestDto(String email, String certificationNumber) {
        this.email = email;
        this.certificationNumber = certificationNumber;
    }
}


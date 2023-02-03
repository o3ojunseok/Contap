package com.project.contap.controller;

import com.project.contap.exception.ContapException;
import com.project.contap.model.user.dto.EmailRequestDto;
import com.project.contap.service.EmailService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.mail.MessagingException;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@Tag(name = "Email Controller Api V1")
public class EmailController {

    @ExceptionHandler(IllegalArgumentException.class)
    public String exceptionHandler(Exception e){
        return e.getMessage();
    }

    private final EmailService emailService;


    @Operation(summary = "인증번호 전송")
    @PostMapping("/email/send")
    public Map<String,String> sendEmail(@RequestBody EmailRequestDto requestDto) throws UnsupportedEncodingException, MessagingException{
        emailService.sendEmail(requestDto.getEmail());
        Map<String, String> result = new HashMap<>();

        result.put("result", "success");
        return result;
    }

    @Operation(summary = "인증번호 일치 확인")
    @PostMapping("/email/confirm")
    public Map<String,String>  emailVerification(@RequestBody EmailRequestDto requestDto) throws ContapException{
        emailService.verifyEmail(requestDto);
        Map<String, String> result = new HashMap<>();
        result.put("result", "success");
        return result;
    }

    @PostMapping("/email/send/password")
    public Map<String,String> sendEmailToChangePw(@RequestBody EmailRequestDto requestDto) throws UnsupportedEncodingException, MessagingException {
        emailService.sendEmailToChangePw(requestDto.getEmail());
        Map<String, String> result = new HashMap<>();
        result.put("result","success");
        return result;
    }

}

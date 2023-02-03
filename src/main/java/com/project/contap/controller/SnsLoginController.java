package com.project.contap.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.project.contap.model.user.User;
import com.project.contap.service.GithubUserService;
import com.project.contap.service.KakaoUserService;
import com.project.contap.security.jwt.JwtTokenProvider;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


import java.util.HashMap;
import java.util.Map;

@RestController
@Tag(name = "UserKakao Controller Api V1")
public class SnsLoginController {

    private final KakaoUserService kakaoUserService;
    private final JwtTokenProvider jwtTokenProvider;
    private final GithubUserService githubUserService;

    public SnsLoginController(KakaoUserService kakaoUserService, JwtTokenProvider jwtTokenProvider ,GithubUserService githubUserService) {
        this.kakaoUserService = kakaoUserService;
        this.jwtTokenProvider = jwtTokenProvider;
        this.githubUserService = githubUserService;
    }

    @Operation(summary = "Kakao Login")
    @GetMapping("/user/kakao")
    public Map<String,String> kakaoLogin(@RequestParam String code) throws JsonProcessingException {
        User user = kakaoUserService.kakaoLogin(code);

        Map<String,String> result =new HashMap<>();
        result.put("token",jwtTokenProvider.createToken(user.getEmail(), user.getEmail(), user.getUserName()));
        result.put("email", user.getEmail());
        result.put("userName", user.getUserName());
        result.put("profile", user.getProfile());
        result.put("result", "success");

        return result;
    }

    @Operation(summary = "github Login")
    @GetMapping("/user/github")
    public Map<String, String> githubLogin(@RequestParam String code) throws JsonProcessingException {
        User user = githubUserService.githubLogin(code);

        Map<String, String> result = new HashMap<>();
        result.put("token", jwtTokenProvider.createToken(user.getEmail(), user.getEmail(), user.getUserName()));
        result.put("email", user.getEmail());
        result.put("userName", user.getUserName());
        result.put("profile", user.getProfile());
        result.put("result", "success");

        return result;
    }



}
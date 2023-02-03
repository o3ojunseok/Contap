package com.project.contap.service;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.contap.common.enumlist.UserStatusEnum;
import com.project.contap.model.user.User;
import com.project.contap.model.user.UserRepository;
import com.project.contap.model.user.dto.SnsUserInfoDto;
import com.project.contap.security.UserDetailsImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.util.UUID;

@Service
public class GithubUserService {
    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;

    @Autowired
    public GithubUserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public User githubLogin(String code) throws JsonProcessingException {
// 1. "인가 코드"로 "액세스 토큰" 요청
        String accessToken = getAccessToken(code);

// 2. "액세스 토큰"으로 "카카오 사용자 정보" 가져오기
       SnsUserInfoDto snsUserInfoDto = getGithubUserInfo(accessToken);

// 3. "카카오 사용자 정보"로 필요시 회원가입  및 이미 같은 이메일이 있으면 기존회원으로 로그인-
        User GithubUser = registerGithubOrUpdateGithub(snsUserInfoDto);

// 4. 강제 로그인 처리
        forceLogin(GithubUser);

        return GithubUser;
    }

    private String getAccessToken(String code) throws JsonProcessingException {
// HTTP Header 생성
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-type", "application/x-www-form-urlencoded;charset=utf-8");
        headers.add("Accept", "application/json");

// HTTP Body 생성
        MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        body.add("client_secret", "638ac36d36739c6e03bc7ca381262e5989ed4379");
        body.add("client_id", "cd8954aa33808615839a");
        body.add("redirect_uri", "https://contap.co.kr/login?github");  //http://localhost:3000/login?github
        body.add("code", code);

// HTTP 요청 보내기
        HttpEntity<MultiValueMap<String, String>> githubTokenRequest =
                new HttpEntity<>(body, headers);
        RestTemplate rt = new RestTemplate();
        ResponseEntity<String> response = rt.exchange(
                "https://github.com/login/oauth/access_token",
                HttpMethod.POST,
                githubTokenRequest,
                String.class
        );

// HTTP 응답 (JSON) -> 액세스 토큰 파싱
        String responseBody = response.getBody();
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode jsonNode = objectMapper.readTree(responseBody);
        return jsonNode.get("access_token").asText();
    }

    private SnsUserInfoDto getGithubUserInfo(String accessToken) throws JsonProcessingException {
// HTTP Header 생성
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Bearer " + accessToken);
        headers.add("Content-type", "application/x-www-form-urlencoded;charset=utf-8");

// HTTP 요청 보내기
        HttpEntity<MultiValueMap<String, String>> githubUserInfoRequest = new HttpEntity<>(headers);
        RestTemplate rt = new RestTemplate();
        ResponseEntity<String> response = rt.exchange(
                "https://api.github.com/user",
                HttpMethod.GET,
                githubUserInfoRequest,
                String.class
        );

        String responseBody = response.getBody();
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode jsonNode = objectMapper.readTree(responseBody);
        Long id = jsonNode.get("id").asLong();
        String email = jsonNode.get("login").asText();
        String name = jsonNode.get("name").asText();
        String profile = jsonNode.get("avatar_url").asText();

        return new SnsUserInfoDto(id, name, email,profile);
    }

    private User registerGithubOrUpdateGithub(SnsUserInfoDto snsUserInfoDto) {
        User sameUser = userRepository.findByEmail(snsUserInfoDto.getEmail()).orElse(null);

        if (sameUser == null) {
            return registerGithubUserIfNeeded(snsUserInfoDto);
        } else {
            return updateGithubUser(sameUser, snsUserInfoDto);
        }
    }

    private User registerGithubUserIfNeeded(SnsUserInfoDto snsUserInfoDto) {
// DB 에 중복된 github Id 가 있는지 확인
        Long githubId = snsUserInfoDto.getId();
        User githubUser = userRepository.findByGithubId(githubId)
                .orElse(null);
        if (githubUser == null) {

            String name = snsUserInfoDto.getNickname();

// password: random UUID
            String password = UUID.randomUUID().toString();
            String encodedPassword = passwordEncoder.encode(password);


            String email = snsUserInfoDto.getEmail();
            String profile = snsUserInfoDto.getProfile();
            githubUser = User.builder()
                    .email(name + "@github.com")
                    .githubId(githubId)
                    .pw(encodedPassword)
                    .userName(name)
                    .hashTagsString("@_@")
                    .profile(profile)
                    .userStatus(UserStatusEnum.ACTIVE)
                    .build();
            userRepository.save(githubUser);

        }
        return githubUser;
    }

    private User updateGithubUser(User sameUser, SnsUserInfoDto snsUserInfoDto) {
        if (sameUser.getGithubId() == null) {
            sameUser.setGithubId(snsUserInfoDto.getId());
            userRepository.save(sameUser);
        }
        return sameUser;
    }

    private void forceLogin(User githubUser) {
        UserDetails userDetails = new UserDetailsImpl(githubUser);
        Authentication authentication = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }

}
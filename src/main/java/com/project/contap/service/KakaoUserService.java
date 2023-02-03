package com.project.contap.service;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.contap.common.enumlist.UserStatusEnum;
import com.project.contap.exception.ContapException;
import com.project.contap.exception.ErrorCode;
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
public class KakaoUserService {
    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;

    @Autowired
    public KakaoUserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public User kakaoLogin(String code) throws JsonProcessingException {
// 1. "인가 코드"로 "액세스 토큰" 요청
        String accessToken = getAccessToken(code);

// 2. "액세스 토큰"으로 "카카오 사용자 정보" 가져오기
        SnsUserInfoDto snsUserInfoDto = getKakaoUserInfo(accessToken);

// 3. "카카오 사용자 정보"로 필요시 회원가입  및 이미 같은 이메일이 있으면 기존회원으로 로그인
        User kakaoUser = registerKakaoOrUpdateKakao(snsUserInfoDto);

// 4. 강제 로그인 처리
        forceLogin(kakaoUser);

        return kakaoUser;
    }

    private String getAccessToken(String code) throws JsonProcessingException {
// HTTP Header 생성
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-type", "application/x-www-form-urlencoded;charset=utf-8");

// HTTP Body 생성
        MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        body.add("grant_type", "authorization_code");
        body.add("client_id", "c9bedfe3483ea916f1399af30bea3522");
        body.add("redirect_uri", "https://contap.co.kr/login?kakao"); //http://localhost:3000/login?kakao
        body.add("code", code);

// HTTP 요청 보내기
        HttpEntity<MultiValueMap<String, String>> kakaoTokenRequest =
                new HttpEntity<>(body, headers);
        RestTemplate rt = new RestTemplate();
        ResponseEntity<String> response = rt.exchange(
                "https://kauth.kakao.com/oauth/token",
                HttpMethod.POST,
                kakaoTokenRequest,
                String.class
        );

// HTTP 응답 (JSON) -> 액세스 토큰 파싱
        String responseBody = response.getBody();
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode jsonNode = objectMapper.readTree(responseBody);
        return jsonNode.get("access_token").asText();
    }

    private SnsUserInfoDto getKakaoUserInfo(String accessToken) throws JsonProcessingException {
// HTTP Header 생성
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Bearer " + accessToken);
        headers.add("Content-type", "application/x-www-form-urlencoded;charset=utf-8");

// HTTP 요청 보내기
        HttpEntity<MultiValueMap<String, String>> kakaoUserInfoRequest = new HttpEntity<>(headers);
        RestTemplate rt = new RestTemplate();
        ResponseEntity<String> response = rt.exchange(
                "https://kapi.kakao.com/v2/user/me",
                HttpMethod.POST,
                kakaoUserInfoRequest,
                String.class
        );

        String responseBody = response.getBody();
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode jsonNode = objectMapper.readTree(responseBody);
        Long id = jsonNode.get("id").asLong();
        String nickname = jsonNode.get("properties")
                .get("nickname").asText();
        String email;
        if(jsonNode.get("kakao_account").get("email") == null)
            email = UUID.randomUUID().toString() + "@contap.com";
        else
            email = jsonNode.get("kakao_account")
                    .get("email").asText();
        JsonNode profile = jsonNode.get("properties")
                .get("profile_image");
        String profileStr = "";
        if(profile != null)
            profileStr = profile.asText();

        return new SnsUserInfoDto(id, nickname, email,profileStr);
    }

    private User registerKakaoOrUpdateKakao(SnsUserInfoDto snsUserInfoDto) {
        User sameUser = userRepository.findByEmail(snsUserInfoDto.getEmail()).orElse(null);

        if (sameUser == null) {
            return registerKakaoUserIfNeeded(snsUserInfoDto);
        } else {
            return updateKakaoUser(sameUser, snsUserInfoDto);
        }
    }

    private User registerKakaoUserIfNeeded(SnsUserInfoDto snsUserInfoDto) {
// DB 에 중복된 Kakao Id 가 있는지 확인
        Long kakaoId = snsUserInfoDto.getId();
        User kakaoUser = userRepository.findByKakaoId(kakaoId)
                .orElse(null);
        if (kakaoUser == null) {
// 회원가입
// username: kakao nickname
            String nickname = snsUserInfoDto.getNickname();

// password: random UUID
            String password = UUID.randomUUID().toString();
            String encodedPassword = passwordEncoder.encode(password);

// email: kakao email
            String email = snsUserInfoDto.getEmail();
            String profile = snsUserInfoDto.getProfile();
            kakaoUser = User.builder()
                    .email(email)
                    .kakaoId(kakaoId)
                    .pw(encodedPassword)
                    .userName(nickname)
                    .profile(profile)
                    .hashTagsString("@_@")
                    .userStatus(UserStatusEnum.ACTIVE)
                    .build();
            userRepository.save(kakaoUser);

        }
        return kakaoUser;
    }

    private User updateKakaoUser(User sameUser, SnsUserInfoDto snsUserInfoDto) {
        if (sameUser.getKakaoId() == null) {
            System.out.println("중복");
            sameUser.setKakaoId(snsUserInfoDto.getId());
            sameUser.setProfile(snsUserInfoDto.getProfile());
            sameUser.setUserName(snsUserInfoDto.getNickname());
            userRepository.save(sameUser);
        }
        return sameUser;
    }

    private void forceLogin(User kakaoUser) {
        UserDetails userDetails = new UserDetailsImpl(kakaoUser);
        Authentication authentication = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }

}
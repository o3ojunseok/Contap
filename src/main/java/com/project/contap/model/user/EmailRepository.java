package com.project.contap.model.user;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Repository;

import java.time.Duration;

@RequiredArgsConstructor
@Repository
public class EmailRepository {

    private final String PREFIX = "email: ";
    private final int LIMIT_TIME = 3 * 60;

    private final StringRedisTemplate stringRedisTemplate;


    public void createEmailCertification(String email, String certificationNumber) {
        //시간제한 설정
        stringRedisTemplate.opsForValue()
                .set(PREFIX + email, certificationNumber, Duration.ofSeconds(LIMIT_TIME));
    }

    //인증번호 가져오기
    public String getEmailCertificationNum(String email) {
        return stringRedisTemplate.opsForValue().get(PREFIX + email);
    }

    //이메일 확인되면 인증번호 삭제
    public void removeEmailCertification(String email) {
        stringRedisTemplate.delete(PREFIX + email);
    }

    //레디스에 저장된 인증번호가 존재하는지 여부 확인하기
    public boolean hasKey(String email) {
        return stringRedisTemplate.hasKey(PREFIX + email);
    }

}

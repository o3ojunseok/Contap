package com.project.contap.model.user;

import com.project.contap.common.enumlist.UserStatusEnum;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long>, CustomUserRepository {

    Optional<User> findByEmail(String email);

    Optional<User> findByKakaoId(Long kakaoId);

    Optional<User> findByUserName(String nickname);

    boolean existsByEmail(String email);

    Page<User> findAll(Pageable pageable);

    Optional<User> findByGithubId(Long githubId);

    User findByEmailAndUserStatusEquals(String email, UserStatusEnum status);

    List<User> findByModifiedDtBeforeAndAndUserStatusEquals(LocalDateTime localDateTime, UserStatusEnum status);
}

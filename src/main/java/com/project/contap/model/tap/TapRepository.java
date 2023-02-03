package com.project.contap.model.tap;

import com.project.contap.model.user.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface TapRepository extends JpaRepository<Tap, Long>,CustomTapRepository {
    List<Tap> findAllByReceiveUser(User user);
}

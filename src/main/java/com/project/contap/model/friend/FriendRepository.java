package com.project.contap.model.friend;

import org.springframework.data.jpa.repository.JpaRepository;

public interface FriendRepository extends JpaRepository<Friend, Long>,CustomFriendRepository {
}

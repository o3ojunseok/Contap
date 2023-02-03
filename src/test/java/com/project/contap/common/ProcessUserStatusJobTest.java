package com.project.contap.common;

import com.project.contap.common.enumlist.UserStatusEnum;
import com.project.contap.common.util.ImageService;
import com.project.contap.common.util.ProcessUserStatusJob;
import com.project.contap.model.card.CardRepository;
import com.project.contap.model.chat.ChatMessageRepository;
import com.project.contap.model.chat.ChatRoomRepository;
import com.project.contap.model.friend.Friend;
import com.project.contap.model.friend.FriendRepository;
import com.project.contap.model.hashtag.HashTagRepositoty;
import com.project.contap.model.tap.TapRepository;
import com.project.contap.model.user.User;
import com.project.contap.model.user.UserRepository;
import com.project.contap.service.MypageService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ProcessUserStatusJobTest {
    @InjectMocks
    private ProcessUserStatusJob processUserStatusJob;

    @Mock
    private UserRepository userRepository;
    @Mock
    private CardRepository cardRepository;
    @Mock
    private FriendRepository friendRepository;
    @Mock
    private ChatRoomRepository chatRoomRepository;
    @Mock
    private ChatMessageRepository chatMessageRepository;
    @Mock
    private TapRepository tapRepository;
    @Mock
    private ImageService imageService;
    @Mock
    private Common common;

    @Test
    @DisplayName("성공 profile null, tag Java")
    void modifyFrontCard_success_1() throws Exception {
        List<User> users = new ArrayList<>();
        for(int i = 0 ; i < 5 ; i++) {
            User user = User.builder().email(String.format("email%d",i)).cards(new ArrayList<>()).build();
            users.add(user);
        }
        String roomId = UUID.randomUUID().toString();
        Friend friend1 = Friend.builder().me(users.get(0)).you(users.get(1)).roomId(roomId).build();
        Friend friend2 = Friend.builder().me(users.get(1)).you(users.get(0)).roomId(roomId).build();
        List<Friend> friends = new ArrayList<>();
        friends.add(friend1);
        friends.add(friend2);
        when(userRepository.findByModifiedDtBeforeAndAndUserStatusEquals(any(LocalDateTime.class),eq(UserStatusEnum.INACTIVE)))
                .thenReturn(users);
        when(friendRepository.getallmyFriend(any(User.class))).thenReturn(friends);
        processUserStatusJob.perform();

    }

}

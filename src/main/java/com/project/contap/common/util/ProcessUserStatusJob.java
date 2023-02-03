package com.project.contap.common.util;

import com.project.contap.model.chat.ChatMessage;
import com.project.contap.model.chat.ChatMessageRepository;
import com.project.contap.model.chat.ChatRoomRepository;
import com.project.contap.common.Common;
import com.project.contap.common.enumlist.UserStatusEnum;
import com.project.contap.model.card.CardRepository;
import com.project.contap.model.friend.Friend;
import com.project.contap.model.friend.FriendRepository;
import com.project.contap.model.tap.Tap;
import com.project.contap.model.tap.TapRepository;
import com.project.contap.model.user.User;
import com.project.contap.model.user.UserRepository;
import io.sentry.Sentry;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
public class ProcessUserStatusJob {
    private final UserRepository userRepository;
    private final CardRepository cardRepository;
    private final FriendRepository friendRepository;
    private final ChatRoomRepository chatRoomRepository;
    private final ChatMessageRepository chatMessageRepository;
    private final TapRepository tapRepository;
    private final ImageService imageService;
    private final Common common;

    // 초, 분, 시, 일, 월, 주 순서
    @Scheduled(cron = "0 0 4 * * *")
    @Transactional
    public void perform() throws Exception {
        try {
            List<User> oldUsers = userRepository.findByModifiedDtBeforeAndAndUserStatusEquals(LocalDateTime.now().minusMonths(1), UserStatusEnum.INACTIVE);

            for (User user : oldUsers) {
                cardRepository.deleteAll(user.getCards()); // 유저가 작성한 카드 삭제

                deleteUserChatFriendsTap(user); // 유저와 관련된 채팅,친구관계,탭 삭제.

                chatRoomRepository.deleteUser(user.getEmail()); // 유저와 관련된 redis관련 정보들 삭제

                imageService.deleteOldFile(user.getProfile()); // 유저와 관련된 이미지 파일 삭제

                userRepository.delete(user); // 유저 삭제
            }
        }
        catch (Exception ex)
        {
            Sentry.captureException(ex);
        }
    }

    private void deleteUserChatFriendsTap(User user) {
        List<Friend> friends = friendRepository.getallmyFriend(user);
        List<String> roomIds = new ArrayList<>();
        for(Friend friend : friends)
        {
            if(roomIds.contains(friend.getRoomId())){
                roomIds.remove(friend.getRoomId());
            }
            else{
                roomIds.add(friend.getRoomId());
                List<ChatMessage> msg = chatMessageRepository.findAllByRoomId(friend.getRoomId());
                chatMessageRepository.deleteAll(msg);
                chatRoomRepository.whenMakeFriend(friend.getRoomId(), friend.getYou().getEmail(),friend.getMe().getEmail());
            }
        }
        friendRepository.deleteAll(friends);
        List<Tap> taps = tapRepository.getMyTaps(user);
        tapRepository.deleteAll(taps);
    }

}

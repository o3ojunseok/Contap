package com.project.contap.common;

import com.project.contap.model.chat.ChatMessage;
import com.project.contap.model.chat.ChatMessageDTO;
import com.project.contap.model.chat.ChatMessageRepository;
import com.project.contap.model.chat.ChatRoomRepository;
import com.project.contap.common.enumlist.AuthorityEnum;
import com.project.contap.common.enumlist.MsgTypeEnum;
import com.project.contap.model.friend.Friend;
import com.project.contap.model.friend.FriendRepository;
import com.project.contap.model.user.User;
import net.nurigo.java_sdk.api.Message;
import net.nurigo.java_sdk.exceptions.CoolsmsException;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

@Component
public class Common {
    private final ChatRoomRepository chatRoomRepository;
    private final RedisTemplate redisTemplate;
    private final ChannelTopic channelTopic;
    private final FriendRepository friendRepository;
    private final ChatMessageRepository chatMessageRepository;

    @Value("${common.sendsms.apikey}")
    private String api_key;
    @Value("${common.sendsms.secret}")
    private  String api_secret;
    @Value("${common.sendsms.sitenumber}")
    private  String siteNumber;

    @Autowired
    public Common(ChatRoomRepository chatRoomRepository,
                  RedisTemplate redisTemplate,
                  ChannelTopic channelTopic,
                  FriendRepository friendRepository,
                  ChatMessageRepository chatMessageRepository)
    {
        this.chatRoomRepository = chatRoomRepository;
        this.redisTemplate=redisTemplate;
        this.channelTopic = channelTopic;
        this.friendRepository = friendRepository;
        this.chatMessageRepository = chatMessageRepository;
    }

    //진짜 잡다한 기능들만 들어갈 예정인 클래스..
    public ChatMessageDTO setChatMessageDTO(MsgTypeEnum type,String receiver, String sender,String receiverSession)
    {
        ChatMessageDTO msg = new ChatMessageDTO();
        msg.setType(type.getValue());
        msg.setReciever(receiver);
        msg.setWriter(sender);
        msg.setMessage(type.getMsg());
        msg.setSessionId(receiverSession);
        return msg;
    }

    public void sendAlarmIfneeded(MsgTypeEnum type, String tapSender, String tapReceiver, User user) {
        String receiverssesion = chatRoomRepository.getSessionId(tapReceiver);
        if(receiverssesion != null) {
            ChatMessageDTO msg = setChatMessageDTO(type,tapReceiver,tapSender,receiverssesion);
            redisTemplate.convertAndSend(channelTopic.getTopic(), msg);
        }
        else{
            chatRoomRepository.setAlarm(tapReceiver,type.getAlarmEnum());
            if(type.equals(MsgTypeEnum.SEND_TAP))
                sendSMS(user);
        }
    }

    public void makeChatRoom(User sendUser, User receiveUser) {
        String roomId = UUID.randomUUID().toString();
        Friend fir = Friend.builder()
                .roomId(roomId)
                .me(receiveUser)
                .you(sendUser)
                .newFriend(1)
                .build();
        Friend sec = Friend.builder()
                .roomId(roomId)
                .me(sendUser)
                .you(receiveUser)
                .newFriend(1)
                .build();
        friendRepository.save(fir);
        friendRepository.save(sec);
        chatRoomRepository.whenMakeFriend(roomId,sendUser.getEmail(),receiveUser.getEmail());
    }

    private void sendSMS(User user) {
        if(user.getPhoneNumber()==null || user.getPhoneNumber().equals(""))
            return;
        if((user.getAuthStatus()&AuthorityEnum.ALARM.getAuthority())!=AuthorityEnum.ALARM.getAuthority())
            return;
        Message coolsms = new Message(api_key, api_secret);
        HashMap<String, String> params = new HashMap<String, String>();
        params.put("to", user.getPhoneNumber());   // 탭요청 알람을 받기위해서 정확하게 기재해주세요
        params.put("from", siteNumber); //사전에 사이트에서 번호를 인증하고 등록하여야 함 // 070 번호하나사고
        params.put("type", "SMS");
        params.put("text", user.getUserName() + "님 탭을 확인해보세요."); //메시지 내용
        params.put("app_version", "test app 1.2");
        try {
            JSONObject obj = (JSONObject) coolsms.send(params);
            System.out.println(obj.toString()); //전송 결과 출력
        } catch (CoolsmsException e) {
            System.out.println(e.getMessage());
            System.out.println(e.getCode());
        }
    }
    public void DbinfoToRedis()
    {
        chatRoomRepository.serverRestart();
        List<Friend> friends =  friendRepository.findAll();
        List<String> roomIds = new ArrayList<>();
        ChatMessage msg = null;
        for(Friend friend : friends)
        {
            if(roomIds.contains(friend.getRoomId())){
                roomIds.remove(friend.getRoomId());
            }
            else{
                roomIds.add(friend.getRoomId());
                msg = chatMessageRepository.findLastMessage(friend.getRoomId());
                chatRoomRepository.setDBinfoToRedis(friend,msg);
            }
        }
    }
}

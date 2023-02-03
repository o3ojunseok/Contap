package com.project.contap.controller;

import com.project.contap.common.enumlist.UserStatusEnum;
import com.project.contap.common.util.ImageService;
import com.project.contap.exception.ContapException;
import com.project.contap.model.card.Card;
import com.project.contap.model.card.CardRepository;
import com.project.contap.model.chat.ChatMessage;
import com.project.contap.model.chat.ChatMessageRepository;
import com.project.contap.model.chat.ChatRoomRepository;
import com.project.contap.model.friend.Friend;
import com.project.contap.model.friend.FriendRepository;
import com.project.contap.model.hashtag.HashTag;
import com.project.contap.model.hashtag.HashTagRepositoty;
import com.project.contap.model.tap.Tap;
import com.project.contap.model.tap.TapRepository;
import com.project.contap.model.user.User;
import com.project.contap.model.user.UserRepository;
import com.project.contap.service.ContapService;
import com.project.contap.service.MainService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@RestController
public class TestController {
    @Value("${logging.module.version}")
    private String version;

    private final UserRepository userRepository;
    private final HashTagRepositoty hashTagRepositoty;
    private final CardRepository cardRepository;
    private final PasswordEncoder passwordEncoder;
    private final FriendRepository friendRepository;
    private final ChatRoomRepository chatRoomRepository;
    private final ChatMessageRepository chatMessageRepository;
    private final TapRepository tapRepository;
    private final MainService mainService;
    private final ContapService contapService;
    private final ImageService imageService;
    @Autowired
    public TestController(
            UserRepository userRepository,
            HashTagRepositoty hashTagRepositoty,
            CardRepository cardRepository,
            PasswordEncoder passwordEncoder,
            FriendRepository friendRepository,
            ChatRoomRepository chatRoomRepository,
            ChatMessageRepository chatMessageRepository,
            TapRepository tapRepository,
            MainService mainService,
            ContapService contapService,
            ImageService imageService
    ) {
        this.userRepository = userRepository;
        this.hashTagRepositoty =hashTagRepositoty;
        this.cardRepository =  cardRepository;
        this.passwordEncoder = passwordEncoder;
        this.friendRepository = friendRepository;
        this.chatRoomRepository = chatRoomRepository;
        this.chatMessageRepository = chatMessageRepository;
        this.tapRepository = tapRepository;
        this.mainService = mainService;
        this.contapService = contapService;
        this.imageService = imageService;
    }

    @GetMapping("/forclient1/{id}") // 한유저가쓴 카드 모두조회
    List<Card> forclient1(
            @PathVariable Long id
    ) throws ContapException {
        return null;
    }

    @GetMapping("/forclient2/{id}") // 한유저가쓴 카드 모두조회
    User forclient2(
            @PathVariable Long id
    ) throws ContapException {
        User user = userRepository.findById(id).orElse(null);
        return user;
    }
    @GetMapping("/forclient3/{id}") // 한유저가쓴 카드 모두조회
    User forclient3(
            @PathVariable String id
    ) throws ContapException {
        User user = userRepository.findByEmail(id).orElse(null);
        return user;
    }
    @GetMapping("/forclient4/{id}") // 한유저가쓴 카드 모두조회
    Card forclient4(
            @PathVariable Long id
    ) throws ContapException {
        Card card = cardRepository.findById(id).orElse(null);
        return card;
    }

    @GetMapping("/HashSet") // 한유저가쓴 카드 모두조회
    void forclient4() throws ContapException {
        HashTag check = hashTagRepositoty.findById(1L).orElse(null);
        if(check != null)
            return;
        List<HashTag> hashs = new ArrayList<>();
        hashs.add(new HashTag("Spring",0));
        hashs.add(new HashTag("Spring Boot",0));
        hashs.add(new HashTag("GO",0));
        hashs.add(new HashTag("React",0));
        hashs.add(new HashTag("React Native",0));
        hashs.add(new HashTag("Flutter",0));
        hashs.add(new HashTag("Node.js",0));
        hashs.add(new HashTag("Python",0));
        hashs.add(new HashTag("C++",0));
        hashs.add(new HashTag("C",0));
        hashs.add(new HashTag("C#",0));
        hashs.add(new HashTag("Angular",0));
        hashs.add(new HashTag("Vue.js",0));
        hashs.add(new HashTag("Express",0));
        hashs.add(new HashTag("Django",0));
        hashs.add(new HashTag("Next.js",0));
        hashs.add(new HashTag("SQL",0));
        hashs.add(new HashTag("Nest.js",0));
        hashs.add(new HashTag("Java",0));
        hashs.add(new HashTag("HTML CSS",0));
        hashs.add(new HashTag("TypeScript",0));
        hashs.add(new HashTag("Android Studio",0));
        hashs.add(new HashTag("Ruby",0));
        hashs.add(new HashTag("JavaScript",0));
        hashs.add(new HashTag("Swift",0));
        hashs.add(new HashTag("Assembly",0));
        hashs.add(new HashTag("PHP",0));
        hashs.add(new HashTag("Nuxt.js",0));
        hashs.add(new HashTag("Flask",0));
        hashs.add(new HashTag("JQuery",0));
        hashs.add(new HashTag("Figma",0));
        hashs.add(new HashTag("After Effects",0));
        hashs.add(new HashTag("Illustrator",0));
        hashs.add(new HashTag("Sketch",0));
        hashs.add(new HashTag("Adobe XD",0));
        hashs.add(new HashTag("Photoshop",0));
        hashs.add(new HashTag("Proto.io",0));
        hashs.add(new HashTag("AutoCAD",0));
        hashs.add(new HashTag("Premiere Pro",0));
        hashs.add(new HashTag("Zeplin",0));
        hashs.add(new HashTag("렌더링",1));
        hashs.add(new HashTag("범용성",1));
        hashs.add(new HashTag("오너십",1));
        hashs.add(new HashTag("TestCode",1));
        hashs.add(new HashTag("가독성",1));
        hashs.add(new HashTag("유지보수",1));
        hashs.add(new HashTag("개발과정",1));
        hashs.add(new HashTag("완성도",1));
        hashs.add(new HashTag("호기심",1));
        hashs.add(new HashTag("Log",1));
        hashs.add(new HashTag("Library",1));
        hashs.add(new HashTag("신기술",1));
        hashs.add(new HashTag("데드라인",1));
        hashs.add(new HashTag("리더십",1));
        hashs.add(new HashTag("성장",1));
        hashs.add(new HashTag("Logic",1));
        hashs.add(new HashTag("리팩토링",1));
        hashs.add(new HashTag("인터랙션",1));
        hashs.add(new HashTag("응답속도",1));
        hashs.add(new HashTag("소통",1));
        hashs.add(new HashTag("커버리지",1));
        hashs.add(new HashTag("Infra",1));
        hashs.add(new HashTag("예외처리",1));
        hashs.add(new HashTag("웹소켓",1));
        hashs.add(new HashTag("Debug",1));
        hashs.add(new HashTag("JWT",1));
        hashs.add(new HashTag("최적화",1));
        hashs.add(new HashTag("OAuth",1));
        hashs.add(new HashTag("시각화",1));
        hashs.add(new HashTag("API설계",1));
        hashs.add(new HashTag("리서치",1));
        hashs.add(new HashTag("UX/UI",1));
        hashs.add(new HashTag("CX",1));
        hashs.add(new HashTag("UX라이팅",1));
        hashs.add(new HashTag("마케팅",1));
        hashs.add(new HashTag("퍼블리싱",1));
        hashs.add(new HashTag("콘텐츠",1));
        hashs.add(new HashTag("PM",1));
        hashs.add(new HashTag("브랜딩",1));
        hashs.add(new HashTag("기획",1));

        hashTagRepositoty.saveAll(hashs);
    }

    @GetMapping("/UserSet") // 한유저가쓴 카드 모두조회
    void userSet() throws ContapException {
        User check = userRepository.findById(1L).orElse(null);
        if(check != null)
            return;
        String pw = passwordEncoder.encode("commonpw"); // 패스워드 암호화
        for(int i = 0 ; i < 50 ; i++)
        {
            User user = User.builder()
                    .email(String.format("useremail%d@gmail.com",i))
                    .pw(pw)
                    .userName(String.format("userName%d",i))
                    .field(i%3)
                    .userStatus(UserStatusEnum.ACTIVE).build();
            userRepository.save(user);
        }
    }

    @GetMapping("/testdeleteuser/{userId}")
    void auserHashSet( @PathVariable Long userId ) throws ContapException
    {
        User user = userRepository.findById(userId).orElse(null);
        userRepository.delete(user);
    }

    @GetMapping("/CardSet")
    void CardSet() throws ContapException
    {
        for(Long i = 1L ; i <= 50L ; i++)
        {
            User user=userRepository.findById(i).orElse(null);
            user.getCards().clear();
            for(int j = 0 ; j <3;j++)
            {
                Card card = Card.builder()
                        .user(user)
                        .cardOrder(new Long(j+1))
                        .title(String.format("title%d_%d",i,j))
                        .content(String.format("content%d_%d",i,j))
                        .link(String.format("link%d_%d",i,j))
                        .build();

                user.getCards().add(cardRepository.save(card));
            }
            userRepository.save(user);
        }
    }
    @GetMapping("/friend")
    void friendSet() throws ContapException
    {
        for(Long i = 1L ; i <= 30L ; i = i+2)
        {
            User user1=userRepository.findById(i).orElse(null);
            User user2=userRepository.findById(i+1).orElse(null);
            String roomId = UUID.randomUUID().toString();
            Friend fir = Friend.builder().me(user1).you(user2).roomId(roomId).newFriend(1).build();
            Friend sec = Friend.builder().me(user2).you(user1).roomId(roomId).newFriend(1).build();
            friendRepository.save(fir);
            friendRepository.save(sec);
            chatRoomRepository.whenMakeFriend(roomId,user1.getEmail(),user2.getEmail());
        }
    }
    @Transactional
    @GetMapping("/friend/{userId}")
    public void perform(@PathVariable Long userId) throws Exception {
        User user2 = userRepository.findById(userId).orElse(null);
        List<User> oldUsers = new ArrayList<>();
        oldUsers.add(user2);
        for(User user: oldUsers){
            List<Card> cards =  cardRepository.findAllByUser(user);
            cardRepository.deleteAll(cards);
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
                    chatRoomRepository.whendeleteFriend(friend.getRoomId(),friend.getYou().getEmail(),friend.getMe().getEmail());
                }
            }
            friendRepository.deleteAll(friends);
            List<Tap> taps = tapRepository.getMyTaps(user);
            tapRepository.deleteAll(taps);
            chatRoomRepository.deleteUser(user.getEmail());
            user.setUserStatus(UserStatusEnum.WITHDRAWN);
        }
        userRepository.delete(user2);
    }

    @GetMapping("/dotappp/{userId}")
    public void performmm(@PathVariable Long userId) throws Exception
    {
        User user = userRepository.findById(userId).orElse(null);
        if(user == null)
            return;
        String pw = passwordEncoder.encode("commonpw"); // 패스워드 암호화
        for(int i = 0 ; i < 100 ; i++)
        {

            User user1 = userRepository.findByEmail(String.format("useremail%d@gmail.com",i)).orElse(null);
            if(user1 == null) {
                user1 = User.builder()
                        .email(String.format("useremail%d@gmail.com", i))
                        .pw(pw)
                        .userName(String.format("userName%d", i))
                        .field(i % 3)
                        .userStatus(UserStatusEnum.ACTIVE).build();
                user1 = userRepository.save(user1);
            }
            mainService.dotap(user1,user.getId(),"우석님을위한API");
        }
    }

    @GetMapping("/mytapok/{userId}")
    public void performnmggm(@PathVariable Long userId) throws Exception
    {
        User user = userRepository.findById(userId).orElse(null);
        if(user == null)
            return;
        List<Tap> taps= tapRepository.findAllByReceiveUser(user);
        for(Tap tap : taps)
        {
            contapService.rapAccept(tap.getId(),user.getEmail());
        }

    }
}


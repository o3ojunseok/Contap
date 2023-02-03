package com.project.contap.service;

import com.project.contap.common.enumlist.AuthorityEnum;
import com.project.contap.common.util.ImageService;
import com.project.contap.exception.ContapException;
import com.project.contap.exception.ErrorCode;
import com.project.contap.model.card.Card;
import com.project.contap.model.card.CardRepository;
import com.project.contap.model.card.dto.BackRequestCardDto;
import com.project.contap.model.card.dto.BackResponseCardDto;
import com.project.contap.model.hashtag.HashTag;
import com.project.contap.model.hashtag.HashTagRepositoty;
import com.project.contap.model.user.User;
import com.project.contap.model.user.UserRepository;
import com.project.contap.model.user.dto.FrontRequestCardDto;
import com.project.contap.model.user.dto.FrontResponseCardDto;
import com.project.contap.model.user.dto.UserInfoDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.*;


@RequiredArgsConstructor
@Service
public class MypageService {

    private final String SPLIT_CHAR = ",";

    private final UserRepository userRepository;
    private final CardRepository cardRepository;
    private final HashTagRepositoty hashTagRepositoty;
    private final ImageService imageService;
    private final UserService userService;

    // 회원 정보 가져오기
    // 가져오는 값 : 기본 회원정보(앞면카드), 모든 뒷면카드
    @Transactional(readOnly = true)
    public UserInfoDto getMyInfo(User user) {
        List<BackResponseCardDto> cardDtoList = new ArrayList<>();
        List<Card> userCards = cardRepository.findAllByUser(user);
        Collections.reverse(userCards);
        for(Card card: userCards) {
            BackResponseCardDto cardDto = makeBackResponseCardDto(card, user);
            cardDtoList.add(cardDto);
        }
        return UserInfoDto.builder()
                .userId(user.getId())
                .password(user.getPw())
                .userName(user.getUserName())
                .profile(user.getProfile())
                .field(user.getField())
                .authStatus(user.getAuthStatus())
                .hashTagsString(user.getHashTagsString())
                .cardDtoList(cardDtoList).build();
    }

    @Transactional
    public FrontResponseCardDto modifyFrontCard(FrontRequestCardDto frontRequestCardDto, User user) throws IOException {
        // nickname 변경했을 경우 중복체크
        boolean bcheck = user.checkForMain();
        if(!user.getUserName().equals(frontRequestCardDto.getUserName())) {
            Boolean found = userRepository.existUserByUserName(frontRequestCardDto.getUserName());
            if (found)
                throw new ContapException(ErrorCode.NICKNAME_DUPLICATE);
        }

        // profile 변경외에는 null
        String uploadImageUrl = getImgPath(frontRequestCardDto.getProfile(),user.getProfile());

        String requestTagStr = frontRequestCardDto.getHashTagsStr();
        Set<String> sets = new HashSet<>();

        if(requestTagStr.contains(SPLIT_CHAR))
            Collections.addAll(sets, requestTagStr.split(SPLIT_CHAR));
        else if(requestTagStr.length()>0)
            sets.add(requestTagStr);

        List<HashTag> hashTagList = hashTagRepositoty.findAllByNameIn(sets);

        StringBuilder retTagStrBuilder = new StringBuilder();
        StringBuilder intrestTagStrBuilder = new StringBuilder();
        for(HashTag tag: hashTagList) {
            if(tag.getType() == 0) { // stack
                retTagStrBuilder.append("@"+tag.getName());
            }
            else if(tag.getType() == 1) { // interest
                intrestTagStrBuilder.append(tag.getName()+"@");
            }
        }
        retTagStrBuilder.append("@_@");
        retTagStrBuilder.append(intrestTagStrBuilder);
        //user
        user.setProfile(uploadImageUrl);
        user.setUserName(frontRequestCardDto.getUserName());
        user.setHashTagsString(retTagStrBuilder.toString());
        user.setField(frontRequestCardDto.getField());
        user = userRepository.save(user);
        if(user.checkForMain() != bcheck)
            User.setUserCount(bcheck);

        //response
        return FrontResponseCardDto.builder()
                .profile(user.getProfile())
                .userName(user.getUserName())
                .hashTagsString(user.getHashTagsString())
                .field(user.getField()).build();
    }

    @Transactional
    public BackResponseCardDto createBackCard(BackRequestCardDto backRequestCardDto, User user) {
        boolean bcheck = user.checkForMain();
        user.setCards(cardRepository.findAllByUser(user));
        int cardSize = user.getCards().size();
        if( cardSize >= 10 )
            throw new ContapException(ErrorCode.EXCESS_CARD_MAX);
        Card card = Card.builder()
                .cardOrder(Long.valueOf(cardSize +1))
                .user(user)
                .title(backRequestCardDto.getTitle())
                .content(backRequestCardDto.getContent())
                .tagsString(backRequestCardDto.getTagsStr())
                .link(backRequestCardDto.getLink())
                .build();
        card = cardRepository.save(card);

        int authStatus = user.getAuthStatus()| AuthorityEnum.CAN_OTHER_READ.getAuthority();
        user.setAuthStatus(authStatus);
        user = userRepository.save(user);
        if(user.checkForMain() != bcheck)
            User.setUserCount(bcheck);
        return makeBackResponseCardDto(card,user);
    }

    //뒷카드 수정
    @Transactional
    public BackResponseCardDto modifyBackCard(Long cardId, BackRequestCardDto backRequestCardDto, User user) {
        boolean bcheck = user.checkForMain();
        Card card = cardRepository.findById(cardId).orElse(null);
        if(card == null)
            throw new ContapException(ErrorCode.NOT_FOUND_CARD); //해당 카드를 찾을 수 없습니다.
        //card 값 넣기
        card.update(backRequestCardDto);
        card = cardRepository.save(card);
        //response
        if(user.checkForMain() != bcheck)
            User.setUserCount(bcheck);
        return makeBackResponseCardDto(card,user);
    }

    //뒷카드 삭제
    @Transactional
    public BackResponseCardDto deleteBackCard(Long cardId, User user) {
        boolean bcheck = user.checkForMain();
        user.setCards(cardRepository.findAllByUser(user)); //우선 지워도 상관없을듯해서 지워봤음 문제발생시 풀어줘요 LSJ size 10 제한 사라지면서 필요없어짐요ㅛ
        Card card = cardRepository.findById(cardId).orElse(null);
        if(card == null)
            throw new ContapException(ErrorCode.NOT_FOUND_CARD); //해당 카드를 찾을 수 없습니다.
        if(!card.isWritedBy(user.getId()))
            throw new ContapException(ErrorCode.ACCESS_DENIED); //권한이 없습니다.

        cardRepository.delete(card);

        if(user.getCards().size()==1) {
            int authStatus = user.getAuthStatus() & (AuthorityEnum.ALL_AUTHORITY.getAuthority() - AuthorityEnum.CAN_OTHER_READ.getAuthority());
            user.setAuthStatus(authStatus);
            userRepository.save(user);
        }
        if(user.checkForMain() != bcheck)
            User.setUserCount(bcheck);
        return makeBackResponseCardDto(card,user);
    }

    // 뒷카드 Response make
    private BackResponseCardDto makeBackResponseCardDto(Card card, User user){
        return BackResponseCardDto.builder()
                .cardId(card.getId())
                .userId(user.getId())
                .title(card.getTitle())
                .content(card.getContent())
                .tagsStr(card.getTagsString())
                .link(card.getLink())
                .field(user.getField())
                .build();
    }

    private String getImgPath(MultipartFile profile, String profile1) throws IOException {
        if(profile!=null)
            return imageService.upload(profile, "static", profile1);
        else
            return profile1;
    }

}

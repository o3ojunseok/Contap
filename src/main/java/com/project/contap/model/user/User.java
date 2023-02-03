package com.project.contap.model.user;

import com.project.contap.common.enumlist.AuthorityEnum;
import com.project.contap.common.enumlist.UserStatusEnum;
import com.project.contap.common.util.TimeStamped;
import com.project.contap.model.card.Card;
import com.project.contap.model.friend.Friend;
import com.project.contap.model.user.dto.PwUpdateRequestDto;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import javax.persistence.*;
import java.util.List;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@EqualsAndHashCode
@Builder
@Schema(name = "사용자 정보", description = "")
public class User extends TimeStamped {
    // ID가 자동으로 생성 및 증가합니다.
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Schema(description = "사용자 아이디")
    private Long id;

    @Column(nullable = false, unique = true)
    @Schema(description = "사용자 이메일")
    private String email;

    @Column(nullable = false)
    @Schema(description = "사용자 비밀번호")
    private String pw;

    @Column(nullable = false)
    @Schema(description = "사용자 이름(닉네임)")
    private String userName;

    @Column(unique = true)
    @Schema(description = "카카오 아이디")
    private Long kakaoId;

    @Column(unique = true)
    private Long githubId;


    @Column(unique = false) // profile img path
    @Schema(description = "프로필 사진")
    private String profile;

    @Column
    @Schema(description = "해쉬태그 String형(형식 예:@Spring@_@여행@ / @_@여행@)")
    private String hashTagsString;

    @Column
    @Schema(description = "필드(0:BE, 1:FE, 2:Designer)")
    private int field; // 0:BE, 1:FE, 2:Designer

    @Column(unique = true)
    @Schema(description = "사용자 핸드폰번호")
    private String phoneNumber;

    @Column
    @Schema(description = "사용자 권한(bit로 관리함) 0001:폰,0010:프로필,0100:otherUserRead,1000:alarm")
    private int authStatus;

    @Column
    @Schema(description = "현재 회원 휴면, 비휴면, 탈퇴상태")
    private UserStatusEnum userStatus;

    @OneToMany(mappedBy = "user")
    @Schema(description = "뒷면카드")
    private List<Card> cards;


    @OneToMany(mappedBy = "me", fetch = FetchType.LAZY)
    @Schema(description = "그랩관계")
    private  List<Friend> friends;

    public static Long userCount = 0L;

    public void updatePw(PwUpdateRequestDto requestDto) {
        this.pw = requestDto.getNewPw();
    }

    public boolean isWrittenBy(User user) {
        return this.email.equals(user.getEmail());
    }
    public boolean checkForMain()
    {
        boolean checkCard = (this.authStatus & AuthorityEnum.CAN_OTHER_READ.getAuthority()) > 0 ;
        boolean checkTag = true;
        if(this.hashTagsString == null || this.hashTagsString.length() <= 3)
            checkTag = false;
        boolean checkUserStatus = this.userStatus == UserStatusEnum.ACTIVE;
        if(checkCard && checkTag && checkUserStatus)
            return true;
        return false;
    }
    public static void setUserCount(boolean check)
    {
        if(check)
            User.userCount = User.userCount-1;
        else
            User.userCount = User.userCount+1;
    }


}

package com.project.contap.model.friend;

import com.project.contap.model.user.dto.UserTapDto;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class SortedFriendsDto {
    private Long userId; // 차후에 쓰일것같아서 변수들 좀 추가했음 LSJ
    private String email;
    private String userName;
    private String profile;
    private String hashTags;
    private String roomId;
    private String roomStatus;
    private String date;
    private Boolean login;
    private Boolean newFriend;
    private int field;
    public SortedFriendsDto(String roomId,String roomStatus)
    {
        this.roomId = roomId;
        this.roomStatus = roomStatus;
    }

    public void setFromUserDto(UserTapDto userDto,Boolean login) {
        this.userId = userDto.getUserId();
        this.email = userDto.getEmail();
        this.userName = userDto.getUserName();
        this.profile = userDto.getProfile();
        this.hashTags = userDto.getHashTags();
        this.field = userDto.getField();
        this.login = login;
        this.newFriend= userDto.getNewFriend();
    }
}

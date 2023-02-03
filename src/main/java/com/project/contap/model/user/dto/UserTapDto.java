package com.project.contap.model.user.dto;

import com.project.contap.model.hashtag.HashTag;
import com.sun.org.apache.xpath.internal.operations.Bool;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@Schema(name = "User RequestDto")
public class UserTapDto {
    private Long userId;
    private String email;
    private String userName;
    private String profile;
    private String hashTags;
    private Long tapId;
    private String roomId;
    private int field;
    private String msg;
    private Boolean newFriend;
    private Long friendId;

    public UserTapDto(Long id,
                      String email,
                      String profile,
                      String userName,
                      String hashTagsString,
                      Long tapId,
                      String msg,
                      int field,
                      int newFriend
    ) {
        this.userId = id;
        this.email = email;
        this.userName = userName;
        this.profile = profile;
        this.hashTags=hashTagsString;
        this.tapId = tapId;
        this.msg = msg;
        this.field = field;
        this.newFriend = newFriend==1;
    }

    public UserTapDto(Long id,
                      String email,
                      String profile,
                      String userName,
                      String hashTagsString,
                      int field,
                      String roomId,
                      int newFriend,
                      Long friendId) {
        this.userId = id;
        this.email = email;
        this.userName = userName;
        this.profile = profile;
        this.hashTags=hashTagsString;
        this.field = field;
        this.roomId = roomId;
        this.newFriend = newFriend==1;
        this.friendId = friendId;
    }

}

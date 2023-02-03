package com.project.contap.model.user.dto;

import com.project.contap.model.hashtag.HashTag;
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
public class UserMainDto {
    private Long userId;
    private String email;
    private String userName;
    private String profile;
    private String hashTags;
    private int field;

    public UserMainDto(
            Long userId,
            String email,
            String profile,
            String userName,
            String hashTags,
            int field
    )
    {
        this.userId = userId;
        this.email = email;
        this.profile = profile;
        this.userName = userName;
        this.hashTags = hashTags;
        this.field = field;
    }
}

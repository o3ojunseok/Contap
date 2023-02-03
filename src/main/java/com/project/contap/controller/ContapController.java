package com.project.contap.controller;

import com.project.contap.common.DefaultRsp;
import com.project.contap.model.friend.SortedFriendsDto;
import com.project.contap.model.hashtag.TagDto;
import com.project.contap.model.user.dto.UserTapDto;
import com.project.contap.security.UserDetailsImpl;
import com.project.contap.service.ContapService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class ContapController {
    private final ContapService contapService;
    @Autowired
    public ContapController(ContapService contapService)
    {
        this.contapService= contapService;
    }

    @GetMapping("/contap/dotap/{page}")
    public List<UserTapDto> getMydoTap(
            @PathVariable int page,
            @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return contapService.getMydoTap(userDetails.getUser(),page);
    }
    @GetMapping("/contap/gettap/{page}")
    public List<UserTapDto> getMyTap(
            @PathVariable int page,
            @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return contapService.getMyTap(userDetails.getUser(),page);
    }

    //원래는 분리되어야할컨트롤러 지만.. 시간이남으면분리할게요 -LSJ-
    @GetMapping("/contap/getothers/{type}/{page}")
    public List<SortedFriendsDto> getMyfriends(
            @PathVariable(required = false) int type,
            @PathVariable int page,
            @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return contapService.getMyfriends(userDetails.getUser(),type,page);
    }

    @PostMapping("/contap/reject")
    public DefaultRsp tapreject(
            @AuthenticationPrincipal UserDetailsImpl userDetails,
            @RequestBody(required = false) TagDto tagId) {
        return contapService.tapReject(tagId.getTagId(),userDetails.getUser().getEmail());
    }
    @PostMapping("/contap/accept")
    public DefaultRsp acreject(
            @AuthenticationPrincipal UserDetailsImpl userDetails,
            @RequestBody(required = false)  TagDto tagId) {
        return contapService.rapAccept(tagId.getTagId(),userDetails.getUser().getEmail());
    }
    @PostMapping("/contap/deletefriend/{userId}")
    public DefaultRsp delFriend(
            @AuthenticationPrincipal UserDetailsImpl userDetails,
            @PathVariable  Long userId) {
        return contapService.delFriend(userDetails.getUser(),userId);
    }
    @PostMapping("/contap/deltap")
    public DefaultRsp delTap(
            @AuthenticationPrincipal UserDetailsImpl userDetails,
            @RequestBody(required = false)  TagDto tagId) {
        return contapService.deleteTap(tagId.getTagId(),userDetails.getUser());
    }

}

package com.project.contap.controller;

import com.project.contap.common.DefaultRsp;
import com.project.contap.common.SearchRequestDto;
import com.project.contap.exception.ContapException;
import com.project.contap.model.card.dto.QCardDto;
import com.project.contap.model.hashtag.HashTag;
import com.project.contap.model.tap.DoTapDto;
import com.project.contap.model.user.dto.UserMainDto;
import com.project.contap.security.UserDetailsImpl;
import com.project.contap.service.MainService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Tag(name = "Main Controller Api V1")
@RestController
@RequiredArgsConstructor
public class MainController {
    private final MainService mainService;

    @Operation(summary = "HashTag")
    @GetMapping("/main/hashtag")
    public List<HashTag> getHashag() throws ContapException {
        return mainService.getHashTag();
    }

    @PostMapping("/main/search") //@RequestBody List<HashTag> hashTags
    public List<UserMainDto> search(
            @RequestBody SearchRequestDto tagsandtype,
            @AuthenticationPrincipal UserDetailsImpl userDetails
    ) throws ContapException {
        Long userId = 0L;
        if (userDetails != null)
            userId = userDetails.getUser().getId();
        return mainService.searchuser(tagsandtype, userId);
    }

    @Operation(summary = "뒷면카드 조회")
    @GetMapping("/main/{userId}")
    public List<QCardDto> getCards(@PathVariable Long userId) throws Exception {
        return mainService.getCards(userId);
    }

    @Operation(summary = "앞면카드 조회")
    @GetMapping("/main")
    public Map<String, Object> getUserDtoList(@AuthenticationPrincipal UserDetailsImpl userDetails) {
        Map<String, Object> result = new HashMap<>();
        List<UserMainDto> users = mainService.getUserDtoList(userDetails);
        result.put("users", users);
        return result;
    }

    @PostMapping("/main/posttap")
    public DefaultRsp tap(
            @RequestBody(required = false) DoTapDto tapDto,
            @AuthenticationPrincipal UserDetailsImpl userDetails
    ) {
        return mainService.dotap(userDetails.getUser(), tapDto.getUserId(), tapDto.getMsg());
    }

    @PostMapping("/main/tutorial")
    public void phoneTutorial(
            @Parameter(name = "tutorialNum", in = ParameterIn.QUERY, description = "튜터리얼 번호(0:핸드폰,1:프로필)") @RequestParam int tutorialNum
            , @Parameter(hidden = true) @AuthenticationPrincipal UserDetailsImpl userDetails) {
        if (userDetails != null)
            mainService.tutorial(tutorialNum, userDetails.getUser());
    }

    @GetMapping("/main/info")
    public int getUserAuth(@Parameter(hidden = true) @AuthenticationPrincipal UserDetailsImpl userDetails) {
        if (userDetails != null)
            return mainService.getUserAuthStatus(userDetails.getUser());

            return -1;
        }
}

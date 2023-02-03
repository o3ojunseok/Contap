package com.project.contap.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.contap.common.SearchRequestDto;
import com.project.contap.model.tap.DoTapDto;
import com.project.contap.model.user.User;
import com.project.contap.security.UserDetailsImpl;
import com.project.contap.security.WebSecurityConfig;
import com.project.contap.service.MainService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.security.Principal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(
        controllers = {MainController.class},
        excludeFilters = {
                @ComponentScan.Filter(
                        type = FilterType.ASSIGNABLE_TYPE,
                        classes = WebSecurityConfig.class
                )
        }
)
public class MainControllerTest {
    private MockMvc mvc;

    private Principal mockPrincipal;

    @Autowired
    private WebApplicationContext context;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    MainService mainService;

    UserDetailsImpl testUserDetails;

    @BeforeEach
    public void setup() {
        mvc = MockMvcBuilders.webAppContextSetup(context)
                .apply(springSecurity(new MockSpringSecurityFilter()))
                .build();
        // Create mock principal for the test user
        User testUser = User.builder().id(9999L).email("testuser@gmail.com").userName("testuser").pw("ab").field(0).build();
        testUserDetails = new UserDetailsImpl(testUser);
        mockPrincipal = new UsernamePasswordAuthenticationToken(testUserDetails, "", Collections.emptyList());
    }

    @Test
    @DisplayName("HashTag 리스트 조회")
    void GetHashTagList() throws Exception {
        mvc.perform(get("/main/hashtag"))
                .andExpect(status().isOk())
                .andDo(print());
    }

    @Test
    @DisplayName("검색기능")
    void SearchbyHashTag() throws Exception {
        SearchRequestDto testDto = new SearchRequestDto();
        List<String> tags = new ArrayList<>();
        tags.add("축구");
        tags.add("java");
        testDto.setField(0);
        testDto.setPage(0);
        testDto.setSearchTags(tags);
        testDto.setType(0);
        String req = objectMapper.writeValueAsString(testDto);
        mvc.perform(post("/main/search")
                        .content(req)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(print());
    }

    @Test
    @DisplayName("한 유저의 뒷면 카드 조회")
    void getCards() throws Exception {
        mvc.perform(get("/main/1"))
                .andExpect(status().isOk())
                .andDo(print());
    }

    @Test
    @DisplayName("앞면 카드 조회")
    void getuserInfos() throws Exception {
        mvc.perform(get("/main"))
                .andExpect(status().isOk())
                .andDo(print());
    }

    @Test
    @DisplayName("탭 요청")
    void tap() throws Exception {
        DoTapDto testDto = new DoTapDto();
        testDto.setMsg("gd");
        testDto.setUserId(4L);

        String req = objectMapper.writeValueAsString(testDto);
        mvc.perform(post("/main/posttap")
                        .content(req)
                        .contentType(MediaType.APPLICATION_JSON)
                        .principal(mockPrincipal)
                        .characterEncoding("utf-8"))
                .andExpect(status().isOk())
                .andDo(print());
        verify(mainService,times(1)).dotap(testUserDetails.getUser(),testDto.getUserId(),testDto.getMsg());
    }

}

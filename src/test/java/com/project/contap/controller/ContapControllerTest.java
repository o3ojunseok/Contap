package com.project.contap.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.contap.model.hashtag.TagDto;
import com.project.contap.model.user.User;
import com.project.contap.security.UserDetailsImpl;
import com.project.contap.security.WebSecurityConfig;
import com.project.contap.service.ContapService;
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
import java.util.Collections;

import static org.mockito.Mockito.verify;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

@WebMvcTest(
        controllers = {ContapController.class},
        excludeFilters = {
                @ComponentScan.Filter(
                        type = FilterType.ASSIGNABLE_TYPE,
                        classes = WebSecurityConfig.class
                )
        }
)
public class ContapControllerTest {
    private MockMvc mvc;

    private Principal mockPrincipal;

    @Autowired
    private WebApplicationContext context;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    ContapService contapService;

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
    @DisplayName("내가 한 탭")
    void MySendTap() throws Exception {
        mvc.perform(get("/contap/dotap/0")
                        .principal(mockPrincipal))
                .andExpect(status().isOk())
                .andDo(print());
        verify(contapService,times(1)).getMydoTap(testUserDetails.getUser(),0);
    }

    @Test
    @DisplayName("내가 받은 탭")
    void MyReceiveTap() throws Exception {
        mvc.perform(get("/contap/gettap/0")
                        .principal(mockPrincipal))
                .andExpect(status().isOk())
                .andDo(print());
        verify(contapService,times(1)).getMyTap(testUserDetails.getUser(),0);
    }

    @Test
    @DisplayName("친구 목록 조회")
    void GetMyFriends() throws Exception {
        mvc.perform(get("/contap/getothers/0/0")
                        .principal(mockPrincipal))
                .andExpect(status().isOk())
                .andDo(print());
        verify(contapService,times(1)).getMyfriends(testUserDetails.getUser(),0,0);
    }

    @Test
    @DisplayName("탭 거절")
    void tapreject() throws Exception {
        TagDto testDto = new TagDto();
        testDto.setTagId(4L);
        String req = objectMapper.writeValueAsString(testDto);
        mvc.perform(post("/contap/reject")
                        .content(req)
                        .contentType(MediaType.APPLICATION_JSON)
                        .principal(mockPrincipal)
                        .characterEncoding("utf-8"))
                .andExpect(status().isOk())
                .andDo(print());
        verify(contapService,times(1)).tapReject(testDto.getTagId(),testUserDetails.getUser().getEmail());
    }

    @Test
    @DisplayName("탭 수락")
    void tapaccept() throws Exception {
        TagDto testDto = new TagDto();
        testDto.setTagId(4L);
        String req = objectMapper.writeValueAsString(testDto);
        mvc.perform(post("/contap/accept")
                        .content(req)
                        .contentType(MediaType.APPLICATION_JSON)
                        .principal(mockPrincipal)
                        .characterEncoding("utf-8"))
                .andExpect(status().isOk())
                .andDo(print());
        verify(contapService,times(1)).rapAccept(testDto.getTagId(),testUserDetails.getUser().getEmail());
    }




}

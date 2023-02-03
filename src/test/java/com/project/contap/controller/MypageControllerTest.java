package com.project.contap.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.contap.common.enumlist.UserStatusEnum;
import com.project.contap.model.card.dto.BackRequestCardDto;
import com.project.contap.model.user.User;
import com.project.contap.model.user.dto.FrontRequestCardDto;
import com.project.contap.security.UserDetailsImpl;
import com.project.contap.security.WebSecurityConfig;
import com.project.contap.service.MypageService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.security.Principal;
import java.util.Collections;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.verify;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = MypageController.class,
        excludeFilters = @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = WebSecurityConfig.class))
class MypageControllerTest {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private WebApplicationContext context;

    @MockBean
    private MypageService mypageService;

    private Principal mockPrincipal;

    User testUser;
    UserDetailsImpl testUserDetails;
    @BeforeEach
    public void setup() {
        mvc = MockMvcBuilders.webAppContextSetup(context)
                .apply(springSecurity(new MockSpringSecurityFilter()))
                .build();
        testUser = User.builder()
                    .id(999L)
                    .email("testUse@gmail.com")
                    .pw("1234qwer")
                    .userName("testUser")
                    .field(0)
                    .userStatus(UserStatusEnum.ACTIVE).build();
        testUserDetails = new UserDetailsImpl(testUser);
        mockPrincipal = new UsernamePasswordAuthenticationToken(testUserDetails, "", Collections.emptyList());
    }

    @Nested
    @DisplayName("POST 요청")
    class Post {
        @Nested
        @DisplayName("POST 성공")
        class PostSuccess {
            @Test
            @DisplayName("앞면카드 수정 성공")
            void modifyFrontCard_Success() throws Exception {
                FrontRequestCardDto frontDto = new FrontRequestCardDto(null, "namelim", "Java",0);
                String req = objectMapper.writeValueAsString(frontDto);

                mvc.perform(post("/mypage/frontCard")
                                .contentType(MediaType.MULTIPART_FORM_DATA)
                                .content(req)
                                .principal(mockPrincipal))
                        .andExpect(status().isOk())
                        .andDo(print());

                verify(mypageService).modifyFrontCard(any(FrontRequestCardDto.class),refEq(testUserDetails.getUser()));
            }

            @Test
            @DisplayName("뒷면카드 생성 성공")
            void createBackCard_Success() throws Exception {
                BackRequestCardDto backCardDto = BackRequestCardDto
                        .builder()
                        .title("카드뒷면 제목1")
                        .content("카드뒷면 내용1")
                        .tagsStr("Java,JavaScript")
                        .link("").build();

                String backCardInfo = objectMapper.writeValueAsString(backCardDto);
                mvc.perform(post("/mypage/backCard")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(backCardInfo)
                                .principal(mockPrincipal))
                            .andExpect(status().isOk())
                            .andDo(print());
                verify(mypageService).createBackCard(any(BackRequestCardDto.class),refEq(testUserDetails.getUser()));
            }

            @Test
            @DisplayName("뒷면카드 수정 성공")
            void modifyBackCard_Success() throws Exception {
                BackRequestCardDto backCardDto = BackRequestCardDto
                        .builder()
                        .title("카드뒷면 수정 제목1")
                        .content("카드뒷면 수정 내용1")
                        .tagsStr("Java,JavaScript")
                        .link("").build();

                String backCardInfo = objectMapper.writeValueAsString(backCardDto);
                mvc.perform(post("/mypage/backCard/1")
                                .principal(mockPrincipal)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(backCardInfo))
                            .andExpect(status().isOk())
                            .andDo(print());
                verify(mypageService).modifyBackCard(eq(1L),any(BackRequestCardDto.class),refEq(testUserDetails.getUser()));
            }
        }

        @Nested
        @DisplayName("POST 실패")
        class PostFail {
            @Test
            @DisplayName("앞면카드 수정 실패_닉네임없음")
            void modifyFrontCard_Fail() throws Exception {
                FrontRequestCardDto frontDto = new FrontRequestCardDto(null, null, "Java",0);
                String req = objectMapper.writeValueAsString(frontDto);
                mvc.perform(post("/mypage/frontCard")
                                .contentType(MediaType.MULTIPART_FORM_DATA)
                                .content(req)
                                .principal(mockPrincipal))
                        .andExpect(status().isOk())
                        .andDo(print());
            }
        }
    }

    @Nested
    @DisplayName("Get")
    class Get {
        @Nested
        @DisplayName("Get 성공")
        class GetSuccess {
            @Test
            @DisplayName("나의 정보 가져오기 성공")
            void getMyinfo_Success() throws Exception {
                mvc.perform(get("/mypage/myinfo").principal(mockPrincipal))
                        .andExpect(status().isOk())
                        .andDo(print());
                verify(mypageService).getMyInfo(testUserDetails.getUser());
            }
        }
    }

    @Nested
    @DisplayName("DELETE")
    class Delete {
        @Nested
        @DisplayName("DELETE 성공")
        class DeleteSuccess {
            @Test
            @DisplayName("뒷면 카드 삭제 성공")
            @WithUserDetails
            void deleteBackCard_Success() throws Exception {

                mvc.perform(delete("/mypage/backCard/1").principal(mockPrincipal))
                        .andDo(print())
                        .andExpect(status().isOk());

                verify(mypageService).deleteBackCard(1L,testUserDetails.getUser());
            }
        }
    }
}
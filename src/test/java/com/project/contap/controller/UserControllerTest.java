package com.project.contap.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.contap.model.user.User;
import com.project.contap.model.user.UserRepository;
import com.project.contap.model.user.dto.PwUpdateRequestDto;
import com.project.contap.model.user.dto.SignUpRequestDto;
import com.project.contap.model.user.dto.UserLoginDto;
import com.project.contap.security.UserDetailsImpl;
import com.project.contap.security.WebSecurityConfig;
import com.project.contap.security.jwt.JwtTokenProvider;
import com.project.contap.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.data.jpa.mapping.JpaMetamodelMappingContext;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.security.Principal;
import java.util.Collections;

import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@WebMvcTest(
        controllers = UserController.class,
        excludeFilters = {
                @ComponentScan.Filter(
                        type = FilterType.ASSIGNABLE_TYPE,
                        classes = WebSecurityConfig.class
                )
        }
)
@MockBean(JpaMetamodelMappingContext.class)
class UserControllerTest {

    private MockMvc mvc;

    private Principal mockPrincipal;

    @Autowired
    private WebApplicationContext context;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private UserRepository userRepository;
    @MockBean
    UserService userService;

    @MockBean
    JwtTokenProvider jwtTokenProvider;

    UserDetailsImpl testUserDetails;
    private User testUser;

    @BeforeEach
    public void setup() {
        mvc = MockMvcBuilders.webAppContextSetup(context)
                .apply(springSecurity(new MockSpringSecurityFilter()))
                .build();
        // Create mock principal for the test user
        User testUser = User.builder().id(9999L).email("test@naver.com").userName("test").pw("1234qwer").field(0).build();
        testUserDetails = new UserDetailsImpl(testUser);
        mockPrincipal = new UsernamePasswordAuthenticationToken(testUserDetails, "", Collections.emptyList());
    }

    @Test
    @DisplayName("로그인")
    void login() throws Exception {
        String email = "test@naver.com";
        String pw = "1234wqer";
        UserLoginDto dto = new UserLoginDto(email, pw);
        mvc.perform(post("/user/login")
                        .content(objectMapper.writeValueAsString(dto))
                        .contentType(MediaType.APPLICATION_JSON)
                        .principal(mockPrincipal)
                        .characterEncoding("utf-8"))
                .andExpect(status().isOk())
                .andDo(MockMvcResultHandlers.print());

        verify(userService, atLeastOnce()).login(refEq(dto));

    }

    @Test
    @DisplayName("회원가입")
    void signUp() throws Exception {
        String email = "test@naver.com";
        String pw = "1234qwer";
        String pwCheck = "1234qwer";
        String userName = "test";
        SignUpRequestDto dto = new SignUpRequestDto(email, pw,pwCheck,userName);
        mvc.perform(post("/user/signup")
                        .content(objectMapper.writeValueAsString(dto))
                        .contentType(MediaType.APPLICATION_JSON)
                        .principal(mockPrincipal)
                        .characterEncoding("utf-8"))
                .andExpect(status().isOk())
                .andDo(MockMvcResultHandlers.print());

        verify(userService, atLeastOnce()).registerUser(refEq(dto));

    }

    @Test
    @DisplayName("회원탈퇴 - 휴면계정")
    void deleteuser() throws Exception {
        String email = "test@naver.com";
        String pw = "1234qwer";
        UserLoginDto dto = new UserLoginDto(email,pw);
        mvc.perform(post("/setting/withdrawal").principal(mockPrincipal)
                        .content(objectMapper.writeValueAsString(dto))
                        .contentType(MediaType.APPLICATION_JSON).characterEncoding("utf-8"))
                        .andExpect(status().isOk())
                        .andDo(print());

        verify(userService,atLeastOnce() ).changeToInactive(refEq(dto),refEq(testUserDetails.getUser()));
    }

    @Test
    @DisplayName("Auth")
    void auth() throws Exception {
        mvc.perform(get("/auth"))
                .andExpect(status().isOk())
                .andDo(print());

    }

    @Test
    @DisplayName("비밀번호 변경")
    void changepw() throws Exception {
        String currentPw = "1234qwer";
        String newPw = "qwer1234";
        String newPwCheck = "qwer1234";
        PwUpdateRequestDto dto = new PwUpdateRequestDto(currentPw,newPw,newPwCheck);
        mvc.perform(post("/setting/password").principal(mockPrincipal)
                        .content(objectMapper.writeValueAsString(dto))
                        .contentType(MediaType.APPLICATION_JSON).characterEncoding("utf-8"))
                .andExpect(status().isOk())
                .andDo(print());

        verify(userService,atLeastOnce() ).updatePassword(refEq(dto),refEq(testUserDetails.getUser().getEmail()));

    }







}
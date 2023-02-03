package com.project.contap.service;

import com.project.contap.model.chat.ChatRoomRepository;
import com.project.contap.common.enumlist.UserStatusEnum;
import com.project.contap.exception.ContapException;
import com.project.contap.exception.ErrorCode;
import com.project.contap.model.user.User;
import com.project.contap.model.user.UserRepository;
import com.project.contap.model.user.dto.PwUpdateRequestDto;
import com.project.contap.model.user.dto.SignUpRequestDto;
import com.project.contap.model.user.dto.UserLoginDto;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
class UserServiceTest {


    @Mock
    PasswordEncoder passwordEncoder;

    @Mock
    UserRepository userRepository;

    @Mock
    ChatRoomRepository chatRoomRepository;

    @Nested
    @DisplayName("회원가입 테스트")
    class registerUser {

        @Test
        @DisplayName("회원가입 - 정상 케이스")
        public void registerUserTest_Normal() {

            SignUpRequestDto requestDto = new SignUpRequestDto("test@naver.com", "1234qwer", "1234qwer", "test");
            UserService userService = new UserService(passwordEncoder, userRepository, chatRoomRepository);

            User user2 = User.builder()
                    .email(requestDto.getEmail())
                    .pw("mytest")
                    .userName(requestDto.getUserName())
                    .userStatus(UserStatusEnum.ACTIVE)
                    .build();

            when(passwordEncoder.encode(requestDto.getPw()))
                    .thenReturn("mytest");
            when(userRepository.save(any()))
                    .thenReturn(user2);

            User user = userService.registerUser(requestDto);

            assertEquals(user.getEmail(), requestDto.getEmail());
            assertEquals(user.getPw(), "mytest");
            assertEquals(user.getUserName(), requestDto.getUserName());
            assertEquals(user.getUserStatus(),UserStatusEnum.ACTIVE);
        }

        @Test
        @DisplayName("회원가입 - 실패 케이스 - 이메일안쓴경우")
        public void register_emptyemail() {

            SignUpRequestDto requestDto = new SignUpRequestDto("", "1234qwer", "1234qwer", "test");
            UserService userService = new UserService(passwordEncoder, userRepository, chatRoomRepository);

            ContapException exception = assertThrows(ContapException.class, () -> {
                userService.registerUser(requestDto);
            });

            assertEquals(exception.getErrorCode(), ErrorCode.REGISTER_ERROR);

        }

        @Test
        @DisplayName("회원가입 - 실패 케이스 - pw안쓴경우")
        public void register_emptypw() {
            SignUpRequestDto requestDto = new SignUpRequestDto("test@naver.com", "", "1234qwer", "test");
            UserService userService = new UserService(passwordEncoder, userRepository, chatRoomRepository);

            ContapException exception = assertThrows(ContapException.class, () -> {
                userService.registerUser(requestDto);
            });

            assertEquals(exception.getErrorCode(), ErrorCode.REGISTER_ERROR);

        }

        @Test
        @DisplayName("회원가입 - 실패 케이스 - userName안쓴경우")
        public void register_emptyuserName() {
            SignUpRequestDto requestDto = new SignUpRequestDto("test@naver.com", "1234qwer", "1234qwer", "");
            UserService userService = new UserService(passwordEncoder, userRepository, chatRoomRepository);

            ContapException exception = assertThrows(ContapException.class, () -> {
                userService.registerUser(requestDto);
            });

            assertEquals(exception.getErrorCode(), ErrorCode.REGISTER_ERROR);

        }

        @Test
        @DisplayName("회원가입 - 실패케이스 - 이메일형식이 맞지 않은경우")
        public void register_emailValid() {
            SignUpRequestDto requestDto = new SignUpRequestDto("test", "1234qwer", "1234qwer", "test");
            UserService userService = new UserService(passwordEncoder, userRepository, chatRoomRepository);

            ContapException exception = assertThrows(ContapException.class, () -> {
                userService.registerUser(requestDto);
            });

            assertEquals(exception.getErrorCode(), ErrorCode.EMAIL_FORM_INVALID);
        }

        @Test
        @DisplayName("회원가입 - 실패케이스 - 비밀번호 6자리 미만인경우")
        public void register_pwValid() {
            SignUpRequestDto requestDto = new SignUpRequestDto("test@naver.com", "1234", "1234", "test");
            UserService userService = new UserService(passwordEncoder, userRepository, chatRoomRepository);

            ContapException exception = assertThrows(ContapException.class, () -> {
                userService.registerUser(requestDto);
            });

            assertEquals(exception.getErrorCode(), ErrorCode.PASSWORD_PATTERN_LENGTH);
        }

        @Test
        @DisplayName("회원가입 - 실패케이스 - 비밀번호랑 체크가 일치하지 않은 경우")
        public void register_pw_notEqual() {
            SignUpRequestDto requestDto = new SignUpRequestDto("test@naver.com", "1234qwer", "qwer1234", "test");
            UserService userService = new UserService(passwordEncoder, userRepository, chatRoomRepository);

            ContapException exception = assertThrows(ContapException.class, () -> {
                userService.registerUser(requestDto);
            });

            assertEquals(exception.getErrorCode(), ErrorCode.NOT_EQUAL_PASSWORD);

        }


        @Test
        @DisplayName("회원가입 - 실패케이스 - 이메일이 중복되었을 경우")
        public void register_emailDuplicate() {
            SignUpRequestDto requestDto = new SignUpRequestDto("test@naver.com", "1234qwer", "1234qwer", "test");
            UserService userService = new UserService(passwordEncoder, userRepository, chatRoomRepository);

            User user = User.builder()
                    .email("test@naver.com")
                    .pw("1234qwer")
                    .userName("test1")
                    .build();

            when(userRepository.findByEmail(user.getEmail()))
                    .thenReturn(Optional.of(user));

            ContapException exception = assertThrows(ContapException.class, () -> {
                userService.registerUser(requestDto);
            });

            assertEquals(exception.getErrorCode(), ErrorCode.EMAIL_DUPLICATE);
        }


        @Test
        @DisplayName("회원가입 - 실패케이스 - username이 중복되었을 경우")
        public void register_userNameDuplicate() {
            SignUpRequestDto requestDto = new SignUpRequestDto("test@naver.com", "1234qwer", "1234qwer", "test");
            UserService userService = new UserService(passwordEncoder, userRepository, chatRoomRepository);

            User user = User.builder()
                    .email("test1@naver.com")
                    .pw("1234qwer")
                    .userName("test")
                    .build();

            when(userRepository.findByUserName(user.getUserName()))
                    .thenReturn(Optional.of(user));

            ContapException exception = assertThrows(ContapException.class, () -> {
                userService.registerUser(requestDto);
            });

            assertEquals(exception.getErrorCode(), ErrorCode.NICKNAME_DUPLICATE);
        }
        @Test
        @DisplayName("회원가입 - Inactive 의 경우")
        public void register_isInactiveUser()
        {
            SignUpRequestDto requestDto = new SignUpRequestDto("test@naver.com", "1234qwer", "1234qwer", "test");
            UserService userService = new UserService(passwordEncoder, userRepository, chatRoomRepository);

            User user = User.builder()
                    .email("test1@naver.com")
                    .pw("1234qwer")
                    .userName("test")
                    .userStatus(UserStatusEnum.INACTIVE)
                    .build();

            when(userRepository.findByEmailAndUserStatusEquals(eq(requestDto.getEmail()),eq(UserStatusEnum.INACTIVE)))
                    .thenReturn(user);
            when(passwordEncoder.encode(eq(requestDto.getPw())))
                    .thenReturn(user.getPw());
            when(userRepository.save(any(User.class)))
                    .thenReturn(user);
            User retUser = userService.registerUser(requestDto);
            assertEquals(user.getUserStatus(),UserStatusEnum.ACTIVE);
        }
    }

    @Nested
    @DisplayName("로그인 테스트")
    class login {
        @Test
        @DisplayName("로그인 - 정상케이스")
        public void login_user() {

            UserLoginDto loginDto = new UserLoginDto("test@naver.com", "1234qwer");
            UserService userService = new UserService(passwordEncoder, userRepository, chatRoomRepository);

            User user = User.builder()
                    .email("test@naver.com")
                    .pw("1234qwer")
                    .userName("test")
                    .userStatus(UserStatusEnum.ACTIVE)
                    .build();

            when(passwordEncoder.matches(loginDto.getPw(), user.getPw()))
                    .thenReturn(true);
            when(userRepository.findByEmail(user.getEmail()))
                    .thenReturn(Optional.of(user));

            User user1 = userService.login(loginDto);


            assertEquals(user1.getEmail(), user.getEmail());
            assertEquals(user1.getPw(), user.getPw());
            assertEquals(user1.getUserName(), user.getUserName());
            assertEquals(UserStatusEnum.ACTIVE,user.getUserStatus());


        }

        @Test
        @DisplayName("로그인 - 실패케이스 - 다른 이메일로 로그인할 경우")
        public void login_useremail() {

            UserLoginDto loginDto = new UserLoginDto("test@naver.com","1234qwer");
            UserService userService = new UserService(passwordEncoder,userRepository,chatRoomRepository);

            User user = User.builder()
                    .email("test1@naver.com")
                    .pw("1234qwer")
                    .userName("test")
                    .build();

            when(userRepository.findByEmail(loginDto.getEmail()))
                    .thenThrow(new ContapException(ErrorCode.USER_NOT_FOUND));

            ContapException exception = assertThrows(ContapException.class, () -> {
                userService.login(loginDto);
            });

            assertEquals(exception.getErrorCode(),ErrorCode.USER_NOT_FOUND);

        }

        @Test
        @DisplayName("로그인 - 실패케이스 - 비밀번호가 틀릴 경우")
        public void login_pw() {
            UserLoginDto loginDto = new UserLoginDto("test@naver.com", "123456");
            UserService userService = new UserService(passwordEncoder, userRepository, chatRoomRepository);

            User user = User.builder()
                    .email("test@naver.com")
                    .pw("1234qwer")
                    .userName("test")
                    .userStatus(UserStatusEnum.ACTIVE)
                    .build();

            when(passwordEncoder.matches(loginDto.getPw(), user.getPw()))
                    .thenReturn(false);
            when(userRepository.findByEmail(user.getEmail()))
                    .thenReturn(Optional.of(user));


            ContapException exception = assertThrows(ContapException.class, () -> {
                userService.login(loginDto);
            });

            assertEquals(exception.getErrorCode(), ErrorCode.NOT_EQUAL_PASSWORD);

        }

        @Test
        @DisplayName("로그인 - Inactive 유저")
        public void login_Inactive() {
            UserLoginDto loginDto = new UserLoginDto("test@naver.com", "123456");
            UserService userService = new UserService(passwordEncoder, userRepository, chatRoomRepository);
            User user = User.builder()
                    .email("test@naver.com")
                    .pw("1234qwer")
                    .userName("test")
                    .userStatus(UserStatusEnum.INACTIVE)
                    .build();
            when(passwordEncoder.matches(loginDto.getPw(), user.getPw()))
                    .thenReturn(true);
            when(userRepository.findByEmail(user.getEmail()))
                    .thenReturn(Optional.of(user));
            userService.login(loginDto);
            verify(userRepository,times(1)).save(user);//any(User.class)
//            ContapException exception = assertThrows(ContapException.class, () -> {
//                userService.login(loginDto);
//            });
//
//            assertEquals(exception.getErrorCode(), ErrorCode.NOT_EQUAL_PASSWORD);
        }
        @Test
        @DisplayName("로그인 - 실패 이메일 못찾음")
        public void login_NotFound() {
            UserLoginDto loginDto = new UserLoginDto("test@naver.com", "123456");
            UserService userService = new UserService(passwordEncoder, userRepository, chatRoomRepository);
            ContapException exception = assertThrows(ContapException.class, () -> {
                userService.login(loginDto);
            });
            assertEquals(exception.getErrorCode(), ErrorCode.USER_NOT_FOUND);
        }
    }

    @Nested
    @DisplayName("비밀번호 변경 테스트")
    class changePw {

        @Test
        @DisplayName("비밀번호 변경 - 실패케이스 - 현재 비번 빈값일때")
        public void currentPw_empty() {
            PwUpdateRequestDto requestDto = new PwUpdateRequestDto("","qwer1234","qwer1234");
            String email = "test@naver.com";

            UserService userService = new UserService(passwordEncoder,userRepository,chatRoomRepository);

            when(userRepository.findByEmail(email)).thenReturn(Optional.of(new User()));

            ContapException exception = assertThrows(ContapException.class, () -> {
                userService.updatePassword(requestDto,email);
            });

            assertEquals(exception.getErrorCode(),ErrorCode.CURRENT_EMPTY_PASSWORD);

        }

        @Test
        @DisplayName("비밀번호 변경 - 실패케이스 - 새로운비번 빈값일때")
        public void newPw_empty() {
            PwUpdateRequestDto requestDto = new PwUpdateRequestDto("1234qwer","","qwer1234");
            String email = "test@naver.com";

            UserService userService = new UserService(passwordEncoder,userRepository,chatRoomRepository);

            when(userRepository.findByEmail(email)).thenReturn(Optional.of(new User()));

            ContapException exception = assertThrows(ContapException.class, () -> {
                userService.updatePassword(requestDto,email);
            });

            assertEquals(exception.getErrorCode(),ErrorCode.CHANGE_EMPTY_PASSWORD);

        }

        @Test
        @DisplayName("비밀번호 변경 - 실패케이스 - 현재비번 새로운비번 같을때")
        public void newPw_currentPw_equal() {
            PwUpdateRequestDto requestDto = new PwUpdateRequestDto("1234qwer","1234qwer","1234qwer");
            String email = "test@naver.com";

            UserService userService = new UserService(passwordEncoder,userRepository,chatRoomRepository);

            when(userRepository.findByEmail(email)).thenReturn(Optional.of(new User()));

            ContapException exception = assertThrows(ContapException.class, () -> {
                userService.updatePassword(requestDto,email);
            });

            assertEquals(exception.getErrorCode(),ErrorCode.EQUAL_PREV_PASSWORD);

        }

        @Test
        @DisplayName("비밀번호 변경 - 실패케이스 - 새로운비번 6자리 미만일때")
        public void newPw_length() {
            PwUpdateRequestDto requestDto = new PwUpdateRequestDto("1234qwer","1234","1234");
            String email = "test@naver.com";

            UserService userService = new UserService(passwordEncoder,userRepository,chatRoomRepository);

            when(userRepository.findByEmail(email)).thenReturn(Optional.of(new User()));

            ContapException exception = assertThrows(ContapException.class, () -> {
                userService.updatePassword(requestDto,email);
            });

            assertEquals(exception.getErrorCode(),ErrorCode.PASSWORD_PATTERN_LENGTH);

        }

        @Test
        @DisplayName("비밀번호 변경 - 실패케이스 - 새로운비번과 체크가 같지 않을 때")
        public void newPw_newPwCheck_equal() {
            PwUpdateRequestDto requestDto = new PwUpdateRequestDto("1234qwer","qwer1234","asdf1234");
            String email = "test@naver.com";

            UserService userService = new UserService(passwordEncoder,userRepository,chatRoomRepository);

            when(userRepository.findByEmail(email)).thenReturn(Optional.of(new User()));

            ContapException exception = assertThrows(ContapException.class, () -> {
                userService.updatePassword(requestDto,email);
            });

            assertEquals(exception.getErrorCode(),ErrorCode.NEW_PASSWORD_NOT_EQUAL);

        }

        @Test
        @DisplayName("비밀번호 변경 - 실패케이스 - 현재비밀번호입력과 기존비밀번호가 일치하지 않을때")
        public void pw_currentPw_equal() {
            PwUpdateRequestDto requestDto = new PwUpdateRequestDto("1234qwer","qwer1234","qwer1234");
            User user = User.builder()
                    .email("test@naver.com")
                    .pw("zxcvasdf")
                    .userName("test")
                    .build();


            UserService userService = new UserService(passwordEncoder,userRepository,chatRoomRepository);

            when(passwordEncoder.matches(requestDto.getCurrentPw(), user.getPw()))
                    .thenReturn(false);
            when(userRepository.findByEmail(user.getEmail()))
                    .thenReturn(Optional.of(user));

            ContapException exception = assertThrows(ContapException.class, () -> {
                userService.updatePassword(requestDto,user.getEmail());
            });

            assertEquals(exception.getErrorCode(),ErrorCode.NOT_EQUAL_PASSWORD);

        }
        @Test
        @DisplayName("비밀번호 변경 - 정상 ")
        public void pw_OK() {
            PwUpdateRequestDto requestDto = new PwUpdateRequestDto("1234qwer","qwer1234","qwer1234");
            User user = User.builder()
                    .email("test@naver.com")
                    .pw("zxcvasdf")
                    .userName("test")
                    .build();
            UserService userService = new UserService(passwordEncoder,userRepository,chatRoomRepository);
            when(userRepository.findByEmail(user.getEmail())).thenReturn(Optional.of(user));
            when(passwordEncoder.matches(requestDto.getCurrentPw(), user.getPw()))
                    .thenReturn(true);
            when(passwordEncoder.encode(requestDto.getNewPw()))
                    .thenReturn(user.getPw());
            userService.updatePassword(requestDto,user.getEmail());
        }
    }
    @Test
    @DisplayName("알람 가져오기")
    public void Alarm() {
        String email = "lsj@gmail.com";
        UserService userService = new UserService(passwordEncoder,userRepository,chatRoomRepository);
        userService.getAlarm(email);
        verify(chatRoomRepository,times(1)).readAlarm(eq(email));
    }
    @Nested
    @DisplayName("PhoneNumber 변경 테스트")
    class changePhone {

        @Test
        @DisplayName("비밀번호 변경 - 정상")
        public void OK() {
            String phoneNumber = "010-6645-4534";
            User user = User.builder().build();
            UserService userService = new UserService(passwordEncoder,userRepository,chatRoomRepository);
            when(userRepository.existUserByPhoneNumber(eq(phoneNumber)))
                    .thenReturn(false);
            userService.modifyPhoneNumber(phoneNumber,user);
            verify(userRepository,times(1)).save(user);
        }

        @Test
        @DisplayName("유효성검사 실패")
        public void INVALID() {
            String phoneNumber = "01066454534";
            User user = User.builder().build();
            UserService userService = new UserService(passwordEncoder,userRepository,chatRoomRepository);
            ContapException exception = assertThrows(ContapException.class, () -> {
                userService.modifyPhoneNumber(phoneNumber,user);
            });

            assertEquals(exception.getErrorCode(),ErrorCode.PHONE_FORM_INVALID);
        }

        @Test
        @DisplayName("중복")
        public void DUPLICATE() {
            String phoneNumber = "010-6645-4534";
            User user = User.builder().build();
            UserService userService = new UserService(passwordEncoder,userRepository,chatRoomRepository);
            when(userRepository.existUserByPhoneNumber(eq(phoneNumber)))
                    .thenReturn(true);
            ContapException exception = assertThrows(ContapException.class, () -> {
                userService.modifyPhoneNumber(phoneNumber,user);
            });

            assertEquals(exception.getErrorCode(),ErrorCode.PHONE_DUPLICATE);

        }
    }
    @Nested
    @DisplayName("authStatus 변경 테스트")
    class authStatus {

        @Test
        @DisplayName("정상 - 0")
        public void OK_0() {
            User user = User.builder().authStatus(0b1000).build();
            UserService userService = new UserService(passwordEncoder,userRepository,chatRoomRepository);
            userService.changeAlarmState(0,user);
            verify(userRepository,times(1)).save(eq(user));
        }

        @Test
        @DisplayName("정상 - 1")
        public void OK_1() {
            User user = User.builder().authStatus(0).build();
            UserService userService = new UserService(passwordEncoder,userRepository,chatRoomRepository);
            userService.changeAlarmState(1,user);
            verify(userRepository,times(1)).save(eq(user));
        }
    }

    @Nested
    @DisplayName("UserStatus 변경 테스트")
    class UserStatus {

        @Test
        @DisplayName("정상")
        public void OK() {
            User user = User.builder().pw("abc").authStatus(0b1000).build();
            UserLoginDto loginDto = new UserLoginDto("test@naver.com", "1234qwer");

            UserService userService = new UserService(passwordEncoder,userRepository,chatRoomRepository);

            when(passwordEncoder.matches(loginDto.getPw(), user.getPw()))
                    .thenReturn(true);

            userService.changeToInactive(loginDto,user);

            verify(userRepository,times(1)).save(eq(user));
        }

        @Test
        @DisplayName("실패 비밀번호 불일치")
        public void fail() {
            User user = User.builder().pw("abc").authStatus(0b1000).build();
            UserLoginDto loginDto = new UserLoginDto("test@naver.com", "1234qwer");

            UserService userService = new UserService(passwordEncoder,userRepository,chatRoomRepository);

            when(passwordEncoder.matches(loginDto.getPw(), user.getPw()))
                    .thenReturn(false);
            ContapException exception = assertThrows(ContapException.class, () -> {
                userService.changeToInactive(loginDto,user);
            });

            assertEquals(exception.getErrorCode(),ErrorCode.NOT_EQUAL_PASSWORD);
        }
    }
    @Nested
    @DisplayName("get PhoneNumber")
    class getPhoneNumber {

        @Test
        @DisplayName("정상 - 1")
        public void OK1() {
            User user = User.builder().pw("abc").authStatus(0b1000).build();
            UserService userService = new UserService(passwordEncoder,userRepository,chatRoomRepository);
            String ret = userService.getPhoneNumber(user);
            assertEquals(ret,"");
        }

        @Test
        @DisplayName("정상 - 2")
        public void OK2() {
            User user = User.builder().phoneNumber("01066454534").pw("abc").authStatus(0b1000).build();
            UserService userService = new UserService(passwordEncoder,userRepository,chatRoomRepository);
            String ret = userService.getPhoneNumber(user);
            assertEquals(ret,"010-6645-4534");
        }
    }


}






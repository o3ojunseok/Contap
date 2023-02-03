package com.project.contap.service;

import com.project.contap.exception.ContapException;
import com.project.contap.exception.ErrorCode;
import com.project.contap.model.chat.ChatMessage;
import com.project.contap.model.chat.ChatMessageDTO;
import com.project.contap.model.chat.ChatMessageRepository;
import com.project.contap.model.chat.ChatRoomRepository;
import com.project.contap.model.user.EmailRepository;
import com.project.contap.model.user.UserRepository;
import com.project.contap.model.user.dto.EmailRequestDto;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import static com.project.contap.common.util.RandomNumberGeneration.makeRandomNumber;

import java.io.UnsupportedEncodingException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class EmailServiceTest {
    @Mock
    EmailRepository emailRepository;
    @Mock
    JavaMailSender emailSender;
    @Mock
    UserRepository userRepository;

    @Mock
    MimeMessage mimeMessage;

    @Nested
    @DisplayName("이메일 전송")
    class GetMsg {
        @Test
        @DisplayName("정상")
        void sendEmail_OK() throws MessagingException, UnsupportedEncodingException {
            String email = "lsj@gmail.com";
            EmailService emailService = new EmailService(emailRepository, emailSender, userRepository);
            when(userRepository.existUserByEmailAndUserStatus(email))
                    .thenReturn(false);
            when(emailSender.createMimeMessage())
                    .thenReturn(mimeMessage);
            emailService.sendEmail(email);
            verify(emailSender,times(1)).send(any(MimeMessage.class));
            verify(emailRepository,times(1)).createEmailCertification(eq(email),any(String.class));
        }

        @Test
        @DisplayName("중복된 유저 존재")
        void sendEmail_dup(){
            String email = "lsj@gmail.com";
            EmailService emailService = new EmailService(emailRepository, emailSender, userRepository);
            when(userRepository.existUserByEmailAndUserStatus(email))
                    .thenReturn(true);
            ContapException exception = assertThrows(ContapException.class, () -> {
                emailService.sendEmail(email);
            });
            assertEquals(exception.getErrorCode(), ErrorCode.EMAIL_DUPLICATE);
        }
    }

    @Nested
    @DisplayName("인증번호 확인")
    class verify_test {

        @Test
        @DisplayName("성공")
        void verifyEmail_suc() {
            EmailRequestDto emailDto = new EmailRequestDto("lsj@gmail.com","abcd");

            EmailService emailService = new EmailService(emailRepository, emailSender, userRepository);

            when(emailRepository.hasKey(emailDto.getEmail()))
                    .thenReturn(true);

            when(emailRepository.getEmailCertificationNum(emailDto.getEmail()))
                    .thenReturn("abcd");

            emailService.verifyEmail(emailDto);
        }

        @Test
        @DisplayName("실패_인증번호_유효하지않음")
        void verifyEmail_fail1() {
            EmailRequestDto emailDto = new EmailRequestDto("lsj@gmail.com","abcd");

            EmailService emailService = new EmailService(emailRepository, emailSender, userRepository);

            when(emailRepository.hasKey(emailDto.getEmail()))
                    .thenReturn(false);
            ContapException exception = assertThrows(ContapException.class, () -> {
                emailService.verifyEmail(emailDto);
            });
            assertEquals(exception.getErrorCode(), ErrorCode.NOT_EQUAL_NUMBER);

        }

        @Test
        @DisplayName("실패_인증번호_실패")
        void verifyEmail_fail2() {
            EmailRequestDto emailDto = new EmailRequestDto("lsj@gmail.com","abcd");

            EmailService emailService = new EmailService(emailRepository, emailSender, userRepository);

            when(emailRepository.hasKey(emailDto.getEmail()))
                    .thenReturn(true);

            when(emailRepository.getEmailCertificationNum(emailDto.getEmail()))
                    .thenReturn("abcdd");

            ContapException exception = assertThrows(ContapException.class, () -> {
                emailService.verifyEmail(emailDto);
            });
            assertEquals(exception.getErrorCode(), ErrorCode.NOT_EQUAL_NUMBER);
        }
    }
}

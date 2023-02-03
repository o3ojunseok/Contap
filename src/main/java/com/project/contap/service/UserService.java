package com.project.contap.service;

import com.project.contap.common.enumlist.AuthorityEnum;
import com.project.contap.common.enumlist.UserStatusEnum;
import com.project.contap.exception.ContapException;
import com.project.contap.exception.ErrorCode;
import com.project.contap.model.chat.ChatRoomRepository;
import com.project.contap.model.user.User;
import com.project.contap.model.user.UserRepository;
import com.project.contap.model.user.dto.PwUpdateRequestDto;
import com.project.contap.model.user.dto.SignUpRequestDto;
import com.project.contap.model.user.dto.UserLoginDto;
import com.project.contap.security.UserDetailsImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


@Service
@RequiredArgsConstructor
public class UserService {
    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final ChatRoomRepository chatRoomRepository;

    public User registerUser(SignUpRequestDto requestDto) throws ContapException {

        checkLength(requestDto);
        String pw = passwordEncoder.encode(requestDto.getPw()); // 패스워드 암호화

        //휴면계정인지 확인
        User user = isInactiveUser(requestDto.getEmail());
        if(user != null) {
            user.setUserStatus(UserStatusEnum.ACTIVE);
            user.setPw(pw);
            user = userRepository.save(user);
            if(user.checkForMain())
                User.setUserCount(false);
            return user;
        }

        checkDupValidation(requestDto);
        user = User.builder()
                .email(requestDto.getEmail())
                .pw(pw)
                .hashTagsString("@_@")
                .userName(requestDto.getUserName())
                .userStatus(UserStatusEnum.ACTIVE)
//                .authStatus(AuthorityEnum.CAN_OTHER_READ.getAuthority()) //default : 다른 사람 카드 읽기 가능
                .build();
        user = userRepository.save(user);
        return user;
    }

    public User login(UserLoginDto requestDto) throws ContapException {
        User user = userRepository.findByEmail(requestDto.getEmail()).orElse(null);
        if(user==null || user.getUserStatus().equals(UserStatusEnum.WITHDRAWN)) //탈퇴 회원은 데이터를 삭제될 수 있는 사항이므로 후에 변경 예정
            throw new ContapException(ErrorCode.USER_NOT_FOUND);

        if (!passwordEncoder.matches(requestDto.getPw(), user.getPw()))
            throw new ContapException(ErrorCode.NOT_EQUAL_PASSWORD);

        if(user.getUserStatus()==UserStatusEnum.INACTIVE) {
            user.setUserStatus(UserStatusEnum.ACTIVE);
            userRepository.save(user);
            if(user.checkForMain())
                User.setUserCount(false);
        }

        return user;
    }

    @Transactional
    public void updatePassword(PwUpdateRequestDto requestDto, String email) throws ContapException {
        User user =userRepository.findByEmail(email)
                .orElseThrow(() -> new ContapException(ErrorCode.REGISTER_ERROR));
        checkUpdatePwVal(requestDto,user);
        String newPw = passwordEncoder.encode(requestDto.getNewPw());
        requestDto.setNewPw(newPw);
        user.updatePw(requestDto);
    }

    public String[] getAlarm(String email) {
        String[] alarms = chatRoomRepository.readAlarm(email);
        return alarms;
    }

    public String modifyPhoneNumber(String phoneNumber, User user) {
        if(!isValidPhoneNumber(phoneNumber)) {
            throw new ContapException((ErrorCode.PHONE_FORM_INVALID)); //핸드폰번호 형식이 맞지 않습니다.
        }
        //휴대폰번호 중복 확인
        Boolean found = userRepository.existUserByPhoneNumber(phoneNumber);
        if (found) {
            throw new ContapException(ErrorCode.PHONE_DUPLICATE); //중복된 핸드폰번호가 존재합니다.
        }
        user.setPhoneNumber(phoneNumber.replace("-",""));
        userRepository.save(user);
        return phoneNumber.replaceAll("([010]{3})([0-9]{4})([0-9]{4})", "$1-$2-$3");
    }

    private boolean isValidPhoneNumber(String phoneNumber) {
        String regex = "^010-\\d{4}-\\d{4}$";
        Pattern p = Pattern.compile(regex);
        Matcher m = p.matcher(phoneNumber);
        return m.matches();
    }

//    public User checkUserAuthority(User requestUser) {
//        if(requestUser == null)
//            throw new ContapException(ErrorCode.USER_NOT_FOUND); //회원 정보를 찾을 수 없습니다.
//
//        User user = userRepository.findById(requestUser.getId()).orElse(null);
//        if (user.getEmail()!=null && (!user.isWrittenBy(requestUser) || user.getUserStatus()!=UserStatusEnum.ACTIVE))
//            throw new ContapException(ErrorCode.ACCESS_DENIED); //권한이 없습니다.
//
//        return user;
//    }
//
//    public User userFromUserDetails(UserDetails userDetails) {
//        if ( userDetails instanceof UserDetailsImpl ) {
//            if(userDetails == null)
//                throw new ContapException(ErrorCode.USER_NOT_FOUND);
//
//            return checkUserAuthority(((UserDetailsImpl) userDetails).getUser());
//        } else {
//            throw new ContapException(ErrorCode.USER_NOT_FOUND);
//        }
//    }

    public void changeAlarmState(int alarmState, User user) {
        int authStatus = user.getAuthStatus();
        if(alarmState==0) {
            authStatus = authStatus & (AuthorityEnum.ALL_AUTHORITY.getAuthority() - AuthorityEnum.ALARM.getAuthority());
        }
        else if(alarmState==1) {
            authStatus = authStatus|AuthorityEnum.ALARM.getAuthority();
        }
        user.setAuthStatus(authStatus);
        userRepository.save(user);
    }

    private void checkLength(SignUpRequestDto requestDto){
        if (requestDto.getEmail().equals("")) {
            throw new ContapException(ErrorCode.REGISTER_ERROR);
        }
        if (requestDto.getPw().equals("")) {
            throw new ContapException(ErrorCode.REGISTER_ERROR);
        }
        if (requestDto.getPwCheck().equals("")) {
            throw new ContapException(ErrorCode.REGISTER_ERROR);
        }
        if (requestDto.getUserName().equals("")) {
            throw new ContapException(ErrorCode.REGISTER_ERROR);
        }

        String password = requestDto.getPw();
        String passwordCheck = requestDto.getPwCheck();

        if (!password.isEmpty() && !passwordCheck.isEmpty()) {
            if (password.length() >= 6 && password.length() <= 20) {
                if (!password.equals(passwordCheck)) {
                    throw new ContapException(ErrorCode.NOT_EQUAL_PASSWORD);
                }
            }
            else
                throw new ContapException(ErrorCode.PASSWORD_PATTERN_LENGTH);
        } else {
            throw new ContapException(ErrorCode.PASSWORD_ENTER);
        }
    }

    private void checkDupValidation(SignUpRequestDto requestDto) {

        //가입 email(id) 중복체크
        String email = requestDto.getEmail();
        if (!isValidEmail(email)) {
            throw new ContapException(ErrorCode.EMAIL_FORM_INVALID);
        }

        Optional<User> found = userRepository.findByEmail(email);
        if (found.isPresent()) {
            throw new ContapException(ErrorCode.EMAIL_DUPLICATE);
        }

        //가입 nickname 중복체크
        String userName = requestDto.getUserName();
        Optional<User> found2 = userRepository.findByUserName(userName);
        if (found2.isPresent()) {
            throw new ContapException(ErrorCode.NICKNAME_DUPLICATE);
        }
    }

    private boolean isValidEmail(String email) {
        boolean err = false;
        String regex = "^[_A-Za-z0-9-]+(.[_A-Za-z0-9-]+)*@(?:\\w+\\.)+\\w+$";
        Pattern p = Pattern.compile(regex);
        Matcher m = p.matcher(email);
        if (m.matches()) {
            err = true;
        }
        return err;
    }

    private void checkUpdatePwVal(PwUpdateRequestDto requestDto, User user) {
        if (requestDto.getCurrentPw().isEmpty()) {
            throw new ContapException(ErrorCode.CURRENT_EMPTY_PASSWORD);
        }

        if (requestDto.getNewPw().isEmpty() || requestDto.getNewPw().isEmpty()) {
            throw new ContapException(ErrorCode.CHANGE_EMPTY_PASSWORD);
        }

        if (requestDto.getCurrentPw().equals(requestDto.getNewPw())) {
            throw new ContapException(ErrorCode.EQUAL_PREV_PASSWORD);
        }

        if (requestDto.getNewPw().length() < 6 || requestDto.getNewPw().length() > 20) {
            throw new ContapException(ErrorCode.PASSWORD_PATTERN_LENGTH);
        }

        if (!requestDto.getNewPw().equals(requestDto.getNewPwCheck())) {
            throw new ContapException(ErrorCode.NEW_PASSWORD_NOT_EQUAL);
        }

        if(!passwordEncoder.matches(requestDto.getCurrentPw(), user.getPw())){
            throw new ContapException(ErrorCode.NOT_EQUAL_PASSWORD);
        }
    }

    @Transactional
    public void changeToInactive(UserLoginDto requestDto, User user) {
        boolean bcheck = user.checkForMain();
        if(!passwordEncoder.matches(requestDto.getPw(), user.getPw()))
            throw new ContapException(ErrorCode.NOT_EQUAL_PASSWORD);

        user.setUserStatus(UserStatusEnum.INACTIVE); // 휴면계정으로 변환
        userRepository.save(user);
        if(user.checkForMain() != bcheck)
            User.setUserCount(bcheck);
    }

    public String getPhoneNumber(User user) {
        if(user.getPhoneNumber() == null)
            return "";
        String returnPhoneNumber = user.getPhoneNumber().replaceAll("([010]{3})([0-9]{4})([0-9]{4})", "$1-$2-$3");
        return returnPhoneNumber;
    }

    private User isInactiveUser(String email) {
        User user = userRepository.findByEmailAndUserStatusEquals(email, UserStatusEnum.INACTIVE);
        return user;
    }

    public void ActiveUsercntSet()
    {
        User.userCount = userRepository.getActiveUsercnt();
        System.out.println("userCnt : " + User.userCount.toString());
    }

    @Transactional
    public void setPassword(PwUpdateRequestDto requestDto) throws ContapException {
        if (!requestDto.getNewPw().equals(requestDto.getNewPwCheck())) {
            throw new ContapException(ErrorCode.NOT_EQUAL_PASSWORD);
        }
        if (requestDto.getNewPw().length() < 6 || requestDto.getNewPw().length() > 20) {
            throw new ContapException(ErrorCode.PASSWORD_PATTERN_LENGTH);
        }

        if (!requestDto.getNewPw().equals(requestDto.getNewPwCheck())) {
            throw new ContapException(ErrorCode.NEW_PASSWORD_NOT_EQUAL);
        }

        User user = userRepository.findByEmail(requestDto.getEmail()).orElseThrow(
                () -> new ContapException(ErrorCode.USER_NOT_FOUND)
        );

        String pw = passwordEncoder.encode(requestDto.getNewPw());
        requestDto.setNewPw(pw);
        user.updatePw(requestDto);

    }

}



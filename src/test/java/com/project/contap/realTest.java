package com.project.contap;

import com.project.contap.common.util.RandomNumberGeneration;
import com.project.contap.model.friend.FriendRepository;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;


//@RequiredArgsConstructor
//@SpringBootTest
public class realTest {
    //여기다테스트 해보고싶은코드 작성하면 편할것같아서 추가해봤어요
//    @Autowired
//    private RedisTemplate<String, String> redisTemplate;
    @Autowired
    private FriendRepository friendRepository;

    @Test
    void test()
    {
        List<String> abc = new ArrayList<>();
        abc.add("1");
        abc.add("2");
        abc.add("3");
        List<String> abc2 = new ArrayList<>();
        abc2.add("4");
        abc2.add("5");
        abc2.add("6");
        abc.addAll(abc2);
        System.out.println(abc);
    }

    @Test
    void test2()
    {
        for(int i = 0 ; i< 1000; i++)
            System.out.println(RandomNumberGeneration.randomRange(0,0));
    }
    @Test
    void test3()
    {
        double date = Double.parseDouble(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyMMddHHmmss")));
        System.out.println(date);
    }
}

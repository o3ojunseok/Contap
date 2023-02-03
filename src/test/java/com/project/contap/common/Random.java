package com.project.contap.common;

import com.project.contap.common.util.RandomNumberGeneration;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class Random {


    @Test
    @DisplayName("0~20")
    void RandomRange()
    {
        for(int i = 0 ; i< 1000; i++)
        {
            int num = RandomNumberGeneration.randomRange(0,20);
            Assertions.assertTrue(0 <= num && num <= 20);
        }
    }

    @Test
    @DisplayName("이메일 인증번호")
    void emailCode()
    {
        for(int i = 0 ; i< 1000; i++)
        {
            String num = RandomNumberGeneration.makeRandomNumber();
            boolean bcheck = 100000 <= Integer.parseInt(num) & Integer.parseInt(num) <= 999999;
            Assertions.assertTrue(bcheck);
        }
    }
}

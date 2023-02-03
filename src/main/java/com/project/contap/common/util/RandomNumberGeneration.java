package com.project.contap.common.util;

import java.util.Random;


public class RandomNumberGeneration {

    public static final String makeRandomNumber() {
        Random random = new Random();
        return String.valueOf(100000 + random.nextInt(900000));
    }

    public static int randomRange(int n1, int n2) {
        return (int) (Math.random() * (n2 - n1 + 1)) + n1;
    }
}


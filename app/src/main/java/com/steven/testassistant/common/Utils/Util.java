package com.steven.testassistant.common.Utils;

import java.util.Random;

class Util {

    public static String randomName(int length){
        String baseChar = "abcdefghijklmnopqrstuvwxyz 0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ";
        int baseCharLen = baseChar.length();
        Random random = new Random();
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < length; i++) {
            int number = random.nextInt(baseCharLen);
            sb.append(baseChar.charAt(number));
        }
        return sb.toString();
    }

    public static String randomNumber(int length){
        String phoneNumber = "";
        Random random = new Random();
        for(int j = 0; j < length;j++){
            phoneNumber = phoneNumber+String.valueOf(random.nextInt(10));
        }
        return phoneNumber;
    }
}

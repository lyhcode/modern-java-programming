package com.example;

import org.apache.commons.codec.binary.Base64;

/**
 * 範例主程式
 */
public class Main {

    public static void main(String args[]) {
        System.out.println(new Main().sayHello());

        Base64 base64 = new Base64();
        byte[] bytes = base64.encode("I Love Gradle".getBytes());

        System.out.println(new String(bytes));
    }

    /**
     * 顯示 Hello 訊息
     * @return 回傳 Hello 訊息文字
     */
    public String sayHello() {
        return "Hello";
    }
}

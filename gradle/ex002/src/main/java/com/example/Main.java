package com.example;

import org.apache.commons.codec.binary.Base64;

public class Main {

    public static void main(String args[]) {
        System.out.println(new Main().sayHello());

        Base64 base64 = new Base64();
        byte[] bytes = base64.encode("I Love Gradle".getBytes());

        System.out.println(new String(bytes));
    }

    public String sayHello() {
        return "Hello";
    }
}

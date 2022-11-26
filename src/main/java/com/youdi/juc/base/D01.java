package com.youdi.juc.base;

public class D01 {
    public static void main(String[] args) {
        Thread hello = new Thread(() -> {
        }, "hello");
        hello.start();
    }
}

package com.xhb.tools.asm.test;

/**
 * Created by Jacky on 2020-05-16
 */
public class HelloWorld {
    public static void main(String[] args) {
        final HelloWorld helloWorld = new HelloWorld();
        helloWorld.tell(new Object());
    }

    private void tell(Object object) {
        System.out.println("tell...");
    }
}

package com.example.demo.test;

public class TestMain {

    public static void main (String[] arg)
    {
        int a = 5; // 0000 0101
        int b = 3; // 0000 0011
        a |= b; // 0000 00111
        System.out.println(a);
    }
}

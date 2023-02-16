package com.roy.reggie_takeout.test;

import org.junit.jupiter.api.Test;

import java.util.UUID;

public class UploadFileTest {
    @Test
    public void test1(){
        String fileName = "efsdfsdf.jpg";
        String[] split = fileName.split("\\.");
        String suffix = split[1];
        String s = UUID.randomUUID().toString();

        String newFileName = s + "." + suffix;

        System.out.println(newFileName);
    }
}

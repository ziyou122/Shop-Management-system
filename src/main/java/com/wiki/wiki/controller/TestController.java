package com.wiki.wiki.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestController {

    @Value("${test.hello:TEST}")
    private String testHello;

    @RequestMapping("/hello")
    public String hello(){
        return "hello world" + testHello;
    }

    @PostMapping("/hello/post")
    public String helloPost(String name){
        return "hello world : post, " + name;
    }
}

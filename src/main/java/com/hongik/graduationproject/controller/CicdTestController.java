package com.hongik.graduationproject.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class CicdTestController {
    @GetMapping("/hello")
    public String hello(){
        return "hello";
    }
}

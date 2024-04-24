package com.example.service01.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/")
public class HomeController {
    @Autowired
    private Environment env;

    @RequestMapping("/service01")
    public String getMess() {
        return "thông báo từ service01";
    }
}


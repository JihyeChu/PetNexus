package com.sparta.petnexus.user.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class UserViewController {

    @GetMapping("/home")
    public String home() {
        return "index";
    }

    @GetMapping("/login")
    public String login() {
        return "oauthLogin";
    }

}

package com.sparta.petnexus.user.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class UserViewController {

    @GetMapping("/home")
    public String home() {
        return "home";
    }

    @GetMapping("/login")
    public String login() {
        return "oauthLogin";
    }

    @GetMapping("/hospital")
    public String search(){
        return "hospital";
    }
}

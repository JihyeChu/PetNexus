package com.sparta.petnexus.user.controller;

import com.sparta.petnexus.common.security.entity.UserDetailsImpl;
import com.sparta.petnexus.user.dto.ProfileResponse;
import com.sparta.petnexus.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@RequiredArgsConstructor
public class UserViewController {

    private final UserService userService;


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

    @GetMapping("/header")
    public String header() {
        return "header";
    }

    @GetMapping("/mypagelist")
    public String mypagelist() {
        return "mypagelist";
    }

    @GetMapping("/profile")
    public String profile(Model model,@AuthenticationPrincipal
    UserDetailsImpl userDetails) {
        ProfileResponse profileResponse = userService.findUserProfile(userDetails.getUser().getId());
        model.addAttribute("profile",profileResponse);
        return "profile";
    }

}

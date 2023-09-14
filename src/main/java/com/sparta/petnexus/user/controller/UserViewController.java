package com.sparta.petnexus.user.controller;

import com.sparta.petnexus.common.security.entity.UserDetailsImpl;
import com.sparta.petnexus.user.dto.ProfileResponse;
import com.sparta.petnexus.user.dto.SignupRequest;
import com.sparta.petnexus.user.service.UserService;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Slf4j
@Controller
@RequiredArgsConstructor
public class UserViewController {

    private final UserService userService;

    @GetMapping("/home")
    public String home() {
        return "home";
    }

    @GetMapping("/signup")
    public String signup() {
        return "signup";
    }

    @PostMapping("/signup")
    public String signup(@Valid @ModelAttribute SignupRequest request,
        BindingResult bindingResult) {
        List<FieldError> fieldErrors = bindingResult.getFieldErrors();
        if (fieldErrors.size() > 0) {
            for (FieldError fieldError : bindingResult.getFieldErrors()) {
                log.error(fieldError.getField() + " 필드 : " + fieldError.getDefaultMessage());
            }
            return "redirect:/signup";
        }
        userService.signUp(request);
        return "redirect:/login"; // 회원가입 후 로그인 페이지로
    }

    @GetMapping("/login")
    public String login() {
        return "login";
    }

    @GetMapping("/hospital")
    public String search() {
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
    public String profile(Model model, @AuthenticationPrincipal
    UserDetailsImpl userDetails) {
        ProfileResponse profileResponse = userService.findUserProfile(
            userDetails.getUser().getId());
        model.addAttribute("profile", profileResponse);
        return "profile";
    }
}

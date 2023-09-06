package com.sparta.petnexus.post.controller;

import com.sparta.petnexus.common.response.ApiResponse;
import com.sparta.petnexus.common.security.entity.UserDetailsImpl;
import com.sparta.petnexus.post.dto.PostResponseDto;
import com.sparta.petnexus.post.service.PostService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequiredArgsConstructor
public class PostViewController {

    private final PostService postService;

    @GetMapping("/community")
    public String community(Model model) {
        List<PostResponseDto> postResponseDtoList = postService.getPosts();
        model.addAttribute("postList", postResponseDtoList);
        return "community";
    }

    @GetMapping("/community/{postId}")
    public String getPost(@PathVariable Long postId, Model model) {
        PostResponseDto postResponseDto = postService.getPostId(postId);
        model.addAttribute("post", postResponseDto);
        return "post";
    }

    @GetMapping("/community/post")
    public String createPost(@RequestParam(required = false) Long postId, Model model, @AuthenticationPrincipal
            UserDetailsImpl userDetails, RedirectAttributes rttr) {
        if(userDetails == null){
            rttr.addFlashAttribute("result", "로그인이 필요합니다.");
            return "redirect:/community";
        }else {
            if(postId==null){
                model.addAttribute("post",new PostResponseDto());
            } else {
                PostResponseDto postResponseDto = postService.getPostId(postId);
                model.addAttribute("post", postResponseDto);
            }
            return "createPost";
        }
    }

}

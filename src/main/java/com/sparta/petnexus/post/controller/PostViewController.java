package com.sparta.petnexus.post.controller;

import com.sparta.petnexus.post.dto.PostResponseDto;
import com.sparta.petnexus.post.service.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Controller
@RequiredArgsConstructor
public class PostViewController {

    private final PostService postService;
    @GetMapping("/community")
    public String community(Model model, @RequestParam("page") Optional<Integer> page,
                            @RequestParam("size") Optional<Integer> size,
                            @RequestParam("sortBy") Optional<String> sortBy,
                            @RequestParam("isAsc") Optional<Boolean> isAsc){
        int currentPage = page.orElse(1)-1;
        int pageSize = size.orElse(5);
        String sort = sortBy.orElse("id");
        boolean Asc = isAsc.orElse(true);
        Page<PostResponseDto> postResponseDtoList = postService.getPosts(currentPage, pageSize, sort, Asc);
        model.addAttribute("postList", postResponseDtoList);
        int totalPages = postResponseDtoList.getTotalPages();
        if (totalPages > 0) {
            List<Integer> pageNumbers = IntStream.rangeClosed(1, totalPages)
                    .boxed()
                    .collect(Collectors.toList());
            model.addAttribute("pageNumbers", pageNumbers);
        }
        return "community";
    }

    @GetMapping("/community/{postId}")
    public String getPost(@PathVariable Long postId, Model model) {
        PostResponseDto postResponseDto = postService.getPostId(postId);
        model.addAttribute("post", postResponseDto);
        return "post";
    }

    @GetMapping("/community/post")
    public String createPost(@RequestParam(required = false) Long postId, Model model) {
        if(postId==null){
            model.addAttribute("post",new PostResponseDto());
        } else {
            PostResponseDto postResponseDto = postService.getPostId(postId);
            model.addAttribute("post", postResponseDto);
        }
        return "createPost";
    }

}

package com.sparta.petnexus.post.service;

import com.sparta.petnexus.common.exception.BusinessException;
import com.sparta.petnexus.common.exception.ErrorCode;
import com.sparta.petnexus.post.dto.PostRequestDto;
import com.sparta.petnexus.post.dto.PostResponseDto;
import com.sparta.petnexus.post.entity.Post;
import com.sparta.petnexus.post.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PostService {

    private final PostRepository postRepository;

    public PostResponseDto createPost(PostRequestDto postRequestDto) {
        try{
            Post post = postRequestDto.toEntity();
            postRepository.save(post);
            return PostResponseDto.of(post);
        } catch (Exception e){
            throw new BusinessException(ErrorCode.POST_NOT_CREATE);
        }
    }
}

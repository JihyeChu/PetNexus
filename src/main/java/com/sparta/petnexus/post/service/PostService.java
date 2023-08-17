package com.sparta.petnexus.post.service;

import com.sparta.petnexus.common.exception.BusinessException;
import com.sparta.petnexus.common.exception.ErrorCode;
import com.sparta.petnexus.common.response.ApiResponse;
import com.sparta.petnexus.post.dto.PostRequestDto;
import com.sparta.petnexus.post.dto.PostResponseDto;
import com.sparta.petnexus.post.entity.Post;
import com.sparta.petnexus.post.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class PostService {

    private final PostRepository postRepository;

    public PostResponseDto createPost(PostRequestDto postRequestDto) {
        try {
            Post post = postRequestDto.toEntity();
            postRepository.save(post);
            return PostResponseDto.of(post);
        } catch (Exception e) {
            throw new BusinessException(ErrorCode.POST_NOT_CREATE);
        }
    }

    public PostResponseDto getPost(Long postId) {
        Post post = findPost(postId);
        return PostResponseDto.of(post);
    }

    @Transactional
    public PostResponseDto updatePost(Long postId, PostRequestDto postRequestDto) {
        Post post = findPost(postId);
        post.update(postRequestDto);
        return PostResponseDto.of(post);
    }

    public ResponseEntity<ApiResponse> deletePost(Long postId) {
        Post post = findPost(postId);
        postRepository.delete(post);
        return ResponseEntity.ok().body(new ApiResponse("post 삭제 완료!", HttpStatus.OK.value()));
    }

    public Post findPost(Long id) {
        return postRepository.findById(id).orElseThrow(() -> new BusinessException(ErrorCode.NOT_POST));
    }
}

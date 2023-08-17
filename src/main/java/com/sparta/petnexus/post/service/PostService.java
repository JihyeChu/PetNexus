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

import java.util.List;

@Service
@RequiredArgsConstructor
public class PostService {

    private final PostRepository postRepository;

    public ResponseEntity<ApiResponse> createPost(PostRequestDto postRequestDto) {
        try {
            Post post = postRequestDto.toEntity();
            postRepository.save(post);
            return ResponseEntity.ok().body(new ApiResponse("post 생성 성공!",HttpStatus.CREATED.value()));
        } catch (Exception e) {
            throw new BusinessException(ErrorCode.POST_NOT_CREATE);
        }
    }

    public ResponseEntity<List<PostResponseDto>> getPosts() {
        return ResponseEntity.ok().body(postRepository.findAll().stream().map(PostResponseDto::of).toList());
    }

    public ResponseEntity<PostResponseDto> getPostId(Long postId) {
        Post post = findPost(postId);
        return ResponseEntity.ok().body(PostResponseDto.of(post));
    }

    @Transactional
    public ResponseEntity<ApiResponse> updatePost(Long postId, PostRequestDto postRequestDto) {
        Post post = findPost(postId);
        post.update(postRequestDto);
        return ResponseEntity.ok().body(new ApiResponse("post 수정 성공!", HttpStatus.OK.value()));
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

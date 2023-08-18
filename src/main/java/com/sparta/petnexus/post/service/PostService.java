package com.sparta.petnexus.post.service;

import com.sparta.petnexus.common.exception.BusinessException;
import com.sparta.petnexus.common.exception.ErrorCode;
import com.sparta.petnexus.post.dto.PostRequestDto;
import com.sparta.petnexus.post.dto.PostResponseDto;
import com.sparta.petnexus.post.entity.Post;
import com.sparta.petnexus.post.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PostService {

    private final PostRepository postRepository;

    public void createPost(PostRequestDto postRequestDto) {
            Post post = postRequestDto.toEntity();
            postRepository.save(post);
    }

    public List<PostResponseDto> getPosts() {
        return postRepository.findAll().stream().map(PostResponseDto::of).toList();
    }

    public PostResponseDto getPostId(Long postId) {
        Post post = findPost(postId);
        return PostResponseDto.of(post);
    }

    @Transactional
    public void updatePost(Long postId, PostRequestDto postRequestDto) {
        Post post = findPost(postId);
        post.update(postRequestDto);
    }

    public void deletePost(Long postId) {
        Post post = findPost(postId);
        postRepository.delete(post);
    }

    public Post findPost(Long id) {
        return postRepository.findById(id).orElseThrow(() -> new BusinessException(ErrorCode.NOT_FOUND_POST));
    }

}

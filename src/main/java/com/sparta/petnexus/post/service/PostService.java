package com.sparta.petnexus.post.service;

import com.sparta.petnexus.common.exception.BusinessException;
import com.sparta.petnexus.common.exception.ErrorCode;
import com.sparta.petnexus.post.dto.PostRequestDto;
import com.sparta.petnexus.post.dto.PostResponseDto;
import com.sparta.petnexus.post.entity.Post;
import com.sparta.petnexus.post.repository.PostRepository;
import com.sparta.petnexus.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PostService {

    private final PostRepository postRepository;

    public void createPost(PostRequestDto postRequestDto, User user) {
            Post post = postRequestDto.toEntity(user);
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
    public void updatePost(Long postId, PostRequestDto postRequestDto, User user) {
        Post post = findPost(postId);

        if(!user.getId().equals(post.getUser().getId())){
            throw new BusinessException(ErrorCode.NOT_POST_UPDATE);
        }
        post.update(postRequestDto);
    }

    public void deletePost(Long postId, User user) {
        Post post = findPost(postId);
        if(!user.getId().equals(post.getUser().getId())){
            throw new BusinessException(ErrorCode.NOT_POST_DELETE);
        }
        postRepository.delete(post);
    }

    public Post findPost(Long id) {
        return postRepository.findById(id).orElseThrow(() -> new BusinessException(ErrorCode.NOT_FOUND_POST));
    }
}

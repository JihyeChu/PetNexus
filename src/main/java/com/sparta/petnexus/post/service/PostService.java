package com.sparta.petnexus.post.service;

import com.sparta.petnexus.post.dto.PostRequestDto;
import com.sparta.petnexus.post.dto.PostResponseDto;
import com.sparta.petnexus.post.entity.Post;
import com.sparta.petnexus.user.entity.User;

import java.util.List;


public interface PostService {

    void createPost(PostRequestDto postRequestDto, User user);

    List<PostResponseDto> getPosts();

    PostResponseDto getPostId(Long postId);

    void updatePost(Long postId, PostRequestDto postRequestDto, User user);

    void deletePost(Long postId, User user);

    void createPostLike(Long postId, User user);
    void deletePostLike(Long postId, User user);
    Post findPost(Long id);
}

package com.sparta.petnexus.post.service;

import com.sparta.petnexus.common.exception.BusinessException;
import com.sparta.petnexus.common.exception.ErrorCode;
import com.sparta.petnexus.post.dto.PostRequestDto;
import com.sparta.petnexus.post.dto.PostResponseDto;
import com.sparta.petnexus.post.entity.Post;
import com.sparta.petnexus.post.postBookmark.entity.PostBookmark;
import com.sparta.petnexus.post.postBookmark.repository.PostBookmarkRepository;
import com.sparta.petnexus.post.postLike.entity.PostLike;
import com.sparta.petnexus.post.postLike.repository.PostLikeRepository;
import com.sparta.petnexus.post.repository.PostRepository;
import com.sparta.petnexus.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PostServiceImpl implements PostService {

    private final PostRepository postRepository;
    private final PostLikeRepository postLikeRepository;
    private final PostBookmarkRepository postBookmarkRepository;

    @Override
    public void createPost(PostRequestDto postRequestDto, User user) {
        Post post = postRequestDto.toEntity(user);
        postRepository.save(post);
    }

    @Override
    public List<PostResponseDto> getPosts() {
        return postRepository.findAll().stream().map(PostResponseDto::of).toList();
    }

    @Override
    public PostResponseDto getPostId(Long postId) {
        Post post = findPost(postId);
        return PostResponseDto.of(post);
    }

    @Override
    @Transactional
    public void updatePost(Long postId, PostRequestDto postRequestDto, User user) {
        Post post = findPost(postId);

        if(!user.getId().equals(post.getUser().getId())){
            throw new BusinessException(ErrorCode.NOT_USER_UPDATE);
        }
        post.update(postRequestDto);
    }

    @Override
    public void deletePost(Long postId, User user) {
        Post post = findPost(postId);
        if(!user.getId().equals(post.getUser().getId())){
            throw new BusinessException(ErrorCode.NOT_USER_DELETE);
        }
        postRepository.delete(post);
    }

    @Override
    public void createPostLike(Long postId, User user) {
        Post post = findPost(postId);
        userCheck(post,user);

        postLikeRepository.findByPostAndUser(post, user).ifPresent(postLike -> {
            throw new BusinessException(ErrorCode.EXISTS_LIKE);
        });

        PostLike postLike = new PostLike(post, user);
        postLikeRepository.save(postLike);
    }

    @Override
    public void deletePostLike(Long postId, User user){
        Post post = findPost(postId);
        Optional<PostLike> postLike =  postLikeRepository.findByPostAndUser(post,user);
        if(postLike.isPresent()){
            postLikeRepository.delete(postLike.get());
        } else{
            throw new BusinessException(ErrorCode.NOT_EXISTS_LIKE);
        }
    }

    @Override
    public void createPostBookmark(Long postId, User user){
        Post post = findPost(postId);
        userCheck(post,user);

        postBookmarkRepository.findByPostAndUser(post, user).ifPresent(postBookmark -> {
            throw new BusinessException(ErrorCode.EXISTS_BOOKMARK);
        });

        PostBookmark postBookmark = new PostBookmark(post, user);
        postBookmarkRepository.save(postBookmark);
    }

    @Override
    public void deletePostBookmark(Long postId, User user){
        Post post = findPost(postId);
        Optional<PostBookmark> postBookmark =  postBookmarkRepository.findByPostAndUser(post,user);
        if(postBookmark.isPresent()){
            postBookmarkRepository.delete(postBookmark.get());
        } else{
            throw new BusinessException(ErrorCode.NOT_EXISTS_BOOKMARK);
        }
    }

    @Override
    public Post findPost(Long id) {
        return postRepository.findById(id).orElseThrow(() -> new BusinessException(ErrorCode.NOT_FOUND_POST));
    }

    public void userCheck(Post post, User user) {
        if (user.getId().equals(post.getUser().getId())) {
            throw new BusinessException(ErrorCode.SELF_USER_POST);
        }
    }
}

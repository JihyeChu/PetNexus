package com.sparta.petnexus.postComment.service;

import com.sparta.petnexus.common.exception.BusinessException;
import com.sparta.petnexus.common.exception.ErrorCode;
import com.sparta.petnexus.post.entity.Post;
import com.sparta.petnexus.post.service.PostService;
import com.sparta.petnexus.postComment.dto.PostCommentRequestsDto;
import com.sparta.petnexus.postComment.entity.PostComment;
import com.sparta.petnexus.postComment.repository.PostCommentRepository;
import com.sparta.petnexus.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class PostCommentServiceImpl implements PostCommentService {

    private final PostCommentRepository postCommentRepository;
    private final PostService postService;

    @Override
    public void createPostComment(Long postId, PostCommentRequestsDto postCommentRequestsDto, User user){
        Post post = postService.findPost(postId);
        PostComment postComment = postCommentRequestsDto.toEntity(post,user);
        postCommentRepository.save(postComment);
    }

    @Override
    @Transactional
    public void updatePostComment(Long postId, Long commentId, PostCommentRequestsDto postCommentRequestsDto, User user){
        PostComment postComment = findPostComment(commentId);

        if(!user.getId().equals(postComment.getUser().getId())){
            throw new BusinessException(ErrorCode.NOT_USER_UPDATE);
        }
        postComment.update(postCommentRequestsDto);
    }

    @Override
    public void deletePostComment(Long postId, Long commentId, User user){
        PostComment postComment = findPostComment(commentId);
        if(!user.getId().equals(postComment.getUser().getId())){
            throw new BusinessException(ErrorCode.NOT_USER_DELETE);
        }
        postCommentRepository.delete(postComment);
    }

    @Override
    public PostComment findPostComment(Long id){
        return postCommentRepository.findById(id).orElseThrow(() -> new BusinessException(ErrorCode.NOT_FOUND_COMMENT));
    }
}

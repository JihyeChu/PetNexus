package com.sparta.petnexus.post.postComment.service;

import com.sparta.petnexus.post.postComment.entity.PostComment;
import com.sparta.petnexus.post.postComment.dto.PostCommentRequestsDto;
import com.sparta.petnexus.user.entity.User;

public interface PostCommentService {
    void createPostComment(Long postId, PostCommentRequestsDto postCommentRequestsDto, User user);
    void updatePostComment(Long postId, Long commentId, PostCommentRequestsDto postCommentRequestsDto, User user);
    void deletePostComment(Long postId, Long commentId, User user);
    PostComment findPostComment(Long id);
}

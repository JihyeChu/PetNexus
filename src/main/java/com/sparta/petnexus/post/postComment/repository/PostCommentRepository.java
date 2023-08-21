package com.sparta.petnexus.post.postComment.repository;

import com.sparta.petnexus.post.postComment.entity.PostComment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostCommentRepository extends JpaRepository<PostComment, Long> {
}

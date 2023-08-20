package com.sparta.petnexus.postComment.repository;

import com.sparta.petnexus.postComment.entity.PostComment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostCommentRepository extends JpaRepository<PostComment, Long> {
}

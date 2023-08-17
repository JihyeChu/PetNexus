package com.sparta.petnexus.post.repository;

import com.sparta.petnexus.post.entity.Post;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostRepository extends JpaRepository<Post, Long> {
}


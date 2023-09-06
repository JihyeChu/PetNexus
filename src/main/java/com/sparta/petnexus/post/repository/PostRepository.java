package com.sparta.petnexus.post.repository;

import com.sparta.petnexus.post.entity.Post;
import com.sparta.petnexus.user.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PostRepository extends JpaRepository<Post, Long> {

    List<Post> findByTitleContainingOrContentContaining(String title, String content);

}


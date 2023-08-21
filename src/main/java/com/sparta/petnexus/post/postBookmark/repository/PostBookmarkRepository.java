package com.sparta.petnexus.post.postBookmark.repository;

import com.sparta.petnexus.post.entity.Post;
import com.sparta.petnexus.post.postBookmark.entity.PostBookmark;
import com.sparta.petnexus.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PostBookmarkRepository extends JpaRepository<PostBookmark, Long> {
    Optional<PostBookmark> findByPostAndUser(Post post, User user);
}

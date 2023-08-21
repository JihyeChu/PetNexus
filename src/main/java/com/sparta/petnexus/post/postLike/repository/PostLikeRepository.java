package com.sparta.petnexus.post.postLike.repository;

import com.sparta.petnexus.post.entity.Post;
import com.sparta.petnexus.post.postLike.entity.PostLike;
import com.sparta.petnexus.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PostLikeRepository extends JpaRepository<PostLike, Long> {
    Boolean existsByPostAndUser(Post post, User user);
    Optional<PostLike> findByPostAndUser(Post post, User user);
}

package com.sparta.petnexus.post.service;

import com.sparta.petnexus.Image.entity.Image;
import com.sparta.petnexus.Image.repository.ImageRepository;
import com.sparta.petnexus.Image.config.AwsS3upload;
import com.sparta.petnexus.common.exception.BusinessException;
import com.sparta.petnexus.common.exception.ErrorCode;
import com.sparta.petnexus.notification.service.NotificationService;
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
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PostServiceImpl implements PostService {

    private final PostRepository postRepository;
    private final PostLikeRepository postLikeRepository;
    private final PostBookmarkRepository postBookmarkRepository;
    private final ImageRepository imageRepository;
    private final AwsS3upload awsS3upload;
    private final NotificationService notificationService;


    @Override
    @Transactional
    public void createPost(User user, List<MultipartFile> files,PostRequestDto postRequestDto) throws IOException {
        Post post = new Post(postRequestDto, user);
        postRepository.save(post);
        if (files != null) {
            for (MultipartFile file : files) {
                String fileUrl = awsS3upload.upload(file, "post " + post.getId());
                if (imageRepository.existsByImageUrlAndId(fileUrl, post.getId())) {
                    throw new BusinessException(ErrorCode.EXISTED_FILE);
                }
                imageRepository.save(new Image(post, fileUrl));
            }
        }
    }

    @Override
    @Transactional(readOnly = true)
    public List<PostResponseDto> getPosts() {
        return postRepository.findAll().stream().map(PostResponseDto::of).toList();
    }

    @Override
    @Transactional(readOnly = true)
    public PostResponseDto getPostId(Long postId) {
        return PostResponseDto.of(findPost(postId));
    }

    @Override
    @Transactional
    public void updatePost(Long postId, PostRequestDto postRequestDto, User user) {
        Post post = findPost(postId);

        if (!user.getId().equals(post.getUser().getId())) {
            throw new BusinessException(ErrorCode.NOT_USER_UPDATE);
        }
        post.update(postRequestDto);
    }

    @Override
    @Transactional
    public void deletePost(Long postId, User user) {
        Post post = findPost(postId);
        if (!user.getId().equals(post.getUser().getId())) {
            throw new BusinessException(ErrorCode.NOT_USER_DELETE);
        }
        postRepository.delete(post);
    }

    @Override
    @Transactional
    public void createPostLike(Long postId, User user) {
        Post post = findPost(postId);
        userCheck(post, user);

        postLikeRepository.findByPostAndUser(post, user).ifPresent(postLike -> {
            throw new BusinessException(ErrorCode.EXISTS_LIKE);
        });
        postLikeRepository.save(new PostLike(post, user));

        notificationService.notifyToUsersThatTheyHaveReceivedLike(new PostLike(post, user)); // 좋아요 알람 추가
    }

    @Override
    @Transactional
    public void deletePostLike(Long postId, User user) {
        Post post = findPost(postId);
        Optional<PostLike> postLike = postLikeRepository.findByPostAndUser(post, user);
        if (postLike.isPresent()) {
            postLikeRepository.delete(postLike.get());
        } else {
            throw new BusinessException(ErrorCode.NOT_EXISTS_LIKE);
        }
    }

    @Override
    @Transactional
    public void createPostBookmark(Long postId, User user) {
        Post post = findPost(postId);
        userCheck(post, user);

        postBookmarkRepository.findByPostAndUser(post, user).ifPresent(postBookmark -> {
            throw new BusinessException(ErrorCode.EXISTS_BOOKMARK);
        });

        postBookmarkRepository.save(new PostBookmark(post, user));
    }

    @Override
    @Transactional
    public void deletePostBookmark(Long postId, User user) {
        Post post = findPost(postId);
        Optional<PostBookmark> postBookmark = postBookmarkRepository.findByPostAndUser(post, user);
        if (postBookmark.isPresent()) {
            postBookmarkRepository.delete(postBookmark.get());
        } else {
            throw new BusinessException(ErrorCode.NOT_EXISTS_BOOKMARK);
        }
    }

    @Override
    public Post findPost(Long id) {
        return postRepository.findById(id).orElseThrow(() -> new BusinessException(ErrorCode.NOT_FOUND_POST));
    }

    @Override
    public void userCheck(Post post, User user) {
        if (user.getId().equals(post.getUser().getId())) {
            throw new BusinessException(ErrorCode.SELF_USER_POST);
        }
    }
}

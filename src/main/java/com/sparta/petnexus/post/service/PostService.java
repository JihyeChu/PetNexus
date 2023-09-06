package com.sparta.petnexus.post.service;

import com.sparta.petnexus.post.dto.PostRequestDto;
import com.sparta.petnexus.post.dto.PostResponseDto;
import com.sparta.petnexus.post.entity.Post;
import com.sparta.petnexus.user.entity.User;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;


public interface PostService {

    /**
     * 게시글 생성
     * @param postRequestDto : 게시글 생성 요청정보
     * @param user : 게시글 생성 요청자
     * @param files : 게시글 생성 첨부 파일           
     * */
    void createPost(User user, List<MultipartFile> files,PostRequestDto postRequestDto) throws IOException;

    /**
     * 게시글 전체 조회
     * @return : 게시글 전체 정보
     * */
    List<PostResponseDto> getPosts();

    /**
     * 게시글 단건 전체 조회
     * @param postId : 조회 할 거래게시글 id
     * @return : 조회된 게시글 단건 정보
     * */
    PostResponseDto getPostId(Long postId);

    /**
     * 게시글 수정
     * @param postId : 수정할 게시글 id
     * @param postRequestDto : 게시글 수정 요청정보
     * @param user : 게시글 수정 요청자
     * */
    void updatePost(Long postId, PostRequestDto postRequestDto, User user, List<MultipartFile> files) throws IOException;

    /**
     * 게시글 삭제
     * @param postId : 삭제할 게시글 id
     * @param user : 게시글 삭제 요청자
     * */
    void deletePost(Long postId, User user);

    /**
     * 좋아요 생성
     * @param postId : 종아요할 게시글 id
     * @param user : 좋아요 생성 요청자
     * */
    void createPostLike(Long postId, User user);

    /**
     * 좋아요 삭제
     * @param postId : 종아요 삭제할 게시글 id
     * @param user : 좋아요 삭제 요청자
     * */
    void deletePostLike(Long postId, User user);

    /**
     * 북마크 생성
     * @param postId : 북마크할 게시글 id
     * @param user : 북마크 요청자
     * */
    void createPostBookmark(Long postId, User user);

    /**
     * 북마크 삭제
     * @param postId : 북마크 삭제할 게시글 id
     * @param user : 북마크 삭제 요청자
     * */
    void deletePostBookmark(Long postId, User user);

    /**
     * 게시글 찾기
     * @param id : 찾을 게시글 id
     * @return : 게시글 Entity
     * */
    Post findPost(Long id);

    /**
     * 게시글 작성자 확인
     * @param post : 작업할 게시글
     * @param user : 게시글 작업 요청자
     * */
    void userCheck(Post post, User user);
}

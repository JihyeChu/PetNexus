package com.sparta.petnexus.post.controller;

import com.sparta.petnexus.common.response.ApiResponse;
import com.sparta.petnexus.common.security.entity.UserDetailsImpl;
import com.sparta.petnexus.post.dto.PostRequestDto;
import com.sparta.petnexus.post.dto.PostResponseDto;
import com.sparta.petnexus.post.service.PostService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
@Tag(name = "post 관련 API", description = "Post CRUD 및 북마크, 좋아요")
public class PostController {

    private final PostService postService;

    @PostMapping("/post")
    @Operation(summary = "post 생성", description = "requestDto로 받아온 데이터로 post를 만듭니다.")
    public ResponseEntity<ApiResponse> createPost(@AuthenticationPrincipal UserDetailsImpl userDetails,
                                                  @RequestParam(value = "file", required = false) List<MultipartFile> files,
                                                  @RequestParam(value = "title") String title,
                                                  @RequestParam(value = "content") String content) throws IOException {
        postService.createPost(userDetails.getUser(),files,title,content);
        return ResponseEntity.ok().body(new ApiResponse("post 생성 성공!", HttpStatus.CREATED.value()));
    }

    @GetMapping("/post")
    @Operation(summary = "post 전체 조회", description = "매개변수를 받지 않고 전체 post를 조회합니다.")
    public ResponseEntity<List<PostResponseDto>> getPosts(){
        return ResponseEntity.ok(postService.getPosts());
    }

    @GetMapping("/post/{postId}")
    @Operation(summary = "post 단건 조회", description = "@PathVariable로 postId 받아 postId 해당하는 post를 조회합니다.")
    public ResponseEntity<PostResponseDto> getPostId(
            @Parameter(name="postId",description = "특정 post id",in= ParameterIn.PATH) @PathVariable Long postId) {
        return ResponseEntity.ok(postService.getPostId(postId));
    }

    @PutMapping("/post/{postId}")
    @Operation(summary = "post 수정", description = "@PathVariable로 postId와 requestDto를 받아 postId 해당하는 post를 수정합니다.")
    public ResponseEntity<ApiResponse> updatePost(
            @Parameter(name="postId",description = "특정 post id",in= ParameterIn.PATH) @PathVariable Long postId, @RequestBody PostRequestDto postRequestDto, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        postService.updatePost(postId, postRequestDto, userDetails.getUser());
        return ResponseEntity.ok().body(new ApiResponse("post 수정 성공!", HttpStatus.OK.value()));
    }

    @DeleteMapping("/post/{postId}")
    @Operation(summary = "post 삭제", description = "@PathVariable로 postId와 requestDto를 받아 postId 해당하는 post를 삭제합니다.")
    public ResponseEntity<ApiResponse> deletePost(
            @Parameter(name="postId",description = "특정 post id",in= ParameterIn.PATH) @PathVariable Long postId, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        postService.deletePost(postId,userDetails.getUser());
        return ResponseEntity.ok().body(new ApiResponse("post 삭제 완료!", HttpStatus.OK.value()));
    }

    @PostMapping("/post/{postId}/like")
    @Operation(summary = "post 좋아요 생성", description = "@PathVariable로 postId와 받아 로그인한 user 정보로 post_like 좋아요 생성합니다")
    public ResponseEntity<ApiResponse> createPostLike(
            @Parameter(name="postId",description = "특정 post id",in= ParameterIn.PATH) @PathVariable Long postId, @AuthenticationPrincipal UserDetailsImpl userDetails){
        postService.createPostLike(postId,userDetails.getUser());
        return ResponseEntity.ok().body(new ApiResponse("해당 post에 좋아요를 눌렀습니다", HttpStatus.CREATED.value()));
    }

    @DeleteMapping("/post/{postId}/like")
    @Operation(summary = "post 좋아요 삭제", description = "@PathVariable로 postId와 받아 로그인한 user 정보로 post_like 좋아요 삭제합니다")
    public ResponseEntity<ApiResponse> deletePostLike(
            @Parameter(name="postId",description = "특정 post id",in= ParameterIn.PATH) @PathVariable Long postId, @AuthenticationPrincipal UserDetailsImpl userDetails){
        postService.deletePostLike(postId,userDetails.getUser());
        return ResponseEntity.ok().body(new ApiResponse("좋아요를 취소 하였습니다", HttpStatus.OK.value()));
    }

    @PostMapping("/post/{postId}/bookmark")
    @Operation(summary = "post 북마크 생성", description = "@PathVariable로 postId와 받아 로그인한 user 정보로 post_bookmark 북마크를 생성합니다")
    public ResponseEntity<ApiResponse> createPostBookmark(
            @Parameter(name="postId",description = "특정 post id",in= ParameterIn.PATH) @PathVariable Long postId, @AuthenticationPrincipal UserDetailsImpl userDetails){
        postService.createPostBookmark(postId,userDetails.getUser());
        return ResponseEntity.ok().body(new ApiResponse("해당 post를 북마크에 추가하였습니다.", HttpStatus.CREATED.value()));
    }

    @DeleteMapping("/post/{postId}/bookmark")
    @Operation(summary = "post 북마크 삭제", description = "@PathVariable로 postId와 받아 로그인한 user 정보로 post_bookmark 북마크를 삭제합니다")
    public ResponseEntity<ApiResponse> deletePostBookmark(
            @Parameter(name="postId",description = "특정 post id",in= ParameterIn.PATH) @PathVariable Long postId, @AuthenticationPrincipal UserDetailsImpl userDetails){
        postService.deletePostBookmark(postId,userDetails.getUser());
        return ResponseEntity.ok().body(new ApiResponse("해당 post를 북마크에 삭제하였습니다.", HttpStatus.OK.value()));
    }
}

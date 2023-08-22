package com.sparta.petnexus.trade.comment.service;


import com.sparta.petnexus.trade.comment.dto.TradeCommentRequestDto;
import com.sparta.petnexus.trade.comment.entity.TradeComment;
import com.sparta.petnexus.user.entity.User;

public interface TradeCommentService {

    /*
     * 거래게시글 댓글 생성
     * @param tradeId : 댓글을 생성 할 게시글 id
     * @param requestDto : 댓글 정보
     * @param user : 댓글 생성할 요청자
     * */
    public void createComment(Long tradeId, TradeCommentRequestDto requestDto, User user);

    /*
     * 거래게시글 댓글 수정
     * @param tradeId : 댓글을 수정 할 게시글 id
     * @param commentId : 수정 할 댓글 id
     * @param requestDto : 댓글 정보
     * @param user : 댓글 수정할 요청자
     * */
    public void updateComment(Long tradeId, Long commentId, TradeCommentRequestDto requestDto, User user);

    /*
     * 거래게시글 댓글 삭제
     * @param tradeId : 댓글을 삭제 할 게시글 id
     * @param commentId : 삭제할 댓글 id
     * @param user : 댓글 삭제할 요청자
     * */
    public void deleteComment(Long tradeId, Long commentId, User user);

    /*
     * 거래게시글 댓글 Entity 한건 조회
     * @param id : 조회할 거래게시글 id
     * @param id : 조회할 거래게시글 댓글 id
     * @return : 거래게시글 댓글 entity
     * */
    public TradeComment findTradeComment(Long tradeCommentId);
}

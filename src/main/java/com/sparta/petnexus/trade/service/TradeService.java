package com.sparta.petnexus.trade.service;

import com.sparta.petnexus.trade.dto.TradeRequestDto;
import com.sparta.petnexus.trade.dto.TradeResponseDto;
import com.sparta.petnexus.trade.entity.Trade;
import com.sparta.petnexus.user.entity.User;

import java.util.List;

public interface TradeService {

    /*
     * 거래게시글 생성
     * @param requestDto : 거래게시글 생성 요청정보
     * @param user : 거래게시글 생성 요청자
     * */
    public void createTrade(TradeRequestDto requestDto, User user);

    /*
     * 거래게시글 전체 조회
     * @return : 거래게시글 전체 정보
     * */
    public List<TradeResponseDto> getTrade();

    /*
     * 거래게시글 단건 조회
     * @param id : 조회 할 거래게시글 id
     * @return : 조회된 한건의 거래게시글 정보
     * */
    public TradeResponseDto selectTrade(Long tradeId);

    /*
     * 거래게시글 수정
     * @param requestDto : 거래게시글 수정 요청정보
     * @param id : 수정 할 거래게시글 id
     * @param user : 거래게시글 수정 요청자
     * */
    public void updateTrade(TradeRequestDto requestDto, Long tradeId, User user);

    /*
     * 거래게시글 삭제
     * @param id : 삭제 할 거래게시글 id
     * @param user : 거래게시글 삭제 요청자
     * */
    public void deleteTrade(Long tradeId, User user);

    /*
     * 거래게시글 좋아요
     * @param id : 좋아요 할 거래게시글 id
     * @param user : 좋아요를 생성 할 요청자
     * */
    public void likeTrade(Long tradeId, User user);

    /*
    * 거래게시글 좋아요 취소
    * @param id : 좋아요 취소 할 거래게시글 id
    * @param user : 좋아요를 생성 할 요청자
    * */
    public void dislikeTrade(Long tradeId, User user);

    /*
     * 거래게시글 Entity 한건 조회
     * @param id : 조회할 거래게시글 id
     * @return : 거래게시글 entity
     * */
    public Trade findTrade(Long tradeId);


}

package com.sparta.petnexus.trade.service;

import com.sparta.petnexus.common.security.entity.UserDetailsImpl;
import com.sparta.petnexus.trade.dto.TradeRequestDto;
import com.sparta.petnexus.trade.dto.TradeResponseDto;
import com.sparta.petnexus.trade.entity.CategoryEnum;
import com.sparta.petnexus.trade.entity.Trade;
import com.sparta.petnexus.user.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface TradeService {

    /*
     * 거래게시글 생성
     * @param requestDto : 거래게시글 생성 요청정보
     * @param user : 거래게시글 생성 요청자
     * @param files : 거래게시글 생성 첨부 파일
     * */
    public void createTrade(TradeRequestDto requestDto, User user,List<MultipartFile> files) throws IOException;

    /*
     * 거래게시글 전체 조회
     * @return : 거래게시글 전체 정보
     * */
    Page<TradeResponseDto> getTrade(int page, int size, String sortBy, boolean isAsc);

    Page<TradeResponseDto> getmyTrade(int page, int size, String sortBy, boolean isAsc, UserDetailsImpl userDetails);

    /*
     * 카테고리 별 거래게시글 전체 조회
     * @param page : 거래게시글 현재페이지
     * @param size : 한 페이지에 조회될 게시글 수
     * @param category : 유저가 조회하고자 하는 카테고리 종류
     * @return : 카테고리 별 거래게시글 정보
     * */
    Page<TradeResponseDto> getCategoryTrade(CategoryEnum category, Pageable pageable);

    Page<TradeResponseDto> searchTrade(String keyword, Pageable pageable);

    /*
     * 거래게시글 단건 조회
     * @param id : 조회 할 거래게시글 id
     * @return : 조회된 한건의 거래게시글 정보
     * */
    TradeResponseDto selectTrade(Long tradeId);

    /*
     * 거래게시글 수정
     * @param requestDto : 거래게시글 수정 요청정보
     * @param id : 수정 할 거래게시글 id
     * @param user : 거래게시글 수정 요청자
     * */
    void updateTrade(TradeRequestDto requestDto, Long tradeId, User user, List<MultipartFile> files) throws IOException;

    /*
     * 거래게시글 삭제
     * @param id : 삭제 할 거래게시글 id
     * @param user : 거래게시글 삭제 요청자
     * */
    void deleteTrade(Long tradeId, User user);

    /*
     * 거래게시글 좋아요
     * @param id : 좋아요 할 거래게시글 id
     * @param user : 좋아요를 생성 할 요청자
     * */
    void likeTrade(Long tradeId, User user);

    /*
     * 거래게시글 좋아요 취소
     * @param id : 좋아요 취소 할 거래게시글 id
     * @param user : 좋아요를 생성 할 요청자
     * */
    void dislikeTrade(Long tradeId, User user);

    /*
     * 거래게시글 북마크
     * @param id : 북마크 할 거래게시글 id
     * @param user : 북마크 요청자
     * */
    void doBookmark(Long tradeId, User user);

    /*
     * 거래게시글 북마크 취소
     * @param id : 북마크 취소 할 거래게시글 id
     * @param user : 북마크 취소 요청자
     * */
    void undoBookmark(Long tradeId, User user);

    /*
     * 거래게시글 Entity 한건 조회
     * @param id : 조회할 거래게시글 id
     * @return : 거래게시글 entity
     * */
    Trade findTrade(Long tradeId);



}

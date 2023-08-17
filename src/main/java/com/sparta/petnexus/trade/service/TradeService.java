package com.sparta.petnexus.trade.service;

import com.sparta.petnexus.trade.dto.TradeRequestDto;
import com.sparta.petnexus.trade.dto.TradeResponseDto;
import com.sparta.petnexus.trade.entity.Trade;
import com.sparta.petnexus.trade.repository.TradeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TradeService {

    private final TradeRepository tradeRepository;

    // 거래 게시글 생성
    @Transactional
    public void createTrade(TradeRequestDto requestDto) {
        try {
            Trade trade = requestDto.toEntity();
            tradeRepository.save(trade);
        } catch (Exception e) {
            throw new RuntimeException("거래 게시글 생성 실패. " + e.getMessage(), e);
        }
    }

    //    거래 게시글 전체 조회
    @Transactional(readOnly = true)
    public List<TradeResponseDto> getTrade() {
        return tradeRepository.findAll().stream().map(TradeResponseDto::of).collect(Collectors.toList());
    }


    //    거래 게시글 단건 조회
    @Transactional(readOnly = true)
    public TradeResponseDto selectTrade(Long tradeId) {
        Trade trade = findTrade(tradeId);
        return TradeResponseDto.of(trade);
    }

    @Transactional
    public void updateTrade(TradeRequestDto requestDto, Long tradeId) {
        Trade trade = findTrade(tradeId);

        trade.update(requestDto);
    }

    @Transactional
    public void deleteTrade(Long tradeId) {
        Trade trade = findTrade(tradeId);

        tradeRepository.delete(trade);
    }

    public Trade findTrade(Long tradeId) {
        return tradeRepository.findById(tradeId).orElseThrow(
                () -> new IllegalArgumentException("선택한 거래 게시글은 존재하지 않습니다.")
        );
    }


}

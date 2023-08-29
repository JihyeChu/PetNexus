package com.sparta.petnexus.trade.comment.service;

import com.sparta.petnexus.common.exception.BusinessException;
import com.sparta.petnexus.common.exception.ErrorCode;
import com.sparta.petnexus.trade.comment.dto.TradeCommentRequestDto;
import com.sparta.petnexus.trade.comment.entity.TradeComment;
import com.sparta.petnexus.trade.comment.repository.TradeCommentRepository;
import com.sparta.petnexus.trade.entity.Trade;
import com.sparta.petnexus.trade.service.TradeService;
import com.sparta.petnexus.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class TradeCommentServiceImpl implements TradeCommentService {

    private final TradeService tradeService;
    private final TradeCommentRepository tradeCommentRepository;

    @Override
    @Transactional
    public void createComment(Long tradeId, TradeCommentRequestDto requestDto, User user) {
        Trade trade = tradeService.findTrade(tradeId);
        TradeComment tradeComment = requestDto.toEntity(user, trade);
        tradeCommentRepository.save(tradeComment);
    }

    @Override
    @Transactional
    public void updateComment(Long tradeId, Long commentId, TradeCommentRequestDto requestDto, User user) {
        Trade trade = tradeService.findTrade(tradeId);
        TradeComment tradeComment = findTradeComment(commentId);

        if (!user.getId().equals(trade.getUser().getId())) {
            throw new BusinessException(ErrorCode.NOT_TRADE_UPDATE);
        }

        tradeComment.update(requestDto);
    }

    @Override
    @Transactional
    public void deleteComment(Long tradeId, Long commentId, User user) {
        Trade trade = tradeService.findTrade(tradeId);
        TradeComment tradeComment = findTradeComment(commentId);

        if (!user.getId().equals(trade.getUser().getId())) {
            throw new BusinessException(ErrorCode.NOT_TRADE_DELETE);
        }

        tradeCommentRepository.delete(tradeComment);
    }


    @Override
    public TradeComment findTradeComment(Long tradeCommentId) {
        return tradeCommentRepository.findById(tradeCommentId).orElseThrow(
                () -> new BusinessException(ErrorCode.NOT_FOUND_TRADECOMMENT)
        );
    }

}

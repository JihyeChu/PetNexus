package com.sparta.petnexus.trade.service;

import com.sparta.petnexus.Image.config.AwsS3upload;
import com.sparta.petnexus.Image.entity.Image;
import com.sparta.petnexus.Image.repository.ImageRepository;
import com.sparta.petnexus.common.exception.BusinessException;
import com.sparta.petnexus.common.exception.ErrorCode;
import com.sparta.petnexus.trade.bookmark.entity.TradeBookmark;
import com.sparta.petnexus.trade.bookmark.repository.TradeBookmarkRepository;
import com.sparta.petnexus.trade.dto.TradeRequestDto;
import com.sparta.petnexus.trade.dto.TradeResponseDto;
import com.sparta.petnexus.trade.entity.Trade;
import com.sparta.petnexus.trade.like.entity.TradeLike;
import com.sparta.petnexus.trade.like.repository.TradeLikeRepository;
import com.sparta.petnexus.trade.repository.TradeRepository;
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
public class TradeServiceImpl implements TradeService {

    private final TradeRepository tradeRepository;
    private final TradeLikeRepository tradeLikeRepository;
    private final TradeBookmarkRepository tradeBookmarkRepository;
    private final ImageRepository imageRepository;
    private final AwsS3upload awsS3upload;

    @Override
    @Transactional
    public void createTrade(TradeRequestDto requestDto, User user, List<MultipartFile> files) throws IOException {
        Trade trade = requestDto.toEntity(user);
        tradeRepository.save(trade);
        if (files != null) {
            for (MultipartFile file : files) {
                String fileUrl = awsS3upload.upload(file, "trade " + trade.getId());
                if (imageRepository.existsByImageUrlAndId(fileUrl, trade.getId())) {
                    throw new BusinessException(ErrorCode.EXISTED_FILE);
                }
                imageRepository.save(new Image(trade, fileUrl));
            }
        }
    }

    @Override
    @Transactional(readOnly = true)
    public List<TradeResponseDto> getTrade() {
        return tradeRepository.findAll().stream().map(TradeResponseDto::of).toList();
    }

    @Override
    @Transactional(readOnly = true)
    public TradeResponseDto selectTrade(Long tradeId) {
        return TradeResponseDto.of(findTrade(tradeId));
    }

    @Override
    @Transactional
    public void updateTrade(TradeRequestDto requestDto, Long tradeId, User user) {
        Trade trade = findTrade(tradeId);

        if (!user.getId().equals(trade.getUser().getId())) {
            throw new BusinessException(ErrorCode.NOT_TRADE_UPDATE);
        }

        trade.update(requestDto);
    }

    @Override
    @Transactional
    public void deleteTrade(Long tradeId, User user) {
        Trade trade = findTrade(tradeId);

        if (!user.getId().equals(trade.getUser().getId())) {
            throw new BusinessException(ErrorCode.NOT_TRADE_DELETE);
        }

        tradeRepository.delete(trade);
    }

    @Override
    @Transactional
    public void likeTrade(Long tradeId, User user) {
        Trade trade = findTrade(tradeId);

        Optional<TradeLike> likeOptional = tradeLikeRepository.findByUserAndTrade(user, trade);
        if (likeOptional.isPresent()) {
            throw new BusinessException(ErrorCode.EXISTED_LIKE);
        } else {
            TradeLike tradeLike = new TradeLike(user, trade);
            tradeLikeRepository.save(tradeLike);
        }
    }

    @Override
    @Transactional
    public void dislikeTrade(Long tradeId, User user) {
        Trade trade = findTrade(tradeId);

        Optional<TradeLike> likeOptional = tradeLikeRepository.findByUserAndTrade(user, trade);
        if (likeOptional.isPresent()) {
            tradeLikeRepository.delete(likeOptional.get());
        } else {
            throw new BusinessException(ErrorCode.EXISTED_DISLIKE);
        }
    }

    @Override
    @Transactional
    public void doBookmark(Long tradeId, User user) {
        Trade trade = findTrade(tradeId);

        if (tradeBookmarkRepository.existsByUserAndTrade(user, trade)) {
            throw new BusinessException(ErrorCode.EXISTED_DO_BOOKMARK);
        } else {
            TradeBookmark tradeBookmark = new TradeBookmark(user, trade);
            tradeBookmarkRepository.save(tradeBookmark);
        }

    }

    @Override
    @Transactional
    public void undoBookmark(Long tradeId, User user) {
        Trade trade = findTrade(tradeId);
        Optional<TradeBookmark> tradeBookmark = tradeBookmarkRepository.findByUserAndTrade(user, trade);

        if (tradeBookmark.isPresent()) {
            tradeBookmarkRepository.delete(tradeBookmark.get());
        } else {
            throw new BusinessException(ErrorCode.EXISTED_UNDO_BOOKMARK);
        }
    }

    @Override
    public Trade findTrade(Long tradeId) {
        return tradeRepository.findById(tradeId).orElseThrow(
                () -> new BusinessException(ErrorCode.NOT_FOUND_TRADE)
        );
    }

}

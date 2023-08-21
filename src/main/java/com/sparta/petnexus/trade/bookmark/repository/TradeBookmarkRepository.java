package com.sparta.petnexus.trade.bookmark.repository;

import com.sparta.petnexus.trade.bookmark.entity.TradeBookmark;
import com.sparta.petnexus.trade.entity.Trade;
import com.sparta.petnexus.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TradeBookmarkRepository extends JpaRepository<TradeBookmark, Long> {
    boolean existsByUserAndTrade(User user, Trade trade);

    Optional<TradeBookmark> findByUserAndTrade(User user, Trade trade);

}

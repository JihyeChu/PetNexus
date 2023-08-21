package com.sparta.petnexus.trade.like.repository;

import com.sparta.petnexus.trade.entity.Trade;
import com.sparta.petnexus.trade.like.entity.TradeLike;
import com.sparta.petnexus.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TradeLikeRepository extends JpaRepository<TradeLike, Long> {
    boolean existsByUserAndTrade(User user, Trade trade);

    Optional<TradeLike> findByUserAndTrade(User user, Trade trade);

}

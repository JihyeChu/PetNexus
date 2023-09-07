package com.sparta.petnexus.trade.repository;

import com.sparta.petnexus.trade.entity.Trade;
import com.sparta.petnexus.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

public interface TradeRepository extends JpaRepository<Trade, Long> {
    List<Trade> findByTitleContainingOrContentContaining(String title, String content);
}

package com.sparta.petnexus.trade.repository;

import com.sparta.petnexus.trade.entity.Trade;
import com.sparta.petnexus.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

public interface TradeRepository extends JpaRepository<Trade, Long> {
}

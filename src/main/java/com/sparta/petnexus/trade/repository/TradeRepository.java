package com.sparta.petnexus.trade.repository;

import com.sparta.petnexus.trade.entity.Trade;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

public interface TradeRepository extends JpaRepository<Trade, Long> {
}

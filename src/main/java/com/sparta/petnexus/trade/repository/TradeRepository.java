package com.sparta.petnexus.trade.repository;

import com.sparta.petnexus.trade.entity.CategoryEnum;
import com.sparta.petnexus.trade.entity.Trade;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TradeRepository extends JpaRepository<Trade, Long> {
    Page<Trade> findByTitleContainingOrContentContainingOrderByTitleDescContentDesc(String keyword, String keyword1, Pageable pageable);

    Page<Trade> findByCategoryOrderByCategoryDesc(CategoryEnum category, Pageable pageable);

    Page<Trade> findAllByUserId(Pageable pageable, Long id);
}

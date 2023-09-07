package com.sparta.petnexus.trade.repository;

import com.sparta.petnexus.post.entity.Post;
import com.sparta.petnexus.trade.entity.Trade;
import com.sparta.petnexus.user.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

public interface TradeRepository extends JpaRepository<Trade, Long> {
    Page<Trade> findByTitleContainingOrContentContainingOrderByTitleDescContentDesc(String keyword, String keyword1, Pageable pageable);
}

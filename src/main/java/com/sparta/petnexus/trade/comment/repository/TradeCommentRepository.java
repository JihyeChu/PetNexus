package com.sparta.petnexus.trade.comment.repository;

import com.sparta.petnexus.trade.comment.entity.TradeComment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TradeCommentRepository extends JpaRepository<TradeComment, Long> {
}

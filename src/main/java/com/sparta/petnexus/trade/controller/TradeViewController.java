package com.sparta.petnexus.trade.controller;

import com.sparta.petnexus.trade.dto.TradeResponseDto;
import com.sparta.petnexus.trade.service.TradeService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class TradeViewController {

    private final TradeService tradeService;

    @GetMapping("/tradeMarket")
    public String tradeList(Model model){
        List<TradeResponseDto> tradeResponseDtoList = tradeService.getTrade();
        model.addAttribute("tradeList", tradeResponseDtoList);
        return "tradeMarket";
    }

    @GetMapping("/tradeMarket/{tradeId}")
    public String getTrade(@PathVariable Long tradeId, Model model) {
        TradeResponseDto tradeResponseDto = tradeService.selectTrade(tradeId);
        model.addAttribute("trade", tradeResponseDto);
        return "trade";
    }

    @GetMapping("/tradeMarket/trade")
    public String createTrade(@RequestParam(required = false) Long tradeId, Model model){
        if(tradeId == null){
            model.addAttribute("trade", new TradeResponseDto());
        }else {
            TradeResponseDto tradeResponseDto = tradeService.selectTrade(tradeId);
            model.addAttribute("trade", tradeResponseDto);
        }
        return "createTrade";
    }
}
package com.sparta.petnexus.trade.controller;

import com.sparta.petnexus.trade.dto.TradeResponseDto;
import com.sparta.petnexus.trade.service.TradeService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@RequiredArgsConstructor
public class TradeViewController {

    private final TradeService tradeService;

    @GetMapping("/trade")
    public String trade(Model model){
        List<TradeResponseDto> tradeResponseDtoList = tradeService.getTrade();
        model.addAttribute("tradeList", tradeResponseDtoList);
        return "trade";
    }


}
